package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.BookAccountType;
import me.zhengjie.uma_mes.repository.ChemicalFiberDeliveryNoteRepository;
import me.zhengjie.utils.*;
import me.zhengjie.uma_mes.repository.BookAccountTypeRepository;
import me.zhengjie.uma_mes.service.BookAccountTypeService;
import me.zhengjie.uma_mes.service.dto.BookAccountTypeDTO;
import me.zhengjie.uma_mes.service.dto.BookAccountTypeQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.BookAccountTypeMapper;
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
@CacheConfig(cacheNames = "bookAccountType")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class BookAccountTypeServiceImpl implements BookAccountTypeService {

    private final BookAccountTypeRepository bookAccountTypeRepository;

    private final BookAccountTypeMapper bookAccountTypeMapper;

    private final ChemicalFiberDeliveryNoteRepository chemicalFiberDeliveryNoteRepository;

    public BookAccountTypeServiceImpl(BookAccountTypeRepository bookAccountTypeRepository, BookAccountTypeMapper bookAccountTypeMapper, ChemicalFiberDeliveryNoteRepository chemicalFiberDeliveryNoteRepository) {
        this.bookAccountTypeRepository = bookAccountTypeRepository;
        this.bookAccountTypeMapper = bookAccountTypeMapper;
        this.chemicalFiberDeliveryNoteRepository = chemicalFiberDeliveryNoteRepository;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(BookAccountTypeQueryCriteria criteria, Pageable pageable){
        Page<BookAccountType> page = bookAccountTypeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(bookAccountTypeMapper::toDto));
    }

    @Override
    @Cacheable
    public List<BookAccountTypeDTO> queryAll(BookAccountTypeQueryCriteria criteria){
        return bookAccountTypeMapper.toDto(bookAccountTypeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public BookAccountTypeDTO findById(Integer id) {
        BookAccountType bookAccountType = bookAccountTypeRepository.findById(id).orElseGet(BookAccountType::new);
        ValidationUtil.isNull(bookAccountType.getId(),"BookAccountType","id",id);
        return bookAccountTypeMapper.toDto(bookAccountType);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public BookAccountTypeDTO create(BookAccountType resources) {
        resources.setCreateTime(new Timestamp(System.currentTimeMillis()));
        resources.setEnable(Boolean.TRUE);
        resources.setCreateUser(chemicalFiberDeliveryNoteRepository.getRealNameByUserName(SecurityUtils.getUsername()));
        return bookAccountTypeMapper.toDto(bookAccountTypeRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(BookAccountType resources) {
        BookAccountType bookAccountType = bookAccountTypeRepository.findById(resources.getId()).orElseGet(BookAccountType::new);
        ValidationUtil.isNull( bookAccountType.getId(),"BookAccountType","id",resources.getId());
        bookAccountType.copy(resources);
        bookAccountTypeRepository.save(bookAccountType);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        bookAccountTypeRepository.deleteById(id);
    }


    @Override
    public void download(List<BookAccountTypeDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (BookAccountTypeDTO bookAccountType : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("记账项目", bookAccountType.getName());
            map.put("创建日期", bookAccountType.getCreateTime());
            map.put("是否可用", bookAccountType.getEnable());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}