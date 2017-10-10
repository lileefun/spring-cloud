package com.libin.controller;

import com.feignext.demo.UserDTO;
import com.libin.UserFeignClient;
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

    @GetMapping("/feign2/{id}")
    public UserDTO findUser2(@PathVariable Integer id){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        return userFeignClient.findObject(userDTO);
    }
}
