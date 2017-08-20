package com.libin.test.beanconf;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.libin.test.api.ApiMethodService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;

/**
 * Created by libin on 2017/8/18.
 */
@Configuration
@MapperScan("com.libin.test.service.dao.mapper")
public class BeanConfigures {

    @Bean
    public ApiMethodService getApiMethodService() {
        return new ApiMethodService();
    }

    /**
     * 修改spring默认 jackjson  转换器为fastjsons
     * @return
     */
    @Bean
    public HttpMessageConverters fastJsonConverters() {
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        HttpMessageConverter httpMessageConverters = fastJsonHttpMessageConverter;
        return new HttpMessageConverters(httpMessageConverters);

    }
}
