package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.AttenceType;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.uma_mes.repository.AttenceTypeRepository;
import me.zhengjie.uma_mes.service.AttenceTypeService;
import me.zhengjie.uma_mes.service.dto.AttenceTypeDTO;
import me.zhengjie.uma_mes.service.dto.AttenceTypeQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.AttenceTypeMapper;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @author wave
* @date 2020-09-08
*/
@Service
@CacheConfig(cacheNames = "attenceType")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AttenceTypeServiceImpl implements AttenceTypeService {

    private final AttenceTypeRepository attenceTypeRepository;

    private final AttenceTypeMapper attenceTypeMapper;

    public AttenceTypeServiceImpl(AttenceTypeRepository attenceTypeRepository, AttenceTypeMapper attenceTypeMapper) {
        this.attenceTypeRepository = attenceTypeRepository;
        this.attenceTypeMapper = attenceTypeMapper;
    }

    @Override
    public Map<String,Object> queryAll(AttenceTypeQueryCriteria criteria, Pageable pageable){
        Page<AttenceType> page = attenceTypeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        BigDecimal zero = new BigDecimal(0.00);
        for(AttenceType a:page){
            if(null != a.getPrice() && a.getPrice().compareTo(zero) == 0){
                a.setPrice(null);
            }
        }
        return PageUtil.toPage(page.map(attenceTypeMapper::toDto));
    }

    @Override
    public List<AttenceTypeDTO> queryAll(AttenceTypeQueryCriteria criteria){
        return attenceTypeMapper.toDto(attenceTypeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    public AttenceTypeDTO findById(Integer id) {
        AttenceType attenceType = attenceTypeRepository.findById(id).orElseGet(AttenceType::new);
        ValidationUtil.isNull(attenceType.getId(),"AttenceType","id",id);
        return attenceTypeMapper.toDto(attenceType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AttenceTypeDTO create(AttenceType resources) {
        resources.setEnable(Boolean.TRUE);
        return attenceTypeMapper.toDto(attenceTypeRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AttenceType resources) {
        AttenceType attenceType = attenceTypeRepository.findById(resources.getId()).orElseGet(AttenceType::new);
        ValidationUtil.isNull( attenceType.getId(),"AttenceType","id",resources.getId());
        attenceType.copy(resources);
        attenceTypeRepository.save(attenceType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        attenceTypeRepository.deleteById(id);
    }


    @Override
    public void download(List<AttenceTypeDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AttenceTypeDTO attenceType : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("类型", attenceType.getAttenceType());
            map.put("状态", attenceType.getEnable());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}