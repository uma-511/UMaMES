package me.zhengjie.uma_mes.service;

import com.lgmn.common.result.Result;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberSalesListQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface ChemicalFiberSalesListService {

    Map<String, Object> queryAll(ChemicalFiberSalesListQueryCriteria criteria, Pageable pageable);

    Result getSalesSummaries(ChemicalFiberSalesListQueryCriteria criteria);
}
