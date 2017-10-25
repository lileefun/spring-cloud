package com.feignext.provider;

import com.feignext.demo.UserDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by libin on 2017/10/16.
 */
@RestController
public class ProviderController {

    @PostMapping("/simple2")
    public UserDTO findObject(@RequestBody UserDTO userDTO) {
        System.out.println(userDTO);
        userDTO.setId(userDTO.getId()+1);
        userDTO.setUserName("test1");
        return userDTO;
    }

}
