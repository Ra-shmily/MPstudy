package com.csdj;

import com.csdj.mapper.UserMapper;
import com.csdj.pojo.User;
import lombok.extern.log4j.Log4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Log4j
class MybatisPlusApplicationTests {

    @Autowired
    private UserMapper userMapper;
    @Test
    void contextLoads() {
        User user = userMapper.selectById(1564061432249384968L);
        System.out.println("user = " + user);
        List<User> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
    }

    @Test
   // @Transactional(propagation = Propagation.REQUIRED)
    void testinsert(){
        User user = new User();
        user.setName("ada");
        user.setAge(18);
        user.setEmail("2421312133@qq.com");
        int insert = userMapper.insert(user);
        System.out.println("insert = " + insert);
        System.out.println("user = " + user);
    }

    @Test
    void testupdate(){
        User user = new User();
        //通过 条件动态拼接SQL
        user.setName("今晚几点睡Ada");
        user.setAge(30);
        user.setEmail("2421312133@qq.com");
        user.setId(1564061432249384969L);
        //updateById   但是参数是一个对象
        log.info(userMapper.updateById(user));

    }
    @Test
    void testtime(){
        User user = new User();
        //通过 条件动态拼接SQL
        user.setId(1L);
        user.setName("RE");
        //updateById   但是参数是一个对象
        userMapper.updateById(user);
    }
    //乐观锁成功
    @Test
    void test1(){
        //查询用户信息
        User user = userMapper.selectById(1L);
        //修改用户信息
        user.setName("今晚几点睡RA");
        user.setEmail("123456789");
        //执行更新操作
        int i = userMapper.updateById(user);
        System.out.println("I====================================="+i);
    }
    //乐观锁失败 多线程下
    @Test
    void test2(){
        //线程1
        User user = userMapper.selectById(1L);
        user.setName("今晚几点睡RA");
        user.setEmail("123456789");
        //模拟另一个线程插队操作
        User user2 = userMapper.selectById(1L);
        user.setName("今晚几点睡Re");
        user.setEmail("111111");
        userMapper.updateById(user2);

       userMapper.updateById(user);


    }
}
