package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.ViewScanRecord;
import me.zhengjie.uma_mes.service.dto.ViewScanRecordDTO;
import me.zhengjie.uma_mes.service.dto.ViewScanRecordQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author Xie Ji Biao
* @date 2019-11-26
*/
public interface ViewScanRecordService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(ViewScanRecordQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<ViewScanRecordDTO>
    */
    List<ViewScanRecordDTO> queryAll(ViewScanRecordQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return ViewScanRecordDTO
     */
    ViewScanRecordDTO findById(Integer id);

    ViewScanRecordDTO create(ViewScanRecord resources);

    void update(ViewScanRecord resources);

    void delete(Integer id);

    void download(List<ViewScanRecordDTO> all, HttpServletResponse response) throws IOException;
}