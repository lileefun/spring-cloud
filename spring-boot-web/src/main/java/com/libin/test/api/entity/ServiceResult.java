package com.libin.test.api.entity;

import com.libin.test.common.entity.ResultDTO;
import com.libin.test.constants.ErrorCode;
import com.libin.test.constants.ErrorMessages;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by libin on 2016/10/24.
 */
public class ServiceResult implements Serializable {


    /**
     * 状态 success:成功 fail:失败
     */
    @Getter
    @Setter
    private String result;
    /**
     * 状态码
     */
    @Getter
    @Setter
    private String code;
    /**
     * 状态描述
     */
    @Getter
    @Setter
    private String message;

    @Getter
    @Setter
    private Object data;

    @Getter
    @Setter
    private byte[] b;

    @Getter
    @Setter
    private String fileName;

    public ServiceResult() {

        super();

    }

    public ServiceResult(byte[] b) {
        this.b = b;
    }

    public ServiceResult(ResultDTO resultDTO) {
        super();
        resultDTO = resultDTO.clone();
        if (resultDTO.getStreamDTO() != null) {
            b = resultDTO.getStreamDTO().getB();
            fileName = resultDTO.getStreamDTO().getFileName();
        }

        if (resultDTO.getResult() == null) {
            //result没有设值，默认成功
            this.result = ErrorCode.SUCCESS;
            this.message = ErrorMessages.getString(this.result);
        } else {
            this.result = resultDTO.getResult();
            this.message = resultDTO.getMessage();
            this.code = resultDTO.getCode();
        }

        //list 需要更换返回参数
        if (resultDTO.getData() != null && resultDTO.getData() instanceof List) {
            resultDTO.setList(resultDTO.getData());//返回list
            resultDTO.setData(null);//data 置空
            if (resultDTO.getListData() != null) {
                resultDTO.setExtendData(resultDTO.getListData());
                resultDTO.setListData(null);
            }
            this.data = resultDTO;//返回ResultDTO
        } else {
            this.data = resultDTO.getData();
        }

        resultDTO.setCode(null);
        resultDTO.setResult(null);
        resultDTO.setMessage(null);
    }

    public ServiceResult(String resultCode) {
        this.message = ErrorMessages.getString(resultCode);
        code = resultCode;
        result = ErrorCode.FAIL;
    }

    public ServiceResult(Object data) {
        super();
        this.result = ErrorCode.SUCCESS;
        this.message = ErrorMessages.getString(this.result);
        this.data = data;
    }

    public ServiceResult(String resultCode, String message) {
        this.message = ErrorMessages.getString(resultCode) + " error: " + message;
        code = resultCode;
        result = ErrorCode.FAIL;
    }

    public ServiceResult(String resultCode, String message, boolean useErrorMessage) {
        this.message = message;
        code = resultCode;
        result = ErrorCode.FAIL;
    }
}
