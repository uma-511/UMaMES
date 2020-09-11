package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import java.io.Serializable;


/**
* @author wave
* @date 2020-09-09
*/
@Data
public class CycleLabelDTO implements Serializable {

    private Long id;

    /**
     * 周期
     */
    private String name;
}