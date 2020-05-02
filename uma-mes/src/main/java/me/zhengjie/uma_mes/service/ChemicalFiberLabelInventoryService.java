package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.ChemicalFiberLabelInventory;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberLabelInventoryDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberLabelInventoryQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author Xie Ji Biao
* @date 2020-04-14
*/
public interface ChemicalFiberLabelInventoryService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(ChemicalFiberLabelInventoryQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<ChemicalFiberLabelInventoryDTO>
    */
    List<ChemicalFiberLabelInventoryDTO> queryAll(ChemicalFiberLabelInventoryQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return ChemicalFiberLabelInventoryDTO
     */
    ChemicalFiberLabelInventoryDTO findById(Integer id);

    ChemicalFiberLabelInventoryDTO create(ChemicalFiberLabelInventory resources);

    void update(ChemicalFiberLabelInventory resources);

    void delete(Integer id);

    void download(List<ChemicalFiberLabelInventoryDTO> all, HttpServletResponse response) throws IOException;
}