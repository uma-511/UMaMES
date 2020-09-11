package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.BonusType;
import me.zhengjie.uma_mes.domain.CycleLabel;
import me.zhengjie.uma_mes.service.dto.BonusTypeDTO;
import me.zhengjie.uma_mes.service.dto.BonusTypeQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author wave
* @date 2020-09-09
*/
public interface BonusTypeService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(BonusTypeQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<BonusTypeDTO>
    */
    List<BonusTypeDTO> queryAll(BonusTypeQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return BonusTypeDTO
     */
    BonusTypeDTO findById(Long id);

    BonusTypeDTO create(BonusType resources);

    void update(BonusType resources);

    void delete(Long id);

    void updateCycleMenu(BonusType resources, BonusTypeDTO bonusTypeDTO);

    Object getCycleMenusTree();

    Object getBonusJobsTree();

    void updateCycle(BonusType resources, BonusTypeDTO bonusTypeDTO);

    void updateJob(BonusType resources, BonusTypeDTO bonusTypeDTO);

    void download(List<BonusTypeDTO> all, HttpServletResponse response) throws IOException;
}