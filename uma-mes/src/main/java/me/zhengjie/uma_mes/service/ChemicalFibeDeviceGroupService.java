package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.ChemicalFibeDeviceGroup;
import me.zhengjie.uma_mes.service.dto.ChemicalFibeDeviceGroupDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFibeDeviceGroupQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author Xie Ji Biao
* @date 2020-04-16
*/
public interface ChemicalFibeDeviceGroupService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(ChemicalFibeDeviceGroupQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<ChemicalFibeDeviceGroupDTO>
    */
    List<ChemicalFibeDeviceGroupDTO> queryAll(ChemicalFibeDeviceGroupQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return ChemicalFibeDeviceGroupDTO
     */
    ChemicalFibeDeviceGroupDTO findById(Integer id);

    ChemicalFibeDeviceGroupDTO create(ChemicalFibeDeviceGroup resources);

    void update(ChemicalFibeDeviceGroup resources);

    void delete(Integer id);

    void download(List<ChemicalFibeDeviceGroupDTO> all, HttpServletResponse response) throws IOException;
}