package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.ReceiptType;
import me.zhengjie.uma_mes.repository.ChemicalFiberDeliveryNoteRepository;
import me.zhengjie.utils.*;
import me.zhengjie.uma_mes.repository.ReceiptTypeRepository;
import me.zhengjie.uma_mes.service.ReceiptTypeService;
import me.zhengjie.uma_mes.service.dto.ReceiptTypeDTO;
import me.zhengjie.uma_mes.service.dto.ReceiptTypeQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.ReceiptTypeMapper;
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
* @author Xie Ji Biao
* @date 2020-08-15
*/
@Service
@CacheConfig(cacheNames = "receiptType")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ReceiptTypeServiceImpl implements ReceiptTypeService {

    private final ReceiptTypeRepository receiptTypeRepository;

    private final ReceiptTypeMapper receiptTypeMapper;

    private final ChemicalFiberDeliveryNoteRepository chemicalFiberDeliveryNoteRepository;

    public ReceiptTypeServiceImpl(ReceiptTypeRepository receiptTypeRepository, ReceiptTypeMapper receiptTypeMapper, ChemicalFiberDeliveryNoteRepository chemicalFiberDeliveryNoteRepository) {
        this.receiptTypeRepository = receiptTypeRepository;
        this.receiptTypeMapper = receiptTypeMapper;
        this.chemicalFiberDeliveryNoteRepository = chemicalFiberDeliveryNoteRepository;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(ReceiptTypeQueryCriteria criteria, Pageable pageable){
        Page<ReceiptType> page = receiptTypeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(receiptTypeMapper::toDto));
    }

    @Override
    @Cacheable
    public List<ReceiptTypeDTO> queryAll(ReceiptTypeQueryCriteria criteria){
        return receiptTypeMapper.toDto(receiptTypeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public ReceiptTypeDTO findById(Integer id) {
        ReceiptType receiptType = receiptTypeRepository.findById(id).orElseGet(ReceiptType::new);
        ValidationUtil.isNull(receiptType.getId(),"ReceiptType","id",id);
        return receiptTypeMapper.toDto(receiptType);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public ReceiptTypeDTO create(ReceiptType resources) {
        resources.setCreateDate(new Timestamp(System.currentTimeMillis()));
        resources.setCreateUser(chemicalFiberDeliveryNoteRepository.getRealNameByUserName(SecurityUtils.getUsername()));
        resources.setEnable(Boolean.TRUE);
        return receiptTypeMapper.toDto(receiptTypeRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(ReceiptType resources) {
        ReceiptType receiptType = receiptTypeRepository.findById(resources.getId()).orElseGet(ReceiptType::new);
        ValidationUtil.isNull( receiptType.getId(),"ReceiptType","id",resources.getId());
        receiptType.copy(resources);
        receiptTypeRepository.save(receiptType);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        receiptTypeRepository.deleteById(id);
    }


    @Override
    public void download(List<ReceiptTypeDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ReceiptTypeDTO receiptType : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("收款类型", receiptType.getName());
            map.put("创建日期", receiptType.getCreateDate());
            map.put("是否可用", receiptType.getEnable());
            map.put("创建用户", receiptType.getCreateUser());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}