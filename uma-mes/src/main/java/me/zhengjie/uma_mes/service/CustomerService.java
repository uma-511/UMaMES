package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.Customer;
import me.zhengjie.uma_mes.service.dto.CustomerDTO;
import me.zhengjie.uma_mes.service.dto.CustomerQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
public interface CustomerService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(CustomerQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<CustomerDTO>
    */
    List<CustomerDTO> queryAll(CustomerQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return CustomerDTO
     */
    CustomerDTO findById(Integer id);

    CustomerDTO findByIdWithTotalArrears(Integer id);

    CustomerDTO findByIdWithTotalArrearsStatement(Integer id, String startTime, String endTime);

    CustomerDTO create(Customer resources);

    void update(Customer resources);

    void updateAccount(Customer customer);

    void delete(Integer id);

    void download(List<CustomerDTO> all, HttpServletResponse response) throws IOException;

    void save(Customer customer);

    void changeOverArrears(CustomerQueryCriteria criteria);
}