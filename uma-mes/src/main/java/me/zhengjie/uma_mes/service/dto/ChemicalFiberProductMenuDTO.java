package me.zhengjie.uma_mes.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@Data
public class ChemicalFiberProductMenuDTO implements Serializable {

    private Integer id;

    // 产品目录名称
    private String productMenusName;





}
