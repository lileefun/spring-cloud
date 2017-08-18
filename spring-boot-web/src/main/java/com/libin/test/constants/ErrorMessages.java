/**
 * techwolf.cn All rights reserved.
 */
package com.libin.test.constants;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Created by libin on 2016/10/25.
 */

public class ErrorMessages {

    private static final String BUNDLE_NAME = "error_code_messages"; //$NON-NLS-1$

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

  /*  public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return "系统出错了T_T(" + key + ")";
        }
    }*/

    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(ErrorCode.asString(key));
        } catch (MissingResourceException e) {
            return "系统出错了T_T(" + key + ")";
        }
    }

    public static String getField(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return "系统出错了T_T(" + key + ")";
        }
    }

    private ErrorMessages() {
    }
}