package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.ChemicalFiberLabel;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberLabelDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberLabelQueryCriteria;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberLabelTotalDTO;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
public interface ChemicalFiberLabelService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(ChemicalFiberLabelQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<ChemicalFiberLabelDTO>
    */
    List<ChemicalFiberLabelDTO> queryAll(ChemicalFiberLabelQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return ChemicalFiberLabelDTO
     */
    ChemicalFiberLabelDTO findById(Integer id);

    ChemicalFiberLabelDTO create(ChemicalFiberLabel resources);

    void update(ChemicalFiberLabel resources);

    void delete(Integer id);

    void download(List<ChemicalFiberLabelDTO> all, HttpServletResponse response) throws IOException;

    void update(List<ChemicalFiberLabel> chemicalFiberLabels);

    ChemicalFiberLabelTotalDTO getTotalByProductionId(Integer productionId);

    ChemicalFiberLabel getByLabelNumber(String labelNumber);
}