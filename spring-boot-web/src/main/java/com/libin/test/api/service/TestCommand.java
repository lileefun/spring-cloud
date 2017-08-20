package com.libin.test.api.service;


import com.libin.test.api.annotation.ApiMethod;
import com.libin.test.api.annotation.ApiService;
import com.libin.test.api.entity.ServiceResult;
import com.libin.test.common.entity.ResultDTO;
import com.libin.test.service.CityService;
import com.libin.test.service.dao.model.City;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by libin on 2016/10/24.
 */
@ApiService(value = "test.service")
public class TestCommand {

    public  static final Logger logger = LoggerFactory.getLogger(TestCommand.class);

    @Autowired
    private CityService cityService;

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

    @ApiMethod(value = "testcity",method = ApiMethod.RequestMethod.POST)
    public ServiceResult testcity() {
        logger.info("aaaaaaaa222aaaaaaaaaaaaa");
        List<City> cities = cityService.loadCitys();
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setData(cities);
        return new ServiceResult(resultDTO);
    }



}
@Data
class TestDTO {
    private String id;

    private String userName;

}