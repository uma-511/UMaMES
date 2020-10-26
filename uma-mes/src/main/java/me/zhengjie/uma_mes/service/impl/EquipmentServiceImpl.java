package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.Equipment;
import me.zhengjie.uma_mes.repository.EquipmentRepository;
import me.zhengjie.uma_mes.service.EquipmentService;
import me.zhengjie.uma_mes.service.dto.EquipmentDTO;
import me.zhengjie.uma_mes.service.dto.EquipmentQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.EquipmentMapper;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@CacheConfig(cacheNames = "equipment")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class EquipmentServiceImpl implements EquipmentService {

    @Autowired
    private EquipmentMapper equipmentMapper;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Override
    public Map<String, Object> queryAll(EquipmentQueryCriteria criteria, Pageable pageable) {
        Page<Equipment> page = equipmentRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(equipmentMapper::toDto));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public EquipmentDTO create(Equipment resources) {
        return equipmentMapper.toDto(equipmentRepository.save(resources));
    }

    @Override
    public Equipment getByIp(String resources) {
        return equipmentRepository.getByIp(resources);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(Equipment resources) {
        Equipment equipment = equipmentRepository.findById(resources.getId()).orElseGet(Equipment::new);
        ValidationUtil.isNull( equipment.getId(),"Equipment","id",resources.getId());
        equipment.copy(resources);
        equipmentRepository.save(equipment);
    }
}
