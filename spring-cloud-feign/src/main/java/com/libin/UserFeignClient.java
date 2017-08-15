package com.libin;

import com.libin.model.UserDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by libin on 2017/8/15.
 */
@FeignClient("appname-libin")
public interface UserFeignClient {

    @RequestMapping(method = RequestMethod.GET, value = "/simple/{id}")
    public UserDTO findUser(@PathVariable("id") Integer id);

}
