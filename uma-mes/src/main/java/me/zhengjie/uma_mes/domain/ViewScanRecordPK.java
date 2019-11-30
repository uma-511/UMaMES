package me.zhengjie.uma_mes.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class ViewScanRecordPK implements Serializable {
    private Integer id;
    private String labelNumber;
}
