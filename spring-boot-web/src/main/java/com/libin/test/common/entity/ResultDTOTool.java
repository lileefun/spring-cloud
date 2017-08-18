package com.libin.test.common.entity;

import com.github.pagehelper.Page;

import java.util.List;

/**
 * Created by libin on 2017/1/16.
 */
public class ResultDTOTool {
    public static ResultDTO setSuccess(ResultDTO result) {
        result = result == null ? new ResultDTO() : result;
        result.setResult(ResultDTO.SUCCESS);
        result.setMessage(PromptUtil.v("sys_success"));
        return result;
    }

    public static ResultDTO setSuccess(ResultDTO result, String promptCode) {
        result.setResult(ResultDTO.SUCCESS);
        //result.setCode(promptCode);
        result.setMessage(PromptUtil.v(promptCode));
        return result;
    }

    public static ResultDTO setSuccess(ResultDTO result, String promptCode,String... changeText) {
        result.setResult(ResultDTO.SUCCESS);
        //result.setCode(promptCode);
        result.setMessage(String.format(PromptUtil.v(promptCode), changeText));
        return result;
    }

    public static ResultDTO setError(ResultDTO result) {
        result = result == null ? new ResultDTO() : result;
        result.setResult(ResultDTO.FAIL);
        return result;
        //TODO 设置code message1
    }

    public static ResultDTO setError(ResultDTO result, String promptCode) {
        result = result == null ? new ResultDTO() : result;
        result.setResult(ResultDTO.FAIL);
        result.setCode(promptCode);
        result.setMessage(PromptUtil.v(promptCode));
        return result;
    }

    public static ResultDTO setError(ResultDTO result, String promptCode, String message) {
        result = result == null ? new ResultDTO() : result;
        result.setResult(ResultDTO.FAIL);
        result.setCode(promptCode);
        result.setMessage(message);
        return result;
    }

    public static ResultDTO setErrorFormat(ResultDTO result, String promptCode,String... changeText) {
        result.setResult(ResultDTO.FAIL);
        result.setCode(promptCode);
        result.setMessage(String.format(PromptUtil.v(promptCode), changeText));
        return result;
    }

    public static ResultDTO setErrorParam(ResultDTO result) {
        result.setResult(ResultDTO.FAIL);
        result.setCode("sys_param_error");
        result.setMessage(PromptUtil.v("sys_param_error"));
        return result;
    }

    public static ResultDTO setErrorTimeout(ResultDTO result) {
        result.setResult(ResultDTO.FAIL);
        result.setMessage(PromptUtil.v("sys_timeout"));
        return result;
    }

    public static ResultDTO setErrorNoPermission(ResultDTO result) {
        result.setResult(ResultDTO.FAIL);
        result.setCode("user.nopermission.error");
        result.setMessage(PromptUtil.v("user.nopermission.error"));
        return result;
    }

    public static void setPage(ResultDTO resultDTO, Page page) {
        resultDTO.setPageNum(page.getPageNum());
        resultDTO.setPageSize(page.getPageSize());
        resultDTO.setPages(page.getPages());
        resultDTO.setTotal(page.getTotal());
    }

    public static void setPage(ResultDTO resultDTO, List list) {
        if (list instanceof Page) {
            Page page = (Page) list;
            setPage(resultDTO, page);
        } else {
            //FIXME
            resultDTO.setPageNum(0);
            resultDTO.setPageSize(0);
            resultDTO.setPages(0);
            resultDTO.setTotal(0L);
        }
    }

}
