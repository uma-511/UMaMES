package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.TravelPersionPerformance;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.uma_mes.repository.TravelPersionPerformanceRepository;
import me.zhengjie.uma_mes.service.TravelPersionPerformanceService;
import me.zhengjie.uma_mes.service.dto.TravelPersionPerformanceDTO;
import me.zhengjie.uma_mes.service.dto.TravelPersionPerformanceQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.TravelPersionPerformanceMapper;
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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @author wave
* @date 2020-09-02
*/
@Service
@CacheConfig(cacheNames = "travelPersionPerformance")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class TravelPersionPerformanceServiceImpl implements TravelPersionPerformanceService {

    private final TravelPersionPerformanceRepository travelPersionPerformanceRepository;

    private final TravelPersionPerformanceMapper travelPersionPerformanceMapper;

    public TravelPersionPerformanceServiceImpl(TravelPersionPerformanceRepository travelPersionPerformanceRepository, TravelPersionPerformanceMapper travelPersionPerformanceMapper) {
        this.travelPersionPerformanceRepository = travelPersionPerformanceRepository;
        this.travelPersionPerformanceMapper = travelPersionPerformanceMapper;
    }

    @Override
    public Map<String,Object> queryAll(TravelPersionPerformanceQueryCriteria criteria, Pageable pageable){
        List<Boolean> booleanList = new ArrayList<>();
        booleanList.add(Boolean.TRUE);
        if (null != criteria.getShowUnEnable() && criteria.getShowUnEnable())
        {
            booleanList.add(Boolean.FALSE);
        }
        criteria.setEnableList(booleanList);
        Page<TravelPersionPerformance> page = travelPersionPerformanceRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        for(TravelPersionPerformance travelPersionPerformance:page) {
            if (null == travelPersionPerformance.getMileageFee() || travelPersionPerformance.getMileageFee().compareTo(new BigDecimal(0.00)) == 0){
                travelPersionPerformance.setMileageFee(null);
            }
            if (null == travelPersionPerformance.getOvertimePay() || travelPersionPerformance.getOvertimePay().compareTo(new BigDecimal(0.00)) == 0){
                travelPersionPerformance.setOvertimePay(null);
            }
            if (null == travelPersionPerformance.getAllowance() || travelPersionPerformance.getAllowance().compareTo(new BigDecimal(0.00)) == 0){
                travelPersionPerformance.setAllowance(null);
            }
            if (null == travelPersionPerformance.getSurcharge() || travelPersionPerformance.getSurcharge().compareTo(new BigDecimal(0.00)) == 0){
                travelPersionPerformance.setSurcharge(null);
            }
            if (null == travelPersionPerformance.getHandlingCost() || travelPersionPerformance.getHandlingCost().compareTo(new BigDecimal(0.00)) == 0){
                travelPersionPerformance.setHandlingCost(null);
            }
            if (null == travelPersionPerformance.getTotalPerformance() || travelPersionPerformance.getTotalPerformance().compareTo(new BigDecimal(0.00)) == 0){
                travelPersionPerformance.setTotalPerformance(null);
            }
        }
        return PageUtil.toPage(page.map(travelPersionPerformanceMapper::toDto));
    }

    @Override
    public List<TravelPersionPerformanceDTO> queryAll(TravelPersionPerformanceQueryCriteria criteria){
        return travelPersionPerformanceMapper.toDto(travelPersionPerformanceRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    public TravelPersionPerformanceDTO findById(Integer id) {
        TravelPersionPerformance travelPersionPerformance = travelPersionPerformanceRepository.findById(id).orElseGet(TravelPersionPerformance::new);
        ValidationUtil.isNull(travelPersionPerformance.getId(),"TravelPersionPerformance","id",id);
        return travelPersionPerformanceMapper.toDto(travelPersionPerformance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TravelPersionPerformanceDTO create(TravelPersionPerformance resources) {
        resources.setEnable(Boolean.TRUE);
        return travelPersionPerformanceMapper.toDto(travelPersionPerformanceRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TravelPersionPerformance resources) {
        TravelPersionPerformance travelPersionPerformance = travelPersionPerformanceRepository.findById(resources.getId()).orElseGet(TravelPersionPerformance::new);
        ValidationUtil.isNull( travelPersionPerformance.getId(),"TravelPersionPerformance","id",resources.getId());
        travelPersionPerformance.copy(resources);
        travelPersionPerformanceRepository.save(travelPersionPerformance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        TravelPersionPerformance travelPersionPerformance = travelPersionPerformanceRepository.findById(id).orElseGet(TravelPersionPerformance::new);
        travelPersionPerformance.setEnable(Boolean.FALSE);
        travelPersionPerformanceRepository.save(travelPersionPerformance);
    }


    @Override
    public void download(List<TravelPersionPerformanceDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TravelPersionPerformanceDTO travelPersionPerformance : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("责任人", travelPersionPerformance.getPersonName());
            map.put("人员id", travelPersionPerformance.getPersonId());
            map.put("职位", travelPersionPerformance.getPermission());
            map.put("里程费", travelPersionPerformance.getMileageFee());
            map.put("加班费", travelPersionPerformance.getOvertimePay());
            map.put("补贴费", travelPersionPerformance.getAllowance());
            map.put("附加费", travelPersionPerformance.getSurcharge());
            map.put("装卸费", travelPersionPerformance.getHandlingCost());
            map.put("绩效总计", travelPersionPerformance.getTotalPerformance());
            map.put("日期", travelPersionPerformance.getCreateTime());
            map.put("是否可用", travelPersionPerformance.getEnable());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}