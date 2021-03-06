package me.zhengjie.uma_mes.service;

import com.lgmn.common.result.Result;
import me.zhengjie.uma_mes.domain.ChemicalFiberDeliveryDetail;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberDeliveryDetailDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberDeliveryDetailQueryCriteria;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberProductionQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
public interface ChemicalFiberDeliveryDetailService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(ChemicalFiberDeliveryDetailQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<ChemicalFiberDeliveryDetailDTO>
    */
    List<ChemicalFiberDeliveryDetailDTO> queryAll(ChemicalFiberDeliveryDetailQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return ChemicalFiberDeliveryDetailDTO
     */
    ChemicalFiberDeliveryDetailDTO findById(Integer id);

    ChemicalFiberDeliveryDetailDTO create(ChemicalFiberDeliveryDetail resources);

    void update(ChemicalFiberDeliveryDetail resources);

    void updateList(List<ChemicalFiberDeliveryDetail> resources);

    void delete(Integer id);

    void download(List<ChemicalFiberDeliveryDetailDTO> all, HttpServletResponse response) throws IOException;

    Result getSalesReportSummaries(ChemicalFiberDeliveryDetailQueryCriteria criteria);

    Map<String,Object> queryStatementDetailsAll(ChemicalFiberDeliveryDetailQueryCriteria criteria, Pageable pageable);
}