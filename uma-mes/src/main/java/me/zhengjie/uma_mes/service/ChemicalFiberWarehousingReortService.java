package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.service.dto.ChemicalFiberWarehousingReortQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface ChemicalFiberWarehousingReortService {


    Map<String,Object> queryAll(ChemicalFiberWarehousingReortQueryCriteria criteria, Pageable pageable);

    Map<String,Object> getSummaryData(ChemicalFiberWarehousingReortQueryCriteria criteria);
}
