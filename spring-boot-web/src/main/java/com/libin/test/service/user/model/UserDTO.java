package com.libin.test.service.user.model;

import com.libin.test.common.entity.BaseDTO;
import lombok.Data;

import java.io.Serializable;

/**
 *
 */
@Data
public class UserDTO extends BaseDTO implements Serializable {
    private static final long serialVersionUID = -5945788635837371063L;
    private Long userId;

    private String userName;

    private String password;

    private String salt;

    private String trueName;

    private String nickName;

    private String mobile;

    private String mobileAreaCode;

    private Integer fkAreaCountryId;

    private Integer userType;

}
