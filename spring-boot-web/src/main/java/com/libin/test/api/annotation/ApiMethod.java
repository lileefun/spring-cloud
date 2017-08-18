package com.libin.test.api.annotation;

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

    class UserType {
        public static final String 牛人 = "1";
        public static final String 机构 = "2";
        public static final String 员工 = "3";

    }

}
