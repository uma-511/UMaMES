package me.zhengjie.domain;

import lombok.Data;
import me.zhengjie.uma_mes.domain.ChemicalFiberLabel;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberProductDTO;

import javax.persistence.Entity;

@Data
public class ReprintInfo {
    String labelNumber;

    ChemicalFiberLabel chemicalFiberLabel;

    ChemicalFiberProductDTO chemicalFiberProductDTO;
}