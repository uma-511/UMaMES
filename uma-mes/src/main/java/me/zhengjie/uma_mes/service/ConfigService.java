package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.Config;
import me.zhengjie.uma_mes.service.dto.ConfigDTO;
import me.zhengjie.uma_mes.service.dto.ConfigQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
public interface ConfigService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(ConfigQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<ConfigDTO>
    */
    List<ConfigDTO> queryAll(ConfigQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return ConfigDTO
     */
    ConfigDTO findById(Integer id);

    ConfigDTO create(Config resources);

    void update(Config resources);

    void delete(Integer id);

    void download(List<ConfigDTO> all, HttpServletResponse response) throws IOException;
}