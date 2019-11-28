package me.zhengjie.uma_mes.utils;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DeliveryVo implements Serializable {
    private String customerName;
    private String customerAddress;
    private String contacts;
    private String contactPhone;
    private String scanNumber;
    private String createDate;
    private String total;
    private String capitalizationTotal;
    private List<DeliveryListVo> deliveryList;
}
