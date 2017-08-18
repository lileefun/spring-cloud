package com.libin.test.common.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by libin on 2017/1/16.
 */
@Data
public class BaseDTO implements Serializable {

    private static final long serialVersionUID = 7684745244521023709L;
    //图片压缩
    private String cropCover;	 	//列表图片

    private String imgCropCover;	 	//美图列表图片

    private String cropUser;	 	//用户头像

    private String cropUserThumb;	//用户小尺寸头像

    private String cropComment;  //社区压缩图片

    private String cropMaster; //达人列表缩略图
}
