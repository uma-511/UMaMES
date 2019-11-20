package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.Customer;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.uma_mes.repository.CustomerRepository;
import me.zhengjie.uma_mes.service.CustomerService;
import me.zhengjie.uma_mes.service.dto.CustomerDTO;
import me.zhengjie.uma_mes.service.dto.CustomerQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.CustomerMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Service
@CacheConfig(cacheNames = "customer")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(CustomerQueryCriteria criteria, Pageable pageable){
        Page<Customer> page = customerRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(customerMapper::toDto));
    }

    @Override
    @Cacheable
    public List<CustomerDTO> queryAll(CustomerQueryCriteria criteria){
        return customerMapper.toDto(customerRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public CustomerDTO findById(Integer id) {
        Customer customer = customerRepository.findById(id).orElseGet(Customer::new);
        ValidationUtil.isNull(customer.getId(),"Customer","id",id);
        return customerMapper.toDto(customer);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public CustomerDTO create(Customer resources) {
        return customerMapper.toDto(customerRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(Customer resources) {
        Customer customer = customerRepository.findById(resources.getId()).orElseGet(Customer::new);
        ValidationUtil.isNull( customer.getId(),"Customer","id",resources.getId());
        customer.copy(resources);
        customerRepository.save(customer);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        customerRepository.deleteById(id);
    }


    @Override
    public void download(List<CustomerDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CustomerDTO customer : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("客户名称", customer.getName());
            map.put("客户编号", customer.getCode());
            map.put("客户地址", customer.getAddress());
            map.put("联系人", customer.getContacts());
            map.put("联系电话", customer.getContactPhone());
            map.put("备注", customer.getRemark());
            map.put("创建日期", customer.getCreateDate());
            map.put("创建人", customer.getCreateUser());
            map.put("删除标识", customer.getDelFlag());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}