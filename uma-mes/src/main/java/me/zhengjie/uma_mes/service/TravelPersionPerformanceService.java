package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.TravelPersionPerformance;
import me.zhengjie.uma_mes.service.dto.TravelPersionPerformanceDTO;
import me.zhengjie.uma_mes.service.dto.TravelPersionPerformanceQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author wave
* @date 2020-09-02
*/
public interface TravelPersionPerformanceService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(TravelPersionPerformanceQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<TravelPersionPerformanceDTO>
    */
    List<TravelPersionPerformanceDTO> queryAll(TravelPersionPerformanceQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return TravelPersionPerformanceDTO
     */
    TravelPersionPerformanceDTO findById(Integer id);

    TravelPersionPerformanceDTO create(TravelPersionPerformance resources);

    void update(TravelPersionPerformance resources);

    void delete(Integer id);

    void download(List<TravelPersionPerformanceDTO> all, HttpServletResponse response) throws IOException;

    void downloadTravelPersonPerformance(TravelPersionPerformanceQueryCriteria criteria, HttpServletResponse response) throws IOException;
}