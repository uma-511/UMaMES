package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.Revenue;
import me.zhengjie.uma_mes.service.dto.RevenueDTO;
import me.zhengjie.uma_mes.service.dto.RevenueQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author Xie Ji Biao
* @date 2020-08-14
*/
public interface RevenueService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(RevenueQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<RevenueDTO>
    */
    List<RevenueDTO> queryAll(RevenueQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return RevenueDTO
     */
    RevenueDTO findById(Integer id);

    RevenueDTO create(Revenue resources);

    void update(Revenue resources);

    void delete(Integer id);

    void download(List<RevenueDTO> all, HttpServletResponse response) throws IOException;
}