package com.libin.test.api.entity;

import lombok.Data;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Created by libin on 2016/10/24.
 */
@Data
public class ApiMethodParam {

    private Method method;

    private String paramName;

    private Annotation[] paramAnnotations;

    private Type paramType;

    private String[] permissionKey;
}
