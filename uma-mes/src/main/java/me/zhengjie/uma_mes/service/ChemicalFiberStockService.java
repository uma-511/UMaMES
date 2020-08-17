package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.ChemicalFiberStock;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberStockDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberStockQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
public interface ChemicalFiberStockService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(ChemicalFiberStockQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<ChemicalFiberStockDTO>
    */
    List<ChemicalFiberStockDTO> queryAll(ChemicalFiberStockQueryCriteria criteria);

    /**
     * 模糊查询型号和产品列表
     * @return List<ChemicalFiberStockDTO>
     */
    List<ChemicalFiberStockDTO> querySelectList(String innerName);

    /**
     * 根据ID查询
     * @param id ID
     * @return ChemicalFiberStockDTO
     */
    ChemicalFiberStockDTO findById(Integer id);

    ChemicalFiberStockDTO findByProdName(String prodName);

    void create(ChemicalFiberStock resources);

    void update(ChemicalFiberStock resources);

    void delete(Integer id);

    void download(List<ChemicalFiberStockDTO> all, HttpServletResponse response) throws IOException;

    void stockTask();

    Object buildTree(List<ChemicalFiberStockDTO> chemicalFiberStockDTO);

    ChemicalFiberStock findByColorAndFineness(String color, String fineness);
}