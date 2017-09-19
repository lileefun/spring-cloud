package com.libin2.service;

import com.feignext.demo.TestService;
import org.springframework.stereotype.Service;

/**
 * Created by libin on 2017/9/19.
 */
@Service
public class TestServiceImpl implements TestService {

    @Override
    public int test(int p1) {
        return 0;
    }
}
