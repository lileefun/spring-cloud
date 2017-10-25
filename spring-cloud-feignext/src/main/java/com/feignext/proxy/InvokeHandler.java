package com.feignext.proxy;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

public class InvokeHandler implements InvocationHandler {

    private RpcInvoker rpcInvoker;

    public InvokeHandler(RpcInvoker rpcInvoker) {
        this.rpcInvoker = rpcInvoker;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().toLowerCase().equals("tostring")) {
            return rpcInvoker.toString();
        }
        System.out.println("执行远程RPC 调用 返回详细内容");
        Map<Method, MethodHandler> methodHandlerMap = rpcInvoker.getMethodHandlerMap();
        MethodHandler methodHandler = methodHandlerMap.get(method);
        return methodHandler.invoke(args);
/*        System.out.println(rpcInvoker.getAppName());
        String rpcResponse = "{'name':'123','password':'456'}";
        //对象转换
        Class<?> returnType = method.getReturnType();
        //执行实现类方法
       // Object invoke = method.invoke(target, args);
        //Object o = JSON.parseObject(rpcResponse, returnType);*/

    }
}
