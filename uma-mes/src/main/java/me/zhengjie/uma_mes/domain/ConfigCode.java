package me.zhengjie.uma_mes.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import java.io.Serializable;

/**
* @author wave
* @date 2020-10-09
*/
@Entity
@Data
@Table(name="uma_config_code")
public class ConfigCode implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 公司名
     */
    @Column(name = "company_name")
    private String companyName;

    /**
     * 流水号编码
     */
    @Column(name = "serial_code")
    private String serialCode;

    /**
     * 默认地址
     */
    @Column(name = "address")
    private String address;

    public void copy(ConfigCode source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}