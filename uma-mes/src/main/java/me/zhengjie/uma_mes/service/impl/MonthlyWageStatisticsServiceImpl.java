package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.MonthlyWageStatistics;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.uma_mes.repository.MonthlyWageStatisticsRepository;
import me.zhengjie.uma_mes.service.MonthlyWageStatisticsService;
import me.zhengjie.uma_mes.service.dto.MonthlyWageStatisticsDTO;
import me.zhengjie.uma_mes.service.dto.MonthlyWageStatisticsQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.MonthlyWageStatisticsMapper;
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
* @author wave
* @date 2020-09-11
*/
@Service
@CacheConfig(cacheNames = "monthlyWageStatistics")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MonthlyWageStatisticsServiceImpl implements MonthlyWageStatisticsService {

    private final MonthlyWageStatisticsRepository monthlyWageStatisticsRepository;

    private final MonthlyWageStatisticsMapper monthlyWageStatisticsMapper;

    public MonthlyWageStatisticsServiceImpl(MonthlyWageStatisticsRepository monthlyWageStatisticsRepository, MonthlyWageStatisticsMapper monthlyWageStatisticsMapper) {
        this.monthlyWageStatisticsRepository = monthlyWageStatisticsRepository;
        this.monthlyWageStatisticsMapper = monthlyWageStatisticsMapper;
    }

    @Override
    public Map<String,Object> queryAll(MonthlyWageStatisticsQueryCriteria criteria, Pageable pageable){
        Page<MonthlyWageStatistics> page = monthlyWageStatisticsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(monthlyWageStatisticsMapper::toDto));
    }

    @Override
    public List<MonthlyWageStatisticsDTO> queryAll(MonthlyWageStatisticsQueryCriteria criteria){
        return monthlyWageStatisticsMapper.toDto(monthlyWageStatisticsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    public MonthlyWageStatisticsDTO findById(Integer id) {
        MonthlyWageStatistics monthlyWageStatistics = monthlyWageStatisticsRepository.findById(id).orElseGet(MonthlyWageStatistics::new);
        ValidationUtil.isNull(monthlyWageStatistics.getId(),"MonthlyWageStatistics","id",id);
        return monthlyWageStatisticsMapper.toDto(monthlyWageStatistics);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MonthlyWageStatisticsDTO create(MonthlyWageStatistics resources) {
        return monthlyWageStatisticsMapper.toDto(monthlyWageStatisticsRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateWage() {
        MonthlyWageStatistics monthlyWageStatistics;

        System.out.println();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(MonthlyWageStatistics resources) {
        MonthlyWageStatistics monthlyWageStatistics = monthlyWageStatisticsRepository.findById(resources.getId()).orElseGet(MonthlyWageStatistics::new);
        ValidationUtil.isNull( monthlyWageStatistics.getId(),"MonthlyWageStatistics","id",resources.getId());
        monthlyWageStatistics.copy(resources);
        monthlyWageStatisticsRepository.save(monthlyWageStatistics);
    }

    @Override
    public void download(List<MonthlyWageStatisticsDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (MonthlyWageStatisticsDTO monthlyWageStatistics : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("姓名", monthlyWageStatistics.getPersonName());
            map.put("部门", monthlyWageStatistics.getDept());
            map.put("岗位", monthlyWageStatistics.getJob());
            map.put("基本工资", monthlyWageStatistics.getBasicSalary());
            map.put("绩效", monthlyWageStatistics.getPerformance());
            map.put("打卡奖", monthlyWageStatistics.getCardPrize());
            map.put("安全奖", monthlyWageStatistics.getSafePrize());
            map.put("全勤奖", monthlyWageStatistics.getFullPrize());
            map.put("高温津贴", monthlyWageStatistics.getHighTemperatureSubsidy());
            map.put("加班费", monthlyWageStatistics.getOvertimePay());
            map.put("其他", monthlyWageStatistics.getOtherPrize());
            map.put("应发工资", monthlyWageStatistics.getWagesPayable());
            map.put("出勤天数", monthlyWageStatistics.getAttendance());
            map.put("实际出勤天数", monthlyWageStatistics.getAttendanceReal());
            map.put("请假", monthlyWageStatistics.getLeave());
            map.put("缺卡", monthlyWageStatistics.getLackCard());
            map.put("违反安全", monthlyWageStatistics.getViolationOfSafety());
            map.put("日期", monthlyWageStatistics.getDateTime());
            map.put("实发工资", monthlyWageStatistics.getNetSalary());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}