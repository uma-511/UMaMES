package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.ChemicalFiberDeliveryNotePayDetail;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberDeliveryNotePayDetailDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberDeliveryNotePayDetailQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* @author Xie Ji Biao
* @date 2020-08-08
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

    List<ChemicalFiberDeliveryNotePayDetailDTO> findListByScanNumber(String scanNumber);

    ChemicalFiberDeliveryNotePayDetailDTO create(ChemicalFiberDeliveryNotePayDetail resources);

    ChemicalFiberDeliveryNotePayDetailDTO doPay(ChemicalFiberDeliveryNotePayDetail resources);

    ChemicalFiberDeliveryNotePayDetailDTO finalPay(ChemicalFiberDeliveryNotePayDetail resources);

    void update(ChemicalFiberDeliveryNotePayDetail resources);

    void delete(Integer id);

    void download(List<ChemicalFiberDeliveryNotePayDetailDTO> all, HttpServletResponse response) throws IOException;
}