---

---

# Mybatis-plus



`依赖`

```xml
   <!--数据库驱动-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
        <!--mybatis_plus-->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.4.2</version>
        </dependency>
```



mapper

```java
//在 对应的mapper上继承基本的类BaseMapper
@Repository
public interface UserMapper extends BaseMapper<User> {
}
```



`配置日志`

```yml
    #配置日志
mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl 
```



## CRUD扩展 

```java
 @Test
    void testinsert(){
        User user = new User();
        user.setName("今晚几点睡Ada");
        user.setAge(18);
        user.setEmail("2421312133@qq.com");
        int insert = userMapper.insert(user);
        System.out.println("insert = " + insert);
        System.out.println("user = " + user);
    }
```

![image-20220829092904737](C:\Users\1\AppData\Roaming\Typora\typora-user-images\image-20220829092904737.png)



`数据库插入的id默认值为：全局唯一id`

### 主键生成策略

------

`@TableId(type = IdType.ID_WORKER)`默认方案  全局唯一id 	

`snowflake`：雪花算法

snowflake是Twitter开源的分布式ID生成算法，结果是一个long型的ID。

使用41bit作为毫秒数，10bit作为机器的ID（5个bit是数据中心，5个bit的机器ID），12bit作为毫秒内的流水号（意味着每个节点在每毫秒可以产生 4096 个 ID），最后还有一个符号位，永远是0。



------

主键自增

1. 实体类字段上`@TableId(type = IdType.AUTO)`
2. 数据库字段一定要是自增的

![image-20220829095332111](C:\Users\1\AppData\Roaming\Typora\typora-user-images\image-20220829095332111.png)





------






```java
AUTO(0), //数据库id自增
NONE(1), //没有操作
INPUT(2),//手动输入   需要自己配置id
ASSIGN_ID(3),//默认的全局id
ASSIGN_UUID(4),
/** @deprecated */
@Deprecated
ID_WORKER(3),
/** @deprecated */
@Deprecated
ID_WORKER_STR(3),// idworker的字符串表示法
/** @deprecated */
@Deprecated
UUID(4);//全局唯一的id 
```





## 更新操作

```java
@Test
void testupdate(){
    User user = new User();
    //通过 条件动态拼接SQL
    user.setName("今晚几点睡Ada");
    user.setAge(18);
    user.setEmail("2421312133@qq.com");
    user.setId(1L);
    //updateById   但是参数是一个对象
    userMapper.updateById(user);
}
```



所有的sql都是帮你自动拼接



## 自动填充

创建事件 修改时间  操作都是自动化完成的	





```java
//字段添加填充内容
@TableField(fill = FieldFill.INSERT)
private Date creat_time;
@TableField(fill = FieldFill.INSERT_UPDATE)
private Date update_time;
```

编写处理器 处理注解



```java
@Slf4j
@Component //一定不要忘记吧处理器加到Ioc容器中
public class MyMetaObjectHandler implements MetaObjectHandler {

    //插入时的填充策略
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill...");
        // setFieldValByName(String fieldName, Object fieldVal, MetaObject metaObject)
        this.setFieldValByName("creat_time",new Date(),metaObject);
        this.setFieldValByName("update_time",new Date(),metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill...");
        this.setFieldValByName("update_time",new Date(),metaObject);
    }
}
```





## 乐观锁



`乐观锁` 

顾名思义 他总是认为不会出现问题，无论干什么都不去上锁

如果出现了问题，再次更新值测试

 `悲观锁`

十分悲观，他认为总是会出现问题，无论干什么都不会去上锁

再去操作

n

 OptimisticLockerInnerInterceptor（乐观锁实现）

- 取出记录时，获取当前 version
- 更新时，带上这个 version
- 执行更新时， set version = newVersion where version = oldVersion
- 如果 version 不对，就更新失败



数据库加字段 version 默认1

```java
 @Version//乐观锁注解
 private Integer version;
```



注册组件

```java
@MapperScan("com.csdj.mapper")
@EnableTransactionManagement
@Configuration
public class MPConfig {

    //注册乐观锁插件
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }

}
```

## 查询操做

```java
//SelectById
@Test
void testSelectById(){
    User user = userMapper.selectById(1L);
    System.out.println("user = " + user);
}
//批量查询
@Test
void testSelectByBatchId(){

    List<User> userList = userMapper.selectBatchIds(Arrays.asList(1, 2, 3));
    userList.forEach(System.out::println);
}
//条件查询之一 map
@Test
void testSelectByBatchIds(){
    HashMap<String, Object> map = new HashMap<>();
    //自定义查询
    map.put("name","ada");
    map.put("age",18);

    List<User> userList = userMapper.selectByMap(map);
    userList.forEach(System.out::println);
}
```

## 分页查询

1. 原始的  ：limit
2. pageHelper第三方插件
3. MP内置了分页插件

------

>   使用

配置拦截器组件

```java
    //分页插件
/*    @Bean
    public PaginationInterceptor paginationInnerInterceptor(){
        return new PaginationInterceptor();
    }  */
    //分页插件
    @Bean
    public PaginationInnerInterceptor paginationInnerInterceptor(){
        return new PaginationInnerInterceptor();
    }
```

直接使用page对象

```java
//测试分页查询
@Test
void TestPage() {
    //current 当前页
    //size  页面大小
    Page<User> page = new Page<>(1,5);
    Page<User> userPage = userMapper.selectPage(page,null);
    page.getRecords().forEach(System.out::println);
}
```



删除操作

基本的删除操作

```java
//测试删除
@Test
void testDel(){
    
    userMapper.deleteBatchIds(Arrays.asList(1,2));
    userMapper.deleteById(3);
    HashMap<String, Object> map = new HashMap<>();
    map.put("name","今晚几点睡Ada");
    userMapper.deleteByMap(map);

}
```

逻辑删除



> 物理删除：在数据库中直接一处
>
> 逻辑删除:在数据库中没有被删除，而是通过变量来让他失效！

管理员可以查看被删除的数据 以防止数据丢失 类似于回收站





在数据表中增加deleted字段





```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class User implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private Integer age;
    private String email;
    @Version//乐观锁注解
    private Integer version;
    @TableLogic//逻辑删除
    private Integer deleted;
    //字段添加填充内容
    @TableField(fill = FieldFill.INSERT)
    private Date creat_time;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date update_time;
}
```



配置

```yml
mybatis-plus:
  global-config:
    db-config:
      logic-delete-field: flag # 全局逻辑删除的实体字段名(since 3.3.0,配置后可以忽略不配置步骤2)
      logic-delete-value: 1 # 逻辑已删除值(默认为 1)
      logic-not-delete-value: 0 # 逻辑未删除值(默认为 0)
```

> 删除操作后 数据库中没有被移除 但是deleted变为1  查询的时候会自动过滤被逻辑删除的数据

![image-20220831182638385](C:\Users\1\AppData\Roaming\Typora\typora-user-images\image-20220831182638385.png)







## 性能分析插件

在开发中会遇到一些慢sql,测试，druid.....

MP也提供性能分析插件 ,如果超过这个时间就停止运行







## 条件构造器	



## 代码自动生成器