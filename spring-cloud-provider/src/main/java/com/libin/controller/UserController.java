package com.libin.controller;

import com.libin.model.UserDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by libin on 2017/8/8.
 */
@RestController
public class UserController {

    @GetMapping("/simple/{id}")
    public UserDTO findUser(@PathVariable Integer id) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        userDTO.setUserName("test1");
        return userDTO;
    }

}
