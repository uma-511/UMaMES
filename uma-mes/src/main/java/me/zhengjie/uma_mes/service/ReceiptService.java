package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.Receipt;
import me.zhengjie.uma_mes.service.dto.ReceiptDTO;
import me.zhengjie.uma_mes.service.dto.ReceiptQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author Xie Ji Biao
* @date 2020-08-14
*/
public interface ReceiptService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(ReceiptQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<ReceiptDTO>
    */
    List<ReceiptDTO> queryAll(ReceiptQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return ReceiptDTO
     */
    ReceiptDTO findById(Integer id);

    ReceiptDTO create(Receipt resources);

    void update(Receipt resources);

    void delete(Integer id);

    void download(List<ReceiptDTO> all, HttpServletResponse response) throws IOException;

    void doFinish(Integer id);
}