package com.libin.controller;

import com.libin.model.UserDTO;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

/**
 * Created by libin on 2017/8/8.
 */
@RestController
public class UserController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private EurekaClient eurekaClient;

    @GetMapping("/simple/{id}")
    public UserDTO findUser(@PathVariable Integer id) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        userDTO.setUserName("test1");
        return userDTO;
    }

    @PostMapping("/simple2")
    public UserDTO findObject(@RequestBody UserDTO userDTO) {
        System.out.println(userDTO);
        userDTO.setId(userDTO.getId()+1);
        userDTO.setUserName("test1");
        return userDTO;
    }

    @GetMapping("/eureka-instance")
    public String serviceUrl() {
        InstanceInfo instance = eurekaClient.getNextServerFromEureka("APPNAME-LIBIN", false);
        return instance.getHomePageUrl();
    }

    @GetMapping("/eureka-info")
    public ServiceInstance info() {
        ServiceInstance localServiceInstance = discoveryClient.getLocalServiceInstance();
        return localServiceInstance;
    }
}
