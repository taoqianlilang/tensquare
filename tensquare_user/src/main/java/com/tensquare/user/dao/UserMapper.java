package com.tensquare.user.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tensquare.user.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: taoqianlilang
 * @Description:
 * @Date: Created in 22:00 2020/5/24
 * @Modified By:
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
