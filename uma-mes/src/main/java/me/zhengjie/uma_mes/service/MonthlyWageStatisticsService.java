package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.MonthlyWageStatistics;
import me.zhengjie.uma_mes.service.dto.MonthlyWageStatisticsDTO;
import me.zhengjie.uma_mes.service.dto.MonthlyWageStatisticsQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author wave
* @date 2020-09-11
*/
public interface MonthlyWageStatisticsService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(MonthlyWageStatisticsQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<MonthlyWageStatisticsDTO>
    */
    List<MonthlyWageStatisticsDTO> queryAll(MonthlyWageStatisticsQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return MonthlyWageStatisticsDTO
     */
    MonthlyWageStatisticsDTO findById(Integer id);

    MonthlyWageStatisticsDTO create(MonthlyWageStatistics resources);

    void doFinish(Integer id);

    void generateWage();

    void update(MonthlyWageStatistics resources);

    void download(List<MonthlyWageStatisticsDTO> all, HttpServletResponse response) throws IOException;
}