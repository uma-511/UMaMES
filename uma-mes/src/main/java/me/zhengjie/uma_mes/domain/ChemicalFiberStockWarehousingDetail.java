package me.zhengjie.uma_mes.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "uma_chemical_fiber_stock_warehousing_detail")
public class ChemicalFiberStockWarehousingDetail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 入库单id
    @Column(name = "warehousing_id")
    private Integer warehousingId;

    // 产品名称
    @Column(name = "prod_name")
    private String prodName;

    // 产品id
    @Column(name = "prod_id")
    private Integer prodId;

    // 入库数量
    @Column(name = "warehousing_number")
    private BigDecimal warehousingNumber;

    // 产品编号
    @Column(name = "prod_model")
    private String prodModel;

    // 单位
    @Column(name = "unit")
    private String unit;

    // 单价
    @Column(name = "price")
    private BigDecimal price;

    // 总价
    @Column(name = "total_price")
    private BigDecimal totalPrice;

    // 库存id
    @Column(name = "stock_id")
    private Integer stockId;

    // 入库单编号
    @Column(name = "scan_number")
    private String scanNumber;

    // 产品备注
    @Column(name = "remark")
    private String remark;
}
