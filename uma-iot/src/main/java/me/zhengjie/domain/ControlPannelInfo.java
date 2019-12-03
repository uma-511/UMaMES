package me.zhengjie.domain;

import lombok.Data;

@Data
public class ControlPannelInfo {
    /**
     * 由数据库带出
     */
    String productionNumber;
    String customerCode;
    String fineness;
    String color;
    String name;
    String coreWeight;
    String machineNumber;
    String factPerBagNumber;

    Integer productionId;
    Integer productId;

    /**
     * 统计得出
     */
    String grossWeight;
    String totalNumber;
    String totalWeight;

    /**
     * 终端机获取
     */
    String loginInfo;
    String tip;
    String banci;
    String netWeight;
    String tare;

    /**
     * 模式 自动设置/手动设置
     */
    String mode = "manual";

    Integer manualModeEventTimes = 2;
}