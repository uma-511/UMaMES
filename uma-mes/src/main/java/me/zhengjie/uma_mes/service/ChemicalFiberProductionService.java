package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.ChemicalFiberProduction;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberProductionDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberProductionQueryCriteria;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberProductionSetMachinesDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberProductionSetProductionStatusDTO;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
public interface ChemicalFiberProductionService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(ChemicalFiberProductionQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<ChemicalFiberProductionDTO>
    */
    List<ChemicalFiberProductionDTO> queryAll(ChemicalFiberProductionQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return ChemicalFiberProductionDTO
     */
    ChemicalFiberProductionDTO findById(Integer id);

    ChemicalFiberProductionDTO create(ChemicalFiberProduction resources);

    void update(ChemicalFiberProduction resources);

    void delete(Integer id);

    void download(List<ChemicalFiberProductionDTO> all, HttpServletResponse response) throws IOException;

    ChemicalFiberProduction setMachines(ChemicalFiberProductionSetMachinesDTO resources);

    ChemicalFiberProduction setProductionStatus(ChemicalFiberProductionSetProductionStatusDTO resources);
}