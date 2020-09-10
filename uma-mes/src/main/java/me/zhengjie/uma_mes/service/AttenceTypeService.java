package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.AttenceType;
import me.zhengjie.uma_mes.service.dto.AttenceTypeDTO;
import me.zhengjie.uma_mes.service.dto.AttenceTypeQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author wave
* @date 2020-09-08
*/
public interface AttenceTypeService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(AttenceTypeQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<AttenceTypeDTO>
    */
    List<AttenceTypeDTO> queryAll(AttenceTypeQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return AttenceTypeDTO
     */
    AttenceTypeDTO findById(Integer id);

    AttenceTypeDTO create(AttenceType resources);

    void update(AttenceType resources);

    void delete(Integer id);

    void download(List<AttenceTypeDTO> all, HttpServletResponse response) throws IOException;
}