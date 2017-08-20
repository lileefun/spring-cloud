package com.libin.test;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;

/**
 * Created by libin on 2017/8/18.
 *  启动类
 * 如有问题联系我 li.leefun@gmail.com
 */

@SpringBootApplication
@MapperScan("com.libin.test.service.dao.mapper")
public class BootStrap {

    public static void main(String[] args) {
        SpringApplication.run(BootStrap.class, args);
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
