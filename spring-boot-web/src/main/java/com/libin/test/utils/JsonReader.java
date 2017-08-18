package com.libin.test.utils;


import com.alibaba.fastjson.JSON;

import java.util.List;


/**
 * Created by libin on 2016/10/20.
 */
public class JsonReader {

    public static <T> T fetchObject(String object, Class<T> objectType, boolean collectionType) {
        if (collectionType) {
            List<T> t1 = JSON.parseArray(object, objectType);
            return (T) t1;
        } else {
            T t1 = JSON.parseObject(object, objectType);
            return t1;
        }
    }



}
