import com.libin.service2.TestService;
import com.test.BeanDefinitionTest;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by libin on 2017/9/8.
 */
public class ConsumerBootStrap {

    public static void main(String[] args) {

        final ClassPathXmlApplicationContext ac;
        ac = new ClassPathXmlApplicationContext("spring-dubbo-consumer.xml");
        //注册动态bean
//        addBean("testDemo", ac);
        TestService bean = ac.getBean(TestService.class);
        //TestDemo bean1 = ac.getBean(TestDemo.class);

        System.out.println(bean.test(1));


    }


    /**
     *
     * @param serviceName 注册别名
     * @param app application上下文
     */
    private static void addBean(String serviceName, ApplicationContext app){
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(BeanDefinitionTest.class);
        registerBean(serviceName, beanDefinitionBuilder.getRawBeanDefinition(), app);
    }

    /**
     * @desc 向spring容器注册bean
     * @param beanName
     * @param beanDefinition
     */
    private static void registerBean(String beanName, BeanDefinition beanDefinition, ApplicationContext context) {
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) context;
        BeanDefinitionRegistry beanDefinitonRegistry = (BeanDefinitionRegistry) configurableApplicationContext
                .getBeanFactory();
        String beanclass = beanDefinition.getBeanClassName();
        beanDefinition.getPropertyValues().add("interfaceClass",beanclass);
        beanDefinitonRegistry.registerBeanDefinition(beanName, beanDefinition);
    }

}
