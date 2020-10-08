package me.zhengjie.uma_mes.service;

import com.lgmn.common.result.Result;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberWarehousingReortDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberWarehousingReortQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ChemicalFiberWarehousingReortService {


    Map<String,Object> queryAll(ChemicalFiberWarehousingReortQueryCriteria criteria, Pageable pageable);

    //List<ChemicalFiberWarehousingReortDTO> queryAll(ChemicalFiberWarehousingReortQueryCriteria criteria);
    List<Map<String, Object>> queryAlls(ChemicalFiberWarehousingReortQueryCriteria criteria);

    Result getSummaryData(ChemicalFiberWarehousingReortQueryCriteria criteria);

    void download(List<Map<String, Object>> all, HttpServletResponse response) throws IOException;
}
