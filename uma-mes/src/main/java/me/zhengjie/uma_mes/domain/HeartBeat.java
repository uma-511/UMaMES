package me.zhengjie.uma_mes.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @author Xie Ji Biao
* @date 2019-12-04
*/
@Entity
@Data
@Table(name="uma_heart_beat")
public class HeartBeat implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // 终端id
    @Column(name = "ip")
    private String ip;

    // 终端端口
    @Column(name = "port")
    private String port;

    // 心跳发送时间
    @Column(name = "send_time")
    private Timestamp sendTime;

    // 心跳返回时间
    @Column(name = "response_time")
    private Timestamp responseTime;

    // 创建时间
    @Column(name = "create_time",nullable = false)
    private Timestamp createTime;

    public void copy(HeartBeat source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}