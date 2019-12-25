package me.zhengjie.uma_mes.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;
import java.util.List;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Entity
@Data
@Table(name="uma_chemical_fiber_production")
public class ChemicalFiberProduction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 订单编号
    @Column(name = "number",nullable = false)
    private String number;

    // 产品id
    @Column(name = "prod_id")
    private Integer prodId;

    // 产品型号
    @Column(name = "prod_model")
    private String prodModel;

    // 产品名称
    @Column(name = "prod_name")
    private String prodName;

    // 产品颜色
    @Column(name = "prod_color")
    private String prodColor;

    // 产品纤度
    @Column(name = "prod_fineness")
    private String prodFineness;

    // 客户id
    @Column(name = "customer_id")
    private Integer customerId;

    // 客户名称
    @Column(name = "customer_name")
    private String customerName;

    // 联系电话
    @Column(name = "customer_contact_phone")
    private String customerContactPhone;

    // 联系人
    @Column(name = "customer_contacts")
    private String customerContacts;

    // 客户地址
    @Column(name = "customer_address")
    private String customerAddress;

    // 客户编号
    @Column(name = "customer_code")
    private String customerCode;

    // 纸芯重量
    @Column(name = "core_weight")
    private BigDecimal coreWeight;

    // 每袋个数
    @Column(name = "per_bag_number")
    private Integer perBagNumber;

    // 计划生产数量
    @Column(name = "plan_number")
    private BigDecimal planNumber;

    // 完成数量
    @Column(name = "quantity_competed")
    private BigDecimal quantityCompeted;

    // 生产进度(%)
    @Column(name = "production_schedule")
    private BigDecimal productionSchedule;

    // 交货日期
    @Column(name = "delivery_date")
    private Timestamp deliveryDate;

    // 机器编号
    @Column(name = "machine_number")
    private String machineNumber;

    // 备注
    @Column(name = "remark")
    private String remark;

    // 状态
    @Column(name = "status")
    private Integer status;

    // 制单时间
    @Column(name = "create_time")
    private Timestamp createTime;

    // 制单人
    @Column(name = "create_user")
    private String createUser;

    // 删除标识
    @Column(name = "del_flag")
    private Integer delFlag;

    @OneToMany(mappedBy = "productionId",cascade={CascadeType.PERSIST,CascadeType.REMOVE})
    private List<ChemicalFiberLabel> chemicalFiberLabels;

    public void copy(ChemicalFiberProduction source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}