package me.zhengjie.modules.monitor.service.impl;

import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.monitor.domain.Visits;
import me.zhengjie.modules.monitor.repository.VisitsRepository;
import me.zhengjie.modules.monitor.service.VisitsService;
import me.zhengjie.repository.LogRepository;
import me.zhengjie.uma_mes.repository.ChemicalFiberDeliveryNoteRepository;
import me.zhengjie.uma_mes.repository.ChemicalFiberLabelRepository;
import me.zhengjie.uma_mes.repository.ChemicalFiberStockRepository;
import me.zhengjie.uma_mes.service.dto.VisitsQueryCrteria;
import me.zhengjie.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Zheng Jie
 * @date 2018-12-13
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class VisitsServiceImpl implements VisitsService {

    private final VisitsRepository visitsRepository;

    private final LogRepository logRepository;

    @Autowired
    private ChemicalFiberStockRepository chemicalFiberStockRepository;

    @Autowired
    private ChemicalFiberDeliveryNoteRepository chemicalFiberDeliveryNoteRepository;

    @Autowired
    private ChemicalFiberLabelRepository chemicalFiberLabelRepository;

    public VisitsServiceImpl(VisitsRepository visitsRepository, LogRepository logRepository) {
        this.visitsRepository = visitsRepository;
        this.logRepository = logRepository;
    }

    @Override
    public void save() {
        LocalDate localDate = LocalDate.now();
        Visits visits = visitsRepository.findByDate(localDate.toString());
        if(visits == null){
            visits = new Visits();
            visits.setWeekDay(StringUtils.getWeekDay());
            visits.setPvCounts(1L);
            visits.setIpCounts(1L);
            visits.setDate(localDate.toString());
            visitsRepository.save(visits);
        }
    }

    @Override
    public void count(HttpServletRequest request) {
        LocalDate localDate = LocalDate.now();
        Visits visits = visitsRepository.findByDate(localDate.toString());
        visits.setPvCounts(visits.getPvCounts()+1);
        long ipCounts = logRepository.findIp(localDate.toString(), localDate.plusDays(1).toString());
        visits.setIpCounts(ipCounts);
        visitsRepository.save(visits);
    }

   /* @Override
    public Object get() {
        Map<String,Object> map = new HashMap<>();
        LocalDate localDate = LocalDate.now();
        Visits visits = visitsRepository.findByDate(localDate.toString());
        List<Visits> list = visitsRepository.findAllVisits(localDate.minusDays(6).toString(),localDate.plusDays(1).toString());

        long recentVisits = 0, recentIp = 0;
        for (Visits data : list) {
            recentVisits += data.getPvCounts();
            recentIp += data.getIpCounts();
        }
        map.put("newVisits",visits.getPvCounts());
        map.put("newIp",visits.getIpCounts());
        map.put("recentVisits",recentVisits);
        map.put("recentIp",recentIp);
        return map;
    }*/

    @Override
    public Object get(VisitsQueryCrteria crteria) {
        Map<String,Object> map = new HashMap<>();
        String time = DashboardTime(crteria);

        Map<String, Object> sumStock = chemicalFiberStockRepository.getSumStock();

        Map<String, Object> sumLabel = chemicalFiberLabelRepository.getSumLabel(time);

        Map<String, Object> sumNote = chemicalFiberDeliveryNoteRepository.getSum(time);

        Map<String, Object> sumNote1 = chemicalFiberDeliveryNoteRepository.getSumDelvery(time);

        BigDecimal a = new BigDecimal(0);
        BigDecimal b = new BigDecimal(0);
        BigDecimal c = new BigDecimal(0);
        BigDecimal d = new BigDecimal(0);
        BigDecimal e = new BigDecimal(0);
        BigDecimal g = new BigDecimal(0);

        if (sumStock.get("net_weight") != null) {
            a = new BigDecimal(sumStock.get("net_weight").toString());
        }
        if (sumStock.get("gross_weight") != null) {
            b = new BigDecimal(sumStock.get("gross_weight").toString());
        }
        if (sumLabel.get("net_weight") != null) {
            c = new BigDecimal(sumLabel.get("net_weight").toString());
        }
        if (sumLabel.get("gross_weight") != null) {
            d = new BigDecimal(sumLabel.get("gross_weight").toString());
        }
        if ( sumNote.get("total_price") != null) {
            e = new BigDecimal(sumNote.get("total_price").toString());
        }
        if ( sumNote1.get("total_weight") != null) {
            g = new BigDecimal(sumNote1.get("total_weight").toString());
        }

        map.put("stockNumber",sumStock.get("bag"));
        map.put("stockNetWeight",a);
        map.put("stockGrossWeight",b);
        map.put("warehousingNumber",sumLabel.get("total_bag"));
        map.put("warehousingNetWeight",c);
        map.put("warehousingGrossWeight",d);
        map.put("deliveryNumber",sumNote.get("id"));
        map.put("deliveryTotalNumber",e);
        map.put("deliveryBagNumber",sumNote1.get("total_bag"));
        map.put("deliveryNetWeight",g);
        //map.put("recentIp",5);
        return map;
    }

    @Override
    public Object getChartData() {
        Map<String,Object> map = new HashMap<>();
        LocalDate localDate = LocalDate.now();
        List<Visits> list = visitsRepository.findAllVisits(localDate.minusDays(6).toString(),localDate.plusDays(1).toString());
        map.put("weekDays",list.stream().map(Visits::getWeekDay).collect(Collectors.toList()));
        map.put("visitsData",list.stream().map(Visits::getPvCounts).collect(Collectors.toList()));
        map.put("ipData",list.stream().map(Visits::getIpCounts).collect(Collectors.toList()));
        return map;
    }

    public String DashboardTime(VisitsQueryCrteria criteria) {
        Long time = criteria.getTime();
        String radio = criteria.getRadio();
        Date date = new Date(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Integer year = calendar.get(Calendar.YEAR);
        Integer month = calendar.get(Calendar.MONTH) + 1;
        Integer day = calendar.get(Calendar.DAY_OF_MONTH);
        String dateTime = "";
        String months = month < 10 ? "0" + month : month + "";
        String days = day < 10 ? "0" + day : day + "";
        if (radio.equals("1")) {
            dateTime = year + "-" + months + "-" + days;
        }
        if (radio.equals("2")) {
            dateTime = year + "-" + months;
        }
        if (radio.equals("3")) {
            dateTime = year + "";
        }

        return dateTime;
    }
}
