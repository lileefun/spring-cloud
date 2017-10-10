package com.feignext.demo;

import com.feignext.DataClient;

/**
 * Created by libin on 2017/9/19.
 */
@DataClient(name = "appname-libin",ref = "testServiceImpl",beanid="testService")
public interface TestService {

    public UserDTO simple2(UserDTO userDTO);
}
