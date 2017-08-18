/**
 * techwolf.cn All rights reserved.
 */
package com.libin.test.constants;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by libin on 2016/10/25.
 */

public class ErrorCode {

    /**
     * 成功
     */
    public final static String SUCCESS = "success";
    /**
     * 失败
     */
    public final static String FAIL = "fail";

    /**
     * 系统错误（不明原因）
     */
    public final static String E_SYS_UNKNOWN = "e_sys_unknown";
    /**
     * 系统错误（不明原因）
     */
    public final static String E_SYS_UNKNOWN_PROD = "e_sys_unknown_prod";



    /**
     * 找不到方法
     */
    public final static String E_SYS_UNKNOWN_METHOD = "e_sys_unknown_method";

    public final static String E_SYS_PARAM = "e_sys_param";

    /**
     * 参数错误
     */
    public final static String E_SYS_PARAM_PROD = "e_sys_param_prod";

    public final static String E_SYS_INVALID_TOKEN = "e_sys_invalid_token";

    public final static String E_SYS_INVALID_SECONDARY_TOKEN = "e_sys_invalid_secondary_token";





    public final static String VERIFYCODE_IMAGECODE_EMPTY = "verifycode_imagecode_empty";

    public final static String E_BIZ_VERIFY_SIGN_EMPTY = "e_biz_verify_sign_empty";

    public final static String E_SYS_PERMISSION_DENY = "e.sys.permission.deny";

    /**系统繁忙*/
    public final static String E_SYS_BUSYNESS = "e_sys_busyness";

    public final static String E_BIZ_ORDER_BUSYNESS = "e_biz_order_busyness";

    public final static String ARTICLE_COMMENT_LIMIT = "article_comment_limit";

    public final static String MASTER_PRODUCT_COMMENT_LIMIT = "master_product_comment_limit";



    private final static Map<String, String> msgMap = new HashMap<String, String>();

    public final static String asString(String resultCode) {
        return msgMap.get(resultCode);
    }

    private ErrorCode() {
    }

    /**
     * 初始化存入对象
     */
    private static void staticInit() {
        Field[] fields = ErrorCode.class.getFields();
        for (Field field : fields) {
            int modifiers = field.getModifiers();
            if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers)//
                    && Modifier.isFinal(modifiers) && (field.getType() == String.class)) {
                try {
                    msgMap.put(field.get(null).toString(), field.getName());
                } catch (IllegalArgumentException e) {
                } catch (IllegalAccessException e) {
                }
            }
        }
    }


    static {
        staticInit();
    }
}
