package com.feignext.proxy;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by libin on 2017/10/18.
 */

public class RpcTransport implements Serializable {

    @Data
    @AllArgsConstructor
    public static class RpcRequestTransport implements Serializable {


        private String methodName;

        private String interfaceName;

        private String urlkey;

        private Object[] args;

    }

    public static RequestBuild RequestBuilder() {
        return new RequestBuild();
    }


    public static class RequestBuild {

        private String methodName;

        private String interfaceName;

        private String urlkey;

        private Object[] args;

        private Class<?>[] parameterTypes;


        public RequestBuild methodName(String methodName) {
            this.methodName = methodName;
            return this;
        }

        public RequestBuild interfaceName(String interfaceName) {
            this.interfaceName = interfaceName;
            return this;
        }

        public RequestBuild args(Object[] args) {
            this.args = args;
            return this;
        }

        public RequestBuild parameterTypes(Class<?>[] parameterTypes) {
            this.parameterTypes = parameterTypes;
            return this;
        }

        public RpcRequestTransport build() {
            StringBuffer typeStr = new StringBuffer();
            for (Class<?> parameterType : parameterTypes) {
                typeStr.append(parameterType.getCanonicalName() + ";");
            }
            if (typeStr.length() != 0) {
                typeStr.substring(0, typeStr.length() - 1);
            }
            urlkey = interfaceName + "[" + methodName + "](" + typeStr+")";
            return new RpcRequestTransport(methodName, interfaceName, urlkey, args);
        }


    }

}

