package com.libin.test.api;

import com.libin.test.api.annotation.ApiMethod;
import com.libin.test.api.annotation.ApiService;
import com.libin.test.api.entity.ApiMethodMapping;
import com.libin.test.api.entity.ApiMethodParam;
import com.libin.test.api.entity.ServiceResult;
import com.libin.test.api.resolver.ApiMethodParamResolver;
import com.libin.test.api.resolver.impl.ApiMethodBasicTypeParamResolver;
import com.libin.test.api.resolver.impl.ApiMethodJsonObjectTypeParamResolver;
import com.libin.test.api.resolver.impl.ApiMethodParamWebResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by libin on 2017/8/18.
 */

public class ApiMethodService  implements InitializingBean, ApplicationContextAware {

    private static Logger logger = LoggerFactory.getLogger(ApiMethodService.class);

    private LocalVariableTableParameterNameDiscoverer localVariableTableParameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    private Map<String, Map<String, ApiMethodMapping>> apiServiceMap;

    private List<ApiMethodParamResolver> defaultApiMethodParamResolvers;

    private ApplicationContext applicationContext;

    private boolean initApiService() {
        if (applicationContext != null) {
            apiServiceMap = new HashMap();

            Map<String, Object> objectMap = applicationContext.getBeansWithAnnotation(ApiService.class);
            for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
                try {
                    Object bean = entry.getValue();

                    if (apiServiceMap.get(entry.getKey()) != null) {
                        throw new IllegalStateException("重复设置服务 key ：" + entry.getKey());
                    }
                   /* if (AopUtils.isAopProxy(bean)) {

                    }*/
                    Method[] methodArray;
                    if (AopUtils.isAopProxy(bean)) {
                        methodArray = AopUtils.getTargetClass(bean).getMethods();
                    } else {
                        methodArray = bean.getClass().getMethods();
                    }
                    Map<String, ApiMethodMapping> apiMethodMap = null;
                    for (Method method : methodArray) {
                        ApiMethod apiMethod = AnnotationUtils.findAnnotation(method, ApiMethod.class);

                        if (apiMethod != null) {
                            if (apiMethodMap == null) {
                                apiMethodMap = new HashMap<String, ApiMethodMapping>();
                            }
                            String[] permissionkey = apiMethod.permissionkey();
                            String methodKey = apiMethod.value();
                            ApiMethodMapping apiMethodMapping = new ApiMethodMapping();
                            apiMethodMapping.setMethodKey(methodKey);
                            apiMethodMapping.setServiceKey(entry.getKey());
                            apiMethodMapping.setBean(entry.getValue());
                            apiMethodMapping.setMethod(method);
                            apiMethodMapping.setParamNames(localVariableTableParameterNameDiscoverer.getParameterNames(method));
                            apiMethodMapping.setParamAnnotations(method.getParameterAnnotations());
                            apiMethodMapping.setParameterTypes(method.getParameterTypes());
                            if (!permissionkey[0].equals("")) {
                                apiMethodMapping.setPermissionKey(permissionkey);
                            }

                            //设置userType
                            String[] usertype = apiMethod.usertype();
                            if (usertype != null && usertype.length > 0 && !usertype[0].equals("")) {
                                apiMethodMapping.setUserType(usertype);
                            }

                            ApiMethodMapping apiMethodMappingTmp = apiMethodMap.get(apiMethodMapping.getMethodKey());
                            apiMethodMapping.setAuth(apiMethod.auth());
                            if (apiMethodMappingTmp == null) {
                                apiMethodMap.put(apiMethodMapping.getMethodKey(), apiMethodMapping);
                            } else {
                                throw new IllegalStateException("重复设置方法 key ：" + apiMethodMapping.getMethodKey());
                            }
                        }
                    }

                    if (apiMethodMap != null && apiMethodMap.size() > 0)
                        apiServiceMap.put(entry.getKey(), apiMethodMap);
                } catch (Exception e) {

                }
            }
        }
        return true;
    }

    public ApiMethodMapping lookupApiMethodCommand(String methodKey, String serviceKey) {
        Map<String, ApiMethodMapping> methodMap = apiServiceMap.get(serviceKey);
        if (methodMap != null) {
            ApiMethodMapping apiMethodMapping = methodMap.get(methodKey);
            return apiMethodMapping;
        }
        return null;
    }

    private boolean initApiMethodParamResolver() {
        defaultApiMethodParamResolvers = new ArrayList();
        defaultApiMethodParamResolvers.add(new ApiMethodBasicTypeParamResolver());
        defaultApiMethodParamResolvers.add(new ApiMethodParamWebResolver());
        defaultApiMethodParamResolvers.add(new ApiMethodJsonObjectTypeParamResolver());
        return true;
    }

    public ServiceResult executeMethod(ApiMethodMapping apiServiceMapping, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Method method = apiServiceMapping.getMethod();
        String[] permissionKey = apiServiceMapping.getPermissionKey();
        Object bean = apiServiceMapping.getBean();
        Type[] parameterTypes = apiServiceMapping.getParameterTypes();
        Annotation[][] annotations = apiServiceMapping.getParamAnnotations();
        String[] paramNames = apiServiceMapping.getParamNames();
        List<Object> paramList = new ArrayList<Object>();


        for (int i = 0; i < parameterTypes.length; i++) {
            boolean collectionType = false;
            Type type = parameterTypes[i];
            if (type.equals(List.class)) {
                collectionType = true;
                ParameterizedType pType = (ParameterizedType) method.getGenericParameterTypes()[i];
                type = pType.getActualTypeArguments()[0];
            }

            Annotation[] paramAnnotations = annotations[i];
            String paramName = paramNames[i];
            ApiMethodParam apiMethodParam = new ApiMethodParam();
            apiMethodParam.setMethod(method);
            apiMethodParam.setParamAnnotations(paramAnnotations);
            apiMethodParam.setParamType(type);
            apiMethodParam.setParamName(paramName);
            apiMethodParam.setPermissionKey(permissionKey);

            Object paramObjectValue = null;
            for (ApiMethodParamResolver apiMethodParamResolver : defaultApiMethodParamResolvers) {
                if (apiMethodParamResolver.supportCheck(apiMethodParam)) {
                    paramObjectValue = apiMethodParamResolver.getParamObject(apiMethodParam, request, response, collectionType);
                    break;
                }
            }
            paramList.add(paramObjectValue);
        }
        Object[] params = paramList.toArray(new Object[paramList.size()]);
        Object result = ReflectionUtils.invokeMethod(method, bean, params);
        ServiceResult apiResult;
        if (result instanceof ServiceResult) {
            apiResult = (ServiceResult) result;
        } else {
            apiResult = new ServiceResult(result);
        }


        return apiResult;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        logger.info("setApplicationContext");
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("afterPropertiesSet");
        Assert.isTrue(initApiService(), "init initApiServices fail");
        Assert.isTrue(initApiMethodParamResolver(), "init initApiServices fail");

    }
}
