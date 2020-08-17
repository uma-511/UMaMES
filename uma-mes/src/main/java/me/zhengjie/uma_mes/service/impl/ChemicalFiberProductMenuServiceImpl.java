package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.ChemicalFiberProductMenu;
import me.zhengjie.uma_mes.repository.ChemicalFiberProductMenuRepository;
import me.zhengjie.uma_mes.service.ChemicalFiberProductMenuService;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberProductMenuDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberProductMenuQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.ChemicalFiberProductMenuMapper;
import me.zhengjie.utils.QueryHelp;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@CacheConfig(cacheNames = "chemicalFiberProductionMenu")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ChemicalFiberProductMenuServiceImpl implements ChemicalFiberProductMenuService {

    private final ChemicalFiberProductMenuRepository chemicalFiberProductMenuRepository;

    private final ChemicalFiberProductMenuMapper chemicalFiberProductMenuMapper;

    public ChemicalFiberProductMenuServiceImpl(ChemicalFiberProductMenuRepository chemicalFiberProductMenuRepository,
                                               ChemicalFiberProductMenuMapper chemicalFiberProductMenuMapper){
        this.chemicalFiberProductMenuRepository = chemicalFiberProductMenuRepository;
        this.chemicalFiberProductMenuMapper = chemicalFiberProductMenuMapper;
    }

    public List<ChemicalFiberProductMenuDTO> queryAll(ChemicalFiberProductMenuQueryCriteria criteria){
        //List<ChemicalFiberProductMenu> b = chemicalFiberProductMenuRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder));
        return chemicalFiberProductMenuMapper.toDto(chemicalFiberProductMenuRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Transactional(rollbackFor = Exception.class)
    public ChemicalFiberProductMenuDTO create(ChemicalFiberProductMenu resources) {
        return chemicalFiberProductMenuMapper.toDto(chemicalFiberProductMenuRepository.save(resources));
    }




}
