package me.zhengjie.uma_mes.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "uma_chemical_fiber_stock_warehousing")
public class ChemicalFiberStockWarehousing implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 制单人
    @Column(name = "create_user")
    private String createUser;

    // 总价格
    @Column(name = "total_price")
    private BigDecimal totalPrice;

    // 入库单号
    @Column(name = "scan_number")
    private String scanNumber;

    // 制单时间
    @Column(name = "create_date")
    private Timestamp createDate;

    // 入库日期
    @Column(name = "warehousing_date")
    private Timestamp warehousingDate;

    // 供应商名字
    @Column(name = "supplier_name")
    private String supplierName;

    // 批号
    @Column(name = "batch_number")
    private Integer batchNumber;

    // 备注
    @Column(name = "remark")
    private String remark;

    // 状态码 0 待新增 1 待入库 2 入库成功
    @Column(name = "warehousing_status")
    private Integer warehousingStatus;

    // 主司机
    @Column(name = "driver_main")
    private String driverMain;

    // 副司机
    @Column(name = "driver_deputy")
    private String driverDeputy;

    // 副司机
    @Column(name = "escort_one")
    private String escortOne;

    // 副司机
    @Column(name = "escort_two")
    private String escortTwo;

    // 副司机
    @Column(name = "car_number")
    private String carNumber;



    public void copy(ChemicalFiberStockWarehousing source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }

}
