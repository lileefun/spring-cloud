package com.feignext.proxy;

import com.feignext.consumer.Client;
import com.feignext.consumer.loadbalance.Request;
import com.feignext.consumer.loadbalance.Response;
import lombok.Data;

import java.lang.reflect.Type;

/**
 * Created by libin on 2017/9/29.
 */
@Data
public class MethodHandler {

    private String appName;

    private String methodName;

    private String url;
    //设置负载均衡
    private Client client;

    //方法返回类型
    private Type returnType;


    /**
     * 方法调用
     * @param argv
     * @return
     * @throws Throwable
     */
    public Object invoke(Object[] argv) throws Throwable {
        //执行远程调用
        Request request = new Request(methodName,url,null,null,null);
        Response execute = client.execute(request, null);
        Response.Body body = execute.body();
        return body;
    }

}
