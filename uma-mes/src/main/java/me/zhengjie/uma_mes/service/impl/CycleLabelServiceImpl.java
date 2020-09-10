package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.CycleLabel;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.uma_mes.repository.CycleLabelRepository;
import me.zhengjie.uma_mes.service.CycleLabelService;
import me.zhengjie.uma_mes.service.dto.CycleLabelDTO;
import me.zhengjie.uma_mes.service.dto.CycleLabelQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.CycleLabelMapper;
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
* @author wave
* @date 2020-09-09
*/
@Service
@CacheConfig(cacheNames = "cycleLabel")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CycleLabelServiceImpl implements CycleLabelService {

    private final CycleLabelRepository cycleLabelRepository;

    private final CycleLabelMapper cycleLabelMapper;

    public CycleLabelServiceImpl(CycleLabelRepository cycleLabelRepository, CycleLabelMapper cycleLabelMapper) {
        this.cycleLabelRepository = cycleLabelRepository;
        this.cycleLabelMapper = cycleLabelMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(CycleLabelQueryCriteria criteria, Pageable pageable){
        Page<CycleLabel> page = cycleLabelRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(cycleLabelMapper::toDto));
    }

    @Override
    @Cacheable
    public List<CycleLabelDTO> queryAll(CycleLabelQueryCriteria criteria){
        return cycleLabelMapper.toDto(cycleLabelRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public CycleLabelDTO findById(Integer id) {
        CycleLabel cycleLabel = cycleLabelRepository.findById(id).orElseGet(CycleLabel::new);
        ValidationUtil.isNull(cycleLabel.getId(),"CycleLabel","id",id);
        return cycleLabelMapper.toDto(cycleLabel);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public CycleLabelDTO create(CycleLabel resources) {
        return cycleLabelMapper.toDto(cycleLabelRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(CycleLabel resources) {
        CycleLabel cycleLabel = cycleLabelRepository.findById(resources.getId()).orElseGet(CycleLabel::new);
        ValidationUtil.isNull( cycleLabel.getId(),"CycleLabel","id",resources.getId());
        cycleLabel.copy(resources);
        cycleLabelRepository.save(cycleLabel);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        CycleLabel cycleLabel = cycleLabelRepository.findById(id).orElseGet(CycleLabel::new);
        cycleLabelRepository.save(cycleLabel);
    }


    @Override
    public void download(List<CycleLabelDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CycleLabelDTO cycleLabel : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("周期", cycleLabel.getName());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}