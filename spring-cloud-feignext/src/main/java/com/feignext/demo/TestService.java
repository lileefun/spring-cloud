package com.feignext.demo;

import com.feignext.DataClient;

/**
 * Created by libin on 2017/9/19.
 */
@DataClient(name = "testdata",ref = "testServiceImpl",beanid="testService")
public interface TestService {

    public int test(int p1);
}
