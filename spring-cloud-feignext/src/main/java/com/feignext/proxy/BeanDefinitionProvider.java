package com.feignext.proxy;

import com.feignext.provider.BeanFactoryMapper;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import java.lang.reflect.Method;

/**
 * Created by libin on 2017/10/16.
 */
@Data
public class BeanDefinitionProvider<T> implements InitializingBean, DisposableBean, ApplicationContextAware, ApplicationListener, BeanNameAware {

    private ApplicationContext applicationContext;
    private String interfaceName;

    private Class<?> interfaceType;

    private String appName;

    private T ref;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;

        //注册bean和方法

        Method[] methods = ref.getClass().getDeclaredMethods();

            for (Method method : methods) {
                //分解的bean 和 ref
                RpcTransport.RequestBuild requestBuild1 = RpcTransport.RequestBuilder()
                        .interfaceName(interfaceName).methodName(method.getName()).parameterTypes(method.getParameterTypes());
                RpcTransport.RpcRequestTransport rpcRequestTransport = requestBuild1.build();
                String urlkey = rpcRequestTransport.getUrlkey();
                Class<?>[] parameterTypes = method.getParameterTypes();

                BeanFactoryMapper.ServiceBean serviceBean = new BeanFactoryMapper.ServiceBean();
                serviceBean.setBean(ref);
                serviceBean.setMethod(method);
                serviceBean.setParameterTypes(parameterTypes);
                BeanFactoryMapper.add( urlkey,serviceBean);
            }

    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        //添加到
    }

    @Override
    public void setBeanName(String name) {

    }

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
