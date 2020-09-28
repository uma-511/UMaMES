package me.zhengjie.uma_mes.service.impl;

import com.lgmn.common.result.Result;
import me.zhengjie.uma_mes.domain.ChemicalFiberDeliveryDetail;
import me.zhengjie.uma_mes.repository.ChemicalFiberDeliveryDetailRepository;
import me.zhengjie.uma_mes.service.ChemicalFiberSalesListService;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberSalesDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberSalesListQueryCriteria;
import me.zhengjie.utils.PageUtil;
import org.hibernate.type.BigDecimalType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;

@Service
@CacheConfig(cacheNames = "chemicalFiberSalesList")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ChemicalFiberSalesListServiceImpl implements ChemicalFiberSalesListService {

    @Autowired
    private ChemicalFiberDeliveryDetailRepository chemicalFiberDeliveryDetailRepository;

    public Map<String,Object> queryAll(ChemicalFiberSalesListQueryCriteria criteria, Pageable pageable){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String data1 = simpleDateFormat.format(criteria.getTempStartTime());
        String data2 = simpleDateFormat.format(criteria.getTempEndTime());
        Pageable tempPageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize());
        Page<Map<String, Object>> page = chemicalFiberDeliveryDetailRepository.getSalesList(data1, data2, tempPageable);
        List<Map<String, Object>> list = page.getContent();
        List<ChemicalFiberSalesDTO> pageList = new ArrayList<>();
        for (Map<String, Object> dto : list) {
            ChemicalFiberSalesDTO sale = new ChemicalFiberSalesDTO();
            Timestamp time = Timestamp.valueOf(dto.get("delivery_date").toString());
            sale.setScanNumber(dto.get("scan_number").toString());
            sale.setCustomerName(dto.get("customer_name").toString());
            sale.setDeliveryDate(time);
            sale.setProdName(dto.get("prod_name").toString());
            sale.setUnit(dto.get("unit").toString());
            sale.setTotalNumber(new BigDecimal(dto.get("total_number").toString()));
            sale.setRealQuantity(new BigDecimal(dto.get("real_quantity").toString()));
            sale.setTotalPrice(new BigDecimal(dto.get("total_price").toString()));
            sale.setRealPrice(new BigDecimal(dto.get("real_price").toString()));
            sale.setSumPrice(new BigDecimal(0));
            sale.setSumNumber(new BigDecimal(0));
            int price = sale.getRealPrice().compareTo(sale.getTotalPrice()); //和0，ZERO比较
            if ( price == 1 ) {
                sale.setSumPrice(sale.getRealPrice().subtract(sale.getTotalPrice()));
            } else if ( price == -1 ) {
                sale.setSumPrice(sale.getTotalPrice().subtract(sale.getRealPrice()));
            }
            int number = sale.getTotalNumber().compareTo(sale.getRealQuantity()); //和0，ZERO比较
            if ( number == 1 ) {
                sale.setSumNumber(sale.getTotalNumber().subtract(sale.getRealQuantity()));
            } else if ( number == -1 ) {
                sale.setSumNumber(sale.getRealQuantity().subtract(sale.getTotalNumber()));
            }
            pageList.add(sale);
        }

        return PageUtil.toPage(new PageImpl(pageList, pageable, page.getTotalElements()));
    }

    public List<ChemicalFiberSalesDTO> queryAll(ChemicalFiberSalesListQueryCriteria criteria){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String data1 = simpleDateFormat.format(criteria.getTempStartTime());
        String data2 = simpleDateFormat.format(criteria.getTempEndTime());
        List<Map<String, Object>> list = chemicalFiberDeliveryDetailRepository.getSales(data1, data2);
        List<ChemicalFiberSalesDTO> pageList = new ArrayList<>();
        for (Map<String, Object> dto : list) {
            ChemicalFiberSalesDTO sale = new ChemicalFiberSalesDTO();
            Timestamp time = Timestamp.valueOf(dto.get("delivery_date").toString());
            sale.setScanNumber(dto.get("scan_number").toString());
            sale.setCustomerName(dto.get("customer_name").toString());
            sale.setDeliveryDate(time);
            sale.setProdName(dto.get("prod_name").toString());
            sale.setUnit(dto.get("unit").toString());
            sale.setTotalNumber(new BigDecimal(dto.get("total_number").toString()));
            sale.setRealQuantity(new BigDecimal(dto.get("real_quantity").toString()));
            sale.setTotalPrice(new BigDecimal(dto.get("total_price").toString()));
            sale.setRealPrice(new BigDecimal(dto.get("real_price").toString()));
            sale.setSumPrice(new BigDecimal(0));
            sale.setSumNumber(new BigDecimal(0));
            int price = sale.getRealPrice().compareTo(sale.getTotalPrice()); //和0，ZERO比较
            if ( price == 1 ) {
                sale.setSumPrice(sale.getRealPrice().subtract(sale.getTotalPrice()));
            } else if ( price == -1 ) {
                sale.setSumPrice(sale.getTotalPrice().subtract(sale.getRealPrice()));
            }
            int number = sale.getTotalNumber().compareTo(sale.getRealQuantity()); //和0，ZERO比较
            if ( number == 1 ) {
                sale.setSumNumber(sale.getTotalNumber().subtract(sale.getRealQuantity()));
            } else if ( number == -1 ) {
                sale.setSumNumber(sale.getRealQuantity().subtract(sale.getTotalNumber()));
            }
            pageList.add(sale);
        }

        return pageList;
    }


    public Result getSalesSummaries(ChemicalFiberSalesListQueryCriteria criteria) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String data1 = simpleDateFormat.format(criteria.getTempStartTime());
        String data2 = simpleDateFormat.format(criteria.getTempEndTime());
        List<ChemicalFiberSalesDTO> salesList = queryAll(criteria);
        String tonAndBarch;

        BigDecimal ton = new BigDecimal(0.0);

        BigDecimal barch = new BigDecimal(0.0);

        BigDecimal totalNumber = new BigDecimal(0.0);

        BigDecimal realQuantity = new BigDecimal(0.0);

        BigDecimal totalPrice = new BigDecimal(0.0);

        BigDecimal realPrice = new BigDecimal(0.0);

        BigDecimal sumNumber = new BigDecimal(0.0);

        BigDecimal sumPrice = new BigDecimal(0.0);

        for (ChemicalFiberSalesDTO dto : salesList) {
            if (dto.getUnit().equals("吨")) {
                ton = ton.add(dto.getSumNumber());
            } else if (dto.getUnit().equals("支")) {
                barch = barch.add(dto.getSumNumber());
            }
            totalNumber = totalNumber.add(dto.getTotalNumber());
            realQuantity = realQuantity.add(dto.getRealQuantity());
            totalPrice = totalPrice.add(dto.getTotalPrice());
            realPrice = realPrice.add(dto.getRealPrice());
            sumPrice = sumPrice.add(dto.getSumPrice());
        }
        tonAndBarch = ton.toString() + "吨/" + barch.toString() + "支";
        List<Object> list = new ArrayList<>();

        list.add("合计");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add(totalNumber);
        list.add(realQuantity);
        list.add(tonAndBarch);
        list.add(totalPrice);
        list.add(realPrice);
        list.add(sumPrice);
        return Result.success(list);


    }
}
