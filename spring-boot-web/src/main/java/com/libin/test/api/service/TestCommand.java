package com.libin.test.api.service;


import com.libin.test.api.annotation.ApiMethod;
import com.libin.test.api.annotation.ApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by libin on 2016/10/24.
 */
@ApiService(value = "test.service")
public class TestCommand {

    Logger logger = LoggerFactory.getLogger(TestCommand.class);

    @ApiMethod(value = "filedownload")
    public String filedownload() {

       return "";
    }



}
