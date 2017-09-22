package com.test.dynamicproxytest;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class InvokeHandler implements InvocationHandler {

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("执行远程RPC 调用 返回详细内容");
        String rpcResponse = "{'name':'123','password':'456'}";
        //对象转换
        Class<?> returnType = method.getReturnType();
        //执行实现类方法
       // Object invoke = method.invoke(target, args);
        Object o = JSON.parseObject(rpcResponse, returnType);

        return o;
    }
}
