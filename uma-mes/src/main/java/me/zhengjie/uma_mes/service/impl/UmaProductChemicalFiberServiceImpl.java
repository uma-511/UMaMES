package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.UmaProductChemicalFiber;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.uma_mes.repository.UmaProductChemicalFiberRepository;
import me.zhengjie.uma_mes.service.UmaProductChemicalFiberService;
import me.zhengjie.uma_mes.service.dto.UmaProductChemicalFiberDTO;
import me.zhengjie.uma_mes.service.dto.UmaProductChemicalFiberQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.UmaProductChemicalFiberMapper;
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
@CacheConfig(cacheNames = "umaProductChemicalFiber")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UmaProductChemicalFiberServiceImpl implements UmaProductChemicalFiberService {

    private final UmaProductChemicalFiberRepository umaProductChemicalFiberRepository;

    private final UmaProductChemicalFiberMapper umaProductChemicalFiberMapper;

    public UmaProductChemicalFiberServiceImpl(UmaProductChemicalFiberRepository umaProductChemicalFiberRepository, UmaProductChemicalFiberMapper umaProductChemicalFiberMapper) {
        this.umaProductChemicalFiberRepository = umaProductChemicalFiberRepository;
        this.umaProductChemicalFiberMapper = umaProductChemicalFiberMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(UmaProductChemicalFiberQueryCriteria criteria, Pageable pageable){
        Page<UmaProductChemicalFiber> page = umaProductChemicalFiberRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(umaProductChemicalFiberMapper::toDto));
    }

    @Override
    @Cacheable
    public List<UmaProductChemicalFiberDTO> queryAll(UmaProductChemicalFiberQueryCriteria criteria){
        return umaProductChemicalFiberMapper.toDto(umaProductChemicalFiberRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public UmaProductChemicalFiberDTO findById(Integer id) {
        UmaProductChemicalFiber umaProductChemicalFiber = umaProductChemicalFiberRepository.findById(id).orElseGet(UmaProductChemicalFiber::new);
        ValidationUtil.isNull(umaProductChemicalFiber.getId(),"UmaProductChemicalFiber","id",id);
        return umaProductChemicalFiberMapper.toDto(umaProductChemicalFiber);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public UmaProductChemicalFiberDTO create(UmaProductChemicalFiber resources) {
        return umaProductChemicalFiberMapper.toDto(umaProductChemicalFiberRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(UmaProductChemicalFiber resources) {
        UmaProductChemicalFiber umaProductChemicalFiber = umaProductChemicalFiberRepository.findById(resources.getId()).orElseGet(UmaProductChemicalFiber::new);
        ValidationUtil.isNull( umaProductChemicalFiber.getId(),"UmaProductChemicalFiber","id",resources.getId());
        umaProductChemicalFiber.copy(resources);
        umaProductChemicalFiberRepository.save(umaProductChemicalFiber);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        umaProductChemicalFiberRepository.deleteById(id);
    }


    @Override
    public void download(List<UmaProductChemicalFiberDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UmaProductChemicalFiberDTO umaProductChemicalFiber : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("产品型号", umaProductChemicalFiber.getModel());
            map.put("产品名称", umaProductChemicalFiber.getName());
            map.put("色号", umaProductChemicalFiber.getColor());
            map.put("纤度", umaProductChemicalFiber.getFineness());
            map.put("创建日期", umaProductChemicalFiber.getCreateDate());
            map.put("创建人", umaProductChemicalFiber.getCreateUser());
            map.put("删除标识", umaProductChemicalFiber.getDelFlag());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}