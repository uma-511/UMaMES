package me.zhengjie.uma_mes.utils;

import lombok.Data;

import java.io.Serializable;

@Data
public class DeliveryListVo implements Serializable {
    private String prodName;
    private String totalBag;
    private String totalNumber;
    private String unit;
    private String sellingPrice;
    private String totalPrice;
}
