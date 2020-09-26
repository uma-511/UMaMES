package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.exception.BadRequestException;
import me.zhengjie.uma_mes.domain.Customer;
import me.zhengjie.uma_mes.domain.OverArrearsPayDetail;
import me.zhengjie.uma_mes.repository.ChemicalFiberDeliveryNoteRepository;
import me.zhengjie.uma_mes.repository.CustomerRepository;
import me.zhengjie.uma_mes.service.dto.CustomerQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.CustomerMapper;
import me.zhengjie.utils.*;
import me.zhengjie.uma_mes.repository.OverArrearsPayDetailRepository;
import me.zhengjie.uma_mes.service.OverArrearsPayDetailService;
import me.zhengjie.uma_mes.service.dto.OverArrearsPayDetailDTO;
import me.zhengjie.uma_mes.service.dto.OverArrearsPayDetailQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.OverArrearsPayDetailMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @author wave
* @date 2020-09-24
*/
@Service
@CacheConfig(cacheNames = "overArrearsPayDetail")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class OverArrearsPayDetailServiceImpl implements OverArrearsPayDetailService {

    private final OverArrearsPayDetailRepository overArrearsPayDetailRepository;

    private final OverArrearsPayDetailMapper overArrearsPayDetailMapper;

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    private final ChemicalFiberDeliveryNoteRepository chemicalFiberDeliveryNoteRepository;

    public OverArrearsPayDetailServiceImpl(OverArrearsPayDetailRepository overArrearsPayDetailRepository, OverArrearsPayDetailMapper overArrearsPayDetailMapper, CustomerRepository customerRepository, CustomerMapper customerMapper, ChemicalFiberDeliveryNoteRepository chemicalFiberDeliveryNoteRepository) {
        this.overArrearsPayDetailRepository = overArrearsPayDetailRepository;
        this.overArrearsPayDetailMapper = overArrearsPayDetailMapper;
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.chemicalFiberDeliveryNoteRepository = chemicalFiberDeliveryNoteRepository;
    }

    @Override
    public Map<String,Object> queryAll(CustomerQueryCriteria criteria, Pageable pageable){
        Page<Customer> page = customerRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(customerMapper::toDto));
    }

    @Override
    public List<OverArrearsPayDetailDTO> queryAll(OverArrearsPayDetailQueryCriteria criteria){
        return overArrearsPayDetailMapper.toDto(overArrearsPayDetailRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    public List<OverArrearsPayDetailDTO> findListByCustomerId(Integer id) {
       return overArrearsPayDetailMapper.toDto(overArrearsPayDetailRepository.getListByCustomerId(id));
    }

    @Override
    public OverArrearsPayDetailDTO findById(Integer id) {
        OverArrearsPayDetail overArrearsPayDetail = overArrearsPayDetailRepository.findById(id).orElseGet(OverArrearsPayDetail::new);
        ValidationUtil.isNull(overArrearsPayDetail.getId(),"OverArrearsPayDetail","id",id);
        return overArrearsPayDetailMapper.toDto(overArrearsPayDetail);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OverArrearsPayDetailDTO create(OverArrearsPayDetail resources) {
        Customer customer = customerRepository.findById(resources.getCustomerId()).orElseGet(Customer::new);
        if(null == resources.getPrice() || resources.getPrice().compareTo(customer.getOverArrears()) == 1){
            throw new BadRequestException("结款金额超出欠款，不能结款");
        }
        customer.setAccount(customer.getAccount().subtract(resources.getPrice()));
        customer.setOverArrears(customer.getOverArrears().subtract(resources.getPrice()));
        customerRepository.save(customer);
        resources.setCreateDate(new Timestamp(System.currentTimeMillis()));
        resources.setEnable(Boolean.TRUE);
        resources.setCreateUser(chemicalFiberDeliveryNoteRepository.getRealNameByUserName(SecurityUtils.getUsername()));
        return overArrearsPayDetailMapper.toDto(overArrearsPayDetailRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(OverArrearsPayDetail resources) {
        OverArrearsPayDetail overArrearsPayDetail = overArrearsPayDetailRepository.findById(resources.getId()).orElseGet(OverArrearsPayDetail::new);
        ValidationUtil.isNull( overArrearsPayDetail.getId(),"OverArrearsPayDetail","id",resources.getId());
        overArrearsPayDetail.copy(resources);
        overArrearsPayDetailRepository.save(overArrearsPayDetail);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        OverArrearsPayDetail overArrearsPayDetail = overArrearsPayDetailRepository.findById(id).orElseGet(OverArrearsPayDetail::new);
        Customer customer = customerRepository.findById(overArrearsPayDetail.getCustomerId()).orElseGet(Customer::new);
        if (null != customer.getOverArrears()) {
            customer.setOverArrears(customer.getOverArrears().add(overArrearsPayDetail.getPrice()));
        }
        overArrearsPayDetail.setEnable(Boolean.FALSE);
        overArrearsPayDetailRepository.save(overArrearsPayDetail);
    }


    @Override
    public void download(List<OverArrearsPayDetailDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (OverArrearsPayDetailDTO overArrearsPayDetail : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("金额", overArrearsPayDetail.getPrice());
            map.put("结款日期", overArrearsPayDetail.getPayDate());
            map.put("创建日期", overArrearsPayDetail.getCreateDate());
            map.put("操作人", overArrearsPayDetail.getCreateUser());
            map.put("客户名", overArrearsPayDetail.getCustomerName());
            map.put("客户id", overArrearsPayDetail.getCustomerId());
            map.put("状态", overArrearsPayDetail.getEnable());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}