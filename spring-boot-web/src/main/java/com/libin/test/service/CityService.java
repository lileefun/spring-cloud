package com.libin.test.service;

import com.github.pagehelper.PageHelper;
import com.libin.test.service.dao.mapper.CityMapper;
import com.libin.test.service.dao.model.City;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by libin on 2017/8/20.
 */
@Service
public class CityService {

    @Resource
    private CityMapper cityMapper;

    public List<City> loadCitys() {
        PageHelper.startPage(1, 1);
        List<City> city = cityMapper.getCity();
        return city;
    }
}
