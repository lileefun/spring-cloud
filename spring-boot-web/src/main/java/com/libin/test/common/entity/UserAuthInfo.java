package com.libin.test.common.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by libin on 2016/12/6.
 */
@Data
public class UserAuthInfo implements Serializable {

    private static final long serialVersionUID = 823282129106783951L;
    private String[] permissionKey;
}
