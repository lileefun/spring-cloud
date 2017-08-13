package com.libin2.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by libin on 2017/8/8.
 */
@Data
public class UserDTO implements Serializable {
    
    private Integer id;
    
    private String userName;
    
}
