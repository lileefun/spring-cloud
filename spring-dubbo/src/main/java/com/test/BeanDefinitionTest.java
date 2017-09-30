package com.test;

import com.test.dynamicproxytest.ProxyFactory;
import com.test.dynamicproxytest.dto.TestDemo;
import lombok.Data;
import org.springframework.beans.factory.FactoryBean;

/**
 * Created by libin on 2017/9/20.
 */
@Data
public class BeanDefinitionTest implements FactoryBean {


    private Class<?> interfaceClass;

    public Object getObject() throws Exception {
        //从rest获取


        return new ProxyFactory(TestDemo.class).getProxy();
    }




    public Class<?> getObjectType() {
        return TestDemo.class;
    }

    public boolean isSingleton() {
        return true;
    }
}