package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.UmaChemicalFiberStatementDetails;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.uma_mes.repository.UmaChemicalFiberStatementDetailsRepository;
import me.zhengjie.uma_mes.service.UmaChemicalFiberStatementDetailsService;
import me.zhengjie.uma_mes.service.dto.UmaChemicalFiberStatementDetailsDTO;
import me.zhengjie.uma_mes.service.dto.UmaChemicalFiberStatementDetailsQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.UmaChemicalFiberStatementDetailsMapper;
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
* @date 2020-03-11
*/
@Service
@CacheConfig(cacheNames = "umaChemicalFiberStatementDetails")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UmaChemicalFiberStatementDetailsServiceImpl implements UmaChemicalFiberStatementDetailsService {

    private final UmaChemicalFiberStatementDetailsRepository umaChemicalFiberStatementDetailsRepository;

    private final UmaChemicalFiberStatementDetailsMapper umaChemicalFiberStatementDetailsMapper;

    public UmaChemicalFiberStatementDetailsServiceImpl(UmaChemicalFiberStatementDetailsRepository umaChemicalFiberStatementDetailsRepository, UmaChemicalFiberStatementDetailsMapper umaChemicalFiberStatementDetailsMapper) {
        this.umaChemicalFiberStatementDetailsRepository = umaChemicalFiberStatementDetailsRepository;
        this.umaChemicalFiberStatementDetailsMapper = umaChemicalFiberStatementDetailsMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(UmaChemicalFiberStatementDetailsQueryCriteria criteria, Pageable pageable){
        Page<UmaChemicalFiberStatementDetails> page = umaChemicalFiberStatementDetailsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(umaChemicalFiberStatementDetailsMapper::toDto));
    }

    @Override
    @Cacheable
    public List<UmaChemicalFiberStatementDetailsDTO> queryAll(UmaChemicalFiberStatementDetailsQueryCriteria criteria){
        return umaChemicalFiberStatementDetailsMapper.toDto(umaChemicalFiberStatementDetailsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public UmaChemicalFiberStatementDetailsDTO findById(Integer id) {
        UmaChemicalFiberStatementDetails umaChemicalFiberStatementDetails = umaChemicalFiberStatementDetailsRepository.findById(id).orElseGet(UmaChemicalFiberStatementDetails::new);
        ValidationUtil.isNull(umaChemicalFiberStatementDetails.getId(),"UmaChemicalFiberStatementDetails","id",id);
        return umaChemicalFiberStatementDetailsMapper.toDto(umaChemicalFiberStatementDetails);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public UmaChemicalFiberStatementDetailsDTO create(UmaChemicalFiberStatementDetails resources) {
        return umaChemicalFiberStatementDetailsMapper.toDto(umaChemicalFiberStatementDetailsRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(UmaChemicalFiberStatementDetails resources) {
        UmaChemicalFiberStatementDetails umaChemicalFiberStatementDetails = umaChemicalFiberStatementDetailsRepository.findById(resources.getId()).orElseGet(UmaChemicalFiberStatementDetails::new);
        ValidationUtil.isNull( umaChemicalFiberStatementDetails.getId(),"UmaChemicalFiberStatementDetails","id",resources.getId());
        umaChemicalFiberStatementDetails.copy(resources);
        umaChemicalFiberStatementDetailsRepository.save(umaChemicalFiberStatementDetails);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        umaChemicalFiberStatementDetailsRepository.deleteById(id);
    }


    @Override
    public void download(List<UmaChemicalFiberStatementDetailsDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UmaChemicalFiberStatementDetailsDTO umaChemicalFiberStatementDetails : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("对账单主键", umaChemicalFiberStatementDetails.getStatementId());
            map.put("送货单号", umaChemicalFiberStatementDetails.getScanNumber());
            map.put("送货日期", umaChemicalFiberStatementDetails.getScanDate());
            map.put("产品名称", umaChemicalFiberStatementDetails.getProdName());
            map.put("总件数", umaChemicalFiberStatementDetails.getTotalBag());
            map.put("重量", umaChemicalFiberStatementDetails.getNetWeight());
            map.put("单价", umaChemicalFiberStatementDetails.getSellingPrice());
            map.put("金额", umaChemicalFiberStatementDetails.getTotalPrice());
            map.put("备注", umaChemicalFiberStatementDetails.getRemark());
            map.put("预付款", umaChemicalFiberStatementDetails.getAdvanceCharge());
            map.put("扣除金额", umaChemicalFiberStatementDetails.getAmountDeducted());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}