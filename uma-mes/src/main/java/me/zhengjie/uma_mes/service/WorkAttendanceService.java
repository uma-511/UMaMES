package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.WorkAttendance;
import me.zhengjie.uma_mes.service.dto.WorkAttendanceDTO;
import me.zhengjie.uma_mes.service.dto.WorkAttendanceQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author wave
* @date 2020-09-07
*/
public interface WorkAttendanceService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(WorkAttendanceQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<WorkAttendanceDTO>
    */
    List<WorkAttendanceDTO> queryAll(WorkAttendanceQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return WorkAttendanceDTO
     */
    WorkAttendanceDTO findById(Integer id);

    WorkAttendanceDTO create(WorkAttendance resources);

    void update(WorkAttendance resources);

    void delete(Integer id);

    void download(List<WorkAttendanceDTO> all, HttpServletResponse response) throws IOException;

    void downloadWorkAttendance(WorkAttendanceQueryCriteria criteria, HttpServletResponse response) throws IOException;
}