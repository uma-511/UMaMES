package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.ChemicalFiberDeliveryNotePayDetail;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberDeliveryNotePayDetailDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberDeliveryNotePayDetailQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author Xie Ji Biao
* @date 2020-07-29
*/
public interface ChemicalFiberDeliveryNotePayDetailService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(ChemicalFiberDeliveryNotePayDetailQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<ChemicalFiberDeliveryNotePayDetailDTO>
    */
    List<ChemicalFiberDeliveryNotePayDetailDTO> queryAll(ChemicalFiberDeliveryNotePayDetailQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return ChemicalFiberDeliveryNotePayDetailDTO
     */
    ChemicalFiberDeliveryNotePayDetailDTO findById(Integer id);

    ChemicalFiberDeliveryNotePayDetailDTO create(ChemicalFiberDeliveryNotePayDetail resources);

    void update(ChemicalFiberDeliveryNotePayDetail resources);

    void delete(Integer id);

    void download(List<ChemicalFiberDeliveryNotePayDetailDTO> all, HttpServletResponse response) throws IOException;
}