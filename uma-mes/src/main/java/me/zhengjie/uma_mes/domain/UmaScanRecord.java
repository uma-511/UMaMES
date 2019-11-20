package me.zhengjie.uma_mes.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Entity
@Data
@Table(name="uma_scan_record")
public class UmaScanRecord implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 单号
    @Column(name = "scan_number",nullable = false)
    private String scanNumber;

    @Column(name = "label_id",nullable = false)
    private Integer labelId;

    // 扫描员
    @Column(name = "scan_user",nullable = false)
    private String scanUser;

    // 扫描时间
    @Column(name = "scan_time",nullable = false)
    private Timestamp scanTime;

    // 扫描类型
    @Column(name = "type")
    private String type;

    public void copy(UmaScanRecord source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}