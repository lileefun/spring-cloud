package com.feignext.proxy;

import com.feignext.consumer.Client;
import com.feignext.consumer.SpringDecoder;
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

    private String interfaceName;

    //设置负载均衡
    private Client client;
    //方法返回类型
    private Type returnType;

    private Class<?>[] parameterTypes;

    //对象转化器
    private SpringEncoder springEncoder;

    private SpringDecoder springDecoder;


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
        url = "http://"+appName+"/";
        Request request = new Request(appName,"POST",url,headers,null,null);
        springEncoder.encode(converParamersObject(argv),request);
        Response execute = client.execute(request, null);
        Object decode = springDecoder.decode(execute, returnType);
        return decode;
    }

    private RpcTransport.RpcRequestTransport converParamersObject(Object[] argv) {
        RpcTransport.RequestBuild requestBuild1 = RpcTransport.RequestBuilder()
                .interfaceName(interfaceName).methodName(methodName).parameterTypes(parameterTypes).args(argv);
        RpcTransport.RpcRequestTransport rpcRequestTransport = requestBuild1.build();
        return rpcRequestTransport;
    }


}
