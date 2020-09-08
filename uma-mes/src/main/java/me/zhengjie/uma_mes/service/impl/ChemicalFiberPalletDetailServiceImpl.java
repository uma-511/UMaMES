package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.service.ChemicalFiberPalletDetailService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@CacheConfig(cacheNames = "chemicalFiberPalletDetail")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ChemicalFiberPalletDetailServiceImpl implements ChemicalFiberPalletDetailService {
}
