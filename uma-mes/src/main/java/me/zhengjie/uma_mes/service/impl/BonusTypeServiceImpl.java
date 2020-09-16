package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.BonusJob;
import me.zhengjie.uma_mes.domain.BonusType;
import me.zhengjie.uma_mes.domain.CycleLabel;
import me.zhengjie.uma_mes.repository.BonusJobRepository;
import me.zhengjie.uma_mes.repository.CycleLabelRepository;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.uma_mes.repository.BonusTypeRepository;
import me.zhengjie.uma_mes.service.BonusTypeService;
import me.zhengjie.uma_mes.service.dto.BonusTypeDTO;
import me.zhengjie.uma_mes.service.dto.BonusTypeQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.BonusTypeMapper;
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

import java.util.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author wave
* @date 2020-09-09
*/
@Service
@CacheConfig(cacheNames = "bonusType")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class BonusTypeServiceImpl implements BonusTypeService {

    private final BonusTypeRepository bonusTypeRepository;

    private final CycleLabelRepository cycleLabelRepository;

    private final BonusJobRepository bonusJobRepository;

    private final BonusTypeMapper bonusTypeMapper;

    public BonusTypeServiceImpl(BonusTypeRepository bonusTypeRepository, CycleLabelRepository cycleLabelRepository, BonusJobRepository bonusJobRepository, BonusTypeMapper bonusTypeMapper) {
        this.bonusTypeRepository = bonusTypeRepository;
        this.cycleLabelRepository = cycleLabelRepository;
        this.bonusJobRepository = bonusJobRepository;
        this.bonusTypeMapper = bonusTypeMapper;
    }

    @Override
    public Map<String,Object> queryAll(BonusTypeQueryCriteria criteria, Pageable pageable){
        Page<BonusType> page = bonusTypeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(bonusTypeMapper::toDto));
    }

    @Override
    public List<BonusTypeDTO> queryAll(BonusTypeQueryCriteria criteria){
        return bonusTypeMapper.toDto(bonusTypeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    public BonusTypeDTO findById(Long id) {
        BonusType bonusType = bonusTypeRepository.findById(id).orElseGet(BonusType::new);
        ValidationUtil.isNull(bonusType.getId(),"BonusType","id",id);
        return bonusTypeMapper.toDto(bonusType);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public BonusTypeDTO create(BonusType resources) {
        return bonusTypeMapper.toDto(bonusTypeRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(BonusType resources) {
        BonusType bonusType = bonusTypeRepository.findById(resources.getId()).orElseGet(BonusType::new);
        ValidationUtil.isNull( bonusType.getId(),"BonusType","id",resources.getId());
        bonusType.copy(resources);
        bonusTypeRepository.save(bonusType);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        BonusType bonusType = bonusTypeRepository.findById(id).orElseGet(BonusType::new);
        bonusType.setEnable(Boolean.FALSE);
        bonusTypeRepository.save(bonusType);
    }



    @Override
    @CacheEvict(allEntries = true)
    public void updateCycleMenu(BonusType resources, BonusTypeDTO bonusTypeDTO) {
        BonusType bonusType = bonusTypeMapper.toEntity(bonusTypeDTO);
        bonusType.setCycles(resources.getCycles());
        bonusTypeRepository.save(bonusType);
    }

    @Override
    public Object getCycleMenusTree() {
        List<Map<String,Object>> list = new LinkedList<>();
        List<CycleLabel> cycleList = cycleLabelRepository.findAll();
        for (CycleLabel c: cycleList) {
            Map<String,Object> map = new HashMap<>();
            map.put("id",c.getId());
            map.put("label",c.getName());
            list.add(map);
        }
        return list;
    }

    @Override
    public Object getBonusJobsTree() {
        List<Map<String,Object>> list = new LinkedList<>();
        List<BonusJob> bonusJobList = bonusJobRepository.findAll();
        for (BonusJob b: bonusJobList) {
            Map<String,Object> map = new HashMap<>();
            map.put("id",b.getId());
            map.put("label",b.getName());
            list.add(map);
        }
        return list;
    }

    @Override
    public void updateCycle(BonusType resources, BonusTypeDTO bonusTypeDTO) {
        BonusType bonusType = bonusTypeMapper.toEntity(bonusTypeDTO);
        bonusType.setCycles(resources.getCycles());
        bonusTypeRepository.save(bonusType);
    }

    @Override
    public void updateJob(BonusType resources, BonusTypeDTO bonusTypeDTO) {
        BonusType bonusType = bonusTypeMapper.toEntity(bonusTypeDTO);
        bonusType.setBonusJobs(resources.getBonusJobs());
        bonusTypeRepository.save(bonusType);
    }

    @Override
    public void download(List<BonusTypeDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (BonusTypeDTO bonusType : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("奖金类别", bonusType.getType());
            map.put("金额", bonusType.getPrice());
            map.put("创建日期", bonusType.getCreateTime());
            map.put("状态", bonusType.getEnable());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}