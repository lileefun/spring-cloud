package com.libin2.service;

import com.feignext.demo.TestService;
import com.feignext.demo.UserDTO;
import org.springframework.stereotype.Service;

/**
 * Created by libin on 2017/9/19.
 */
@Service
public class TestServiceImpl implements TestService {

    @Override
    public UserDTO simple2(UserDTO userDTO) {
        return userDTO;
    }
}
