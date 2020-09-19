package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.AcidPersionPerformance;
import me.zhengjie.uma_mes.service.dto.AcidPersionPerformanceDTO;
import me.zhengjie.uma_mes.service.dto.AcidPersionPerformanceQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author wave
* @date 2020-09-05
*/
public interface AcidPersionPerformanceService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(AcidPersionPerformanceQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<AcidPersionPerformanceDTO>
    */
    List<AcidPersionPerformanceDTO> queryAll(AcidPersionPerformanceQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return AcidPersionPerformanceDTO
     */
    AcidPersionPerformanceDTO findById(Integer id);

    AcidPersionPerformanceDTO create(AcidPersionPerformance resources);

    void update(AcidPersionPerformance resources);

    void delete(Integer id);

    void download(List<AcidPersionPerformanceDTO> all, HttpServletResponse response) throws IOException;
}