package com.libin.test.api.service;


import com.libin.test.api.annotation.ApiMethod;
import com.libin.test.api.annotation.ApiService;
import com.libin.test.api.entity.ServiceResult;
import com.libin.test.common.entity.ResultDTO;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by libin on 2016/10/24.
 */
@ApiService(value = "test.service")
public class TestCommand {

    Logger logger = LoggerFactory.getLogger(TestCommand.class);

    @ApiMethod(value = "test",method = ApiMethod.RequestMethod.POST)
    public ServiceResult filedownload() {
        logger.info("aaaaaaaa222aaaaaaaaaaaaa");
        ResultDTO resultDTO = new ResultDTO();
        TestDTO testDTO = new TestDTO();
        testDTO.setUserName("123");
        testDTO.setId("id");
        resultDTO.setData(testDTO);
        return new ServiceResult(resultDTO);
    }



}

@Data
class TestDTO {
    private String id;

    private String userName;

}