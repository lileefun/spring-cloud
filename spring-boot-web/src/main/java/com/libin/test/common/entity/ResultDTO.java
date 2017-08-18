package com.libin.test.common.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by libin on 2017/1/16.
 *
 */
public class ResultDTO<T> extends BaseDTO implements Serializable, Cloneable {
    public static final String SUCCESS = "success";
    public static final String FAIL = "fail";
    private static final long serialVersionUID = -5997583884539481798L;

    /**
     * 状态 success:成功 fail:失败
     */
    @Setter
    @Getter
    private String result;
    /**
     * 状态码
     */
    @Setter
    @Getter
    private String code;
    /**
     * 状态描述
     */
    @Setter
    @Getter
    private String message;
    @Setter
    @Getter
    private T data;
    @Setter
    @Getter
    private Object extendData;
    @Setter
    @Getter
    private T imgSuffix;
    /**
     * 列表
     */
    @Setter
    @Getter
    private T list;

    /**
     * 当前是第n页
     */
    @Setter
    @Getter
    private Integer pageNum;
    /**
     * 每页多少条数据
     */
    @Setter
    @Getter
    private Integer pageSize;
    /**
     * 总页数
     */
    @Setter
    @Getter
    private Integer pages;
    /**
     * 总数据数
     */
    @Setter
    @Getter
    private Long total;
    /**当前页数据数*//*
    private Integer count;*/
    /**
     * stream
     */
    @Setter
    @Getter
    private StreamDTO streamDTO;


   public boolean success() {
        return this.result != null && this.result.equals(this.SUCCESS);
    }

    public boolean fail() {
        return this.result == null || !this.result.equals(this.SUCCESS);
    }

    @Getter
    private Object listData;

    public void setListData(Object listData) {
        this.listData = listData;
    }


    @Override
    public ResultDTO clone() {
        ResultDTO resultDTO = null;
        try {
            resultDTO = (ResultDTO) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return resultDTO;
    }
}
