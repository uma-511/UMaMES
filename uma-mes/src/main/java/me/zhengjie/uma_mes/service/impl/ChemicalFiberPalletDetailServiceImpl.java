package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.ChemicalFiberPalletDetail;
import me.zhengjie.uma_mes.repository.ChemicalFiberPalletDetailRepository;
import me.zhengjie.uma_mes.service.ChemicalFiberPalletDetailService;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberPalletDetailDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberPalletDetailQueryCeiteria;
import me.zhengjie.uma_mes.service.mapper.ChemicalFiberPalletDetailMapper;
import me.zhengjie.uma_mes.service.mapper.ChemicalFiberPalletDetailMapperImpl;
import me.zhengjie.utils.QueryHelp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@CacheConfig(cacheNames = "chemicalFiberPalletDetail")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ChemicalFiberPalletDetailServiceImpl implements ChemicalFiberPalletDetailService {

    @Autowired
    private ChemicalFiberPalletDetailMapper chemicalFiberPalletDetailMapper;

    @Autowired
    private ChemicalFiberPalletDetailRepository chemicalFiberPalletDetailRepository;

    public List<ChemicalFiberPalletDetailDTO> queryAll(ChemicalFiberPalletDetailQueryCeiteria resources) {
        return chemicalFiberPalletDetailMapper.toDto(chemicalFiberPalletDetailRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,resources,criteriaBuilder)));
    }
}
