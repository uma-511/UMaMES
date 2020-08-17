package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.BookAccountType;
import me.zhengjie.uma_mes.service.dto.BookAccountTypeDTO;
import me.zhengjie.uma_mes.service.dto.BookAccountTypeQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author Xie Ji Biao
* @date 2020-08-15
*/
public interface BookAccountTypeService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(BookAccountTypeQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<BookAccountTypeDTO>
    */
    List<BookAccountTypeDTO> queryAll(BookAccountTypeQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return BookAccountTypeDTO
     */
    BookAccountTypeDTO findById(Integer id);

    BookAccountTypeDTO create(BookAccountType resources);

    void update(BookAccountType resources);

    void delete(Integer id);

    void download(List<BookAccountTypeDTO> all, HttpServletResponse response) throws IOException;
}