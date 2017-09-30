package com.feignext.proxy;

import com.feignext.consumer.Client;
import com.feignext.consumer.ConsumerContext;
import com.feignext.consumer.Util;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by libin on 2017/9/20.
 */
@Data
public class BeanDefinitionTest implements FactoryBean<Object>, InitializingBean,
        ApplicationContextAware {


    private String interfaceName;

    private Class<?> interfaceType;

    private String appName;

    private Client client;


    public Object getObject() throws Exception {
        return get();
    }

    public Object get() {
        //走自动配置
        ConsumerContext bean = applicationContext.getBean(ConsumerContext.class);

        client = getOptional(bean, Client.class);


        Map<Method, MethodHandler> methodMethodHandlerMap = methodHandlerMap(interfaceType);
        RpcInvoker rpcInvoker = new RpcInvoker(interfaceName, interfaceType, appName,methodMethodHandlerMap);
        return new ProxyFactory(interfaceType).getProxy(rpcInvoker);

    }

    protected <T> T getOptional(ConsumerContext context, Class<T> type) {
        return (T) context.getInstance(this.appName, type);
    }

    /**
     * 解析接口方法
     * @param interfaceType
     * @return
     */
    private Map<Method,MethodHandler> methodHandlerMap(Class interfaceType) {
        Map<Method, MethodHandler> methodMethodHandlerMap = new LinkedHashMap<>();
        Method[] methods = interfaceType.getMethods();
        for (Method method : methods) {
            MethodHandler methodHandler = new MethodHandler();
            methodHandler.setAppName(appName);
            methodHandler.setClient(client);
            methodHandler.setMethodName(method.getName());
            methodHandler.setUrl("http://"+appName);

            if (method.getDeclaringClass() == Object.class ||
                    (method.getModifiers() & Modifier.STATIC) != 0 ||
                    Util.isDefault(method)) {
                continue;
            }

            //解析方法

            //处理方法注解

            //解析方法参数
            Class<?>[] parameterTypes = method.getParameterTypes();

            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            int count = parameterAnnotations.length;


           /* MethodMetadata metadata = parseAndValidateMetadata(interfaceType, method);
            checkState(!result.containsKey(metadata.configKey()), "Overrides unsupported: %s",
                    metadata.configKey());
            result.put(metadata.configKey(), metadata);*/


            methodMethodHandlerMap.put(method,methodHandler );
        }
        return methodMethodHandlerMap;
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

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
/*
        Assert.hasText(this.name, "Name must be set");
*/
    }
}