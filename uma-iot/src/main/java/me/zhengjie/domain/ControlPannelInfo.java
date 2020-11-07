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
    String machineNumber = "0";
    String factPerBagNumber = "0";
    String factory;
    String flowingWater;

    Integer productionId;
    Integer productId;

    /**
     * 统计得出
     */
    String grossWeight;
    String totalNumber;
    String totalWeight;
    String dayNumber;
    String dayWeight;

    /**
     * 终端机获取
     */
    String loginInfo;
    String tip;
    String banci;
    String netWeight = "0";
    String tare;

    /**
     * 模式 自动设置/手动设置
     */
    String mode = "manual";

    Integer manualModeEventTimes = 2;

    boolean hadGetWeight = false;
}
