package com.libin.test.api.annotation;

import org.apache.commons.lang.StringUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by libin on 2016/10/20.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiMethod {

    public String value();

    public Auth auth() default Auth.OPTION;

    public String[] permissionkey() default "";

    public String[] usertype() default "";

    public enum Auth {

        OPTION(0),
        REQUIRED(1);
        private int authType;
        private Auth(int authType) {
            this.authType = authType;
        }
        public int getAuthType() {
            return this.authType;
        }
    }

    public RequestMethod method() default RequestMethod.ALL;

    public enum RequestMethod {

        ALL("all"), GET("get"), POST("post"), UPDATE("update"), DELETE("delete");

        private String methodType;

        private RequestMethod(String methodType) {
            this.methodType = methodType;
        }

        public String getMethodType() {
            return this.methodType;
        }

        public boolean equals(String method) {
            return StringUtils.equalsIgnoreCase(method, methodType);
        }
    }

    class UserType {
        public static final String 牛人 = "1";
        public static final String 机构 = "2";
        public static final String 员工 = "3";

    }

}
