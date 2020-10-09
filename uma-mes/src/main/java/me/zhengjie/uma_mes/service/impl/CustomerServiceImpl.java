package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.exception.BadRequestException;
import me.zhengjie.uma_mes.domain.Customer;
import me.zhengjie.uma_mes.repository.CustomerRepository;
import me.zhengjie.uma_mes.service.CustomerService;
import me.zhengjie.uma_mes.service.dto.CustomerDTO;
import me.zhengjie.uma_mes.service.dto.CustomerQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.CustomerMapper;
import me.zhengjie.utils.*;
import org.hibernate.type.BigDecimalType;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

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

    public static Map monthTimeInMillis() {
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Long time = calendar.getTimeInMillis();
        int month = calendar.get(Calendar.MONTH)+1;
        int year = calendar.get(Calendar.YEAR);

        Map<String, Object> map = new HashMap<>();
        map.put("time", time);
        map.put("month", month < 10 ? "0" + month : month);
        map.put("year", year);
        return map;
    }

    private String getCurrenMonthStartTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        String firstday, lastday;
        // 获取前月的第一天
        Calendar cale = Calendar.getInstance();
        cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        return format.format(cale.getTime());
    }

    private String getCurrenMonthEndTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        String firstday, lastday;
        // 获取前月的最后一天
        Calendar cale = Calendar.getInstance();
        cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 1);
        cale.set(Calendar.DAY_OF_MONTH, 0);
        return format.format(cale.getTime());
    }

    @Override
    public Map<String,Object> queryAll(CustomerQueryCriteria criteria, Pageable pageable){
        criteria.setDelFlag(0);
        int start = pageable.getPageSize();
        int end = pageable.getPageNumber();
        int PageNumber = start * end;
        Map<String, Object> map = new HashMap<>();
        if (null == criteria.getName()){
            criteria.setName("");
        }
        if (null == criteria.getAddress()){
            criteria.setAddress("");
        }
        if (null == criteria.getCode()){
            criteria.setCode("");
        }
        if (null == criteria.getContacts()){
            criteria.setContacts("");
        }
        if (null == criteria.getContactPhone()){
            criteria.setContactPhone("");
        }
        String startTime = getCurrenMonthStartTime();
        String endTime = getCurrenMonthEndTime();
        List<Customer> pages = customerRepository.findAllWithTotalArrears(PageNumber, start,criteria.getName(),criteria.getCode(),criteria.getAddress(),criteria.getContacts(),criteria.getContactPhone(),startTime,endTime);
        for(Customer customer : pages){
            // 客户往期欠款=往期系统内订单欠款+用系统之前的旧账
            if (null == customer.getOverArrears()) {
                customer.setOverArrears(new BigDecimal(0));
            }
            if(null == customer.getTotalArrears()) {
                customer.setTotalArrears(new BigDecimal(0));
            }
            customer.setTotalArrears(customer.getTotalArrears().add(customer.getOverArrears()));

            if (null == customer.getCurrentArrears() || customer.getCurrentArrears().compareTo(new BigDecimal(0.00)) == 0 || customer.getCurrentArrears().equals(0.00)) {
                customer.setCurrentArrears(null);
            }if (null == customer.getTotalArrears() || customer.getTotalArrears().compareTo(new BigDecimal(0.00)) == 0 || customer.getTotalArrears().equals(0.00)) {
                customer.setTotalArrears(null);
            }if (null == customer.getAccount() || customer.getAccount().compareTo(new BigDecimal(0.00)) == 0 || customer.getAccount().equals(0.00)) {
                customer.setAccount(null);
            }
        }
        Integer Size = customerRepository.findSize();
        map.put("content", pages);
        map.put("totalElements", Size);
        return map;
    }

    @Override
    public List<CustomerDTO> queryAll(CustomerQueryCriteria criteria){
        criteria.setDelFlag(0);
        if (null == criteria.getName()){
            criteria.setName("");
        }
        if (null == criteria.getAddress()){
            criteria.setAddress("");
        }
        if (null == criteria.getCode()){
            criteria.setCode("");
        }
        if (null == criteria.getContacts()){
            criteria.setContacts("");
        }
        if (null == criteria.getContactPhone()){
            criteria.setContactPhone("");
        }
        String startTime = getCurrenMonthStartTime();
        String endTime = getCurrenMonthEndTime();
        List<CustomerDTO> customerDTOList = customerMapper.toDto(customerRepository.findAllWithTotalArrearsForGetList(criteria.getName(),criteria.getCode(),criteria.getAddress(),criteria.getContacts(),criteria.getContactPhone(),startTime,endTime));
        for (CustomerDTO customerDTO : customerDTOList){
            // 客户往期欠款=往期系统内订单欠款+用系统之前的旧账
            if (null == customerDTO.getOverArrears()) {
                customerDTO.setOverArrears(new BigDecimal(0));
            }
            if(null == customerDTO.getTotalArrears()) {
                customerDTO.setTotalArrears(new BigDecimal(0));
            }
            customerDTO.setTotalArrears(customerDTO.getTotalArrears().add(customerDTO.getOverArrears()));
        }
        return customerDTOList;
    }
/*
    public List<CustomerDTO> queryAllWithTotalArrears(CustomerQueryCriteria criteria){
        criteria.setDelFlag(0);
        return customerMapper.toDto(customerRepository.findAllWithTotalArrears((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }*/

    @Override
//    @Cacheable(key = "#p0")
    public CustomerDTO findById(Integer id) {
        Customer customer = customerRepository.findById(id).orElseGet(Customer::new);
        ValidationUtil.isNull(customer.getId(),"Customer","id",id);
        return customerMapper.toDto(customer);
    }

    @Override
    public CustomerDTO findByIdWithTotalArrears(Integer id) {
        String startTime = getCurrenMonthStartTime();
        String endTime = getCurrenMonthEndTime();
        Customer customer = customerRepository.findByIdWithArrears(id,startTime,endTime);
        // 客户往期欠款=往期系统内订单欠款+用系统之前的旧账
        if (null == customer.getOverArrears()) {
            customer.setOverArrears(new BigDecimal(0));
        }
        if(null == customer.getTotalArrears()) {
            customer.setTotalArrears(new BigDecimal(0));
        }
        customer.setTotalArrears(customer.getTotalArrears().add(customer.getOverArrears()));
        ValidationUtil.isNull(customer.getId(),"Customer","id",id);
        return customerMapper.toDto(customer);
    }

    @Override
    public CustomerDTO findByIdWithTotalArrearsStatement(Integer id, String startTime, String endTime) {
        Customer customer = customerRepository.findByIdWithArrearsList(id, startTime, endTime);
        Map<String, Object> customerMap = customerRepository.findByIdWithArrearsMap(id, startTime, endTime);
        // 客户往期欠款=往期系统内订单欠款+用系统之前的旧账
        customer.setOverArrears(new BigDecimal(customerMap.get("over_arrears").toString()));
        customer.setTotalArrears(new BigDecimal(customerMap.get("total_arrears").toString()));
        customer.setCurrentArrears(new BigDecimal(customerMap.get("current_arrears").toString()));
        if (null == customerMap.get("over_arrears")) {
            customer.setOverArrears(new BigDecimal(0));
        }
        if(null == customer.getTotalArrears()) {
            customer.setTotalArrears(new BigDecimal(0));
        }
        customer.setTotalArrears(customer.getTotalArrears().add(customer.getOverArrears()));
        ValidationUtil.isNull(customer.getId(),"Customer","id",id);
        CustomerDTO customerDTO = customerMapper.toDto(customer);
        customerDTO.setRemainder(new BigDecimal(customerMap.get("remainder").toString()));
        return customerDTO;
    }

    @Override
//    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public CustomerDTO create(Customer resources) {
        CustomerQueryCriteria criteria = new CustomerQueryCriteria();
        criteria.setCodeAccurate(resources.getCode());
        criteria.setDelFlag(0);
        List<CustomerDTO> customerDTOs = customerMapper.toDto(customerRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria,criteriaBuilder)));
        if (customerDTOs.size() > 0 && customerDTOs.get(0).getCode().equals(resources.getCode())) {
            throw new BadRequestException("请确保客户编号唯一");
        }

        CustomerQueryCriteria criteria2 = new CustomerQueryCriteria();
        criteria2.setNameAccurate(resources.getName());
        criteria2.setDelFlag(0);
        List<CustomerDTO> customerDTOs2 = customerMapper.toDto(customerRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria2,criteriaBuilder)));
        if (customerDTOs2.size() > 0 && customerDTOs2.get(0).getName().equals(resources.getName())) {
            throw new BadRequestException("请确保客户名称唯一");
        }

        CustomerQueryCriteria criteria3 = new CustomerQueryCriteria();
        criteria3.setFullNameAccurate(resources.getFullName());
        criteria3.setDelFlag(0);
        List<CustomerDTO> customerDTOs3 = customerMapper.toDto(customerRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria3,criteriaBuilder)));
        if (customerDTOs3.size() > 0 && customerDTOs3.get(0).getFullName().equals(resources.getFullName())){
            throw new BadRequestException("请确保客户全称唯一");
        }
        else {
            resources.setCreateUser(SecurityUtils.getUsername());
            resources.setCreateDate(new Timestamp(System.currentTimeMillis()));
            resources.setDelFlag(0);
            resources.setAccount(new BigDecimal(0));
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
        if (customerDTOs.size() > 0 && customerDTOs.get(0).getCode().equals(resources.getCode()) && !resources.getId().equals(customerDTOs.get(0).getId())) {
            throw new BadRequestException("请确保客户编号唯一");
        }
        CustomerQueryCriteria criteria2 = new CustomerQueryCriteria();
        criteria2.setNameAccurate(resources.getName());
        criteria2.setDelFlag(0);
        List<CustomerDTO> customerDTOs2 = customerMapper.toDto(customerRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria2,criteriaBuilder)));
        if (customerDTOs2.size() > 0 && customerDTOs2.get(0).getName().equals(resources.getName()) && !resources.getId().equals(customerDTOs2.get(0).getId())) {
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
    public void updateAccount(Customer customer) {
        customerRepository.save(customer);
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

    @Override
    public void save(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public void changeOverArrears(CustomerQueryCriteria criteria) {
        Customer customer = customerRepository.findById(criteria.getId()).orElseGet(Customer::new);
        customer.setOverArrears(criteria.getOverArrears());
        customerRepository.save(customer);
    }
}