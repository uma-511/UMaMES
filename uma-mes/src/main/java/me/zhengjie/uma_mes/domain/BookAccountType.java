package me.zhengjie.uma_mes.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @author Xie Ji Biao
* @date 2020-08-15
*/
@Entity
@Data
@Table(name="uma_book_account_type")
public class BookAccountType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 记账项目
    @Column(name = "name")
    private String name;

    // 创建用户
    @Column(name = "create_user")
    private String createUser;

    // 创建日期
    @Column(name = "create_time")
    private Timestamp createTime;

    // 是否可用
    @Column(name = "enable")
    private Boolean enable;

    public void copy(BookAccountType source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}