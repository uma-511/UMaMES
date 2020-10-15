package me.zhengjie.uma_mes.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.hutool.extra.template.TemplateConfig;
import me.zhengjie.uma_mes.domain.AcidPersionPerformance;
import me.zhengjie.uma_mes.service.dto.TravelPersionPerformanceDTO;
import me.zhengjie.uma_mes.service.dto.TravelPersionPerformanceQueryCriteria;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.uma_mes.repository.AcidPersionPerformanceRepository;
import me.zhengjie.uma_mes.service.AcidPersionPerformanceService;
import me.zhengjie.uma_mes.service.dto.AcidPersionPerformanceDTO;
import me.zhengjie.uma_mes.service.dto.AcidPersionPerformanceQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.AcidPersionPerformanceMapper;
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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

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
        BigDecimal zero = new BigDecimal(0.00);
        for(AcidPersionPerformance a:page){
            if(null != a.getNumber() && a.getNumber().compareTo(zero) == 0){
                a.setNumber(null);
            }
            if(null != a.getSpecifications() && a.getSpecifications().compareTo(zero) == 0){
                a.setSpecifications(null);
            }
            if(null != a.getWeight() && a.getWeight().compareTo(zero) == 0){
                a.setWeight(null);
            }
            if(null != a.getUnitPrice() && a.getUnitPrice().compareTo(zero) == 0){
                a.setUnitPrice(null);
            }
            if(null != a.getPrice() && a.getPrice().compareTo(zero) == 0){
                a.setPrice(null);
            }
        }
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void downloadAcidPersonPerformance(AcidPersionPerformanceQueryCriteria acidPersionPerformanceQueryCriteria, HttpServletResponse response) {
        acidPersionPerformanceQueryCriteria.setEnable(Boolean.TRUE);
        List<AcidPersionPerformanceDTO> acidPersionPerformanceDTOList = this.queryAll(acidPersionPerformanceQueryCriteria);
        String lastName = "";
        lastName = "/acidPersonPerformance_temp.xls";
        String templatePath = new TemplateConfig("template/excel", TemplateConfig.ResourceMode.CLASSPATH).getPath() + lastName;
        // 加载模板
        TemplateExportParams params = new TemplateExportParams(templatePath);
        // params.setReadonly(Boolean.TRUE);
        Workbook workbook = null;
        // 单条记录
        Map<String, Object> map = new HashMap<String, Object>();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
        if(null != acidPersionPerformanceQueryCriteria.getMonthTime()){
            map.put("lessDate", longDateTOStr(acidPersionPerformanceQueryCriteria.getMonthTime()));
            map.put("longDate", sdf.format(acidPersionPerformanceQueryCriteria.getStartTime()) + " 至 " + sdf.format(acidPersionPerformanceQueryCriteria.getEndTime()));
        }
        BigDecimal countNumber = BigDecimal.ZERO;
        BigDecimal countWeight = BigDecimal.ZERO;
        BigDecimal countPrice = BigDecimal.ZERO;
        // 组装循环列表
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
        for(AcidPersionPerformanceDTO t: acidPersionPerformanceDTOList){
            Map<String, String> lm = new HashMap<String, String>();
            lm.put("taskDate", timestampToStr(t.getTaskDate()));
            lm.put("person", t.getPerson());
            lm.put("productName", t.getProductName());
            lm.put("number", zeroDecimalToNull(t.getNumber()));
            lm.put("specifications", zeroDecimalToNull(t.getSpecifications()));
            lm.put("weight", zeroDecimalToNull(t.getWeight()));
            lm.put("unitPrice", zeroDecimalToNull(t.getUnitPrice()));
            lm.put("price", zeroDecimalToNull(t.getPrice()));
            listMap.add(lm);
            // 统计
            countNumber = addDecimalWithoutNull(countNumber,t.getNumber());
            countWeight = addDecimalWithoutNull(countWeight,t.getWeight());
            countPrice = addDecimalWithoutNull(countPrice,t.getPrice());
        }
        map.put("acidPersonPerformanceList", listMap);

        map.put("totalNumber",zeroDecimalToNull(countNumber) + "桶");
        map.put("totalWeight",zeroDecimalToNull(countWeight) + "吨");
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

    private String timestampToStr(Timestamp timestamp){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(timestamp.getTime());
        return sdf.format(date);
    }
}