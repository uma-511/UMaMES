package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.BonusJob;
import me.zhengjie.uma_mes.repository.BonusJobRepository;
import me.zhengjie.uma_mes.service.BonusJobService;
import me.zhengjie.uma_mes.service.dto.BonusJobQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.BonusJobMapper;
import me.zhengjie.utils.QueryHelp;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author Zheng Jie
* @date 2019-03-29
*/
@Service
@CacheConfig(cacheNames = "bonusJob")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class BonusJobServiceImpl implements BonusJobService {

    private final BonusJobRepository bonusJobRepository;

    private final BonusJobMapper bonusJobMapper;

    public BonusJobServiceImpl(BonusJobRepository bonusJobRepository, BonusJobMapper bonusJobMapper) {
        this.bonusJobRepository = bonusJobRepository;
        this.bonusJobMapper = bonusJobMapper;
    }

    @Override
    @Cacheable
    public List<BonusJob> queryAll(BonusJobQueryCriteria criteria) {
        List<BonusJob> list = bonusJobRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder));
        return list;
    }
}