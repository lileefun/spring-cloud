package com.feignext.provider;

import com.feignext.DataClient;
import com.feignext.provider.annotation.ProviderDataTranClients;
import com.feignext.proxy.BeanDefinitionProvider;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by libin on 2017/9/22.
 */
public class DataTranProviderRegistrar implements ApplicationContextAware,ImportBeanDefinitionRegistrar,ResourceLoaderAware,BeanClassLoaderAware,EnvironmentAware {

    private ResourceLoader resourceLoader;

    private Environment environment;

    private ClassLoader classLoader;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        ClassPathScanningCandidateComponentProvider scanner = getScanner();
        scanner.setResourceLoader(this.resourceLoader);

        //设置过滤条件
        AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(DataClient.class);
        scanner.addIncludeFilter(annotationTypeFilter);
        //读取的包设置
        Set<String> basePackages = getBasePackages(importingClassMetadata);

        //遍历包
        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidateComponents = scanner
                    .findCandidateComponents(basePackage);
            for (BeanDefinition candidateComponent : candidateComponents) {
                if (candidateComponent instanceof AnnotatedBeanDefinition) {
                    //验证是否为接口
                    AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) candidateComponent;
                    AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();

                    Assert.isTrue(annotationMetadata.isInterface(),
                            "@DataClient can only be specified on an interface");

                    Map<String, Object> attributes = annotationMetadata
                            .getAnnotationAttributes(
                                    DataClient.class.getCanonicalName());
                    String className = annotationMetadata.getClassName();

                    String beanid = (String)attributes.get("beanid");
                    String appName = (String)attributes.get("name");
                    String ref = (String)attributes.get("ref");
                    String beanclass = beanDefinition.getBeanClassName();
                    //初始化解析配置的DataClient 接口将数据保存到map中.
/*                    String ref  = (String)attributes.get("ref");
                    RuntimeBeanReference reference = new RuntimeBeanReference(ref);*/

                    //注册到spring
                    BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(BeanDefinitionProvider.class);
                    AbstractBeanDefinition rawBeanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
                    rawBeanDefinition.getPropertyValues().addPropertyValue("appName",appName);
                    rawBeanDefinition.getPropertyValues().addPropertyValue("interfaceName",beanclass);
                    rawBeanDefinition.getPropertyValues().addPropertyValue("interfaceType", className);

                    //检测ref

                    if (ref!=null && registry.containsBeanDefinition(ref)) {
                        BeanDefinition refBean = registry.getBeanDefinition(ref);
                        if (!refBean.isSingleton()) {
                            throw new IllegalStateException("The exported service ref scope=\"singleton\" ...>");
                        }
                    }
                    Object reference = new RuntimeBeanReference(ref);
                    rawBeanDefinition.getPropertyValues().addPropertyValue("ref", reference);

                    registerBean(beanid,rawBeanDefinition,registry);





                }
            }
        }
    }


    /**
     * @desc 向spring容器注册bean
     * @param beanName
     * @param beanDefinition
     */
    private static void registerBean(String beanName, BeanDefinition beanDefinition, BeanDefinitionRegistry registry) {
        registry.registerBeanDefinition(beanName, beanDefinition);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;

    }


    protected Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> attributes = importingClassMetadata
                .getAnnotationAttributes(ProviderDataTranClients.class.getCanonicalName());

        Set<String> basePackages = new HashSet<>();
        for (String pkg : (String[]) attributes.get("value")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }

        //读取包配置路径
        for (String pkg : (String[]) attributes.get("basePackages")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }

        if (basePackages.isEmpty()) {
            basePackages.add(
                    ClassUtils.getPackageName(importingClassMetadata.getClassName()));
        }
        return basePackages;
    }


    protected ClassPathScanningCandidateComponentProvider getScanner() {
        return new ClassPathScanningCandidateComponentProvider(false, this.environment) {

            @Override
            protected boolean isCandidateComponent(
                    AnnotatedBeanDefinition beanDefinition) {
                if (beanDefinition.getMetadata().isIndependent()) {
                    if (beanDefinition.getMetadata().isInterface()
                            && beanDefinition.getMetadata()
                            .getInterfaceNames().length == 1
                            && Annotation.class.getName().equals(beanDefinition
                            .getMetadata().getInterfaceNames()[0])) {
                        try {
                            Class<?> target = ClassUtils.forName(
                                    beanDefinition.getMetadata().getClassName(),
                                    DataTranProviderRegistrar.this.classLoader);
                            return !target.isAnnotation();
                        }
                        catch (Exception ex) {
                            this.logger.error(
                                    "Could not load target class: "
                                            + beanDefinition.getMetadata().getClassName(),
                                    ex);

                        }
                    }
                    return true;
                }
                return false;

            }
        };
    }

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
