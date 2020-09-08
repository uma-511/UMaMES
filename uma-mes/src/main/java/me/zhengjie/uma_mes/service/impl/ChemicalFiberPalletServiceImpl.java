package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.ChemicalFiberPallet;
import me.zhengjie.uma_mes.repository.ChemicalFiberPalletRepository;
import me.zhengjie.uma_mes.service.ChemicalFiberPalletService;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberPalletQueryCeiteria;
import me.zhengjie.uma_mes.service.mapper.ChemicalFiberPalletMapper;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Map;

@Service
@CacheConfig(cacheNames = "chemicalFiberPallet")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ChemicalFiberPalletServiceImpl implements ChemicalFiberPalletService {

    @Autowired
    private ChemicalFiberPalletRepository chemicalFiberPalletRepository;

    @Autowired
    private ChemicalFiberPalletMapper chemicalFiberPalletMapper;

    public Map<String, Object> queryAll(ChemicalFiberPalletQueryCeiteria criteria, Pageable pageable) {
        /*if (criteria.getTempStartTime() != null) {
            criteria.setStartTime(new Timestamp(criteria.getTempStartTime()));
            criteria.setEndTime(new Timestamp(criteria.getTempEndTime()));
        }*/
        Page<ChemicalFiberPallet> page = chemicalFiberPalletRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(chemicalFiberPalletMapper::toDto));

    }
}
