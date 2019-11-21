package me.zhengjie.uma_mes.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @author Tan Jun Ming
* @date 2019-11-21
*/
@Entity
@Data
@Table(name="uma_scan_record_label")
public class ScanRecordLabel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 扫描记录id
    @Column(name = "scan_record_id",nullable = false)
    private Integer scanRecordId;

    // 标签id
    @Column(name = "label_id",nullable = false)
    private Integer labelId;

    // 扫描时间
    @Column(name = "scan_time",nullable = false)
    private Timestamp scanTime;

    public void copy(ScanRecordLabel source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}