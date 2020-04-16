package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.ChemicalFibeDevice;
import me.zhengjie.uma_mes.service.dto.ChemicalFibeDeviceDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFibeDeviceQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author Xie Ji Biao
* @date 2020-04-16
*/
public interface ChemicalFibeDeviceService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(ChemicalFibeDeviceQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<ChemicalFibeDeviceDTO>
    */
    List<ChemicalFibeDeviceDTO> queryAll(ChemicalFibeDeviceQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return ChemicalFibeDeviceDTO
     */
    ChemicalFibeDeviceDTO findById(Integer id);

    ChemicalFibeDeviceDTO create(ChemicalFibeDevice resources);

    void update(ChemicalFibeDevice resources);

    void delete(Integer id);

    void download(List<ChemicalFibeDeviceDTO> all, HttpServletResponse response) throws IOException;
}