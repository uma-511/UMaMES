package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.BonusCycle;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.uma_mes.repository.BonusCycleRepository;
import me.zhengjie.uma_mes.service.BonusCycleService;
import me.zhengjie.uma_mes.service.dto.BonusCycleDTO;
import me.zhengjie.uma_mes.service.dto.BonusCycleQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.BonusCycleMapper;
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
* @date 2020-09-10
*/
@Service
@CacheConfig(cacheNames = "bonusCycle")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class BonusCycleServiceImpl implements BonusCycleService {

    private final BonusCycleRepository bonusCycleRepository;

    private final BonusCycleMapper bonusCycleMapper;

    public BonusCycleServiceImpl(BonusCycleRepository bonusCycleRepository, BonusCycleMapper bonusCycleMapper) {
        this.bonusCycleRepository = bonusCycleRepository;
        this.bonusCycleMapper = bonusCycleMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(BonusCycleQueryCriteria criteria, Pageable pageable){
        Page<BonusCycle> page = bonusCycleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(bonusCycleMapper::toDto));
    }

    @Override
    @Cacheable
    public List<BonusCycleDTO> queryAll(BonusCycleQueryCriteria criteria){
        return bonusCycleMapper.toDto(bonusCycleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public BonusCycleDTO create(BonusCycle resources) {
        return bonusCycleMapper.toDto(bonusCycleRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(BonusCycle resources) {
        BonusCycle bonusCycle = bonusCycleRepository.findById(resources.getBonusCycle()).orElseGet(BonusCycle::new);
        ValidationUtil.isNull( bonusCycle.getBonusCycle(),"BonusCycle","id",resources.getBonusCycle());
        bonusCycle.copy(resources);
        bonusCycleRepository.save(bonusCycle);
    }

    @Override
    public void download(List<BonusCycleDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (BonusCycleDTO bonusCycle : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}