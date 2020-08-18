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
@Table(name="uma_receipt_type")
public class ReceiptType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 收款类型
    @Column(name = "name")
    private String name;

    // 创建日期
    @Column(name = "create_date")
    private Timestamp createDate;

    // 是否可用
    @Column(name = "enable")
    private Boolean enable;

    // 创建用户
    @Column(name = "create_user")
    private String createUser;

    public void copy(ReceiptType source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}