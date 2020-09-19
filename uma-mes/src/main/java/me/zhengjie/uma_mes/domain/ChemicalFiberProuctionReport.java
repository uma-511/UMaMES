package me.zhengjie.uma_mes.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Data
@Table(name="uma_chemical_fiber_production_report")
public class ChemicalFiberProuctionReport implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 生产单单号
    @Column(name = "production_number")
    private String productionNumber;

    // 生产单id
    @Column(name = "production_id")
    private Integer productionId;

    // 机台号
    @Column(name = "machine")
    private String machine;

    // 班次
    @Column(name = "shifts")
    private String shifts;

    // 纤度
    @Column(name = "fineness")
    private String fineness;

    // 色号
    @Column(name = "color")
    private String color;

    // 生产包数
    @Column(name = "production_packet_number")
    private BigDecimal productionPacketNumber;

    // 生产个数
    @Column(name = "production_fact_per_bag_number")
    private BigDecimal productionFactPerBagNumber;

    // 生产净重
    @Column(name = "production_net_weight")
    private BigDecimal productionNetWeight;

    // 生产毛重
    @Column(name = "production_gross_weight")
    private BigDecimal productionGrossWeight;

    // 入库包数
    @Column(name = "warehousing_packet_number")
    private BigDecimal warehousingPacketNumber;

    // 入库个数
    @Column(name = "warehousing_fact_per_bag_number")
    private BigDecimal warehousingFactPerBagNumber;

    // 入库净重
    @Column(name = "warehousing_net_weight")
    private BigDecimal warehousingNetWeight;

    // 入库毛重
    @Column(name = "warehousing_gross_weight")
    private BigDecimal warehousingGrossWeight;

    //
    @Column(name = "prod_id")
    private Integer prodId;

    @Column(name = "time")
    private Timestamp time;


}
