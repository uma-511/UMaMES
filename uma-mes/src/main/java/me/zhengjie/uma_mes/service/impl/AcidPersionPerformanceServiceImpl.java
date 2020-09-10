package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.AcidPersionPerformance;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.uma_mes.repository.AcidPersionPerformanceRepository;
import me.zhengjie.uma_mes.service.AcidPersionPerformanceService;
import me.zhengjie.uma_mes.service.dto.AcidPersionPerformanceDTO;
import me.zhengjie.uma_mes.service.dto.AcidPersionPerformanceQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.AcidPersionPerformanceMapper;
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

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @author wave
* @date 2020-09-05
*/
@Service
@CacheConfig(cacheNames = "acidPersionPerformance")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AcidPersionPerformanceServiceImpl implements AcidPersionPerformanceService {

    private final AcidPersionPerformanceRepository acidPersionPerformanceRepository;

    private final AcidPersionPerformanceMapper acidPersionPerformanceMapper;

    public AcidPersionPerformanceServiceImpl(AcidPersionPerformanceRepository acidPersionPerformanceRepository, AcidPersionPerformanceMapper acidPersionPerformanceMapper) {
        this.acidPersionPerformanceRepository = acidPersionPerformanceRepository;
        this.acidPersionPerformanceMapper = acidPersionPerformanceMapper;
    }

    @Override
    public Map<String,Object> queryAll(AcidPersionPerformanceQueryCriteria criteria, Pageable pageable){
        List<Boolean> booleanList = new ArrayList<>();
        booleanList.add(Boolean.TRUE);
        if (null != criteria.getShowUnEnable() && criteria.getShowUnEnable())
        {
            booleanList.add(Boolean.FALSE);
        }
        criteria.setEnableList(booleanList);
        Page<AcidPersionPerformance> page = acidPersionPerformanceRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(acidPersionPerformanceMapper::toDto));
    }

    @Override
    public List<AcidPersionPerformanceDTO> queryAll(AcidPersionPerformanceQueryCriteria criteria){
        return acidPersionPerformanceMapper.toDto(acidPersionPerformanceRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    public AcidPersionPerformanceDTO findById(Integer id) {
        AcidPersionPerformance acidPersionPerformance = acidPersionPerformanceRepository.findById(id).orElseGet(AcidPersionPerformance::new);
        ValidationUtil.isNull(acidPersionPerformance.getId(),"AcidPersionPerformance","id",id);
        return acidPersionPerformanceMapper.toDto(acidPersionPerformance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AcidPersionPerformanceDTO create(AcidPersionPerformance resources) {
        resources.setCreateDate((new Timestamp(System.currentTimeMillis())));
        resources.setEnable(Boolean.TRUE);
        return acidPersionPerformanceMapper.toDto(acidPersionPerformanceRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AcidPersionPerformance resources) {
        AcidPersionPerformance acidPersionPerformance = acidPersionPerformanceRepository.findById(resources.getId()).orElseGet(AcidPersionPerformance::new);
        ValidationUtil.isNull( acidPersionPerformance.getId(),"AcidPersionPerformance","id",resources.getId());
        acidPersionPerformance.copy(resources);
        acidPersionPerformanceRepository.save(acidPersionPerformance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        AcidPersionPerformance acidPersionPerformance = acidPersionPerformanceRepository.findById(id).orElseGet(AcidPersionPerformance::new);
        acidPersionPerformance.setEnable(Boolean.FALSE);
        acidPersionPerformanceRepository.save(acidPersionPerformance);
    }


    @Override
    public void download(List<AcidPersionPerformanceDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AcidPersionPerformanceDTO acidPersionPerformance : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("责任人", acidPersionPerformance.getPerson());
            map.put("任务日期", acidPersionPerformance.getTaskDate());
            map.put("产品名称", acidPersionPerformance.getProductName());
            map.put("桶数", acidPersionPerformance.getNumber());
            map.put("规格(公斤)", acidPersionPerformance.getSpecifications());
            map.put("吨数", acidPersionPerformance.getWeight());
            map.put("单价（元）", acidPersionPerformance.getUnitPrice());
            map.put("金额", acidPersionPerformance.getPrice());
            map.put("状态", acidPersionPerformance.getEnable());
            map.put("创建日期", acidPersionPerformance.getCreateDate());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}