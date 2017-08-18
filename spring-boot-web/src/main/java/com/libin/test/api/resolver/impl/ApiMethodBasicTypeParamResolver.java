package com.libin.test.api.resolver.impl;

import com.libin.test.api.annotation.ApiParam;
import com.libin.test.api.entity.ApiMethodParam;
import com.libin.test.api.exception.ACPIllegalArgumentException;
import com.libin.test.api.resolver.ApiMethodParamResolver;
import com.libin.test.utils.JsonReader;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Created by libin on 2016/10/25.
 */
public class ApiMethodBasicTypeParamResolver implements ApiMethodParamResolver {

    private static final Class[] BASIC_TYPE_CLASS = {String.class, Byte.class, byte.class, Short.class, short.class, Integer.class, int.class, Long.class, long.class,
            Float.class, float.class, Double.class, double.class, Character.class, char.class, Boolean.class, boolean.class, int[].class,Integer[].class, long[].class,Long[].class, String[].class};


    @Override
    public boolean supportCheck(ApiMethodParam apiMethodParam) {
        return ArrayUtils.contains(BASIC_TYPE_CLASS, apiMethodParam.getParamType());
    }

    @Override
    public Object getParamObject(ApiMethodParam apiMethodParam, HttpServletRequest request, HttpServletResponse response, boolean collectionType) {
        if (supportCheck(apiMethodParam)) {
            Type type = apiMethodParam.getParamType();
            String paramName = apiMethodParam.getParamName();
            String paramDefaultValue = null;
            boolean required = true;

            Annotation[] paramAnnotations = apiMethodParam.getParamAnnotations();
            ApiParam apiParamAnnotation = null;

            if (paramAnnotations != null) {
                for (Annotation paramAnnotation : paramAnnotations) {
                    if (paramAnnotation instanceof ApiParam) {
                        apiParamAnnotation = (ApiParam) paramAnnotation;
                        paramName = apiParamAnnotation.value();
                        paramDefaultValue = apiParamAnnotation.defaultValue();
                        required = apiParamAnnotation.required();
                        break;
                    }
                }
            }

            String paramValue = request.getParameter(paramName);
            if (required && paramValue == null) {
                throw new ACPIllegalArgumentException("参数错误");
            }
            if (paramValue == null) {
                paramValue = paramDefaultValue;
            }

           /* if (apiParamAnnotation != null && StringUtils.isNotBlank(apiParamAnnotation.validateRegx())) {
                //校验
                Pattern pattern = Pattern.compile(apiParamAnnotation.validateRegx());
                Matcher matcher = pattern.matcher(String.valueOf(paramValue));
                if (!matcher.find()) {
                    return new BossException(ErrorCode.E_SYS_PARAM_VALUE_ERROR);
                }
            }*/


            if (type.equals(String.class)) {
                return paramValue;
            } else if (type.equals(Integer.class) || type.equals(int.class)|| type.equals(Integer.class)) {
                return NumberUtils.toInt(paramValue);
            } else if (type.getClass().equals(Long.class) || type.equals(long.class)|| type.equals(Long.class)) {
                return NumberUtils.toLong(paramValue);
            } else if (type.getClass().equals(Float.class) || type.equals(float.class)) {
                return NumberUtils.toFloat(paramValue);
            } else if (type.getClass().equals(Double.class) || type.equals(double.class)) {
                return NumberUtils.toDouble(paramValue);
            } else if (type.getClass().equals(Character.class) || type.equals(char.class)) {
                if (StringUtils.isNotBlank(paramValue)) {
                    return paramValue.charAt(0);
                }
            } else if (type.getClass().equals(Byte.class) || type.equals(byte.class)) {
                return (byte) NumberUtils.toInt(paramValue);
            } else if (type.getClass().equals(Short.class) || type.equals(short.class)) {
                return (short) NumberUtils.toInt(paramValue);
            } else if (type.getClass().equals(Boolean.class) || type.equals(boolean.class)) {
                return BooleanUtils.toBoolean(paramValue);
            } else if (type.getClass().equals(int[].class) || type.equals(int[].class)) {
                if (StringUtils.equalsIgnoreCase(paramValue, "-1")) {
                    return null;
                }
                return JsonReader.fetchObject(paramValue, int[].class,false);
            }else if (type.getClass().equals(Integer[].class) || type.equals(Integer[].class)) {
                if (StringUtils.equalsIgnoreCase(paramValue, "-1")) {
                    return null;
                }
                return JsonReader.fetchObject(paramValue, Integer[].class,false);
            } else if (type.getClass().equals(long[].class) || type.equals(long[].class)) {
                if (StringUtils.equalsIgnoreCase(paramValue,"-1")) {
                    return null;
                }
                return JsonReader.fetchObject(paramValue, long[].class,false);
            }else if (type.getClass().equals(Long[].class) || type.equals(Long[].class)) {
                if (StringUtils.equalsIgnoreCase(paramValue,"-1")) {
                    return null;
                }
                return JsonReader.fetchObject(paramValue, Long[].class,false);
            } else if (type.getClass().equals(String[].class) || type.equals(String[].class)) {
                if (StringUtils.equalsIgnoreCase(paramValue, "-1")) {
                    return null;
                }
                return JsonReader.fetchObject(paramValue, String[].class,false);
            }
        }
        return null;
    }
}
