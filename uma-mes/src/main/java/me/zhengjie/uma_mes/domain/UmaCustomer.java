package me.zhengjie.uma_mes.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Entity
@Data
@Table(name="uma_customer")
public class UmaCustomer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 客户名称
    @Column(name = "name")
    private String name;

    // 客户编号
    @Column(name = "code")
    private String code;

    // 客户地址
    @Column(name = "address")
    private String address;

    // 联系人
    @Column(name = "contacts")
    private String contacts;

    // 联系电话
    @Column(name = "contact_phone")
    private String contactPhone;

    // 备注
    @Column(name = "remark")
    private String remark;

    // 创建日期
    @Column(name = "create_date")
    private Timestamp createDate;

    // 创建人
    @Column(name = "create_user")
    private String createUser;

    // 删除标识
    @Column(name = "del_flag")
    private Integer delFlag;

    public void copy(UmaCustomer source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}