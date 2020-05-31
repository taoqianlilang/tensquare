package com.tensquare.notice.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * (NoticeFreshMapper)实体类
 *
 * @author makejava
 * @since 2020-05-26 15:15:11
 */
@Data
@TableName("tb_notice_fresh")
public class NoticeFresh  {
    /**
    * 用户id
    */
    private String userid;
    /**
    * 通知id
    */
    private String noticeid;

}