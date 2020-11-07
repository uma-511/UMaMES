package me.zhengjie.uma_mes.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
* @author Xie Ji Biao
* @date 2020-10-30
*/
@Entity
@Data
@Table(name="uma_chemical_fiber_customer_order")
public class ChemicalFiberCustomerOrder implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 客户id
    @Column(name = "customer_id")
    private Integer customerId;

    // 订单编号
    @Column(name = "customer_number")
    private String customerNumber;

    // 客户名称
    @Column(name = "customer_name")
    private String customerName;

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

    // 状态
    @Column(name = "status")
    private Integer status;

    // 状态
    @Column(name = "warehousing_date")
    private Timestamp warehousingDate;

    // 删除标识
    @Column(name = "del_flag")
    private Integer delFlag;

    // 备注
    @Column(name = "remark")
    private String remark;

    // 客户单号
    @Column(name = "contact_number")
    private String contactNumber;

    // 传真
    @Column(name = "fax")
    private String fax;

    // 制单人
    @Column(name = "create_user")
    private String createUser;

    // 送货时间
    @Column(name = "device_date")
    private Timestamp deviceDate;

    public void copy(ChemicalFiberCustomerOrder source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
