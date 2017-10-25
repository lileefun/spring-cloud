package com.feignext.provider;

import lombok.Data;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by libin on 2017/10/18.
 */
public class BeanFactoryMapper {

    private static ConcurrentHashMap<String ,ServiceBean> beanMap = new ConcurrentHashMap();

    public static ServiceBean getBeanMapper(String key) {
        return beanMap.get(key);
    }

    public static void add(String key, ServiceBean serviceBean) {
        beanMap.put(key, serviceBean);
    }


    @Data
    public static class ServiceBean{

        private Object bean;

        private Method method;

        private String beanName;

        private Class<?>[] parameterTypes;

    }

}
