package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.exception.BadRequestException;
import me.zhengjie.uma_mes.domain.*;
import me.zhengjie.uma_mes.repository.WageUserRepository;
import me.zhengjie.uma_mes.service.*;
import me.zhengjie.uma_mes.service.dto.*;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.uma_mes.repository.MonthlyWageStatisticsRepository;
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
import java.text.DateFormat;
import java.text.ParseException;
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

    private final WorkAttendanceService workAttendanceService;

    private final AcidPersionPerformanceService acidPersionPerformanceService;

    public MonthlyWageStatisticsServiceImpl(MonthlyWageStatisticsRepository monthlyWageStatisticsRepository, MonthlyWageStatisticsMapper monthlyWageStatisticsMapper, WageUserRepository wageUserRepository, BonusTypeService bonusTypeService, TravelPersionPerformanceService travelPersionPerformanceService, WorkAttendanceService workAttendanceService, AcidPersionPerformanceService acidPersionPerformanceService) {
        this.monthlyWageStatisticsRepository = monthlyWageStatisticsRepository;
        this.monthlyWageStatisticsMapper = monthlyWageStatisticsMapper;
        this.wageUserRepository = wageUserRepository;
        this.bonusTypeService = bonusTypeService;
        this.travelPersionPerformanceService = travelPersionPerformanceService;
        this.workAttendanceService = workAttendanceService;
        this.acidPersionPerformanceService = acidPersionPerformanceService;
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
    public void doFinish(Integer id) {
        MonthlyWageStatisticsDTO monthlyWageStatisticsDTO = findById(id);
        monthlyWageStatisticsDTO.setStatus(1);
        monthlyWageStatisticsRepository.save(monthlyWageStatisticsMapper.toEntity(monthlyWageStatisticsDTO));
    }

    public Timestamp getLastMonthStartTime() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        // 设置为当前时间
        calendar.setTime(date);
        // 设置为上一个月
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        date=sdf.parse(sdf.format(calendar.getTime()));
        calendar.setTime(date);
        return new Timestamp(calendar.getTimeInMillis());
    }
    public Timestamp getLastMonthEndTime() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        // 设置为当前时间
        calendar.setTime(date);
        // 设置为上一个月
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH,0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        date=sdf.parse(sdf.format(calendar.getTime()));
        calendar.setTime(date);
        return new Timestamp(calendar.getTimeInMillis());
    }

    public Timestamp getQuarterStartTime() throws ParseException {
        Calendar calendar = Calendar.getInstance();
        if (getMonthCn().equals("三月")) {
            calendar.set(calendar.get(Calendar.YEAR), Calendar.JANUARY, 1);
        }
        if (getMonthCn().equals("六月")) {
            calendar.set(calendar.get(Calendar.YEAR), Calendar.APRIL, 1);
        }
        if (getMonthCn().equals("九月")) {
            calendar.set(calendar.get(Calendar.YEAR), Calendar.JULY, 1);
        }
        if (getMonthCn().equals("十月")) {
            calendar.set(calendar.get(Calendar.YEAR), Calendar.OCTOBER, 1);
        }
        return new Timestamp(calendar.getTimeInMillis());
    }

    public Timestamp getQuarterEndTime() {
        Calendar calendar = Calendar.getInstance();
        if (getMonthCn().equals("三月")) {
            calendar.set(calendar.get(Calendar.YEAR), Calendar.MARCH, 1);
        }
        if (getMonthCn().equals("六月")) {
            calendar.set(calendar.get(Calendar.YEAR), Calendar.JUNE, 1);
        }
        if (getMonthCn().equals("九月")) {
            calendar.set(calendar.get(Calendar.YEAR), Calendar.SEPTEMBER, 1);
        }
        if (getMonthCn().equals("十二月")) {
            calendar.set(calendar.get(Calendar.YEAR), Calendar.DECEMBER, 1);
        }
        return new Timestamp(calendar.getTimeInMillis());
    }

    public int getDaysOfMonth() {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }


    public String getMonthCn(){
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        // 设置为当前时间
        calendar.setTime(date);
        // 设置为上一个月
        calendar.add(Calendar.MONTH, 0);
        Integer month = calendar.get(Calendar.MONTH);
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
        // 查询上月已生成记录
        MonthlyWageStatisticsQueryCriteria monthlyWageStatisticsQueryCriteria = new MonthlyWageStatisticsQueryCriteria();
        try {
            monthlyWageStatisticsQueryCriteria.setStartTime(getLastMonthStartTime());
            monthlyWageStatisticsQueryCriteria.setEndTime(getLastMonthEndTime());
        }catch (Exception e){
            throw new BadRequestException("生成失败");
        }
        List<MonthlyWageStatisticsDTO> monthlyWageStatisticsDTOS = this.queryAll(monthlyWageStatisticsQueryCriteria);
        BigDecimal zero = new BigDecimal(0);
        String monthCn = getMonthCn();
        List<Boolean> booleanList = new ArrayList<>();
        booleanList.add(Boolean.TRUE);
        MonthlyWageStatistics monthlyWageStatistics;
        Boolean isQuarter = false;

        if (monthCn.equals("三月") || monthCn.equals("六月") || monthCn.equals("九月") || monthCn.equals("十二月")) {
            isQuarter = true;
        }

        // 上月放酸绩效
        AcidPersionPerformanceQueryCriteria acidPersionPerformanceQueryCriteria = new AcidPersionPerformanceQueryCriteria();
        acidPersionPerformanceQueryCriteria.setEnableList(booleanList);
        try {
            acidPersionPerformanceQueryCriteria.setStartTime(getLastMonthStartTime());
            acidPersionPerformanceQueryCriteria.setEndTime(getLastMonthEndTime());
        }catch (Exception e){
            throw new BadRequestException("生成失败");
        }
        List<AcidPersionPerformanceDTO> acidPersonPerformanceDTOS = acidPersionPerformanceService.queryAll(acidPersionPerformanceQueryCriteria);

        // 上月司机绩效列表
        TravelPersionPerformanceQueryCriteria travelPersionPerformanceQueryCriteria = new TravelPersionPerformanceQueryCriteria();
        travelPersionPerformanceQueryCriteria.setEnableList(booleanList);
        try{
            travelPersionPerformanceQueryCriteria.setStartTime(getLastMonthStartTime());
            travelPersionPerformanceQueryCriteria.setEndTime(getLastMonthEndTime());
        }catch (Exception e){
            throw new BadRequestException("生成失败");
        }
        List<TravelPersionPerformanceDTO> travelPersionPerformanceDTOList = travelPersionPerformanceService.queryAll(travelPersionPerformanceQueryCriteria);

        // 奖金、补贴列表
        BonusTypeQueryCriteria bonusTypeQueryCriteria = new BonusTypeQueryCriteria();
        List<BonusTypeDTO> bonusTypeDTOList = bonusTypeService.queryAll(bonusTypeQueryCriteria);

        // 考勤统计
        WorkAttendanceQueryCriteria workAttendanceQueryCriteria = new WorkAttendanceQueryCriteria();
        workAttendanceQueryCriteria.setEnableList(booleanList);
        try{
            workAttendanceQueryCriteria.setStartTime(getLastMonthStartTime());
            workAttendanceQueryCriteria.setEndTime(getLastMonthEndTime());
        }catch (Exception e){
            throw new BadRequestException("生成失败");
        }
        List<WorkAttendanceDTO> workAttendanceDTOS = workAttendanceService.queryAll(workAttendanceQueryCriteria);

        List<WageUser> wageUserList = wageUserRepository.getWageUser(Boolean.TRUE);
        for(WageUser w : wageUserList) {
            Boolean isGen = Boolean.FALSE;
            for(MonthlyWageStatisticsDTO m : monthlyWageStatisticsDTOS){
                if(w.getRealName().equals(m.getPersonName())){
                    isGen = Boolean.TRUE;
                    break;
                }
            }
            if(isGen){continue;}
            monthlyWageStatistics = new MonthlyWageStatistics();
            BigDecimal performance = zero;
            if(null == w.getRealName() || w.getRealName().equals("")){
                throw new BadRequestException("请完用户信息，用户id:"+w.getId());
            }
            if(null == w.getJob() || null == w.getDept() || w.getJob().equals("") || w.getDept().equals("")){
                throw new BadRequestException("请完用户信息，用户名:"+w.getRealName());
            }
            monthlyWageStatistics.setPersonName(w.getRealName());
            monthlyWageStatistics.setJob(w.getJob());
            monthlyWageStatistics.setDept(w.getDept());
            if(null == w.getBasicSalary() || w.getBasicSalary().compareTo(new BigDecimal(0.00)) == 0 ){
                w.setBasicSalary(new BigDecimal(0));
            }
            monthlyWageStatistics.setBasicSalary(w.getBasicSalary());
            if(w.getRealName().equals("拖头车司机") && w.getRealName().equals("槽罐车司机")) {

                // 绩效统计
                // 出车绩效
                for (TravelPersionPerformanceDTO travelPersionPerformanceDTO : travelPersionPerformanceDTOList) {
                    if (travelPersionPerformanceDTO.getPersonName().equals(monthlyWageStatistics.getPersonName())) {
                        performance = performance.add(travelPersionPerformanceDTO.getTotalPerformance());
                    }
                }
                //放酸绩效
                for (AcidPersionPerformanceDTO acidPersionPerformanceDTO : acidPersonPerformanceDTOS) {
                    if (acidPersionPerformanceDTO.getPerson().equals(monthlyWageStatistics.getPersonName())) {
                        performance = performance.add(acidPersionPerformanceDTO.getPrice());
                    }
                }
                monthlyWageStatistics.setPerformance(performance.equals(zero) ? null : performance);

            }
            // 打卡奖
            BigDecimal cardPrize = zero;
            // 安全奖
            BigDecimal safePrize = zero;
            // 全勤奖
            BigDecimal fullPrize = zero;
            // 高温津贴
            BigDecimal highTemperatureSubsidy = zero;
            for (BonusTypeDTO bonusTypeDTO : bonusTypeDTOList) {
                Set<CycleLabel> cycles = bonusTypeDTO.getCycles();
                Set<BonusJob> bonusJobs = bonusTypeDTO.getBonusJobs();
                for (CycleLabel cycleLabel : cycles) {
                    if(null != cycleLabel && null != cycleLabel.getName() && cycleLabel.getName().equals(monthCn)) {
                        for (BonusJob bonusJob : bonusJobs) {
                            if (null != bonusJob && null != bonusJob.getName() && bonusJob.getName().equals(monthlyWageStatistics.getJob())) {
                                if (bonusTypeDTO.getType().equals("打卡奖")) {
                                    cardPrize = cardPrize.add(bonusTypeDTO.getPrice());
                                }
                                if (bonusTypeDTO.getType().equals("押运人员安全奖") && isQuarter) {
                                    safePrize = safePrize.add(bonusTypeDTO.getPrice());
                                }
                                if (bonusTypeDTO.getType().equals("放酸人员安全奖") && isQuarter) {
                                    safePrize = safePrize.add(bonusTypeDTO.getPrice());
                                }
                                if (bonusTypeDTO.getType().equals("司机安全奖") && isQuarter) {
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
            monthlyWageStatistics.setCardPrize(cardPrize.equals(zero)? null : cardPrize);
            monthlyWageStatistics.setSafePrize(safePrize.equals(zero)? null : safePrize);
            monthlyWageStatistics.setFullPrize(fullPrize.equals(zero)? null : fullPrize);
            monthlyWageStatistics.setHighTemperatureSubsidy(highTemperatureSubsidy.equals(zero)? null : highTemperatureSubsidy);


            BigDecimal payable = zero;
            payable = payable.add(monthlyWageStatistics.getBasicSalary());
            payable = payable.add(performance);
            payable = payable.add(cardPrize);
            payable = payable.add(safePrize);
            payable = payable.add(fullPrize);
            payable = payable.add(highTemperatureSubsidy);
            monthlyWageStatistics.setWagesPayable(payable.equals(zero)? null : payable);

            // 出勤天数 当月天数-休息天数
            BigDecimal restDay = zero;
            for (WorkAttendanceDTO workAttendanceDTO : workAttendanceDTOS) {
                if (workAttendanceDTO.getPersonName().equals(monthlyWageStatistics.getPersonName())) {
                    if(workAttendanceDTO.getAttenceType().equals("休息")) {
                        restDay = restDay.add(workAttendanceDTO.getDay());
                    }
                }
            }
            monthlyWageStatistics.setAttendance(new BigDecimal(getDaysOfMonth()).subtract(restDay));
            monthlyWageStatistics.setRestDay(fullPrize.equals(zero)? null : fullPrize);

            // 请假(金额)
            BigDecimal leaveDay = zero;
            for (WorkAttendanceDTO workAttendanceDTO : workAttendanceDTOS) {
                if (workAttendanceDTO.getPersonName().equals(monthlyWageStatistics.getPersonName())) {
                    if(workAttendanceDTO.getAttenceType().equals("请假")) {
                        leaveDay = leaveDay.add(workAttendanceDTO.getDay());
                    }
                }
            }

            if (leaveDay.compareTo(zero) > 0) {
                monthlyWageStatistics.setLeaveCount(fullPrize);
            }

            // 缺卡/迟到满7次 取消打卡奖
            BigDecimal lackCount = zero;
            for (WorkAttendanceDTO workAttendanceDTO : workAttendanceDTOS) {
                if (workAttendanceDTO.getPersonName().equals(monthlyWageStatistics.getPersonName())) {
                    if(workAttendanceDTO.getAttenceType().equals("缺卡")) {
                        lackCount = lackCount.add(workAttendanceDTO.getDay());
                    }
                    if(workAttendanceDTO.getAttenceType().equals("迟到")) {
                        lackCount = lackCount.add(workAttendanceDTO.getDay());
                    }
                }
            }
            if (lackCount.compareTo(new BigDecimal(7)) >= 0) {
                monthlyWageStatistics.setLackCard(cardPrize);
            }

            // 实际出勤天数
            monthlyWageStatistics.setAttendanceReal(new BigDecimal(getDaysOfMonth()).subtract(restDay).subtract(leaveDay));

            // 违反安全(金额) 统计近三个月的总违反安全记录
            BigDecimal safePrice = zero;
            if (isQuarter) {
                // 考勤统计
                workAttendanceQueryCriteria.setEnableList(booleanList);
                try {
                    workAttendanceQueryCriteria.setStartTime(getQuarterStartTime());
                    workAttendanceQueryCriteria.setEndTime(getQuarterEndTime());
                }catch (Exception e){
                    throw new BadRequestException("查询违反安全记录失败");
                }

                workAttendanceDTOS = workAttendanceService.queryAll(workAttendanceQueryCriteria);
                for (WorkAttendanceDTO workAttendanceDTO : workAttendanceDTOS) {
                    if (workAttendanceDTO.getPersonName().equals(monthlyWageStatistics.getPersonName())) {
                        if(workAttendanceDTO.getAttenceType().equals("安全问题")) {
                            safePrice = safePrice.add(workAttendanceDTO.getPrice());
                        }
                    }
                }
                monthlyWageStatistics.setViolationOfSafety(safePrice.equals(zero)? null : safePrice);
            }

            // 实发工资
            BigDecimal netSalary = monthlyWageStatistics.getWagesPayable().subtract(leaveDay).subtract(lackCount).subtract(safePrice);
            monthlyWageStatistics.setNetSalary(netSalary);

            // 日期
            try {
                monthlyWageStatistics.setDateTime(getLastMonthStartTime());
            } catch (ParseException e) {
                throw new BadRequestException("生成失败");
            }
            monthlyWageStatistics.setStatus(0);
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
        MonthlyWageStatisticsQueryCriteria monthlyWageStatisticsQueryCriteria = new MonthlyWageStatisticsQueryCriteria();
        try {
            monthlyWageStatisticsQueryCriteria.setStartTime(getLastMonthStartTime());
            monthlyWageStatisticsQueryCriteria.setEndTime(getLastMonthEndTime());
        }catch (Exception e){
            throw new BadRequestException("获取上月时间失败");
        }
        monthlyWageStatisticsQueryCriteria.setStatus("1");
        all = this.queryAll(monthlyWageStatisticsQueryCriteria);
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