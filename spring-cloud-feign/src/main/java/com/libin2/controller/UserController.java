package com.libin2.controller;

import com.feignext.demo.TestService;
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
    public Integer findUser(@PathVariable Integer id){
        int test = testService.test(1);

        return test;
    }

}
