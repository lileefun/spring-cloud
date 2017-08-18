package com.libin.test.api.resolver.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.libin.test.api.annotation.ApiParam;
import com.libin.test.api.entity.ApiMethodParam;
import com.libin.test.api.exception.ACPIllegalArgumentException;
import com.libin.test.api.resolver.ApiMethodParamResolver;
import com.libin.test.constants.Consts;
import com.libin.test.utils.JsonReader;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * Created by libin on 2016/10/25.
 */
public class ApiMethodJsonObjectTypeParamResolver implements ApiMethodParamResolver {

    private static Logger log = LoggerFactory.getLogger(ApiMethodJsonObjectTypeParamResolver.class);

    private HashMap<String, Class> classMap = new HashMap<>();

    private final String TYPE_NAME_PREFIX = "class ";

    public String getClassName(String typeName) {
        if (typeName == null) {
            return "";
        }
        String className = typeName;
        if (className.startsWith(TYPE_NAME_PREFIX)) {
            className = className.substring(TYPE_NAME_PREFIX.length());
        }
        return className;
    }

    public Class<?> getClass(Type type) {
        String className = getClassName(type.getTypeName());
        if (className == null || className.isEmpty()) {
            return null;
        }
        try {
            Class clazz = classMap.get(className);
            if (clazz == null) {
                clazz = Class.forName(className);
                classMap.put(className, clazz);
            }
            return clazz;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean supportCheck(ApiMethodParam apiMethodParam) {
        Annotation[] paramAnnotations = apiMethodParam.getParamAnnotations();
        ApiParam apiParamAnnotation;
        if (paramAnnotations != null) {
            for (Annotation paramAnnotation : paramAnnotations) {
                if (paramAnnotation instanceof ApiParam) {
                    apiParamAnnotation = (ApiParam) paramAnnotation;
                    return apiParamAnnotation.jsonType();
                }
            }
        }

        return false;
    }

    @Override
    public Object getParamObject(ApiMethodParam apiMethodParam, HttpServletRequest request, HttpServletResponse response, boolean collectionType) throws Exception {
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
                throw new ACPIllegalArgumentException("参数错误 参数:" + paramName + " 参数不能为空");
            }
            if (paramValue == null) {
                paramValue = paramDefaultValue;
            }

            return paramFilter(paramName, paramValue, type, collectionType);
        }
        return null;
    }

    /**
     * 参数拦截器
     *
     * @param paramName
     * @param paramValue
     * @return
     */
    private Object paramFilter(String paramName, String paramValue, Type type, boolean collectionType) throws Exception {
        if ("data".equals(paramName) && StringUtils.isNotBlank(paramValue)) {
            try {
                Object parse = JSON.parse(paramValue);
                if (parse instanceof JSONObject) {
                    JSONObject jsonObject = JSON.parseObject(paramValue);
                    String pageNumStr = "pagenum";
                    String pageSizeStr = "pagesize";
                    Integer pageNum = jsonObject.getInteger(pageNumStr);
                    if (pageNum == null) {
                        pageNumStr = "pageNum";
                        pageNum = jsonObject.getInteger(pageNumStr);
                    }

                    Integer pageSize = jsonObject.getInteger(pageSizeStr);
                    if (pageSize == null) {
                        pageSizeStr = "pageSize";
                        pageSize = jsonObject.getInteger(pageSizeStr);
                    }

                    if (pageNum == null || pageNum.intValue() <= 0) {
                        pageNum = 1;
                        jsonObject.put(pageNumStr, pageNum);
                    }

                    if (pageSize == null || pageSize.intValue() <= 0) {
                        pageSize = Consts.DEFAULT_PAGE_SIZE;
                        jsonObject.put(pageSizeStr, pageSize);
                    } else if (pageSize.intValue() > Consts.MAX_PAGE_SIZE) {
                        pageSize = Consts.MAX_PAGE_SIZE;
                        jsonObject.put(pageSizeStr, pageSize);
                    }
                    paramValue = JSON.toJSONString(jsonObject);
                }

                return JsonReader.fetchObject(paramValue, getClass(type), collectionType);

            } catch (Exception e) {
                log.error("ApiMethodJsonObjectTypeParamResolver.paramFilter异常 {} {} {}", e, paramName, paramValue);
                throw new Exception("json 格式错误  key:" + paramName + "  value:" + paramValue);
            }
        }else{
            return null;
        }

    }


}
