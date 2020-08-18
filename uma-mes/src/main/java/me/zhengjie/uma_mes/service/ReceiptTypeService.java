package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.ReceiptType;
import me.zhengjie.uma_mes.service.dto.ReceiptTypeDTO;
import me.zhengjie.uma_mes.service.dto.ReceiptTypeQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author Xie Ji Biao
* @date 2020-08-15
*/
public interface ReceiptTypeService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(ReceiptTypeQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<ReceiptTypeDTO>
    */
    List<ReceiptTypeDTO> queryAll(ReceiptTypeQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return ReceiptTypeDTO
     */
    ReceiptTypeDTO findById(Integer id);

    ReceiptTypeDTO create(ReceiptType resources);

    void update(ReceiptType resources);

    void delete(Integer id);

    void download(List<ReceiptTypeDTO> all, HttpServletResponse response) throws IOException;
}