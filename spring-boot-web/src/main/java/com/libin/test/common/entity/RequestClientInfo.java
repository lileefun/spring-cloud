package com.libin.test.common.entity;

import eu.bitwalker.useragentutils.UserAgent;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by libin on 2016/10/26.
 */

@Data
public class RequestClientInfo implements Serializable {

    private static final long serialVersionUID = 6525446067565053379L;
    private UserAgent userAgent;

    private String clientIp;

    /**
     * 渠道
     */
    private String channel;

    /**
     * 设备来源
     */
    private String platform;

    /**
     * app版本
     */
    private String version;

    /**
     * app版本号
     */
    private Integer versionDigit;

    /**
     * 设备唯一标识
     */
    private String deviceId;

    /**
     * 用户id (客户端保存的加密id)
     */
    private String userIdstr;

    /**
     * 用户id 解密后的ID
     */
    private Long userId;

    private String userToken;

    /**
     * token
     */
    private String token;

    private String lng;

    private String lat;

    private Integer userType;//1普通用户 2粉丝会 3机构 4.owhat员工 5.明星 DBConsts.User.UserType

    /**
     * 用户昵称
     */
    private String userNickName;

    /**
     * 用户头像
     */
    private String userAvatarImg;

    /**
     * 是否微信
     */
    private boolean isWeiXin = false;

    private String secondaryToken;


//    public Map<String, String> getInfoMap() {
//        Map<String, String> map = new HashMap<String, String>();
//        Field[] declaredFields = this.getClass().getDeclaredFields();
//        for (Field field : declaredFields) {
//            field.setAccessible(true);
//            try {
//                if (field.getType().equals(String.class) || field.getType().equals(Integer.class)) {
//                    String value = (String) field.get(this);
//                    if (StringUtils.isNotBlank(value)) {
//                        map.put(field.getName(), value);
//                    }
//                }
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
//        return map;
//    }
}
