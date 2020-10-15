package me.zhengjie.uma_mes.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.hutool.extra.template.TemplateConfig;
import me.zhengjie.uma_mes.domain.WorkAttendance;
import me.zhengjie.uma_mes.service.dto.AcidPersionPerformanceDTO;
import me.zhengjie.uma_mes.service.dto.AcidPersionPerformanceQueryCriteria;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.uma_mes.repository.WorkAttendanceRepository;
import me.zhengjie.uma_mes.service.WorkAttendanceService;
import me.zhengjie.uma_mes.service.dto.WorkAttendanceDTO;
import me.zhengjie.uma_mes.service.dto.WorkAttendanceQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.WorkAttendanceMapper;
import org.apache.poi.ss.usermodel.Workbook;
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
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author wave
* @date 2020-09-07
*/
@Service
@CacheConfig(cacheNames = "workAttendance")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class WorkAttendanceServiceImpl implements WorkAttendanceService {

    private final WorkAttendanceRepository workAttendanceRepository;

    private final WorkAttendanceMapper workAttendanceMapper;

    public WorkAttendanceServiceImpl(WorkAttendanceRepository workAttendanceRepository, WorkAttendanceMapper workAttendanceMapper) {
        this.workAttendanceRepository = workAttendanceRepository;
        this.workAttendanceMapper = workAttendanceMapper;
    }

    @Override
    public Map<String,Object> queryAll(WorkAttendanceQueryCriteria criteria, Pageable pageable){
        List<Boolean> booleanList = new ArrayList<>();
        booleanList.add(Boolean.TRUE);
        if (null != criteria.getShowUnEnable() && criteria.getShowUnEnable())
        {
            booleanList.add(Boolean.FALSE);
        }
        criteria.setEnableList(booleanList);
        Page<WorkAttendance> page = workAttendanceRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        BigDecimal zero = new BigDecimal(0.00);
        for(WorkAttendance w:page){
            if(null != w.getDay() && w.getDay().compareTo(zero) == 0){
                w.setDay(null);
            }
            if(null != w.getPrice() && w.getPrice().compareTo(zero) == 0){
                w.setPrice(null);
            }
        }
        return PageUtil.toPage(page.map(workAttendanceMapper::toDto));
    }

    @Override
    public List<WorkAttendanceDTO> queryAll(WorkAttendanceQueryCriteria criteria){
        return workAttendanceMapper.toDto(workAttendanceRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    public WorkAttendanceDTO findById(Integer id) {
        WorkAttendance workAttendance = workAttendanceRepository.findById(id).orElseGet(WorkAttendance::new);
        ValidationUtil.isNull(workAttendance.getId(),"WorkAttendance","id",id);
        return workAttendanceMapper.toDto(workAttendance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WorkAttendanceDTO create(WorkAttendance resources) {
        resources.setSerialNumber(generateSerialNumber());
        resources.setEnable(Boolean.TRUE);
        resources.setCreateDate(new Timestamp(System.currentTimeMillis()));
        return workAttendanceMapper.toDto(workAttendanceRepository.save(resources));
    }

    private String generateSerialNumber() {
        String serialNumber;
        Map<String, Object> timeMap = monthTimeInMillis();
        String year = timeMap.get("year").toString();
        String month = timeMap.get("month").toString();

        String currenNumber=workAttendanceRepository.getCurrenAttendanceCountWithMaxNumber(year+"-"+month);

        if (null == currenNumber || currenNumber.equals("")) {
            serialNumber = "C" + year.substring(2,4) + month + "00001";
        } else {
            Integer lastNumber = Integer.parseInt(currenNumber.substring(currenNumber.length()-5,currenNumber.length()));
            Integer number = lastNumber+ 1;
            String tempNumberStr = String.format("%5d", number++).replace(" ", "0");
            serialNumber = "C" + year.substring(2,4) + month + tempNumberStr;
        }
        return serialNumber;
    }

    public Map monthTimeInMillis() {
        // 获取当前日期
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.MONTH, 0);
        // 设置为1号,当前日期既为本月第一天
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Long time = calendar.getTimeInMillis();
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);

        Map<String, Object> map = new HashMap<>();
        map.put("time", time);
        map.put("month", month < 10 ? "0" + month : month);
        map.put("year", year);
        return map;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(WorkAttendance resources) {
        WorkAttendance workAttendance = workAttendanceRepository.findById(resources.getId()).orElseGet(WorkAttendance::new);
        ValidationUtil.isNull( workAttendance.getId(),"WorkAttendance","id",resources.getId());
        workAttendance.copy(resources);
        workAttendanceRepository.save(workAttendance);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        WorkAttendance workAttendance = workAttendanceRepository.findById(id).orElseGet(WorkAttendance::new);
        workAttendance.setEnable(Boolean.FALSE);
        workAttendanceRepository.save(workAttendance);
    }


    @Override
    public void download(List<WorkAttendanceDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (WorkAttendanceDTO workAttendance : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("人员姓名", workAttendance.getPersonName());
            map.put("人员id", workAttendance.getPersonId());
            map.put("制单日期", workAttendance.getAttenceDate());
            map.put("类型", workAttendance.getAttenceType());
            map.put("天数", workAttendance.getDay());
            map.put("备注", workAttendance.getRemark());
            map.put("创建日期", workAttendance.getCreateDate());
            map.put("状态", workAttendance.getEnable());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void downloadWorkAttendance(WorkAttendanceQueryCriteria criteria, HttpServletResponse response) {
        criteria.setEnable(Boolean.TRUE);
        List<WorkAttendanceDTO> dtoList = this.queryAll(criteria);
        String lastName = "";
        lastName = "/workAttendance_temp.xls";
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
        BigDecimal countDay = BigDecimal.ZERO;
        BigDecimal countPrice = BigDecimal.ZERO;
        // 组装循环列表
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
        for(WorkAttendanceDTO t: dtoList){
            Map<String, String> lm = new HashMap<String, String>();
            lm.put("attenceDate", timestampToStr(t.getAttenceDate()));
            lm.put("person", t.getPersonName());
            lm.put("attenceType", t.getAttenceType());
            lm.put("day", dayToString(t.getDay()));
            lm.put("price", zeroDecimalToNull(t.getPrice()));
            lm.put("remark", t.getRemark());
            listMap.add(lm);
            // 统计
            countDay = addDecimalWithoutNull(countDay,t.getDay());
            countPrice = addDecimalWithoutNull(countPrice,t.getPrice());
        }
        map.put("acidPersonPerformanceList", listMap);

        map.put("totalDay",dayToString(countDay) + "天");
        map.put("totalPrice",zeroDecimalToNull(countPrice) + "元");
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
            return null;
        }else{
            return bigDecimal.toString();
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

    private String dayToString(BigDecimal bigDecimal) {
        if(null == bigDecimal || bigDecimal.compareTo(BigDecimal.ZERO) == 0) {
            return "";
        }else{
            return Float.parseFloat(bigDecimal.toString())+"";
        }
    }

    private String timestampToStr(Timestamp timestamp){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(timestamp.getTime());
        return sdf.format(date);
    }
}