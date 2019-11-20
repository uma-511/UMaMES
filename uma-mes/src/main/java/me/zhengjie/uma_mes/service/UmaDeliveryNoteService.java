package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.UmaDeliveryNote;
import me.zhengjie.uma_mes.service.dto.UmaDeliveryNoteDTO;
import me.zhengjie.uma_mes.service.dto.UmaDeliveryNoteQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
public interface UmaDeliveryNoteService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(UmaDeliveryNoteQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<UmaDeliveryNoteDTO>
    */
    List<UmaDeliveryNoteDTO> queryAll(UmaDeliveryNoteQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return UmaDeliveryNoteDTO
     */
    UmaDeliveryNoteDTO findById(Integer id);

    UmaDeliveryNoteDTO create(UmaDeliveryNote resources);

    void update(UmaDeliveryNote resources);

    void delete(Integer id);

    void download(List<UmaDeliveryNoteDTO> all, HttpServletResponse response) throws IOException;
}