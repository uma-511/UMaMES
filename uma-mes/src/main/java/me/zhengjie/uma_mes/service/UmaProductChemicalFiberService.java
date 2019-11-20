package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.UmaProductChemicalFiber;
import me.zhengjie.uma_mes.service.dto.UmaProductChemicalFiberDTO;
import me.zhengjie.uma_mes.service.dto.UmaProductChemicalFiberQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
public interface UmaProductChemicalFiberService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(UmaProductChemicalFiberQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<UmaProductChemicalFiberDTO>
    */
    List<UmaProductChemicalFiberDTO> queryAll(UmaProductChemicalFiberQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return UmaProductChemicalFiberDTO
     */
    UmaProductChemicalFiberDTO findById(Integer id);

    UmaProductChemicalFiberDTO create(UmaProductChemicalFiber resources);

    void update(UmaProductChemicalFiber resources);

    void delete(Integer id);

    void download(List<UmaProductChemicalFiberDTO> all, HttpServletResponse response) throws IOException;
}