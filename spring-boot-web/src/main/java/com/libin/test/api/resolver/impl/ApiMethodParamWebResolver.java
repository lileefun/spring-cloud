package com.libin.test.api.resolver.impl;

import com.alibaba.fastjson.JSON;
import com.libin.test.api.entity.ApiMethodParam;
import com.libin.test.api.exception.ACPIllegalArgumentException;
import com.libin.test.api.resolver.ApiMethodParamResolver;
import com.libin.test.common.entity.RequestClientInfo;
import com.libin.test.common.entity.UserAuthInfo;
import com.libin.test.constants.HttpConstants;
import com.libin.test.service.user.model.UserDTO;
import com.libin.test.utils.HttpTools;
import com.libin.test.utils.JsonReader;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Type;

/**
 * Created by libin on 2016/10/25.
 */
public class ApiMethodParamWebResolver implements ApiMethodParamResolver {

    private static final Class[] SERVLET_TYPE_CLASS = {HttpServletRequest.class, ServletRequest.class, HttpServletResponse.class,
            ServletResponse.class, MultipartRequest.class, MultipartFile.class, RequestClientInfo.class, UserAuthInfo.class};


    @Override
    public boolean supportCheck(ApiMethodParam apiMethodParam) {
        return ArrayUtils.contains(SERVLET_TYPE_CLASS, apiMethodParam.getParamType());
    }

    @Override
    public Object getParamObject(ApiMethodParam apiMethodParam, HttpServletRequest request, HttpServletResponse response, boolean collectionType) {
        if (supportCheck(apiMethodParam)) {
            Type type = apiMethodParam.getParamType();

            String paramName = apiMethodParam.getParamName();

            if (type.equals(HttpServletRequest.class) || type.equals(ServletRequest.class)) {
                return request;
            } else if (type.equals(HttpServletResponse.class) || type.equals(ServletResponse.class)) {
                return response;
            } else if (type.equals(UserAuthInfo.class)) {
                UserAuthInfo userAuthInfo = null;
                if (apiMethodParam.getPermissionKey() != null) {
                    userAuthInfo = new UserAuthInfo();
                    userAuthInfo.setPermissionKey(apiMethodParam.getPermissionKey());
                }
                return userAuthInfo;

            }else if (type.equals(RequestClientInfo.class)) {
                String paramValue = request.getParameter(HttpConstants.PARAM_CLIENT);
                RequestClientInfo requestClientInfo = JsonReader.fetchObject(paramValue, RequestClientInfo.class,false);
                RequestClientInfo requestClientInfo1 = new RequestClientInfo();

                String userInfo = (String) request.getAttribute(HttpConstants.ATTRIBUTE_USERID);

                String paramToken = request.getParameter(HttpConstants.PARAM_TOKEN);
                /*if(StringUtils.isEmpty(paramToken)){
                    throw new ACPIllegalArgumentException(String.format("client.%s 参数不能为空", HttpConstants.PARAM_TOKEN));
                }*/
                String paramUserid = request.getParameter(HttpConstants.PARAM_USERID);
                Long userId = null;
                if (StringUtils.isNotEmpty(paramUserid)) {
                    userId = NumberUtils.toLong(paramUserid, 0L);
                    // userId = NumberUtils.toLong(SecurityUtils.rc4Decrypt(paramUserid), 0L);
                }
                /*if(StringUtils.isEmpty(paramUserid)){
                    throw new ACPIllegalArgumentException(String.format("client.%s 参数不能为空", HttpConstants.PARAM_USERID));
                }*/

                if (StringUtils.isNotEmpty(userInfo) && userId != null) {
                    //解析userInfo
                    UserDTO userDTO = JSON.parseObject(userInfo, UserDTO.class);
                    if (userDTO == null || NumberUtils.compare(userDTO.getUserId(), userId) != 0) {
                        throw new ACPIllegalArgumentException("登录用户参数异常");
                    } else {
                        requestClientInfo1.setUserIdstr(paramUserid);
                        requestClientInfo1.setUserId(userDTO.getUserId());
                        requestClientInfo1.setUserType(userDTO.getUserType());
                        requestClientInfo1.setUserNickName(userDTO.getNickName());
                        requestClientInfo1.setUserToken(paramToken);
                    }
                }

                String paramLat = request.getParameter(HttpConstants.PARAM_LAT);
               /* if(StringUtils.isEmpty(paramLat)){
                    throw new ACPIllegalArgumentException(String.format("client.%s 参数不能为空", HttpConstants.PARAM_LAT));
                }*/
                String paramLng = request.getParameter(HttpConstants.PARAM_LNG);
                /*if(StringUtils.isEmpty(paramLng)){
                    throw new ACPIllegalArgumentException(String.format("client.%s 参数不能为空", HttpConstants.PARAM_LNG));
                }*/
                String paramV = request.getParameter(HttpConstants.PARAM_VER);
                if (StringUtils.isBlank(paramV)) {
                    throw new ACPIllegalArgumentException(String.format("client.%s 参数不能为空", HttpConstants.PARAM_VER));
                }

                requestClientInfo1.setToken(paramToken);
                //requestClientInfo1.setUserId(userId);
                requestClientInfo1.setLat(paramLat);
                requestClientInfo1.setLng(paramLng);
                requestClientInfo1.setVersion(paramV);

                UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
                if (requestClientInfo != null) {
                    requestClientInfo1.setChannel(requestClientInfo.getChannel());
                    requestClientInfo1.setDeviceId(requestClientInfo.getDeviceId());
                    requestClientInfo1.setPlatform(requestClientInfo.getPlatform());
                    requestClientInfo1.setVersion(requestClientInfo.getVersion());
                    if (requestClientInfo1.getVersion() == null) {
                        throw new ACPIllegalArgumentException("client.version 参数不能为空");
                    }
                    String s = requestClientInfo.getVersion().replaceAll("\\.", "");
                    int v = Integer.valueOf(s);
                    requestClientInfo1.setVersionDigit(v);

                } else {
                    throw new ACPIllegalArgumentException("client 参数不能为空");
                }

                if (requestClientInfo1.getChannel() == null) {
                    throw new ACPIllegalArgumentException("client.channel 参数不能为空");
                }
                if (requestClientInfo1.getDeviceId() == null) {
                    throw new ACPIllegalArgumentException("client.deviceid 参数不能为空");
                }
                if (requestClientInfo1.getPlatform() == null) {
                    throw new ACPIllegalArgumentException("client.platform 参数不能为空");
                }

                requestClientInfo1.setUserAgent(userAgent);
                requestClientInfo1.setClientIp(HttpTools.getIpAddress(request));

                String header = request.getHeader("user-agent");
                if (!StringUtils.isBlank(header)) {
                    if (header.toLowerCase().indexOf("micromessenger") > 0) {// 是微信浏览器
                        requestClientInfo1.setWeiXin(true);
                    } else {
                        requestClientInfo1.setWeiXin(false);
                    }
                } else {
                    requestClientInfo1.setWeiXin(false);
                }

                return requestClientInfo1;
            } else if (type.equals(MultipartFile.class)) {
                if (request instanceof MultipartRequest) {
                    MultipartRequest multipartRequest = (MultipartRequest) request;
                    return multipartRequest.getFile(paramName);
                }
            }
        }
        return null;
    }
}
