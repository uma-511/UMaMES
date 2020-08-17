package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.ChemicalFiberProductMenu;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberProductMenuDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberProductMenuQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChemicalFiberProductMenuService {

    List<ChemicalFiberProductMenuDTO> queryAll(ChemicalFiberProductMenuQueryCriteria criteria);

    ChemicalFiberProductMenuDTO create(ChemicalFiberProductMenu resources);

}
