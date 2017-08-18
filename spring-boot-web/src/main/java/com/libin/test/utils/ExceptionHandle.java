package com.libin.test.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.regex.Pattern;

/**
 * Created by libin on 2016/10/26.
 */
public class ExceptionHandle {
    private static final Log log = LogFactory.getLog(ExceptionHandle.class);
    private static Pattern p = Pattern.compile("\\{requesttimestap=\\d*\\}");
        public static void exceptionHandle(String content, Exception e){

        }

    public static void errorHandle(String content){

    }

    public static void main(String[] args) {
//        jedisCluster.get("1");
//        System.out.println(jedisCluster.get("11"));
        String a = "100.116.36.1r-agent=AHC/1.0}] url[http://www.owhat.cn/api] param:{requesttimestap=1486955417069}] param:{}";
        System.out.println(a.contains("100.116.36"));
        System.out.println(a.contains("param:{}"));


        System.out.println(p.matcher(a).find());
    }
}
