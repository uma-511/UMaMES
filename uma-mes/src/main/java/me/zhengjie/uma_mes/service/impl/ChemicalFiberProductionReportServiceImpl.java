package me.zhengjie.uma_mes.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.hutool.extra.template.TemplateConfig;
import com.lgmn.common.result.Result;
import com.lgmn.common.utils.ObjectTransfer;
import me.zhengjie.uma_mes.domain.ChemicalFiberLabel;
import me.zhengjie.uma_mes.domain.ChemicalFiberProduction;
import me.zhengjie.uma_mes.domain.ChemicalFiberProductionReport;
import me.zhengjie.uma_mes.repository.ChemicalFiberProductionReportRepository;
import me.zhengjie.uma_mes.repository.ChemicalFiberProductionRepository;
import me.zhengjie.uma_mes.service.ChemicalFiberProductionReportService;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberProductionReportDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberProductionReportQueryCriteria;
import me.zhengjie.uma_mes.service.mapper.ChemicalFiberProductionReportMapper;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@CacheConfig(cacheNames = "chemicalFiberProductionReport")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ChemicalFiberProductionReportServiceImpl implements ChemicalFiberProductionReportService {


    @Autowired
    private ChemicalFiberProductionReportRepository chemicalFiberProductionReportRepository;

    @Autowired
    private ChemicalFiberProductionReportMapper chemicalFiberProductionReportMapper;

    @Autowired
    private ChemicalFiberProductionRepository chemicalFiberProductionRepository;


    public Map<String, Object> queryAll(ChemicalFiberProductionReportQueryCriteria criteria, Pageable pageable) {
        if (criteria.getTempStartTime() != null) {
            String StartTime = new SimpleDateFormat("yyyy-MM-dd").format(criteria.getTempStartTime());
            StartTime = StartTime + " 07:00:00";
            String EndTime = new SimpleDateFormat("yyyy-MM-dd").format(criteria.getTempEndTime());
            EndTime = EndTime + " 06:59:00";
            Timestamp time1 = Timestamp.valueOf(StartTime);
            Timestamp time2 = Timestamp.valueOf(EndTime);
            criteria.setStartTime(time1);
            criteria.setEndTime(time2);
           /* String StartTime = new SimpleDateFormat("yyyy-MM-dd").format(criteria.getTempStartTime());
            StartTime = StartTime + " 07:00:00";
            Timestamp time1 = Timestamp.valueOf(StartTime);
            Timestamp time2 = new Timestamp(time1.getTime() + (long)1000*3600*24);
            criteria.setStartTime(time1);
            criteria.setEndTime(time2);*/
        }
        Page<ChemicalFiberProductionReport> page = chemicalFiberProductionReportRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(chemicalFiberProductionReportMapper::toDto));
    }

    public Result getProductionReportSummaries(ChemicalFiberProductionReportQueryCriteria criteria) {
        if (criteria.getTempStartTime() != null) {
            String StartTime = new SimpleDateFormat("yyyy-MM-dd").format(criteria.getTempStartTime());
            StartTime = StartTime + " 07:00:00";
            String EndTime = new SimpleDateFormat("yyyy-MM-dd").format(criteria.getTempEndTime());
            EndTime = EndTime + " 06:59:00";
            Timestamp time1 = Timestamp.valueOf(StartTime);
            Timestamp time2 = Timestamp.valueOf(EndTime);
            criteria.setStartTime(time1);
            criteria.setEndTime(time2);
            /*criteria.setStartTime(new Timestamp(criteria.getTempStartTime()));
            criteria.setEndTime(new Timestamp(criteria.getTempEndTime()));*/
        }
        List<ChemicalFiberProductionReport> reportList = chemicalFiberProductionReportRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder));


        // 生产包数
        BigDecimal productionPacketNumber = new BigDecimal(0);

        // 生产个数
        BigDecimal productionFactPerBagNumber = new BigDecimal(0);

        // 生产净重
        BigDecimal productionNetWeight = new BigDecimal(0);

        // 生产毛重
        BigDecimal productionGrossWeight = new BigDecimal(0);

        // 入库包数
        BigDecimal warehousingPacketNumber = new BigDecimal(0);

        // 入库个数
        BigDecimal warehousingFactPerBagNumber = new BigDecimal(0);

        // 入库净重
        BigDecimal warehousingNetWeight = new BigDecimal(0);

        // 入库毛重
        BigDecimal warehousingGrossWeight = new BigDecimal(0);

        // 入库包数
        BigDecimal toVoidPacketNumber = new BigDecimal(0);

        List<Object> list = new ArrayList<>();
        list.add("总计");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        for (ChemicalFiberProductionReport dto : reportList) {
            productionPacketNumber = productionPacketNumber.add(dto.getProductionPacketNumber());
            productionFactPerBagNumber = productionFactPerBagNumber.add(dto.getProductionFactPerBagNumber());
            productionNetWeight = productionNetWeight.add(dto.getProductionNetWeight());
            productionGrossWeight = productionGrossWeight.add(dto.getProductionGrossWeight());

            warehousingPacketNumber = warehousingPacketNumber.add(dto.getWarehousingPacketNumber());
            warehousingFactPerBagNumber = warehousingFactPerBagNumber.add(dto.getWarehousingFactPerBagNumber());
            warehousingNetWeight = warehousingNetWeight.add(dto.getWarehousingNetWeight());
            warehousingGrossWeight = warehousingGrossWeight.add(dto.getWarehousingGrossWeight());

            toVoidPacketNumber = toVoidPacketNumber.add(dto.getToVoidPacketNumber());


        }

        list.add(productionPacketNumber);
        list.add(productionFactPerBagNumber);
        list.add(productionNetWeight);
        list.add(productionGrossWeight);
        list.add(warehousingPacketNumber);
        list.add(warehousingFactPerBagNumber);
        list.add(warehousingNetWeight);
        list.add(warehousingGrossWeight);
        list.add(toVoidPacketNumber);

        return Result.success(list);
    }

    public ChemicalFiberProductionReport getReport(Timestamp time, String shifts, String machine) {
/*
        Map<String, Object> timeMap = monthTimeInMillis();
        String year = timeMap.get("year").toString();
        String month = timeMap.get("month").toString();
        String day = timeMap.get("day").toString();
        return chemicalFiberProductionReportRepository.getReport(year + "-" + month + "-" + day, shifts, machine);*/
        String StartTime = new SimpleDateFormat("yyyy-MM-dd").format(time);
        StartTime = StartTime + " 07:00:00";
        Timestamp ttime1 = Timestamp.valueOf(StartTime);
        Timestamp ttime2 = new Timestamp(ttime1.getTime() + (long)1000*3600*24);
        Timestamp ttime3 = new Timestamp(ttime1.getTime() - (long)1000*3600*24);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time1 = simpleDateFormat.format(ttime1);
        String time2 = simpleDateFormat.format(ttime2);
        String time3 = simpleDateFormat.format(ttime3);
        if (ttime1.getTime() < time.getTime()) {
            if ( time.getTime() < ttime2.getTime()) {
                return chemicalFiberProductionReportRepository.getReport(time1, time2, shifts, machine);
            }
        } else if (ttime3.getTime() < time.getTime()) {
            if (time.getTime() < ttime1.getTime()) {
                return chemicalFiberProductionReportRepository.getReport(time3, time1, shifts, machine);
            }

        }
        return null;
    }

    /*public ChemicalFiberProductionReport getDelectReport(String time, String shifts, String machine) {

       *//* Map<String, Object> timeMap = monthTimeInMillis();
        String year = timeMap.get("year").toString();
        String month = timeMap.get("month").toString();
        String day = timeMap.get("day").toString();*//*
        return chemicalFiberProductionReportRepository.getReport(time, time, shifts, machine);
    }*/

    public void update(ChemicalFiberProductionReport report) {
        chemicalFiberProductionReportRepository.save(report);
    }

    public void create(ChemicalFiberProductionReportDTO reportDTO) {
        ChemicalFiberProductionReport report = new ChemicalFiberProductionReport();
        ObjectTransfer.transValue(reportDTO, report);
        chemicalFiberProductionReportRepository.save(report);
    }

    public void delectReport(ChemicalFiberLabel label) {
        /*DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String time = sdf.format(label.getPrintTime());*/
        ChemicalFiberProductionReport report = getReport(label.getPrintTime(),label.getShifts(), label.getMachine());
        if (report != null) {
            BigDecimal toVoidPacketNumber = report.getToVoidPacketNumber();
            BigDecimal toVoidFactPerBagNumber = report.getToVoidFactPerBagNumber();
            BigDecimal toVoidNetWeight = report.getToVoidNetWeight();
            BigDecimal toVoidGrossWeight = report.getToVoidGrossWeight();

            BigDecimal productionPacketNumber = report.getProductionPacketNumber();
            BigDecimal productionFactPerBagNumber = report.getProductionFactPerBagNumber();
            BigDecimal productionNetWeight = report.getProductionNetWeight();
            BigDecimal productionGrossWeight = report.getProductionGrossWeight();

            BigDecimal warehousingPacketNumber = report.getWarehousingPacketNumber();
            BigDecimal warehousingFactPerBagNumber = report.getWarehousingFactPerBagNumber();
            BigDecimal warehousingNetWeight = report.getWarehousingNetWeight();
            BigDecimal warehousingGrossWeight = report.getWarehousingGrossWeight();



            report.setToVoidPacketNumber(toVoidPacketNumber.add(new BigDecimal(1)));
            report.setToVoidFactPerBagNumber(toVoidFactPerBagNumber.add(new BigDecimal(label.getFactPerBagNumber())));
            report.setToVoidNetWeight(toVoidNetWeight.add(label.getNetWeight()));
            report.setToVoidGrossWeight(toVoidGrossWeight.add(label.getGrossWeight()));

            report.setProductionPacketNumber(productionPacketNumber.subtract(new BigDecimal(1)));
            report.setProductionFactPerBagNumber(productionFactPerBagNumber.subtract(new BigDecimal(label.getFactPerBagNumber())));
            report.setProductionNetWeight(productionNetWeight.subtract(label.getNetWeight()));
            report.setProductionGrossWeight(productionGrossWeight.subtract(label.getGrossWeight()));

            if (label.getStatus() != 0) {
                report.setWarehousingPacketNumber(warehousingPacketNumber.subtract(new BigDecimal(1)));
                report.setWarehousingFactPerBagNumber(warehousingFactPerBagNumber.subtract(new BigDecimal(label.getFactPerBagNumber())));
                report.setWarehousingNetWeight(warehousingNetWeight.subtract(label.getNetWeight()));
                report.setWarehousingGrossWeight(warehousingGrossWeight.subtract(label.getGrossWeight()));
            }

            update(report);

        }
    }

    public void downloadProduct(ChemicalFiberProductionReportQueryCriteria criteria,  Pageable pageable, HttpServletResponse response) throws IOException {
        if (criteria.getTempStartTime() != null) {
            String StartTime = new SimpleDateFormat("yyyy-MM-dd").format(criteria.getTempStartTime());
            StartTime = StartTime + " 07:00:00";
            String EndTime = new SimpleDateFormat("yyyy-MM-dd").format(criteria.getTempEndTime());
            EndTime = EndTime + " 06:59:00";
            Timestamp time1 = Timestamp.valueOf(StartTime);
            Timestamp time2 = Timestamp.valueOf(EndTime);
            criteria.setStartTime(time1);
            criteria.setEndTime(time2);
           /* String StartTime = new SimpleDateFormat("yyyy-MM-dd").format(criteria.getTempStartTime());
            StartTime = StartTime + " 07:00:00";
            Timestamp time1 = Timestamp.valueOf(StartTime);
            Timestamp time2 = new Timestamp(time1.getTime() + (long)1000*3600*24);
            criteria.setStartTime(time1);
            criteria.setEndTime(time2);*/
        }
        List<ChemicalFiberProductionReport> procuctionReportList = chemicalFiberProductionReportRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder));

        List<Map<String, Object>> listMap = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        ChemicalFiberProductionReportDTO sum = new ChemicalFiberProductionReportDTO();
        for (int i = 1; i <= procuctionReportList.size(); i++) {
            Map<String, Object> maps = new HashMap<>();
            ChemicalFiberProductionReport dto = procuctionReportList.get(i - 1);
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String time = sdf.format(dto.getTime());
            maps.put("index", i);
            maps.put("printTime", time);
            maps.put("number", dto.getProductionNumber());
            maps.put("machine", dto.getMachine());
            maps.put("shifts", dto.getShifts());
            maps.put("color", dto.getColor());
            maps.put("fineness", dto.getFineness());
            maps.put("productionPacketNumber", dto.getProductionPacketNumber());
            maps.put("productionFactPerBagNumber", dto.getProductionFactPerBagNumber());
            maps.put("productionNetWeight", dto.getProductionNetWeight());
            maps.put("productionGrossWeight", dto.getProductionGrossWeight());
            maps.put("warehousingPacketNumber", dto.getWarehousingPacketNumber());
            maps.put("warehousingPerBagNumber", dto.getWarehousingFactPerBagNumber());
            maps.put("warehousingNetWeight", dto.getWarehousingNetWeight());
            maps.put("warehousingGrossWeight", dto.getWarehousingGrossWeight());
            maps.put("toVoidPacketNumber", dto.getToVoidPacketNumber());

            sum.setProductionPacketNumber(sum.getProductionPacketNumber().add(dto.getProductionPacketNumber()));
            sum.setProductionFactPerBagNumber(sum.getProductionFactPerBagNumber().add(dto.getProductionFactPerBagNumber()));
            sum.setProductionNetWeight(sum.getProductionNetWeight().add(dto.getProductionNetWeight()));
            sum.setProductionGrossWeight(sum.getProductionGrossWeight().add(dto.getProductionGrossWeight()));

            sum.setWarehousingPacketNumber(sum.getWarehousingPacketNumber().add(dto.getWarehousingPacketNumber()));
            sum.setWarehousingFactPerBagNumber(sum.getWarehousingFactPerBagNumber().add(dto.getWarehousingFactPerBagNumber()));
            sum.setWarehousingNetWeight(sum.getWarehousingNetWeight().add(dto.getWarehousingNetWeight()));
            sum.setWarehousingGrossWeight(sum.getWarehousingGrossWeight().add(dto.getWarehousingGrossWeight()));

            sum.setToVoidPacketNumber(sum.getToVoidPacketNumber().add(dto.getToVoidPacketNumber()));
            listMap.add(maps);
        }

        map.put("ProdctionList", listMap);
        map.put("productionPacketNumber", sum.getProductionPacketNumber());
        map.put("productionFactPerBagNumber", sum.getProductionFactPerBagNumber());
        map.put("productionNetWeight", sum.getProductionNetWeight());
        map.put("productionGrossWeight", sum.getProductionGrossWeight());
        map.put("warehousingPacketNumber", sum.getWarehousingPacketNumber());
        map.put("warehousingPerBagNumber", sum.getWarehousingFactPerBagNumber());
        map.put("warehousingNetWeight", sum.getWarehousingNetWeight());
        map.put("warehousingGrossWeight", sum.getWarehousingGrossWeight());
        map.put("toVoidPacketNumber", sum.getToVoidPacketNumber());

        Workbook workbook = null;
        String templatePath = new TemplateConfig("template/excel", TemplateConfig.ResourceMode.CLASSPATH).getPath() + "/prodtion.xls";
        // 加载模板
        TemplateExportParams params = new TemplateExportParams(templatePath);
        workbook = ExcelExportUtil.exportExcel(params, map);
        FileUtil.downLoadExcel("生产报表导出.xls", response, workbook);

    }


    public Map monthTimeInMillis() {
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.MONTH, 0);
        //calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Long time = calendar.getTimeInMillis();
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        Map<String, Object> map = new HashMap<>();
        map.put("time", time);
        map.put("month", month < 10 ? "0" + month : month);
        map.put("year", year);
        map.put("day", day < 10 ? "0" + day : day);
        return map;
    }
}


