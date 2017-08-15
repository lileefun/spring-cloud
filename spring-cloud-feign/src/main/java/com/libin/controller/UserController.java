package com.libin.controller;

import com.libin.UserFeignClient;
import com.libin.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by libin on 2017/8/15.
 */
@RestController
public class UserController {

    @Autowired
    private UserFeignClient userFeignClient;

    @GetMapping("/feign/{id}")
    public UserDTO findUser(@PathVariable Integer id){
        return userFeignClient.findUser(id);
    }

}
