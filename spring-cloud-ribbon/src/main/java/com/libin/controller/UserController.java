package com.libin.controller;

import com.libin.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Created by libin on 2017/8/8.
 */
@RestController
public class UserController {

    @Autowired
    private RestTemplate restTemplate;


    /**
     * 硬编码调用
     * @param id
     * @return
     */
    @GetMapping("/simple/{id}")
    public UserDTO findUser(@PathVariable Integer id) {
        UserDTO userDTO = restTemplate.getForObject("http://appname-libin/simple/" + id, UserDTO.class);
        return userDTO;
    }


}
