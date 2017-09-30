package com.feignext.proxy;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Created by libin on 2017/9/25.
 */
@Data
@AllArgsConstructor
public class RpcInvoker {

    private String interfaceName;

    private Class<?> interfaceType;

    private String appName;

    private Map<Method,MethodHandler> methodHandlerMap;

    public String getUrl() {
        return "http://" + appName ;
    }

    private String getUrl(Map<String, Object> attributes) {
        String url = null;
        if (StringUtils.hasText(url)) {
            if (!url.contains("://")) {
                url = "http://" + appName;
            }
            try {
                new URL(url);
            }
            catch (MalformedURLException e) {
                throw new IllegalArgumentException(url + " is malformed", e);
            }
        }
        return url;
    }





}
