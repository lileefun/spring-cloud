package com.test.dynamicproxytest;

import com.test.dynamicproxytest.dto.TestDemo;

/**
 * Created by libin on 2017/9/21.
 */
public class Test {

    public static void main(String[] args) {
        ProxyFactory proxyFactory = new ProxyFactory(TestDemo.class);
        TestDemo proxy = (TestDemo)proxyFactory.getProxy();
        System.out.println(proxy.test(10));
    }
}
