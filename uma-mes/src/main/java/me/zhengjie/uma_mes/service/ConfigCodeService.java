package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.ConfigCode;
import me.zhengjie.uma_mes.service.dto.ConfigCodeDTO;
import me.zhengjie.uma_mes.service.dto.ConfigCodeQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author wave
* @date 2020-10-09
*/
public interface ConfigCodeService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(ConfigCodeQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<ConfigCodeDTO>
    */
    List<ConfigCodeDTO> queryAll(ConfigCodeQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return ConfigCodeDTO
     */
    ConfigCodeDTO findById(Integer id);

    ConfigCodeDTO create(ConfigCode resources);

    void update(ConfigCode resources);

    void delete(Integer id);

    void download(List<ConfigCodeDTO> all, HttpServletResponse response) throws IOException;
}