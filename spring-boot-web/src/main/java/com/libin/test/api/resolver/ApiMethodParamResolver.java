package com.libin.test.api.resolver;

import com.libin.test.api.entity.ApiMethodParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by libin on 2016/10/25.
 */
public interface ApiMethodParamResolver {

    public boolean supportCheck(ApiMethodParam apiMethodParam);

    public Object getParamObject(ApiMethodParam apiMethodParam, HttpServletRequest request, HttpServletResponse response, boolean collectionType) throws Exception;

}
