package com.libin.test.service.dao.mapper;

import com.libin.test.service.dao.model.City;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by libin on 2017/8/20.
 */
public interface CityMapper {

    @Select("select * from city")
    public List<City> getCity();
}
