package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import me.zhengjie.uma_mes.domain.ChemicalFiberDeliveryDetail;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
public class ChemicalFiberDeliveryNoteSalesReportDTO implements Serializable {
    // 单号
    private String scanNumber;
    // 客户名称
    private String customerName;
    /*// 出库包数
    private BigDecimal outOfStockPackageNumber = new BigDecimal(0);
    // 出库个数
    private BigDecimal outOfStockFactPerBagNumber = new BigDecimal(0);
    // 出库净重
    private BigDecimal outOfStockNetWeight = new BigDecimal(0.0);
    // 应收金额
    private BigDecimal receivablePrice = new BigDecimal(0.0);*/
    // 总成本
    private BigDecimal totalCost = new BigDecimal(0.0);
    // 制单时间
    private Timestamp createDate;

    // 交付日期
    private Timestamp deliveryDate;

    // 订单状态：0-销毁 1-待打印 2-待出库 3-待签收 4-回填 5-待结款 6-完结
    private Integer noteStatus;

    // 结款后余数
    private BigDecimal remainder = new BigDecimal(0.0);

    // 总价
    private BigDecimal totalPrice = new BigDecimal(0.0);

    // 总价
    private BigDecimal sumTotalPrice = new BigDecimal(0.0);

    // 制单人
    private String createUser;


    // 详情列表
    private List<ChemicalFiberDeliveryDetail> chemicalFiberDeliveryDetails;
}
