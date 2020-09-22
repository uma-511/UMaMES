package me.zhengjie.uma_mes.service.dto;

import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class ChemicalFiberProductionReportDTO implements Serializable {


    // 生产单单号
    private String productionNumber;

    // 生产单id
    private Integer productionId;

    // 机台号
    private String machine;

    // 班次
    private String shifts;

    // 纤度
    private String fineness;

    // 色号
    private String color;

    // 生产包数
    private BigDecimal productionPacketNumber = new BigDecimal(0);

    // 生产个数
    private BigDecimal productionFactPerBagNumber = new BigDecimal(0);

    // 生产净重
    private BigDecimal productionNetWeight = new BigDecimal(0);

    // 生产毛重
    private BigDecimal productionGrossWeight = new BigDecimal(0);

    // 入库包数
    private BigDecimal warehousingPacketNumber = new BigDecimal(0);

    // 入库个数
    private BigDecimal warehousingFactPerBagNumber = new BigDecimal(0);

    // 入库净重
    private BigDecimal warehousingNetWeight = new BigDecimal(0);

    // 入库毛重
    private BigDecimal warehousingGrossWeight = new BigDecimal(0);

    // 入库包数
    private BigDecimal toVoidPacketNumber = new BigDecimal(0);

    // 入库个数
    private BigDecimal toVoidFactPerBagNumber = new BigDecimal(0);

    // 入库净重
    private BigDecimal toVoidNetWeight = new BigDecimal(0);

    // 入库毛重
    private BigDecimal toVoidGrossWeight = new BigDecimal(0);

    // 产品id
    private Integer prodId;

    // 时间
    private Timestamp time;


}
