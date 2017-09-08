package com.libin.service;

import com.libin.service2.TestService;
import org.springframework.stereotype.Service;

/**
 * Created by libin on 2016/10/19.
 */

@Service
public class TestServiceImpl implements TestService {


    public Integer test(int i) {
        return 1+i;
    }
}
