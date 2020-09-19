package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.TravelExpenses;
import me.zhengjie.uma_mes.service.dto.TravelExpensesDTO;
import me.zhengjie.uma_mes.service.dto.TravelExpensesQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author wave
* @date 2020-09-02
*/
public interface TravelExpensesService {

    /**
     * 查询数据分页
     *
     * @param criteria 条件参数
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Map<String, Object> queryAll(TravelExpensesQueryCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List<TravelExpensesDTO>
     */
    List<TravelExpensesDTO> queryAll(TravelExpensesQueryCriteria criteria);

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return TravelExpensesDTO
     */
    TravelExpensesDTO findById(Integer id);

    TravelExpensesDTO create(TravelExpenses resources);

    void update(TravelExpenses resources);

    void delete(Integer id);
}