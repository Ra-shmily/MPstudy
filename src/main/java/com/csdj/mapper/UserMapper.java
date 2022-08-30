package com.csdj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csdj.pojo.User;
import org.springframework.stereotype.Repository;

//在 对应的mapper上继承基本的类BaseMapper
@Repository
public interface UserMapper extends BaseMapper<User> {
}
