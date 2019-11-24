package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.Machine;
import me.zhengjie.uma_mes.service.dto.MachineDTO;
import me.zhengjie.uma_mes.service.dto.MachineQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author Tan Jun Ming
* @date 2019-11-24
*/
public interface MachineService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(MachineQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<MachineDTO>
    */
    List<MachineDTO> queryAll(MachineQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return MachineDTO
     */
    MachineDTO findById(Integer id);

    MachineDTO create(Machine resources);

    void update(Machine resources);

    void delete(Integer id);

    void download(List<MachineDTO> all, HttpServletResponse response) throws IOException;
}