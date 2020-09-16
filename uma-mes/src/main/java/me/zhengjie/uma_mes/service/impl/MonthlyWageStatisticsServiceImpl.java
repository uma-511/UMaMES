package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.*;
import me.zhengjie.uma_mes.repository.WageUserRepository;
import me.zhengjie.uma_mes.service.BonusTypeService;
import me.zhengjie.uma_mes.service.TravelPersionPerformanceService;
import me.zhengjie.uma_mes.service.dto.*;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.uma_mes.repository.MonthlyWageStatisticsRepository;
import me.zhengjie.uma_mes.service.MonthlyWageStatisticsService;
import me.zhengjie.uma_mes.service.mapper.MonthlyWageStatisticsMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

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

    private final WageUserRepository wageUserRepository;

    private final BonusTypeService bonusTypeService;

    private final TravelPersionPerformanceService travelPersionPerformanceService;

    public MonthlyWageStatisticsServiceImpl(MonthlyWageStatisticsRepository monthlyWageStatisticsRepository, MonthlyWageStatisticsMapper monthlyWageStatisticsMapper, WageUserRepository wageUserRepository, BonusTypeService bonusTypeService, TravelPersionPerformanceService travelPersionPerformanceService) {
        this.monthlyWageStatisticsRepository = monthlyWageStatisticsRepository;
        this.monthlyWageStatisticsMapper = monthlyWageStatisticsMapper;
        this.wageUserRepository = wageUserRepository;
        this.bonusTypeService = bonusTypeService;
        this.travelPersionPerformanceService = travelPersionPerformanceService;
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

    public Timestamp getLastMonthStartTime() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        // 设置为当前时间
        calendar.setTime(date);
        // 设置为上一个月
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        return new Timestamp(calendar.getTimeInMillis());
    }
    public Timestamp getLastMonthEndTime() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        // 设置为当前时间
        calendar.setTime(date);
        // 设置为上一个月
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH,0);
        return new Timestamp(calendar.getTimeInMillis());
    }

    public int getDaysOfMonth() {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)-4;
    }


    public String getMonthCn(){
        Integer month = new Timestamp(System.currentTimeMillis()).getMonth();
        String monthCn = "";
        if (month.equals(1)) {
            monthCn = "一月";
        }
        if (month.equals(2)) {
            monthCn = "二月";
        }
        if (month.equals(3)) {
            monthCn = "三月";
        }
        if (month.equals(4)) {
            monthCn = "四月";
        }
        if (month.equals(5)) {
            monthCn = "五月";
        }
        if (month.equals(6)) {
            monthCn = "六月";
        }
        if (month.equals(7)) {
            monthCn = "七月";
        }
        if (month.equals(8)) {
            monthCn = "八月";
        }
        if (month.equals(9)) {
            monthCn = "九月";
        }
        if (month.equals(10)) {
            monthCn = "十月";
        }
        if (month.equals(11)) {
            monthCn = "十一月";
        }
        if (month.equals(12)) {
            monthCn = "十二月";
        }
        return monthCn;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateWage() {
        MonthlyWageStatistics monthlyWageStatistics;
        //上月绩效列表
        TravelPersionPerformanceQueryCriteria travelPersionPerformanceQueryCriteria = new TravelPersionPerformanceQueryCriteria();
        travelPersionPerformanceQueryCriteria.setStartTime(getLastMonthStartTime());
        travelPersionPerformanceQueryCriteria.setEndTime(getLastMonthEndTime());
        List<TravelPersionPerformanceDTO> travelPersionPerformanceDTOList = travelPersionPerformanceService.queryAll(travelPersionPerformanceQueryCriteria);

        //奖金、补贴列表
        BonusTypeQueryCriteria bonusTypeQueryCriteria = new BonusTypeQueryCriteria();
        List<BonusTypeDTO> bonusTypeDTOList = bonusTypeService.queryAll(bonusTypeQueryCriteria);

        List<WageUser> wageUserList = wageUserRepository.getWageUser(Boolean.TRUE);
        for(WageUser w : wageUserList) {
            monthlyWageStatistics = new MonthlyWageStatistics();
            monthlyWageStatistics.setPersonName(w.getRealName());
            monthlyWageStatistics.setJob(w.getJob());
            monthlyWageStatistics.setDept(w.getDept());
            monthlyWageStatistics.setBasicSalary(w.getBasicSalary());

            //绩效统计
            BigDecimal performance = new BigDecimal(0);
            for (TravelPersionPerformanceDTO travelPersionPerformanceDTO : travelPersionPerformanceDTOList) {
                if (travelPersionPerformanceDTO.getPersonName().equals(monthlyWageStatistics.getPersonName())) {
                    performance = performance.add(travelPersionPerformanceDTO.getTotalPerformance());
                }
            }
            monthlyWageStatistics.setPerformance(performance);

            //打卡奖
            BigDecimal cardPrize = new BigDecimal(0);
            //安全奖
            BigDecimal safePrize = new BigDecimal(0);
            //全勤奖
            BigDecimal fullPrize = new BigDecimal(0);
            //高温津贴
            BigDecimal highTemperatureSubsidy = new BigDecimal(0);
            for (BonusTypeDTO bonusTypeDTO : bonusTypeDTOList) {
                Set<CycleLabel> cycles = bonusTypeDTO.getCycles();
                Set<BonusJob> bonusJobs = bonusTypeDTO.getBonusJobs();
                for (CycleLabel cycleLabel : cycles) {
                    if(null != cycleLabel && null != cycleLabel.getName() && cycleLabel.getName().equals(getMonthCn())) {
                        for (BonusJob bonusJob : bonusJobs) {
                            if (null != bonusJob && null != bonusJob.getName() && bonusJob.getName().equals(monthlyWageStatistics.getJob())) {
                                if (bonusTypeDTO.getType().equals("打卡奖")) {
                                    cardPrize = cardPrize.add(bonusTypeDTO.getPrice());
                                }
                                if (bonusTypeDTO.getType().equals("安全奖（押运）")) {
                                    safePrize = safePrize.add(bonusTypeDTO.getPrice());
                                }
                                if (bonusTypeDTO.getType().equals("安全奖（放酸）")) {
                                    safePrize = safePrize.add(bonusTypeDTO.getPrice());
                                }
                                if (bonusTypeDTO.getType().equals("安全奖（司机）")) {
                                    safePrize = safePrize.add(bonusTypeDTO.getPrice());
                                }
                                if (bonusTypeDTO.getType().equals("全勤奖")) {
                                    fullPrize = fullPrize.add(bonusTypeDTO.getPrice());
                                }
                                if (bonusTypeDTO.getType().equals("高温津贴")) {
                                    highTemperatureSubsidy = highTemperatureSubsidy.add(bonusTypeDTO.getPrice());
                                }
                            }
                        }
                    }
                }
            }
            monthlyWageStatistics.setCardPrize(cardPrize);
            monthlyWageStatistics.setSafePrize(safePrize);
            monthlyWageStatistics.setFullPrize(fullPrize);
            monthlyWageStatistics.setHighTemperatureSubsidy(highTemperatureSubsidy);

            monthlyWageStatistics.setOvertimePay(new BigDecimal(0));
            monthlyWageStatistics.setOtherPrize(new BigDecimal(0));
            BigDecimal payable = new BigDecimal(0);
            payable = payable.add(cardPrize);
            payable = payable.add(safePrize);
            payable = payable.add(fullPrize);
            monthlyWageStatistics.setWagesPayable(payable);
            monthlyWageStatistics.setAttendance(new BigDecimal(getDaysOfMonth()));
            monthlyWageStatistics.setAttendanceReal(new BigDecimal(0));

            monthlyWageStatistics.setLeaveCount(new BigDecimal(0));

            monthlyWageStatistics.setLackCard(new BigDecimal(0));
            
            monthlyWageStatistics.setViolationOfSafety(new BigDecimal(0));
            monthlyWageStatistics.setNetSalary(new BigDecimal(0));
            monthlyWageStatistics.setDateTime(new Timestamp(System.currentTimeMillis()));
            monthlyWageStatisticsRepository.save(monthlyWageStatistics);
        }
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
            map.put("请假", monthlyWageStatistics.getLeaveCount());
            map.put("缺卡", monthlyWageStatistics.getLackCard());
            map.put("违反安全", monthlyWageStatistics.getViolationOfSafety());
            map.put("日期", monthlyWageStatistics.getDateTime());
            map.put("实发工资", monthlyWageStatistics.getNetSalary());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}