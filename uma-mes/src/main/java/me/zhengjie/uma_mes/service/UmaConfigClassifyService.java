package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.UmaConfigClassify;
import me.zhengjie.uma_mes.service.dto.UmaConfigClassifyDTO;
import me.zhengjie.uma_mes.service.dto.UmaConfigClassifyQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
public interface UmaConfigClassifyService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(UmaConfigClassifyQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<UmaConfigClassifyDTO>
    */
    List<UmaConfigClassifyDTO> queryAll(UmaConfigClassifyQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return UmaConfigClassifyDTO
     */
    UmaConfigClassifyDTO findById(Integer id);

    UmaConfigClassifyDTO create(UmaConfigClassify resources);

    void update(UmaConfigClassify resources);

    void delete(Integer id);

    void download(List<UmaConfigClassifyDTO> all, HttpServletResponse response) throws IOException;
}