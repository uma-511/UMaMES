package me.zhengjie.uma_mes.service.impl;


import me.zhengjie.uma_mes.domain.ChemicalFiberStockWarehousing;
import me.zhengjie.uma_mes.domain.ChemicalFiberStockWarehousingDetail;
import me.zhengjie.uma_mes.repository.ChemicalFiberStockWarehousingDetailRepository;
import me.zhengjie.uma_mes.repository.ChemicalFiberStockWarehousingRepository;
import me.zhengjie.uma_mes.service.ChemicalFiberWarehousingReortService;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberStockWarehousingQueryCriteria;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberWarehousingReortDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberWarehousingReortQueryCriteria;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

@Service
@CacheConfig(cacheNames = "chemicalFiberWarehousingReort")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ChemicalFiberWarehousingReortServiceImpl implements ChemicalFiberWarehousingReortService {

    @Autowired
    private ChemicalFiberStockWarehousingRepository chemicalFiberStockWarehousingRepository;

    @Autowired
    private ChemicalFiberStockWarehousingDetailRepository chemicalFiberStockWarehousingDetailRepository;

    public Map<String,Object> queryAll(ChemicalFiberWarehousingReortQueryCriteria criteria, Pageable pageable){
        ChemicalFiberStockWarehousingQueryCriteria ca = new ChemicalFiberStockWarehousingQueryCriteria();
        String createUser = "";
        String supplierName = "";
        if (criteria.getTempStartTime() != null) {
            ca.setStartTime(new Timestamp(criteria.getTempStartTime()));
            criteria.setStartTime(new Timestamp(criteria.getTempStartTime()));
            ca.setEndTime(new Timestamp(criteria.getTempEndTime()));
            criteria.setEndTime(new Timestamp(criteria.getTempEndTime()));
        }
        if (criteria.getCreateUser() != null) {
            ca.setCreateUser(criteria.getCreateUser());
            createUser = criteria.getCreateUser();
        }
        if (criteria.getSupplierName() != null) {
            ca.setSupplierName(criteria.getSupplierName());
            supplierName = criteria.getSupplierName();
        }
        List<Integer> invalidList = new ArrayList<>();
        invalidList.add(0);
        if (null != criteria.getQueryWithInvalid() && criteria.getQueryWithInvalid())
        {
            invalidList.add(1);
        }
        ca.setInvalidList(invalidList);
        ca.setWarehousingStatus(2);
        List<ChemicalFiberStockWarehousing> list =
                chemicalFiberStockWarehousingRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,ca,criteriaBuilder));
        List<Integer> notid = chemicalFiberStockWarehousingRepository.getNotId(createUser, supplierName);
        criteria.setWarehousingIdList(notid);
        Page<ChemicalFiberStockWarehousingDetail> page1 =
                chemicalFiberStockWarehousingDetailRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        List<ChemicalFiberStockWarehousingDetail> list1 = page1.getContent();
        List<ChemicalFiberWarehousingReortDTO> warehousingReortList = new ArrayList<>();
        Integer Elements = 0;
        for (int i = 0; i < list1.size(); i++) {
            ChemicalFiberStockWarehousingDetail dto = list1.get(i);
            ChemicalFiberStockWarehousing warehousing = indexWarehousingId(list, dto.getWarehousingId());
            if (warehousing != null) {
                ++Elements;
                ChemicalFiberWarehousingReortDTO WarehousingReort = new ChemicalFiberWarehousingReortDTO();
                WarehousingReort.setScanNumber(dto.getScanNumber());
                WarehousingReort.setWarehousingDate(warehousing.getWarehousingDate());
                WarehousingReort.setWarehousingNumber(dto.getWarehousingNumber());
                WarehousingReort.setSupplierName(warehousing.getSupplierName());
                WarehousingReort.setProdName(dto.getProdName());
                WarehousingReort.setUnit(dto.getUnit());
                WarehousingReort.setPrice(dto.getPrice());
                WarehousingReort.setTotalPrice(dto.getTotalPrice());
                WarehousingReort.setCreateUser(warehousing.getCreateUser());
                WarehousingReort.setCreateDate(warehousing.getCreateDate());
                warehousingReortList.add(WarehousingReort);
            }
        }
        return PageUtil.toPage(new PageImpl(warehousingReortList, pageable, page1.getTotalElements()));
    }

    public Map<String,Object> getSummaryData(ChemicalFiberWarehousingReortQueryCriteria criteria){
        Map<String, Object> map = new HashMap<>();
        ChemicalFiberStockWarehousingQueryCriteria ca = new ChemicalFiberStockWarehousingQueryCriteria();
        String createUser = "";
        String supplierName = "";
        BigDecimal sum = new BigDecimal(0);
        BigDecimal sumTon = new BigDecimal(0);
        BigDecimal sumTotalPrice = new BigDecimal(0);
        if (criteria.getTempStartTime() != null) {
            ca.setStartTime(new Timestamp(criteria.getTempStartTime()));
            criteria.setStartTime(new Timestamp(criteria.getTempStartTime()));
            ca.setEndTime(new Timestamp(criteria.getTempEndTime()));
            criteria.setEndTime(new Timestamp(criteria.getTempEndTime()));
        }
        if (criteria.getCreateUser() != null) {
            ca.setCreateUser(criteria.getCreateUser());
            createUser = criteria.getCreateUser();
        }
        if (criteria.getSupplierName() != null) {
            ca.setSupplierName(criteria.getSupplierName());
            supplierName = criteria.getSupplierName();
        }
        List<Integer> invalidList = new ArrayList<>();
        invalidList.add(0);
        if (null != criteria.getQueryWithInvalid() && criteria.getQueryWithInvalid())
        {
            invalidList.add(1);
        }
        ca.setInvalidList(invalidList);
        ca.setWarehousingStatus(2);
        List<ChemicalFiberStockWarehousing> list =
                chemicalFiberStockWarehousingRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,ca,criteriaBuilder));
        List<Integer> notid = chemicalFiberStockWarehousingRepository.getNotId(createUser, supplierName);
        criteria.setWarehousingIdList(notid);
        List<ChemicalFiberStockWarehousingDetail> list1 = chemicalFiberStockWarehousingDetailRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder));
        for (int i = 0; i < list1.size(); i++) {
            ChemicalFiberStockWarehousingDetail dto = list1.get(i);
            ChemicalFiberStockWarehousing warehousing = indexWarehousingId(list, dto.getWarehousingId());
            if (warehousing != null) {
                sumTotalPrice = sumTotalPrice.add(warehousing.getTotalPrice());
                if (dto.getUnit().equals("吨")) {
                    sumTon = sumTon.add(dto.getWarehousingNumber());
                } else {
                    sum = sum.add(dto.getWarehousingNumber());
                }

            }
        }
        map.put("sumTotalPrice",sumTotalPrice);
        map.put("sumTon",sumTon);
        map.put("sum",sum);
        return map;
    }




    public ChemicalFiberStockWarehousing indexWarehousingId(List<ChemicalFiberStockWarehousing> list,Integer warehousingId) {

        for (int j = 0; j < list.size(); j++) {
            if (list.get(j).getId() == warehousingId) {
                return list.get(j);
            }
        }
        return null;
    }
}
