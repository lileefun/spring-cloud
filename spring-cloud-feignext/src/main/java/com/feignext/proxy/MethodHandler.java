package com.feignext.proxy;

import com.feignext.consumer.Client;
import com.feignext.consumer.SpringEncoder;
import com.feignext.consumer.loadbalance.Request;
import com.feignext.consumer.loadbalance.Response;
import lombok.Data;

import java.lang.reflect.Type;
import java.util.*;

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

    private Class<?>[] parameterTypes;

    //对象转化器
    private SpringEncoder springEncoder;

    /**
     * 方法调用
     * @param argv
     * @return
     * @throws Throwable
     */
    public Object invoke(Object[] argv) throws Throwable {
        //执行远程调用

        //设置参数

        Map<String, Collection<String>> headers = new HashMap<>();
        url = "http://"+appName+"/"+methodName;
        Request request = new Request(appName,"POST",url,headers,null,null);
       // springEncoder.encode(converParamersObject(argv),request);
        //TODO: 测试走第一个参数
        springEncoder.encode(argv[0],request);
        Response execute = client.execute(request, null);
        Response.Body body = execute.body();
        return body;
    }

    private List converParamersObject(Object[] argv) {
        return Arrays.asList(argv);
    }


}
