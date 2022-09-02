package com.csdj;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csdj.mapper.UserMapper;
import com.csdj.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

/**
 * Author : 今晚几点睡_Ada
 * Date: 2022/9/1
 * Time: 8:17
 * 注:
 */
@SpringBootTest
public class WrapperTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
     //查询name email 不为空 年龄大于18
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNotNull("name")
        .isNotNull("email").ge("age",18);
        userMapper.selectList(queryWrapper).forEach(System.out::println);
    }

    @Test
    void test2(){
        //查询名字为ada的
        //查询一个数据   出现多个结果使用list 或者 map
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("name", "Sandy"));
        System.out.println("user = " + user);
    }
    @Test
    void test3(){
        //查询年龄在10-18
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("age",10,18);
        //查询结果数
        System.out.println("count = " + userMapper.selectCount(queryWrapper));
    }
    //模糊查询
    @Test
    void test4(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper
                //左和右%e%
                .notLike("name","ada")
                .likeRight("email","t");
        List<Map<String, Object>> maps = userMapper.selectMaps(queryWrapper);
        maps.forEach(System.out::println);
    }
    //子查询
    @Test
    void test5(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //id在子查询  中查出来
        queryWrapper.inSql("id","select id from user where id<=20");
       userMapper.selectObjs(queryWrapper).forEach(System.out::println);

    }
    //order by
    @Test
    void test6(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //id在子查询  中查出来
       // queryWrapper.orderByDesc("age");
        queryWrapper.orderByAsc("age");
        userMapper.selectList(queryWrapper).forEach(System.out::println);
    }
    @Test
    void test7(){

    }
}
