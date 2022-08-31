package com.csdj.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Author : 今晚几点睡_Ada
 * Date: 2022/8/29
 * Time: 8:51
 * 注:
 */
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
