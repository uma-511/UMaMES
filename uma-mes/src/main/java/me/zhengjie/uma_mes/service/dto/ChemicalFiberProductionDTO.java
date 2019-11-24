package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;


/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Data
public class ChemicalFiberProductionDTO implements Serializable {

    private Integer id;

    // 订单编号
    private String number;

    // 产品id
    private Integer prodId;

    // 产品型号
    private String prodModel;

    // 产品名称
    private String prodName;

    // 产品颜色
    private String prodColor;

    // 产品纤度
    private String prodFineness;

    // 客户id
    private Integer customerId;

    // 客户名称
    private String customerName;

    // 联系电话
    private String customerContactPhone;

    // 联系人
    private String customerContacts;

    // 客户地址
    private String customerAddress;

    // 客户编号
    private String customerCode;

    // 纸芯重量
    private BigDecimal coreWeight;

    // 每袋个数
    private Integer perBagNumber;

    // 计划生产数量
    private BigDecimal planNumber;

    // 完成数量
    private BigDecimal quantityCompeted;

    // 生产进度(%)
    private BigDecimal productionSchedule;

    // 交货日期
    private Timestamp deliveryDate;

    // 机器编号
    private String machineNumber;

    // 备注
    private String remark;

    // 状态
    private Integer status;

    // 制单时间
    private Timestamp createTime;

    // 制单人
    private String createUser;

    // 删除标识
    private Integer delFlag;
}