package me.zhengjie.uma_mes.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @author Xie Ji Biao
* @date 2020-08-14
*/
@Entity
@Data
@Table(name="uma_account_name")
public class AccountName implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 账户名称
    @Column(name = "name")
    private String name;

    // 创建时间
    @Column(name = "create_time")
    private Timestamp createTime;

    // 创建用户
    @Column(name = "create_user")
    private String createUser;

    // 是否可用
    @Column(name = "enable")
    private Boolean enable;

    public void copy(AccountName source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}