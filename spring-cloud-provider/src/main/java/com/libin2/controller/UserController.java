package com.libin2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feignext.demo.UserDTO;
import com.feignext.provider.BeanFactoryMapper;
import com.feignext.proxy.RpcTransport;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;

/**
 * Created by libin on 2017/8/8.
 */
@RestController
public class UserController{



    @PostMapping("/*")
    public Object findObject(@RequestBody RpcTransport.RpcRequestTransport rpcRequestTransport) {
        System.out.println(rpcRequestTransport);
        UserDTO userDTO = new UserDTO();
        userDTO.setUserName("test1");
        //返回扫描结果
        BeanFactoryMapper.ServiceBean beanMapper = BeanFactoryMapper.getBeanMapper(rpcRequestTransport.getUrlkey());
        Object bean = beanMapper.getBean();
        Method method = beanMapper.getMethod();
        ObjectMapper objectMapper = new ObjectMapper();
        Object[] args = rpcRequestTransport.getArgs();

        Class<?>[] parameterTypes = beanMapper.getParameterTypes();
        Object[] newObject = new Object[parameterTypes.length];
        int count = 0;
        for (Class<?> parameterType : parameterTypes) {
            Object o = objectMapper.convertValue(args[count], parameterType);
            newObject[count] = o;
            count++;
        }

        return ReflectionUtils.invokeMethod(method, bean, newObject);
    }

}
