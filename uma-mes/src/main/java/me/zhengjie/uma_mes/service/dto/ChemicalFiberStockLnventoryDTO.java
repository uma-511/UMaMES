package me.zhengjie.uma_mes.service.dto;

import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class ChemicalFiberStockLnventoryDTO implements Serializable {

    private Integer id;

    // 盘点单单号
    private String lnventoryNumber;

    // 盘点单名字
    private String lnventoryName;

    // 盘盈数量
    private Integer lnventorySurplus;

    // 盘亏数量
    private Integer lnventoryLoss;

    // 状态
    private Integer lnventoryStatus;

    // 制单人名字
    private String lnventoryUser;

    // 时间
    private Timestamp createDate;


    // 盘盈数量
    private String lnventorySurplusStr;

    // 盘亏数量
    private String lnventoryLossStr;

}
