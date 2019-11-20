package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.ConfigClassify;
import me.zhengjie.uma_mes.service.dto.ConfigClassifyDTO;
import me.zhengjie.uma_mes.service.dto.ConfigClassifyQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
public interface ConfigClassifyService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(ConfigClassifyQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<ConfigClassifyDTO>
    */
    List<ConfigClassifyDTO> queryAll(ConfigClassifyQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return ConfigClassifyDTO
     */
    ConfigClassifyDTO findById(Integer id);

    ConfigClassifyDTO create(ConfigClassify resources);

    void update(ConfigClassify resources);

    void delete(Integer id);

    void download(List<ConfigClassifyDTO> all, HttpServletResponse response) throws IOException;
}