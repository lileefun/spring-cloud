package com.test;

import com.test.dynamicproxytest.dto.ResultDTO;
import com.test.dynamicproxytest.dto.TestDemo;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by libin on 2017/9/20.
 * 动态创建bean
 */
public class TestBeanSpringRegister {

    public static void main(String[] args) {
        ApplicationContext app = new ClassPathXmlApplicationContext("spring.xml");
        //注册动态bean
        addBean("testDemo", app);
        TestDemo beanDefinitionTest = (TestDemo)app.getBean("testDemo");
        //调用bean 通过通过代理执行
        ResultDTO resultDTO = beanDefinitionTest.testDto();
        System.out.println(resultDTO);
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
        beanDefinitonRegistry.registerBeanDefinition(beanName, beanDefinition);
    }


}
