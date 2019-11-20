package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.UmaLabelChemicalFiber;
import me.zhengjie.uma_mes.service.dto.UmaLabelChemicalFiberDTO;
import me.zhengjie.uma_mes.service.dto.UmaLabelChemicalFiberQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
public interface UmaLabelChemicalFiberService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(UmaLabelChemicalFiberQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<UmaLabelChemicalFiberDTO>
    */
    List<UmaLabelChemicalFiberDTO> queryAll(UmaLabelChemicalFiberQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return UmaLabelChemicalFiberDTO
     */
    UmaLabelChemicalFiberDTO findById(Integer id);

    UmaLabelChemicalFiberDTO create(UmaLabelChemicalFiber resources);

    void update(UmaLabelChemicalFiber resources);

    void delete(Integer id);

    void download(List<UmaLabelChemicalFiberDTO> all, HttpServletResponse response) throws IOException;
}