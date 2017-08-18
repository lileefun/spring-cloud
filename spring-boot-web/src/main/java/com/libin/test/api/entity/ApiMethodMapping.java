package com.libin.test.api.entity;


import com.libin.test.api.annotation.ApiMethod;
import lombok.Data;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;


/**
 * Created by libin on 2016/10/25.
 */

@Data
public class ApiMethodMapping {

    private Object bean;

    private Method method;

    private String serviceKey;

    private String methodKey;

    private String[] paramNames;

    private Annotation[][] paramAnnotations;

    private Type[] parameterTypes;

    private ApiMethod.Auth auth;

    private String[] permissionKey;

    private String secondaryPasswordKey;

    private String[] userType;
}
