package me.zhengjie.uma_mes.service.impl;

import me.zhengjie.uma_mes.domain.ChemicalFiberStock;
import me.zhengjie.uma_mes.repository.ChemicalFiberDeliveryNoteRepository;
import me.zhengjie.uma_mes.repository.ChemicalFiberStockRepository;
import me.zhengjie.uma_mes.repository.ChemicalFiberStockWarehousingRepository;
import me.zhengjie.uma_mes.service.ChemicalFibeDashboardService;
import me.zhengjie.uma_mes.service.dto.ChemicalFibeDashboardDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFibeDashboardQueryCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@CacheConfig(cacheNames = "chemicalFibeDashboard")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ChemicalFibeDashboardServiceImpl implements ChemicalFibeDashboardService {

    @Autowired
    private ChemicalFiberStockWarehousingRepository chemicalFiberStockWarehousingRepository;

    @Autowired
    private ChemicalFiberDeliveryNoteRepository chemicalFiberDeliveryNoteRepository;

    @Autowired
    private ChemicalFiberStockRepository chemicalFiberStockRepository;

    public ChemicalFibeDashboardDTO queryAll(ChemicalFibeDashboardQueryCriteria criteria) {
        String dateTime = DashboardTime(criteria);

        ChemicalFibeDashboardDTO dto = new ChemicalFibeDashboardDTO();
        dto.setWarehousingNumber(chemicalFiberStockWarehousingRepository.getWarehousingOrders(dateTime));
        dto.setWarehousingTonNumber(chemicalFiberStockWarehousingRepository.getTonnageInStorage(dateTime, "吨"));
        dto.setWarehousingBranchNumber(chemicalFiberStockWarehousingRepository.getTonnageInStorage(dateTime, "支"));
        dto.setDeliveryNumber(chemicalFiberDeliveryNoteRepository.getCurrenNoteCount(dateTime));
        dto.setDeliveryTonNumber(chemicalFiberDeliveryNoteRepository.getTonnageInStorage(dateTime, "吨"));
        dto.setDeliveryBranchNumber(chemicalFiberDeliveryNoteRepository.getTonnageInStorage(dateTime, "支"));
        dto.setDeliveryTotalNumber(chemicalFiberDeliveryNoteRepository.getDeliveryAmount(dateTime));
        dto.setStockTonNumber(chemicalFiberStockRepository.getTonnageInStorage("吨"));
        dto.setStockBranchNumber(chemicalFiberStockRepository.getTonnageInStorage("支"));

        return dto;
    }

    public String DashboardTime(ChemicalFibeDashboardQueryCriteria criteria) {
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
