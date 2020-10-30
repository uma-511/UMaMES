package me.zhengjie.uma_mes.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ChemicalFiberLabelTotalDTO implements Serializable {
    Integer totalNumber;
    BigDecimal totalWeight;
    Integer dayNumber;
    BigDecimal dayWeight;
}
