package me.zhengjie.uma_mes.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "uma_error_text")
public class ErrorText implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 错误信息
    @Column(name = "label_error")
    private String labelError;

    // 标签号码
    @Column(name = "label_number")
    private String labelNumber;

    // 状态
    @Column(name = "label_status")
    private Integer labelStatus;

    // 状态
    @Column(name = "create_date")
    private Timestamp createDate;

}
