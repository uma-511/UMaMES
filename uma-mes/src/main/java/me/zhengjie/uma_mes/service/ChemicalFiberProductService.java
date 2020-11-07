package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.ChemicalFiberProduct;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberProductDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberProductQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
public interface ChemicalFiberProductService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(ChemicalFiberProductQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<ChemicalFiberProductDTO>
    */
    List<ChemicalFiberProductDTO> queryAll(ChemicalFiberProductQueryCriteria criteria);

    List<ChemicalFiberProductDTO> getListColor(ChemicalFiberProductQueryCriteria criteria);

    List<ChemicalFiberProductDTO> getListFineness(ChemicalFiberProductQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return ChemicalFiberProductDTO
     */
    ChemicalFiberProductDTO findById(Integer id);

    ChemicalFiberProductDTO create(ChemicalFiberProduct resources);

    ChemicalFiberProductDTO createSave(ChemicalFiberProduct resources);

    ChemicalFiberProductDTO createForTerminal(ChemicalFiberProduct resources);

    void update(ChemicalFiberProduct resources);

    void delete(Integer id);

    void download(List<ChemicalFiberProductDTO> all, HttpServletResponse response) throws IOException;
}
