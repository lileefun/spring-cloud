package com.feignext.proxy;

import lombok.Data;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;

/**
 * Created by libin on 2017/9/20.
 */
@Data
public class BeanDefinitionTest implements FactoryBean<Object> {


    private String interfaceName;

    private Class<?> interfaceType;

    private String serviceName;


    public Object getObject() throws Exception {
        //从rest获取


        return new ProxyFactory(interfaceType).getProxy();
    }



    @Override
    public Class<?> getObjectType() {
        return interfaceType;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    private ApplicationContext applicationContext;

}