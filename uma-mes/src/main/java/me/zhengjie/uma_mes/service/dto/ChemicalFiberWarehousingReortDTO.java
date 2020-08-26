package me.zhengjie.uma_mes.service.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class ChemicalFiberWarehousingReortDTO {


    private String scanNumber;

    private Timestamp warehousingDate;

    private BigDecimal warehousingNumber;

    private String supplierName;

    private String prodName;

    private String unit;

    private BigDecimal price;

    private BigDecimal totalPrice;

    private String createUser;

    private Timestamp createDate;


}
