package com.libin.test.api.controller;

import com.alibaba.fastjson.JSON;
import com.libin.test.api.ApiMethodService;
import com.libin.test.api.annotation.ApiMethod;
import com.libin.test.api.entity.AcpResponse;
import com.libin.test.api.entity.ApiMethodMapping;
import com.libin.test.api.entity.ServiceResult;
import com.libin.test.api.exception.ACPIllegalArgumentException;
import com.libin.test.api.exception.BaseException;
import com.libin.test.constants.ErrorCode;
import com.libin.test.constants.ErrorMessages;
import com.libin.test.constants.HttpConstants;
import com.libin.test.service.user.model.UserDTO;
import com.libin.test.utils.Env;
import com.libin.test.utils.ExceptionHandle;
import com.libin.test.utils.HttpTools;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by libin on 2017/8/18.
 */
@RestController
public class ServiceController {

    Logger logger = LoggerFactory.getLogger(ServiceController.class);


    @Autowired
    private ApiMethodService apiMethodService;

    @RequestMapping(value = "/*")
    public ModelAndView apiMethodService(HttpServletRequest request, HttpServletResponse response) {
        AcpResponse acpResult = new AcpResponse(request, response);
        try {

            ApiMethodMapping apiMethodMapping = apiMethodService.lookupApiMethodCommand(request.getMethod(),request.getParameter(HttpConstants.PARAM_CMD_METHOD), request.getParameter(HttpConstants.PARAM_CMD_SERVICE));

            if (apiMethodMapping == null) {
                acpResult.write(new ServiceResult(ErrorCode.E_SYS_UNKNOWN_METHOD));
                errorInfo(request, response, null, true);
                logger.error(ErrorCode.E_SYS_UNKNOWN_METHOD);
                return null;
            }

            if (!authValidate(request, apiMethodMapping, acpResult)) {
                return null;
            }


            ServiceResult serviceResult = apiMethodService.executeMethod(apiMethodMapping, request, response);


            acpResult.write(serviceResult);


        } catch (ACPIllegalArgumentException e) {
            if (Env.isProd()) {
                acpResult.write(new ServiceResult(ErrorCode.E_SYS_PARAM_PROD));
            } else {
                acpResult.write(new ServiceResult(ErrorCode.E_SYS_PARAM, e.getMessage()));
            }
            errorInfo(request, response, e, true);
        } catch (BaseException e) {
            acpResult.write(new ServiceResult(e.getCode(), e.getMessage(), true));
            errorInfo(request, response, e, true);
        } catch (Exception e) {
            if (Env.isProd()) {
                acpResult.write(new ServiceResult(ErrorCode.E_SYS_UNKNOWN_PROD));
            } else {
                acpResult.write(new ServiceResult(ErrorCode.E_SYS_UNKNOWN, e + ":" + e.getMessage()));
            }
            errorInfo(request, response, e, true);
        }
        return null;
    }

    private void errorInfo(HttpServletRequest request, HttpServletResponse response, Exception e, boolean sendEmail) {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map map2 = new HashMap<>();
        if (parameterMap != null) {
            Iterator<Map.Entry<String, String[]>> iterator = parameterMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String[]> next = iterator.next();
                String key = next.getKey();
                String[] value = next.getValue();
                map2.put(key, value[0]);
            }
        }

        String message = "ip[" + HttpTools.getIpAddress(request) + "] headers[" + map + "] url[" + request.getRequestURL() + "] "
                + " param:" + map2;
        logger.error(message);
        logger.error(String.valueOf(this), e);
        if (sendEmail) {
            if (e != null) {
                ExceptionHandle.exceptionHandle(message, e);
            } else {
                ExceptionHandle.errorHandle(message);

            }
        }
    }

    /**
     * 授权检测
     *
     * @param request
     * @param apiMethodMapping
     * @param acpResult
     * @return
     */
    private boolean authValidate(HttpServletRequest request, ApiMethodMapping apiMethodMapping, AcpResponse acpResult) {
        String token = request.getParameter(HttpConstants.PARAM_TOKEN);
        String userInfo = null;
        if (StringUtils.isNotBlank(token)) {
            // userInfo = jedisCluster.get(RedisKeyConsts.BUSINESS_USER_LOGIN_TOKEN + token);
        }

        if (apiMethodMapping.getAuth() == ApiMethod.Auth.REQUIRED) {
            if (StringUtils.isBlank(token)) {
                acpResult.write(new ServiceResult(ErrorCode.E_SYS_INVALID_TOKEN, ErrorMessages.getField("E_SYS_INVALID_TOKEN"), true));
                return false;
            }

            if (StringUtils.isBlank(userInfo)) {
                acpResult.write(new ServiceResult(ErrorCode.E_SYS_INVALID_TOKEN));
                return false;
            }

            UserDTO userDTO = JSON.parseObject(userInfo, UserDTO.class);

            if (userDTO == null) {
                acpResult.write(new ServiceResult(ErrorCode.E_SYS_INVALID_TOKEN, ErrorMessages.getField("E_SYS_INVALID_TOKEN"), true));
                return false;
            }

            String paramUserid = request.getParameter(HttpConstants.PARAM_USERID);

            if (StringUtils.isBlank(paramUserid)) {
                acpResult.write(new ServiceResult(ErrorCode.E_SYS_INVALID_TOKEN, ErrorMessages.getField("E_SYS_INVALID_TOKEN"), true));
                return false;
            }

            Long userId = NumberUtils.toLong(paramUserid);
            if (NumberUtils.compare(userDTO.getUserId(), userId) != 0) {
                acpResult.write(new ServiceResult(ErrorCode.E_SYS_INVALID_TOKEN, ErrorMessages.getField("E_SYS_INVALID_TOKEN"), true));
                return false;
            }


            //判断用户类型权限
            String[] userTypeArr = apiMethodMapping.getUserType();
            if (userTypeArr != null) {
                //检测用户权限类型
                Integer userType = userDTO.getUserType();
                if (!Arrays.asList(userTypeArr).contains(userType + "")) {
                    acpResult.write(new ServiceResult(ErrorCode.E_SYS_PERMISSION_DENY));
                    return false;
                }
            }


        }

        request.setAttribute(HttpConstants.ATTRIBUTE_USERID, userInfo);
        return true;

    }


}
