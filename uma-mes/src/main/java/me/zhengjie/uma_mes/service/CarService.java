package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.Car;
import me.zhengjie.uma_mes.service.dto.CarDTO;
import me.zhengjie.uma_mes.service.dto.CarQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author wave
* @date 2020-08-29
*/
public interface CarService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(CarQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<CarDTO>
    */
    List<CarDTO> queryAll(CarQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return CarDTO
     */
    CarDTO findById(Integer id);

    CarDTO create(Car resources);

    void update(Car resources);

    void delete(Integer id);

    void download(List<CarDTO> all, HttpServletResponse response) throws IOException;
}