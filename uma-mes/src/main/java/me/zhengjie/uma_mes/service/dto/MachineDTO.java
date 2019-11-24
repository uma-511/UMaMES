package me.zhengjie.uma_mes.service.dto;

import lombok.Data;
import java.io.Serializable;


/**
* @author Tan Jun Ming
* @date 2019-11-24
*/
@Data
public class MachineDTO implements Serializable {

    private Integer id;

    // 机台号
    private Integer number;

    // 生产单id
    private Integer productionId;

    // 机台状态
    private Integer status;

    // 终端id
    private Integer terminalId;
}