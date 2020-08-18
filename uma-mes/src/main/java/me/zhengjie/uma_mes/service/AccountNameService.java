package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.AccountName;
import me.zhengjie.uma_mes.service.dto.AccountNameDTO;
import me.zhengjie.uma_mes.service.dto.AccountNameQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author Xie Ji Biao
* @date 2020-08-14
*/
public interface AccountNameService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(AccountNameQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<AccountNameDTO>
    */
    List<AccountNameDTO> queryAll(AccountNameQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return AccountNameDTO
     */
    AccountNameDTO findById(Integer id);

    AccountNameDTO create(AccountName resources);

    void update(AccountName resources);

    void delete(Integer id);

    void download(List<AccountNameDTO> all, HttpServletResponse response) throws IOException;
}