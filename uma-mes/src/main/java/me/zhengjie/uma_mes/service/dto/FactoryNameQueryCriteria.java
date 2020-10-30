package me.zhengjie.uma_mes.service.dto;

import lombok.Data;

import javax.persistence.Id;

@Data
public class FactoryNameQueryCriteria {

    private Integer id;

    private String name;
}
