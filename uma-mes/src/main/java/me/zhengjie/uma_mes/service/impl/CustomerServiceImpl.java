package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.exception.BadRequestException;
import me.zhengjie.uma_mes.domain.Customer;
import me.zhengjie.uma_mes.repository.CustomerRepository;
import me.zhengjie.uma_mes.service.CustomerService;
import me.zhengjie.uma_mes.service.dto.CustomerDTO;
import me.zhengjie.uma_mes.service.dto.CustomerQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.CustomerMapper;
import me.zhengjie.utils.*;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    public Map<String,Object> queryAll(CustomerQueryCriteria criteria, Pageable pageable){
        criteria.setDelFlag(0);
        Page<Customer> page = customerRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(customerMapper::toDto));
    }

    @Override
    public List<CustomerDTO> queryAll(CustomerQueryCriteria criteria){
        criteria.setDelFlag(0);
        return customerMapper.toDto(customerRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
//    @Cacheable(key = "#p0")
    public CustomerDTO findById(Integer id) {
        Customer customer = customerRepository.findById(id).orElseGet(Customer::new);
        ValidationUtil.isNull(customer.getId(),"Customer","id",id);
        return customerMapper.toDto(customer);
    }

    @Override
//    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public CustomerDTO create(Customer resources) {
        CustomerQueryCriteria criteria = new CustomerQueryCriteria();
        criteria.setCodeAccurate(resources.getCode());
        criteria.setDelFlag(0);
        List<CustomerDTO> customerDTOs = customerMapper.toDto(customerRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria,criteriaBuilder)));
        CustomerQueryCriteria criteria2 = new CustomerQueryCriteria();
        criteria2.setNameAccurate(resources.getName());
        criteria2.setDelFlag(0);
        List<CustomerDTO> customerDTOs2 = customerMapper.toDto(customerRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria2,criteriaBuilder)));
        if (customerDTOs.size() > 0 && customerDTOs.get(0).getCode().equals(resources.getCode()) && resources.getId() != customerDTOs.get(0).getId()) {
            throw new BadRequestException("请确保客户编号唯一");
        }
        if (customerDTOs2.size() > 0 && customerDTOs2.get(0).getName().equals(resources.getName()) && resources.getId() != customerDTOs2.get(0).getId()) {
            throw new BadRequestException("请确保客户名称唯一");
        } else {
            resources.setCreateUser(SecurityUtils.getUsername());
            resources.setCreateDate(new Timestamp(System.currentTimeMillis()));
            resources.setDelFlag(0);
            return customerMapper.toDto(customerRepository.save(resources));
        }
    }

    @Override
//    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(Customer resources) {
        CustomerQueryCriteria criteria = new CustomerQueryCriteria();
        criteria.setCodeAccurate(resources.getCode());
        criteria.setDelFlag(0);
        List<CustomerDTO> customerDTOs = customerMapper.toDto(customerRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria,criteriaBuilder)));
        if (customerDTOs.size() > 0 && customerDTOs.get(0).getCode().equals(resources.getCode()) && resources.getId() != customerDTOs.get(0).getId()) {
            throw new BadRequestException("请确保客户编号唯一");
        }
        CustomerQueryCriteria criteria2 = new CustomerQueryCriteria();
        criteria2.setNameAccurate(resources.getName());
        criteria2.setDelFlag(0);
        List<CustomerDTO> customerDTOs2 = customerMapper.toDto(customerRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria2,criteriaBuilder)));
        if (customerDTOs2.size() > 0 && customerDTOs2.get(0).getName().equals(resources.getName()) && resources.getId() != customerDTOs2.get(0).getId()) {
            throw new BadRequestException("请确保客户名称唯一");
        } else {
            Customer customer = customerRepository.findById(resources.getId()).orElseGet(Customer::new);
            resources.setCreateUser(SecurityUtils.getUsername());
            resources.setDelFlag(0);
            ValidationUtil.isNull( customer.getId(),"Customer","id",resources.getId());
            customer.copy(resources);
            customerRepository.save(customer);
        }
    }

    @Override
//    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        Customer customer = customerRepository.findById(id).orElseGet(Customer::new);
        customer.setDelFlag(1);
        customerRepository.save(customer);
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