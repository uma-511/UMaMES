package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.CycleLabel;
import me.zhengjie.uma_mes.service.dto.CycleLabelDTO;
import me.zhengjie.uma_mes.service.dto.CycleLabelQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author wave
* @date 2020-09-09
*/
public interface CycleLabelService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(CycleLabelQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<CycleLabelDTO>
    */
    List<CycleLabelDTO> queryAll(CycleLabelQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return CycleLabelDTO
     */
    CycleLabelDTO findById(Integer id);

    CycleLabelDTO create(CycleLabel resources);

    void update(CycleLabel resources);

    void delete(Integer id);

    void download(List<CycleLabelDTO> all, HttpServletResponse response) throws IOException;
}