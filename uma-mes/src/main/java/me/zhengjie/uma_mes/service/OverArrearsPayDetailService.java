package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.OverArrearsPayDetail;
import me.zhengjie.uma_mes.service.dto.CustomerQueryCriteria;
import me.zhengjie.uma_mes.service.dto.OverArrearsPayDetailDTO;
import me.zhengjie.uma_mes.service.dto.OverArrearsPayDetailQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author wave
* @date 2020-09-24
*/
public interface OverArrearsPayDetailService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(CustomerQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<OverArrearsPayDetailDTO>
    */
    List<OverArrearsPayDetailDTO> queryAll(OverArrearsPayDetailQueryCriteria criteria);

    List<OverArrearsPayDetailDTO> findListByCustomerId(Integer id);

    /**
     * 根据ID查询
     * @param id ID
     * @return OverArrearsPayDetailDTO
     */
    OverArrearsPayDetailDTO findById(Integer id);

    OverArrearsPayDetailDTO create(OverArrearsPayDetail resources);

    void update(OverArrearsPayDetail resources);

    void delete(Integer id);

    void download(List<OverArrearsPayDetailDTO> all, HttpServletResponse response) throws IOException;
}