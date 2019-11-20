package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.UmaConfig;
import me.zhengjie.uma_mes.service.dto.UmaConfigDTO;
import me.zhengjie.uma_mes.service.dto.UmaConfigQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
public interface UmaConfigService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(UmaConfigQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<UmaConfigDTO>
    */
    List<UmaConfigDTO> queryAll(UmaConfigQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return UmaConfigDTO
     */
    UmaConfigDTO findById(Integer id);

    UmaConfigDTO create(UmaConfig resources);

    void update(UmaConfig resources);

    void delete(Integer id);

    void download(List<UmaConfigDTO> all, HttpServletResponse response) throws IOException;
}