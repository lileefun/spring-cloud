package com.libin.test.common.entity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ResourceBundle;

/**
 * 功能描述：读取提示工具类
 *
 * @author yejianzhou
 * @since 2016-10-27
 */
public class PromptUtil {
    private static final Log log = LogFactory.getLog(PromptUtil.class);
    private static ResourceBundle bundle;

    static {
        try {
            bundle = ResourceBundle.getBundle("prompt");
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public static String v(String key) {
        return bundle.getString(key);
    }
}
