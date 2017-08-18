package com.libin.test.beanconf;

import com.libin.test.api.ApiMethodService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by libin on 2017/8/18.
 */
@Configuration
public class BeanConfigures {

    @Bean
    public ApiMethodService getApiMethodService (){
        return new ApiMethodService();
    }

}
