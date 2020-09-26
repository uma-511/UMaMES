package me.zhengjie.uma_mes.service.dto;

import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ChemicalFiberStockWarehousingDetailDTO implements Serializable {

    private Integer id;

    // 入库单id
    private Integer warehousingId;

    // 产品名称
    private String prodName;

    // 入库数量
    private BigDecimal warehousingNumber;

    // 产品编号
    private String prodModel;

    // 单位
    private String unit;

    // 单价
    private BigDecimal price;

    // 总价
    private BigDecimal totalPrice;

    // 产品id
    private Integer stockId;

    // 入库单编号
    private String scanNumber;

    // 产品备注
    private String remark;

    // 产品Id
    private Integer prodId;

    private Integer ton;

    private Integer branch;
}
