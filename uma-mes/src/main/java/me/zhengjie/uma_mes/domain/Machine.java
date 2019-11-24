package me.zhengjie.uma_mes.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import java.io.Serializable;

/**
* @author Tan Jun Ming
* @date 2019-11-24
*/
@Entity
@Data
@Table(name="uma_machine")
public class Machine implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 机台号
    @Column(name = "number",nullable = false)
    private Integer number;

    // 生产单id
    @Column(name = "production_id")
    private Integer productionId;

    // 机台状态
    @Column(name = "status",nullable = false)
    private Integer status;

    // 终端id
    @Column(name = "terminal_id",nullable = false)
    private Integer terminalId;

    public void copy(Machine source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}