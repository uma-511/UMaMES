package me.zhengjie.uma_mes.service.impl;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.hutool.extra.template.TemplateConfig;
import com.lgmn.common.result.Result;
import me.zhengjie.uma_mes.domain.ChemicalFiberStockWarehousing;
import me.zhengjie.uma_mes.domain.ChemicalFiberStockWarehousingDetail;
import me.zhengjie.uma_mes.repository.ChemicalFiberStockWarehousingDetailRepository;
import me.zhengjie.uma_mes.repository.ChemicalFiberStockWarehousingRepository;
import me.zhengjie.uma_mes.service.ChemicalFiberWarehousingReortService;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberStockWarehousingQueryCriteria;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberWarehousingReortDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberWarehousingReortQueryCriteria;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
            if (warehousing.getId() != null) {
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

    public List<Map<String, Object>> queryAlls(ChemicalFiberWarehousingReortQueryCriteria criteria){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date a = new Date(criteria.getTempStartTime());
        Date b = new Date(criteria.getTempEndTime());
        String data1 = simpleDateFormat.format(a);
        String data2 = simpleDateFormat.format(b);

        List<Map<String, Object>> reort = chemicalFiberStockWarehousingRepository.getReort(data1, data2,
                StringUtils.isEmpty(criteria.getScanNumber()) ? "" : criteria.getScanNumber(),
                StringUtils.isEmpty(criteria.getProdName()) ? "" : criteria.getProdName(),
                StringUtils.isEmpty(criteria.getCreateUser()) ? "" : criteria.getCreateUser(),
                StringUtils.isEmpty(criteria.getSupplierName()) ? "" : criteria.getSupplierName()
                );

        //List<ChemicalFiberWarehousingReortDTO> Warehousing = new ArrayList<>();
        /*for (Map<String, Object> dto : reort) {
            ChemicalFiberWarehousingReortDTO reortDto = new ChemicalFiberWarehousingReortDTO();
            reortDto.setWarehousingDate(dto.get("warehousing_date"));
            reortDto.setWarehousingNumber(dto.get("warehousing_number"));
            reortDto.setSupplierName(dto.get("supplier_name"));
            reortDto.setProdName(dto.get("prod_name"));
            reortDto.setUnit(dto.get("unit"));
            reortDto.setPrice(dto.get("price"));
            reortDto.setTotalPrice(dto.get("total_price"));
            reortDto.setCreateUser(dto.get("create_user"));
            reortDto.setCreateDate(dto.get("create_date"));
            Warehousing.add(reortDto);
        }*/
        return reort;
    }

    public void download(List<Map<String, Object>> all,ChemicalFiberWarehousingReortQueryCriteria criteria, HttpServletResponse response) {
        String templatePath = new TemplateConfig("template/excel", TemplateConfig.ResourceMode.CLASSPATH).getPath() + "/roet.xls";
        TemplateExportParams params = new TemplateExportParams(templatePath);
        /*params.setReadonly(Boolean.TRUE);*/
        Workbook workbook = null;
        Map<String, Object> map = new HashMap();
        BigDecimal sum = new BigDecimal(0);
        BigDecimal sumTon = new BigDecimal(0);
        BigDecimal sumBarch = new BigDecimal(0);
        BigDecimal sumTotalPrice = new BigDecimal(0);
        List<Map<String, Object>> listMap = new ArrayList<>();
        for (int i = 1; i <= all.size(); i++) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Map<String, Object> in = new HashMap<>();
            Map<String, Object> on = new HashMap<>();
            in = all.get(i - 1);
            on.put("index", i);
            on.put("warehousing_date", simpleDateFormat.format(in.get("warehousing_date")));
            on.put("warehousing_number", in.get("warehousing_number"));
            on.put("supplier_name", in.get("supplier_name"));
            on.put("scan_number", in.get("scan_number"));
            on.put("prod_name", in.get("prod_name"));
            on.put("unit", in.get("unit"));
            on.put("price", in.get("price"));
            on.put("total_price", in.get("total_price"));
            on.put("create_user", in.get("create_user"));
            on.put("create_date", simpleDateFormat.format(in.get("create_date")));
            if (in.get("unit").equals("吨")) {
                sumTon = sumTon.add(new BigDecimal(in.get("warehousing_number").toString()));
            } else if (in.get("unit").equals("支")) {
                sumBarch = sumBarch.add(new BigDecimal(in.get("warehousing_number").toString()));
            }
            sumTotalPrice = sumTotalPrice.add(new BigDecimal(in.get("total_price").toString()));

            listMap.add(on);
        }
        String sumTonAndBarch = sumTon.toString() + "吨/" + sumBarch.toString() + "支";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date a = new Date(criteria.getTempStartTime());
        Date b = new Date(criteria.getTempEndTime());
        String data1 = simpleDateFormat.format(a);
        String data2 = simpleDateFormat.format(b);

        SimpleDateFormat fir = new SimpleDateFormat("yyyy-MM");
        Date c = new Date(criteria.getTempStartTime());
        String data3 = fir.format(c);
        map.put("list", listMap);
        map.put("tonAndBarch", sumTonAndBarch);
        map.put("sumTotalPrice", sumTotalPrice);
        map.put("startDateMum", data3);
        map.put("startDate", data1);
        map.put("endDate", data2);


        workbook = ExcelExportUtil.exportExcel(params, map);
        FileUtil.downLoadExcel("导出.xlsx", response, workbook);

    }

    public Result getSummaryData(ChemicalFiberWarehousingReortQueryCriteria criteria){
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
            if (warehousing.getId() != null) {
                sumTotalPrice = sumTotalPrice.add(dto.getTotalPrice());
                if (dto.getUnit().equals("吨")) {
                    sumTon = sumTon.add(dto.getWarehousingNumber());
                } else {
                    sum = sum.add(dto.getWarehousingNumber());
                }
            }
        }
        String tonAndBarch = sumTon.toString() + "吨/" + sum.toString() + "支";
        List<Object> lists = new ArrayList<>();
        lists.add("合计");
        lists.add("");
        lists.add("");
        lists.add("");
        lists.add("");
        lists.add("");
        lists.add(tonAndBarch);
        lists.add("");
        lists.add(sumTotalPrice);
       /* map.put("sumTotalPrice",sumTotalPrice);
        map.put("sumTon",sumTon);
        map.put("sum",sum);*/
        return Result.success(lists);
    }




    public ChemicalFiberStockWarehousing indexWarehousingId(List<ChemicalFiberStockWarehousing> list,Integer warehousingId) {
        ChemicalFiberStockWarehousing is = new ChemicalFiberStockWarehousing();
        for (int j = 0; j < list.size(); j++) {
            Integer a = list.get(j).getId();
            if (Objects.equals(a, warehousingId)) {
                is = list.get(j);
                return is;
            }
        }
        return is;
    }
}
