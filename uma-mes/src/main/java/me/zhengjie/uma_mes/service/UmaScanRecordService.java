package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.UmaScanRecord;
import me.zhengjie.uma_mes.service.dto.UmaScanRecordDTO;
import me.zhengjie.uma_mes.service.dto.UmaScanRecordQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
public interface UmaScanRecordService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(UmaScanRecordQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<UmaScanRecordDTO>
    */
    List<UmaScanRecordDTO> queryAll(UmaScanRecordQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return UmaScanRecordDTO
     */
    UmaScanRecordDTO findById(Integer id);

    UmaScanRecordDTO create(UmaScanRecord resources);

    void update(UmaScanRecord resources);

    void delete(Integer id);

    void download(List<UmaScanRecordDTO> all, HttpServletResponse response) throws IOException;
}