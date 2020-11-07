package me.zhengjie.uma_mes.service;


import me.zhengjie.uma_mes.domain.ChemicalFiberCustomerOrder;
import me.zhengjie.uma_mes.domain.ChemicalFiberProduction;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberCustomerOrderDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberCustomerOrderQueryCriteria;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberProductionDTO;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author Xie Ji Biao
* @date 2020-10-30
*/
public interface ChemicalFiberCustomerOrderService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(ChemicalFiberCustomerOrderQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<ChemicalFiberCustomerOrderDTO>
    */
    List<ChemicalFiberCustomerOrderDTO> queryAll(ChemicalFiberCustomerOrderQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return ChemicalFiberCustomerOrderDTO
     */
    ChemicalFiberCustomerOrderDTO findById(Integer id);

    ChemicalFiberCustomerOrderDTO create(ChemicalFiberCustomerOrder resources);

    List<ChemicalFiberProductionDTO> getProdction(ChemicalFiberCustomerOrder resources);

    void update(ChemicalFiberCustomerOrder resources);

    void delete(Integer id);

    void download(Integer id, HttpServletResponse response) throws IOException;
}
