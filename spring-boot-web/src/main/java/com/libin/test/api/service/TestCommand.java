package com.libin.test.api.service;


import com.libin.test.api.annotation.ApiMethod;
import com.libin.test.api.annotation.ApiService;
import com.libin.test.api.entity.ServiceResult;
import com.libin.test.common.entity.ResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by libin on 2016/10/24.
 */
@ApiService(value = "test.service")
public class TestCommand {

    Logger logger = LoggerFactory.getLogger(TestCommand.class);

    @ApiMethod(value = "filedownload",method = ApiMethod.RequestMethod.POST)
    public ServiceResult filedownload() {
        logger.info("aaaaaaaaaaaaaaaaaaaaa");
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setData("1234");
        return new ServiceResult(resultDTO);
    }



}
