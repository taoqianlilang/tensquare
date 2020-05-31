package com.tensquare.user.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: taoqianlilang
 * @Description:
 * @Date: Created in 21:58 2020/5/24
 * @Modified By:
 */
@TableName("tb_user")
@Data
public class User implements Serializable {

    @TableId(type = IdType.INPUT)
    private String id;

    private String mobile;
    private String password;
    private String nickname;
    private String sex;
    private Date birthday;
    private String avatar;
    private String email;
    private Date regdate;
    private Date updatedate;
    private Date lastdate;
    private Long online;
    private String interest;
    private String personality;
    private Integer fanscount;
    private Integer followcount;

}
