package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.UmaCustomer;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.uma_mes.repository.UmaCustomerRepository;
import me.zhengjie.uma_mes.service.UmaCustomerService;
import me.zhengjie.uma_mes.service.dto.UmaCustomerDTO;
import me.zhengjie.uma_mes.service.dto.UmaCustomerQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.UmaCustomerMapper;
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
@CacheConfig(cacheNames = "umaCustomer")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UmaCustomerServiceImpl implements UmaCustomerService {

    private final UmaCustomerRepository umaCustomerRepository;

    private final UmaCustomerMapper umaCustomerMapper;

    public UmaCustomerServiceImpl(UmaCustomerRepository umaCustomerRepository, UmaCustomerMapper umaCustomerMapper) {
        this.umaCustomerRepository = umaCustomerRepository;
        this.umaCustomerMapper = umaCustomerMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(UmaCustomerQueryCriteria criteria, Pageable pageable){
        Page<UmaCustomer> page = umaCustomerRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(umaCustomerMapper::toDto));
    }

    @Override
    @Cacheable
    public List<UmaCustomerDTO> queryAll(UmaCustomerQueryCriteria criteria){
        return umaCustomerMapper.toDto(umaCustomerRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public UmaCustomerDTO findById(Integer id) {
        UmaCustomer umaCustomer = umaCustomerRepository.findById(id).orElseGet(UmaCustomer::new);
        ValidationUtil.isNull(umaCustomer.getId(),"UmaCustomer","id",id);
        return umaCustomerMapper.toDto(umaCustomer);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public UmaCustomerDTO create(UmaCustomer resources) {
        return umaCustomerMapper.toDto(umaCustomerRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(UmaCustomer resources) {
        UmaCustomer umaCustomer = umaCustomerRepository.findById(resources.getId()).orElseGet(UmaCustomer::new);
        ValidationUtil.isNull( umaCustomer.getId(),"UmaCustomer","id",resources.getId());
        umaCustomer.copy(resources);
        umaCustomerRepository.save(umaCustomer);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        umaCustomerRepository.deleteById(id);
    }


    @Override
    public void download(List<UmaCustomerDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UmaCustomerDTO umaCustomer : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("客户名称", umaCustomer.getName());
            map.put("客户编号", umaCustomer.getCode());
            map.put("客户地址", umaCustomer.getAddress());
            map.put("联系人", umaCustomer.getContacts());
            map.put("联系电话", umaCustomer.getContactPhone());
            map.put("备注", umaCustomer.getRemark());
            map.put("创建日期", umaCustomer.getCreateDate());
            map.put("创建人", umaCustomer.getCreateUser());
            map.put("删除标识", umaCustomer.getDelFlag());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}