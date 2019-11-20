package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.ScanRecord;
import me.zhengjie.uma_mes.service.dto.ScanRecordDTO;
import me.zhengjie.uma_mes.service.dto.ScanRecordQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
public interface ScanRecordService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(ScanRecordQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<ScanRecordDTO>
    */
    List<ScanRecordDTO> queryAll(ScanRecordQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return ScanRecordDTO
     */
    ScanRecordDTO findById(Integer id);

    ScanRecordDTO create(ScanRecord resources);

    void update(ScanRecord resources);

    void delete(Integer id);

    void download(List<ScanRecordDTO> all, HttpServletResponse response) throws IOException;
}