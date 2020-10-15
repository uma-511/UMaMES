package me.zhengjie.uma_mes.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.hutool.extra.template.TemplateConfig;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.uma_mes.domain.TravelPersionPerformance;
import me.zhengjie.uma_mes.repository.ChemicalFiberDeliveryNoteRepository;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.uma_mes.repository.TravelPersionPerformanceRepository;
import me.zhengjie.uma_mes.service.TravelPersionPerformanceService;
import me.zhengjie.uma_mes.service.dto.TravelPersionPerformanceDTO;
import me.zhengjie.uma_mes.service.dto.TravelPersionPerformanceQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.TravelPersionPerformanceMapper;
import org.apache.poi.ss.usermodel.Workbook;
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
* @date 2020-09-02
*/
@Service
@CacheConfig(cacheNames = "travelPersionPerformance")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class TravelPersionPerformanceServiceImpl implements TravelPersionPerformanceService {

    private final TravelPersionPerformanceRepository travelPersionPerformanceRepository;

    private final TravelPersionPerformanceMapper travelPersionPerformanceMapper;

    private final ChemicalFiberDeliveryNoteRepository chemicalFiberDeliveryNoteRepository;

    public TravelPersionPerformanceServiceImpl(TravelPersionPerformanceRepository travelPersionPerformanceRepository, TravelPersionPerformanceMapper travelPersionPerformanceMapper, ChemicalFiberDeliveryNoteRepository chemicalFiberDeliveryNoteRepository) {
        this.travelPersionPerformanceRepository = travelPersionPerformanceRepository;
        this.travelPersionPerformanceMapper = travelPersionPerformanceMapper;
        this.chemicalFiberDeliveryNoteRepository = chemicalFiberDeliveryNoteRepository;
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
        if(null != resources.getScanNumber()){
            resources.setCustomerName(resources.getScanNumber());
        }
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
    @Transactional(rollbackFor = Exception.class)
    public void downloadTravelPersonPerformance(TravelPersionPerformanceQueryCriteria travelPersionPerformanceQueryCriteria, HttpServletResponse response) {
        travelPersionPerformanceQueryCriteria.setEnable(Boolean.TRUE);
        List<TravelPersionPerformanceDTO> travelPersonPerformanceDTOList = this.queryAll(travelPersionPerformanceQueryCriteria);
        String lastName = "";
        lastName = "/travelPersonPerformance_temp.xls";
        String templatePath = new TemplateConfig("template/excel", TemplateConfig.ResourceMode.CLASSPATH).getPath() + lastName;
        // 加载模板
        TemplateExportParams params = new TemplateExportParams(templatePath);
        // params.setReadonly(Boolean.TRUE);
        Workbook workbook = null;
        // 单条记录
        Map<String, Object> map = new HashMap<String, Object>();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
        if(null != travelPersionPerformanceQueryCriteria.getMonthTime()){
            map.put("lessDate", longDateTOStr(travelPersionPerformanceQueryCriteria.getMonthTime()));
            map.put("longDate", sdf.format(travelPersionPerformanceQueryCriteria.getStartTime()) + " 至 " + sdf.format(travelPersionPerformanceQueryCriteria.getEndTime()));
        }
        BigDecimal countMileageFee = BigDecimal.ZERO;
        BigDecimal countOvertimePay = BigDecimal.ZERO;
        BigDecimal countAllowance = BigDecimal.ZERO;
        BigDecimal countSurcharge = BigDecimal.ZERO;
        BigDecimal countHandlingCost = BigDecimal.ZERO;
        BigDecimal countTotalPerformance = BigDecimal.ZERO;
        // 组装循环列表
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
        for(TravelPersionPerformanceDTO t: travelPersonPerformanceDTOList){
            Map<String, String> lm = new HashMap<String, String>();
            lm.put("date", timestampToStr(t.getCreateTime()));
            lm.put("personName", t.getPersonName());
            lm.put("permission", t.getPermission());
            lm.put("customerName", t.getCustomerName());
            lm.put("mileageFee", zeroDecimalToNull(t.getMileageFee()));
            lm.put("overtimePay", zeroDecimalToNull(t.getOvertimePay()));
            lm.put("allowance", zeroDecimalToNull(t.getAllowance()));
            lm.put("surcharge", zeroDecimalToNull(t.getSurcharge()));
            lm.put("handlingCost", zeroDecimalToNull(t.getHandlingCost()));
            lm.put("totalPerformance", zeroDecimalToNull(t.getTotalPerformance()));
            listMap.add(lm);
            // 统计
            countMileageFee = addDecimalWithoutNull(countMileageFee,t.getMileageFee());
            countOvertimePay = addDecimalWithoutNull(countOvertimePay,t.getOvertimePay());
            countAllowance = addDecimalWithoutNull(countAllowance,t.getAllowance());
            countSurcharge = addDecimalWithoutNull(countSurcharge,t.getSurcharge());
            countHandlingCost = addDecimalWithoutNull(countHandlingCost,t.getHandlingCost());
            countTotalPerformance = addDecimalWithoutNull(countTotalPerformance,t.getTotalPerformance());
        }
        map.put("travelPersonPerformanceList", listMap);

        map.put("countMileageFee",zeroDecimalToNull(countMileageFee) + "元");
        map.put("countOvertimePay",zeroDecimalToNull(countOvertimePay) + "元");
        map.put("countAllowance",zeroDecimalToNull(countAllowance) + "元");
        map.put("countSurcharge",zeroDecimalToNull(countSurcharge) + "元");
        map.put("countHandlingCost",zeroDecimalToNull(countHandlingCost) + "元");
        map.put("countTotalPerformance",zeroDecimalToNull(countTotalPerformance) + "元");
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

    private String timestampToStr(Timestamp timestamp){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(timestamp.getTime());
        return sdf.format(date);
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