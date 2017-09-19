package com.feignext;

import lombok.Data;

/**
 * Created by libin on 2017/9/19.
 */
@Data
public class BeanClass<T> {

    /**
     * 服务名
     */
    private String serviceName;

    /**
     * 引用的类
     */
    private T ref;


}
