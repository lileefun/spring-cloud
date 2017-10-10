package com.libin2.controller;

import com.feignext.demo.TestService;
import com.feignext.demo.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by libin on 2017/8/15.
 */
@RestController
public class UserController {

/*    @Autowired
    private UserFeignClient userFeignClient;*/

    @Autowired
    private TestService testService;

    @GetMapping("/feign/{id}")
    public UserDTO findUser(@PathVariable Integer id){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1);
        UserDTO test = testService.simple2(userDTO);

        return test;
    }

}
