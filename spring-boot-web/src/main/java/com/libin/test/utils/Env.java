package com.libin.test.utils;

public class Env {
    private static String env = System.getProperty("env");

    public Env() {
    }

    public static boolean isTest() {
        return "TEST".equals(env) || "TEST".toLowerCase().equals(env);
    }

    public static boolean isDev() {
        return env == null || "DEV".equals(env) || "DEV".toLowerCase().equals(env);
    }

    public static boolean isProd() {
        return "PROD".equals(env) || "PROD".toLowerCase().equals(env);
    }

    public static String getEnv() {
        return isProd()?"PROD":(isTest()?"TEST":"DEV");
    }
}
