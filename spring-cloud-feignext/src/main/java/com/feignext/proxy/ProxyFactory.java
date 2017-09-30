package com.feignext.proxy;

import java.lang.reflect.Proxy;

/**
 * Created by libin on 2017/9/21.
 * 动态代理对象
 */
public class ProxyFactory {

    //动态代理目标
    private Class target;

    public ProxyFactory(Class target) {
        this.target = target;
    }

    //给目标生成对象

    public Object getProxy(RpcInvoker rpcInvoker) {

       return Proxy.newProxyInstance(target.getClassLoader(), new Class[]{target}, new InvokeHandler(rpcInvoker));
    }

/*    public <T> T getProxy( Class<?>[] interfaces) {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), interfaces, new InvokeHandler());
    }*/

}

