package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.UmaProductionChemicalFiber;
import me.zhengjie.uma_mes.service.dto.UmaProductionChemicalFiberDTO;
import me.zhengjie.uma_mes.service.dto.UmaProductionChemicalFiberQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
public interface UmaProductionChemicalFiberService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(UmaProductionChemicalFiberQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<UmaProductionChemicalFiberDTO>
    */
    List<UmaProductionChemicalFiberDTO> queryAll(UmaProductionChemicalFiberQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return UmaProductionChemicalFiberDTO
     */
    UmaProductionChemicalFiberDTO findById(Integer id);

    UmaProductionChemicalFiberDTO create(UmaProductionChemicalFiber resources);

    void update(UmaProductionChemicalFiber resources);

    void delete(Integer id);

    void download(List<UmaProductionChemicalFiberDTO> all, HttpServletResponse response) throws IOException;
}