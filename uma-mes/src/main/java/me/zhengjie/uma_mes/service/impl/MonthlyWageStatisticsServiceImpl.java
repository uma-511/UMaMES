package me.zhengjie.uma_mes.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.hutool.extra.template.TemplateConfig;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.uma_mes.domain.*;
import me.zhengjie.uma_mes.repository.WageUserRepository;
import me.zhengjie.uma_mes.service.*;
import me.zhengjie.uma_mes.service.dto.*;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.uma_mes.repository.MonthlyWageStatisticsRepository;
import me.zhengjie.uma_mes.service.mapper.MonthlyWageStatisticsMapper;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        if(null != criteria.getMonthTime() && !criteria.getMonthTime().equals("")){
            Date date = new Date(criteria.getMonthTime());
            try{
                criteria.setStartTime(changeToStartTime(date));
                criteria.setEndTime(changeToEndTime(date));
            }catch (Exception e){
                throw new BadRequestException("日期条件转换异常");
            }
        }
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
    public void oneKeyDelete() {
        MonthlyWageStatisticsQueryCriteria monthlyWageStatisticsQueryCriteria = new MonthlyWageStatisticsQueryCriteria();
        monthlyWageStatisticsQueryCriteria.setStatus("0");
        try{
            monthlyWageStatisticsQueryCriteria.setStartTime(changeToStartTime(getLastMonthStartTime()));
            monthlyWageStatisticsQueryCriteria.setEndTime(changeToEndTime(getLastMonthEndTime()));
        }catch (Exception e){
            throw new BadRequestException("删除失败");
        }
        List<MonthlyWageStatisticsDTO> monthlyWageStatisticsDTOList = this.queryAll(monthlyWageStatisticsQueryCriteria);
        for(MonthlyWageStatisticsDTO dto : monthlyWageStatisticsDTOList){
            this.delete(dto.getId());
        }
    } @Override
    public void oneKeyReset() {
        MonthlyWageStatisticsQueryCriteria monthlyWageStatisticsQueryCriteria = new MonthlyWageStatisticsQueryCriteria();
        try{
            monthlyWageStatisticsQueryCriteria.setStartTime(changeToStartTime(getLastMonthStartTime()));
            monthlyWageStatisticsQueryCriteria.setEndTime(changeToEndTime(getLastMonthEndTime()));
        }catch (Exception e){
            throw new BadRequestException("重置失败");
        }
        List<MonthlyWageStatisticsDTO> monthlyWageStatisticsDTOList = this.queryAll(monthlyWageStatisticsQueryCriteria);
        for(MonthlyWageStatisticsDTO dto : monthlyWageStatisticsDTOList){
            this.delete(dto.getId());
        }
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

    @Override
    public void delete(Integer id) {
        monthlyWageStatisticsRepository.deleteById(id);
    }

    public Date changeToEndTime(Date date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH,0);
        date=sdf.parse(sdf.format(calendar.getTime()));
        return date;
    }

    public Date changeToStartTime(Date date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        date=sdf.parse(sdf.format(calendar.getTime()));
        return date;
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
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
    public String getQuarterStartDate() {
        SimpleDateFormat sdf;
        Date date = new Date();
        if (getMonthCn().equals("三月")) {
            sdf = new SimpleDateFormat("yyyy-01-01 00:00:00");
        }
        if (getMonthCn().equals("六月")) {
            sdf = new SimpleDateFormat("yyyy-04-01 00:00:00");
        }
        if (getMonthCn().equals("九月")) {
            sdf = new SimpleDateFormat("yyyy-07-01 00:00:00");
        }else{
            sdf = new SimpleDateFormat("yyyy-10-01 00:00:00");
        }
        return sdf.format(date);
    }

    public String getQuarterEndDate() {
        SimpleDateFormat sdf;
        Date date = new Date();
        if (getMonthCn().equals("三月")) {
            sdf = new SimpleDateFormat("yyyy-03-31 23:59:59");
        }
        if (getMonthCn().equals("六月")) {
            sdf = new SimpleDateFormat("yyyy-06-30 23:59:59");
        }
        if (getMonthCn().equals("九月")) {
            sdf = new SimpleDateFormat("yyyy-09-30 23:59:59");
        }
        else{
            sdf = new SimpleDateFormat("yyyy-12-31 23:59:59");
        }
        return sdf.format(date);
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
            monthlyWageStatisticsQueryCriteria.setStartTime(changeToStartTime(getLastMonthStartTime()));
            monthlyWageStatisticsQueryCriteria.setEndTime(changeToEndTime(getLastMonthEndTime()));
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
            acidPersionPerformanceQueryCriteria.setStartTime(changeToStartTime(getLastMonthStartTime()));
            acidPersionPerformanceQueryCriteria.setEndTime(changeToEndTime(getLastMonthEndTime()));
        }catch (Exception e){
            throw new BadRequestException("生成失败");
        }
        List<AcidPersionPerformanceDTO> acidPersonPerformanceDTOS = acidPersionPerformanceService.queryAll(acidPersionPerformanceQueryCriteria);

        // 上月司机绩效列表
        TravelPersionPerformanceQueryCriteria travelPersionPerformanceQueryCriteria = new TravelPersionPerformanceQueryCriteria();
        travelPersionPerformanceQueryCriteria.setEnableList(booleanList);
        try{
            travelPersionPerformanceQueryCriteria.setStartTime(changeToStartTime(getLastMonthStartTime()));
            travelPersionPerformanceQueryCriteria.setEndTime(changeToEndTime(getLastMonthEndTime()));
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
            workAttendanceQueryCriteria.setStartTime(changeToStartTime(getLastMonthStartTime()));
            workAttendanceQueryCriteria.setEndTime(changeToEndTime(getLastMonthEndTime()));
        }catch (Exception e){
            throw new BadRequestException("生成失败");
        }
        List<WorkAttendanceDTO> workAttendanceDTOS = workAttendanceService.queryAll(workAttendanceQueryCriteria);


        WorkAttendanceQueryCriteria workAttendanceQueryCriteriaForSafe = new WorkAttendanceQueryCriteria();
        String startDate = getQuarterStartDate();
        String endDate = getQuarterEndDate();
        workAttendanceQueryCriteriaForSafe.setEnableList(booleanList);
        workAttendanceQueryCriteriaForSafe.setAttenceType("安全问题");
        workAttendanceQueryCriteriaForSafe.setStartDate(startDate);
        workAttendanceQueryCriteriaForSafe.setEndDate(endDate);
        List<WorkAttendanceDTO> attendancesForSafe  =  workAttendanceService.queryAll(workAttendanceQueryCriteriaForSafe);

        List<WageUser> wageUserList = wageUserRepository.getWageUser(Boolean.TRUE);
        for(WageUser w : wageUserList) {
            // 排除掉已生成的人员
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

            // 打卡奖
            BigDecimal cardPrize = zero;
            // 安全奖
            BigDecimal safePrize = zero;
            // 全勤奖
            BigDecimal fullPrize = zero;
            // 加班费
            BigDecimal overTimePrice = zero;
            // 其他
            BigDecimal otherPrice = zero;
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
            // 加班费
            for (WorkAttendanceDTO workAttendanceDTO : workAttendanceDTOS) {
                if (workAttendanceDTO.getPersonName().equals(monthlyWageStatistics.getPersonName())) {
                    if(workAttendanceDTO.getAttenceType().equals("加班费")) {
                        overTimePrice = otherPrice.add(workAttendanceDTO.getPrice());
                    }
                }
            }
            // 其他
            for (WorkAttendanceDTO workAttendanceDTO : workAttendanceDTOS) {
                if (workAttendanceDTO.getPersonName().equals(monthlyWageStatistics.getPersonName())) {
                    if(workAttendanceDTO.getAttenceType().equals("其他")) {
                        otherPrice = otherPrice.add(workAttendanceDTO.getPrice());
                    }
                }
            }
            monthlyWageStatistics.setOvertimePay(overTimePrice.equals(zero)? null : overTimePrice);
            monthlyWageStatistics.setOtherPrize(otherPrice.equals(zero)? null : otherPrice);

            // 请假(金额)
            BigDecimal leaveDay = zero;
            BigDecimal leavePrice = zero;
            for (WorkAttendanceDTO workAttendanceDTO : workAttendanceDTOS) {
                if (workAttendanceDTO.getPersonName().equals(monthlyWageStatistics.getPersonName())) {
                    if(workAttendanceDTO.getAttenceType().equals("请假")) {
                        leaveDay = leaveDay.add(workAttendanceDTO.getDay());
                        leavePrice = leavePrice.add(workAttendanceDTO.getPrice());
                    }
                }
            }

            // 司机请假满7天，取消基本工资
            if(leaveDay.compareTo(new BigDecimal(7)) >= 0) {
                if(w.getJob().equals("拖头车司机") || w.getJob().equals("槽罐车司机") || w.getJob().equals("厢式车司机")) {
                    monthlyWageStatistics.setBasicSalary(null);
                }
            }

            if (leaveDay.compareTo(zero) > 0) {
                // 请假扣除 = 全勤奖扣除+考勤记录金额
                monthlyWageStatistics.setLeaveCount(fullPrize.add(leavePrice));
            }

            BigDecimal payable = zero;
            if(null != monthlyWageStatistics.getBasicSalary()){
                payable = payable.add(monthlyWageStatistics.getBasicSalary());
            }
            payable = payable.add(performance);
            payable = payable.add(cardPrize);
            payable = payable.add(safePrize);
            payable = payable.add(fullPrize);
            payable = payable.add(overTimePrice);
            payable = payable.add(otherPrice);
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

            // 缺卡/迟到满7次 取消打卡奖
            BigDecimal lackCount = zero;
            BigDecimal lackCardAndlatePrice = BigDecimal.ZERO;
            for (WorkAttendanceDTO workAttendanceDTO : workAttendanceDTOS) {
                if (workAttendanceDTO.getPersonName().equals(monthlyWageStatistics.getPersonName())) {
                    if(workAttendanceDTO.getAttenceType().equals("缺卡")) {
                        if(null != workAttendanceDTO.getDay()) {
                            lackCount = lackCount.add(workAttendanceDTO.getDay());
                        }
                        if(null != workAttendanceDTO.getPrice()){
                            lackCardAndlatePrice = lackCardAndlatePrice.add(workAttendanceDTO.getPrice());
                        }
                    }
                    if(workAttendanceDTO.getAttenceType().equals("迟到")) {
                        if(null != workAttendanceDTO.getDay()) {
                            lackCount = lackCount.add(workAttendanceDTO.getDay());
                        }
                        if(null != workAttendanceDTO.getPrice()){
                            lackCardAndlatePrice = lackCardAndlatePrice.add(workAttendanceDTO.getPrice());
                        }
                    }
                }
            }
            if (lackCount.compareTo(new BigDecimal(7)) >= 0) {
                lackCardAndlatePrice = lackCardAndlatePrice.add(cardPrize);
            }
            monthlyWageStatistics.setLackCard(lackCardAndlatePrice);

            // 实际出勤天数
            monthlyWageStatistics.setAttendanceReal((new BigDecimal(getDaysOfMonth()).subtract(restDay)).subtract(leaveDay));

            // 违反安全(金额) 统计近三个月的总违反安全记录
            BigDecimal violationsafePrice = zero;
            if (isQuarter) {
                // 考勤统计
                for (WorkAttendanceDTO workAttendanceDTO : attendancesForSafe) {
                    if (workAttendanceDTO.getPersonName().equals(monthlyWageStatistics.getPersonName())) {
                        violationsafePrice = violationsafePrice.add(workAttendanceDTO.getPrice());
                    }
                }
                monthlyWageStatistics.setViolationOfSafety(violationsafePrice.equals(zero)? null : violationsafePrice);
            }

            // 实发工资
            BigDecimal netSalary = monthlyWageStatistics.getWagesPayable();
            // 扣除请假
            if(null != monthlyWageStatistics.getLeaveCount()){
                netSalary = netSalary.subtract(monthlyWageStatistics.getLeaveCount());
            }
            // 扣除缺卡
            if(null != monthlyWageStatistics.getLackCard()){
                netSalary = netSalary.subtract(monthlyWageStatistics.getLackCard());
            }
            // 扣除安全
            if(null != monthlyWageStatistics.getViolationOfSafety()){
                netSalary = netSalary.subtract(monthlyWageStatistics.getViolationOfSafety());
            }
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
            monthlyWageStatisticsQueryCriteria.setStartTime(changeToStartTime(getLastMonthStartTime()));
            monthlyWageStatisticsQueryCriteria.setEndTime(changeToEndTime(getLastMonthEndTime()));
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void downloadMonthlyWageStatistics(MonthlyWageStatisticsQueryCriteria criteria, HttpServletResponse response) {
        criteria.setStatus("1");
        List<MonthlyWageStatisticsDTO> dtoList = this.queryAll(criteria);
        String lastName = "";
        lastName = "/monthlyWageStatistics_temp.xls";
        String templatePath = new TemplateConfig("template/excel", TemplateConfig.ResourceMode.CLASSPATH).getPath() + lastName;
        // 加载模板
        TemplateExportParams params = new TemplateExportParams(templatePath);
        // params.setReadonly(Boolean.TRUE);
        Workbook workbook = null;
        // 单条记录
        Map<String, Object> map = new HashMap<String, Object>();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
        if(null != criteria.getMonthTime()){
            map.put("lessDate", longDateTOStr(criteria.getMonthTime()));
            map.put("longDate", sdf.format(criteria.getStartTime()) + " 至 " + sdf.format(criteria.getEndTime()));
        }
        BigDecimal basicSalary = BigDecimal.ZERO;
        BigDecimal performance = BigDecimal.ZERO;
        BigDecimal cardPrize = BigDecimal.ZERO;
        BigDecimal safePrize = BigDecimal.ZERO;
        BigDecimal fullPrize = BigDecimal.ZERO;
        BigDecimal highTemperatureSubsidy = BigDecimal.ZERO;
        BigDecimal overtimePay = BigDecimal.ZERO;
        BigDecimal otherPrize = BigDecimal.ZERO;
        BigDecimal wagesPayable = BigDecimal.ZERO;
        BigDecimal leaveCount = BigDecimal.ZERO;
        BigDecimal lackCard = BigDecimal.ZERO;
        BigDecimal violationOfSafety = BigDecimal.ZERO;
        BigDecimal netSalary = BigDecimal.ZERO;
        // 组装循环列表
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
        for(MonthlyWageStatisticsDTO t: dtoList){
            Map<String, String> lm = new HashMap<String, String>();
            lm.put("person", t.getPersonName());
            lm.put("dept", t.getDept());
            lm.put("job", t.getJob());
            lm.put("basicSalary", zeroDecimalToNull(t.getBasicSalary()));
            lm.put("performance", zeroDecimalToNull(t.getPerformance()));
            lm.put("cardPrize", zeroDecimalToNull(t.getCardPrize()));
            lm.put("safePrize", zeroDecimalToNull(t.getSafePrize()));
            lm.put("fullPrize", zeroDecimalToNull(t.getFullPrize()));
            lm.put("highTemperatureSubsidy", zeroDecimalToNull(t.getHighTemperatureSubsidy()));
            lm.put("overtimePay", zeroDecimalToNull(t.getOvertimePay()));
            lm.put("otherPrize", zeroDecimalToNull(t.getOtherPrize()));
            lm.put("wagesPayable", zeroDecimalToNull(t.getWagesPayable()));
            lm.put("attendance", dayToString(t.getAttendance()));
            lm.put("attendanceReal", dayToString(t.getAttendanceReal()));
            lm.put("leaveCount", zeroDecimalToNull(t.getLeaveCount()));
            lm.put("lackCard", zeroDecimalToNull(t.getLackCard()));
            lm.put("violationOfSafety", zeroDecimalToNull(t.getViolationOfSafety()));
            lm.put("netSalary", zeroDecimalToNull(t.getNetSalary()));



            listMap.add(lm);
            // 统计
            basicSalary = addDecimalWithoutNull(basicSalary,t.getBasicSalary());
            performance = addDecimalWithoutNull(performance,t.getPerformance());
            cardPrize = addDecimalWithoutNull(cardPrize,t.getCardPrize());
            safePrize = addDecimalWithoutNull(safePrize,t.getSafePrize());
            fullPrize = addDecimalWithoutNull(fullPrize,t.getFullPrize());
            highTemperatureSubsidy = addDecimalWithoutNull(highTemperatureSubsidy,t.getHighTemperatureSubsidy());
            overtimePay = addDecimalWithoutNull(overtimePay,t.getOvertimePay());
            otherPrize = addDecimalWithoutNull(otherPrize,t.getOtherPrize());
            wagesPayable = addDecimalWithoutNull(wagesPayable,t.getWagesPayable());
            leaveCount = addDecimalWithoutNull(leaveCount,t.getLeaveCount());
            lackCard = addDecimalWithoutNull(lackCard,t.getLackCard());
            violationOfSafety = addDecimalWithoutNull(violationOfSafety,t.getViolationOfSafety());
            netSalary = addDecimalWithoutNull(netSalary,t.getNetSalary());
        }
        map.put("monthlyWageStatisticsList", listMap);

        map.put("totalBasicSalary",zeroDecimalToNull(basicSalary) + "元");
        map.put("totalPerformance",zeroDecimalToNull(performance) + "元");
        map.put("totalCardPrize",zeroDecimalToNull(cardPrize) + "元");
        map.put("totalCardPrize",zeroDecimalToNull(safePrize) + "元");
        map.put("totalFullPrize",zeroDecimalToNull(fullPrize) + "元");
        map.put("totalHighTemperatureSubsidy",zeroDecimalToNull(highTemperatureSubsidy) + "元");
        map.put("totalOvertimePay",zeroDecimalToNull(overtimePay) + "元");
        map.put("totalOtherPrize",zeroDecimalToNull(otherPrize) + "元");
        map.put("totalWagesPayable",zeroDecimalToNull(wagesPayable) + "元");
        map.put("totalLeaveCount",zeroDecimalToNull(leaveCount) + "元");
        map.put("totalLackCard",zeroDecimalToNull(lackCard) + "元");
        map.put("totalViolationOfSafety",zeroDecimalToNull(violationOfSafety) + "元");
        map.put("totalNetSalary",zeroDecimalToNull(netSalary) + "元");
        workbook = ExcelExportUtil.exportExcel(params, map);
        // String fileName = longDateTOStr(travelPersionPerformanceQueryCriteria.getMonthTime())+"送货绩效统计.xlsx";
        FileUtil.downLoadExcel("fileName", response, workbook);
    }



    private BigDecimal addDecimalWithoutNull(BigDecimal self,BigDecimal count){
        if(null == count){
            return self;
        }else{
            return self.add(count);
        }
    }

    private String zeroDecimalToNull(BigDecimal bigDecimal) {
        if(null == bigDecimal || bigDecimal.compareTo(BigDecimal.ZERO) == 0) {
            return "";
        }else{
            return bigDecimal.toString();
        }
    }

    private String dayToString(BigDecimal bigDecimal) {
        if(null == bigDecimal || bigDecimal.compareTo(BigDecimal.ZERO) == 0) {
            return "";
        }else{
            return Float.parseFloat(bigDecimal.toString())+"";
        }
    }

    private String longDateTOStr(Long longDate){
        if(null == longDate){
            return null;
        }else{
            Date date = new Date(longDate);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            return sdf.format(date);
        }
    }

    private String timestampToStr(Timestamp timestamp){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(timestamp.getTime());
        return sdf.format(date);
    }
}