package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.ChemicalFibeDeviceGroup;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.uma_mes.repository.ChemicalFibeDeviceGroupRepository;
import me.zhengjie.uma_mes.service.ChemicalFibeDeviceGroupService;
import me.zhengjie.uma_mes.service.dto.ChemicalFibeDeviceGroupDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFibeDeviceGroupQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.ChemicalFibeDeviceGroupMapper;
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
* @author Xie Ji Biao
* @date 2020-04-16
*/
@Service
@CacheConfig(cacheNames = "chemicalFibeDeviceGroup")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ChemicalFibeDeviceGroupServiceImpl implements ChemicalFibeDeviceGroupService {

    private final ChemicalFibeDeviceGroupRepository chemicalFibeDeviceGroupRepository;

    private final ChemicalFibeDeviceGroupMapper chemicalFibeDeviceGroupMapper;

    public ChemicalFibeDeviceGroupServiceImpl(ChemicalFibeDeviceGroupRepository chemicalFibeDeviceGroupRepository, ChemicalFibeDeviceGroupMapper chemicalFibeDeviceGroupMapper) {
        this.chemicalFibeDeviceGroupRepository = chemicalFibeDeviceGroupRepository;
        this.chemicalFibeDeviceGroupMapper = chemicalFibeDeviceGroupMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(ChemicalFibeDeviceGroupQueryCriteria criteria, Pageable pageable){
        Page<ChemicalFibeDeviceGroup> page = chemicalFibeDeviceGroupRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(chemicalFibeDeviceGroupMapper::toDto));
    }

    @Override
    @Cacheable
    public List<ChemicalFibeDeviceGroupDTO> queryAll(ChemicalFibeDeviceGroupQueryCriteria criteria){
        return chemicalFibeDeviceGroupMapper.toDto(chemicalFibeDeviceGroupRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public ChemicalFibeDeviceGroupDTO findById(Integer id) {
        ChemicalFibeDeviceGroup chemicalFibeDeviceGroup = chemicalFibeDeviceGroupRepository.findById(id).orElseGet(ChemicalFibeDeviceGroup::new);
        ValidationUtil.isNull(chemicalFibeDeviceGroup.getId(),"ChemicalFibeDeviceGroup","id",id);
        return chemicalFibeDeviceGroupMapper.toDto(chemicalFibeDeviceGroup);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public ChemicalFibeDeviceGroupDTO create(ChemicalFibeDeviceGroup resources) {
        return chemicalFibeDeviceGroupMapper.toDto(chemicalFibeDeviceGroupRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(ChemicalFibeDeviceGroup resources) {
        ChemicalFibeDeviceGroup chemicalFibeDeviceGroup = chemicalFibeDeviceGroupRepository.findById(resources.getId()).orElseGet(ChemicalFibeDeviceGroup::new);
        ValidationUtil.isNull( chemicalFibeDeviceGroup.getId(),"ChemicalFibeDeviceGroup","id",resources.getId());
        chemicalFibeDeviceGroup.copy(resources);
        chemicalFibeDeviceGroupRepository.save(chemicalFibeDeviceGroup);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        chemicalFibeDeviceGroupRepository.deleteById(id);
    }


    @Override
    public void download(List<ChemicalFibeDeviceGroupDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ChemicalFibeDeviceGroupDTO chemicalFibeDeviceGroup : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("设备分组名称", chemicalFibeDeviceGroup.getName());
            map.put("使用状态：0：停用 1：正常", chemicalFibeDeviceGroup.getStatus());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}