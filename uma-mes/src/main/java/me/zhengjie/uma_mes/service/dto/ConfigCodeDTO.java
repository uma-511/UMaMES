package me.zhengjie.uma_mes.service.dto;

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;


/**
* @author wave
* @date 2020-10-09
*/
@Data
public class ConfigCodeDTO implements Serializable {

    private Integer id;

    /**
     * 公司名
     */
    private String companyName;

    /**
     * 流水号编码
     */
    private String serialCode;

    /**
     * 默认地址
     */
    private String address;
}