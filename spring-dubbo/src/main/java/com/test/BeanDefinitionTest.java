package com.test;

import com.test.dynamicproxytest.ProxyFactory;
import com.test.dynamicproxytest.dto.TestDemo;
import org.springframework.beans.factory.FactoryBean;

/**
 * Created by libin on 2017/9/20.
 */
class BeanDefinitionTest implements FactoryBean {


    public Object getObject() throws Exception {
        //从rest获取


        return new ProxyFactory(TestDemo.class).getProxy();
    }




    public Class<?> getObjectType() {
        return null;
    }

    public boolean isSingleton() {
        return true;
    }
}