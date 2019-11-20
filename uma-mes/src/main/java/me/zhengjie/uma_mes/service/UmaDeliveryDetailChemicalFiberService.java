package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.UmaDeliveryDetailChemicalFiber;
import me.zhengjie.uma_mes.service.dto.UmaDeliveryDetailChemicalFiberDTO;
import me.zhengjie.uma_mes.service.dto.UmaDeliveryDetailChemicalFiberQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
public interface UmaDeliveryDetailChemicalFiberService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(UmaDeliveryDetailChemicalFiberQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<UmaDeliveryDetailChemicalFiberDTO>
    */
    List<UmaDeliveryDetailChemicalFiberDTO> queryAll(UmaDeliveryDetailChemicalFiberQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return UmaDeliveryDetailChemicalFiberDTO
     */
    UmaDeliveryDetailChemicalFiberDTO findById(Integer id);

    UmaDeliveryDetailChemicalFiberDTO create(UmaDeliveryDetailChemicalFiber resources);

    void update(UmaDeliveryDetailChemicalFiber resources);

    void delete(Integer id);

    void download(List<UmaDeliveryDetailChemicalFiberDTO> all, HttpServletResponse response) throws IOException;
}