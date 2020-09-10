package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.BonusCycle;
import me.zhengjie.uma_mes.service.dto.BonusCycleDTO;
import me.zhengjie.uma_mes.service.dto.BonusCycleQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author wave
* @date 2020-09-10
*/
public interface BonusCycleService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(BonusCycleQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<BonusCycleDTO>
    */
    List<BonusCycleDTO> queryAll(BonusCycleQueryCriteria criteria);

    BonusCycleDTO create(BonusCycle resources);

    void update(BonusCycle resources);

    void download(List<BonusCycleDTO> all, HttpServletResponse response) throws IOException;
}