package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import java.io.Serializable;


/**
* @author wave
* @date 2020-09-08
*/
@Data
public class AttenceTypeDTO implements Serializable {

    private Integer id;

    /**
     * 类型
     */
    private String attenceType;

    /**
     * 状态
     */
    private Boolean enable;
}