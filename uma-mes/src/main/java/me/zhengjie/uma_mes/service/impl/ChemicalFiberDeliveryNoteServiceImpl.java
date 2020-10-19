package me.zhengjie.uma_mes.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelStyleType;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import com.lgmn.common.result.Result;
import com.lgmn.common.utils.ObjectTransfer;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.uma_mes.domain.ChemicalFiberDeliveryDetail;
import me.zhengjie.uma_mes.domain.ChemicalFiberDeliveryNote;
import me.zhengjie.uma_mes.service.*;
import me.zhengjie.uma_mes.service.dto.*;
import me.zhengjie.uma_mes.utils.NumberToCN;
import me.zhengjie.utils.*;
import me.zhengjie.uma_mes.repository.ChemicalFiberDeliveryNoteRepository;
import me.zhengjie.uma_mes.service.mapper.ChemicalFiberDeliveryNoteMapper;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import javax.servlet.http.HttpServletResponse;

import static cn.afterturn.easypoi.excel.ExcelExportUtil.SHEET_NAME;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Service
@CacheConfig(cacheNames = "chemicalFiberDeliveryNote")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ChemicalFiberDeliveryNoteServiceImpl implements ChemicalFiberDeliveryNoteService {

    private final ChemicalFiberDeliveryNoteRepository chemicalFiberDeliveryNoteRepository;

    private final ChemicalFiberDeliveryNoteMapper chemicalFiberDeliveryNoteMapper;

    private final CustomerService customerService;

    private final ChemicalFiberDeliveryDetailService chemicalFiberDeliveryDetailService;

    private final ScanRecordService scanRecordService;

    private final ScanRecordLabelService scanRecordLabelService;

    private final ChemicalFiberLabelService chemicalFiberLabelService;

    public ChemicalFiberDeliveryNoteServiceImpl(ChemicalFiberDeliveryNoteRepository chemicalFiberDeliveryNoteRepository,
                                                ChemicalFiberDeliveryNoteMapper chemicalFiberDeliveryNoteMapper,
                                                CustomerService customerService,
                                                ChemicalFiberDeliveryDetailService chemicalFiberDeliveryDetailService,
                                                ScanRecordService scanRecordService,
                                                ScanRecordLabelService scanRecordLabelService,
                                                ChemicalFiberLabelService chemicalFiberLabelService) {
        this.chemicalFiberDeliveryNoteRepository = chemicalFiberDeliveryNoteRepository;
        this.chemicalFiberDeliveryNoteMapper = chemicalFiberDeliveryNoteMapper;
        this.customerService = customerService;
        this.chemicalFiberDeliveryDetailService = chemicalFiberDeliveryDetailService;
        this.scanRecordService = scanRecordService;
        this.scanRecordLabelService = scanRecordLabelService;
        this.chemicalFiberLabelService = chemicalFiberLabelService;
    }

    @Override
//    @Cacheable
    public Map<String,Object> queryAll(ChemicalFiberDeliveryNoteQueryCriteria criteria, Pageable pageable){
        if (criteria.getTempStartTime() != null) {
            criteria.setStartTime(new Timestamp(criteria.getTempStartTime()));
            criteria.setEndTime(new Timestamp(criteria.getTempEndTime()));
        }
        Page<ChemicalFiberDeliveryNote> page = chemicalFiberDeliveryNoteRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        List<ChemicalFiberDeliveryNote>  pageList = page.getContent();
        List<ChemicalFiberDeliveryNoteDTO> pageLists = chemicalFiberDeliveryNoteMapper.toDto(pageList);
        //ObjectTransfer.transValue(pageList, pageLists);
        List<Map<String, Object>> sum = chemicalFiberDeliveryNoteRepository.getSum();
        Map<String, Integer> sumBag = new HashMap<>();
        Map<String, BigDecimal> sumTotal = new HashMap<>();
        Map<String, BigDecimal> sumGrossWeight = new HashMap<>();
        for (Map<String, Object> sumDto : sum) {
            BigDecimal b = new BigDecimal(sumDto.get("total_bag").toString());
            Integer bag = b.intValue();
            sumBag.put(sumDto.get("note_id").toString(), bag);
            sumTotal.put(sumDto.get("note_id").toString(), new BigDecimal(sumDto.get("total_weight").toString()));
            if (sumDto.get("gross_weight") != null ) {
                sumGrossWeight.put(sumDto.get("note_id").toString(), new BigDecimal(sumDto.get("gross_weight").toString()));
            } else {
                sumGrossWeight.put(sumDto.get("note_id").toString(), new BigDecimal(0));
            }
        }
        List<ChemicalFiberDeliveryNoteDTO> pageDtoLists = new ArrayList<>();
        for (ChemicalFiberDeliveryNoteDTO dto : pageLists) {
            dto.setBag(sumBag.get(dto.getId().toString()));
            dto.setWeight(sumTotal.get(dto.getId().toString()));
            dto.setGrossWeight(sumGrossWeight.get(dto.getId().toString()));
            pageDtoLists.add(dto);
        }
       // return PageUtil.toPage(page.map(chemicalFiberDeliveryNoteMapper::toDto));
        return PageUtil.toPage(new PageImpl(pageDtoLists,pageable,page.getTotalElements()));
    }

    @Override
//    @Cacheable
    public List<ChemicalFiberDeliveryNoteDTO> queryAll(ChemicalFiberDeliveryNoteQueryCriteria criteria){
        if (criteria.getTempStartTime() != null) {
            criteria.setStartTime(new Timestamp(criteria.getTempStartTime()));
            criteria.setEndTime(new Timestamp(criteria.getTempEndTime()));
        }
        List<ChemicalFiberDeliveryNote>  pageList = chemicalFiberDeliveryNoteRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder));
        List<ChemicalFiberDeliveryNoteDTO> pageLists = chemicalFiberDeliveryNoteMapper.toDto(pageList);
        //ObjectTransfer.transValue(pageList, pageLists);
        List<Map<String, Object>> sum = chemicalFiberDeliveryNoteRepository.getSum();
        Map<String, Integer> sumBag = new HashMap<>();
        Map<String, BigDecimal> sumTotal = new HashMap<>();
        for (Map<String, Object> sumDto : sum) {
            BigDecimal b = new BigDecimal(sumDto.get("total_bag").toString());
            Integer bag = b.intValue();
            sumBag.put(sumDto.get("note_id").toString(), bag);
            sumTotal.put(sumDto.get("note_id").toString(), new BigDecimal(sumDto.get("total_weight").toString()));
        }
        List<ChemicalFiberDeliveryNoteDTO> pageDtoLists = new ArrayList<>();
        for (ChemicalFiberDeliveryNoteDTO dto : pageLists) {

            dto.setBag(sumBag.get(dto.getId().toString()));
            dto.setWeight(sumTotal.get(dto.getId().toString()));
            pageDtoLists.add(dto);
        }
        return pageDtoLists;
    }

    @Override
//    @Cacheable(key = "#p0")
    public ChemicalFiberDeliveryNoteDTO findById(Integer id) {
        ChemicalFiberDeliveryNote chemicalFiberDeliveryNote = chemicalFiberDeliveryNoteRepository.findById(id).orElseGet(ChemicalFiberDeliveryNote::new);
        ValidationUtil.isNull(chemicalFiberDeliveryNote.getId(),"ChemicalFiberDeliveryNote","id",id);
        return chemicalFiberDeliveryNoteMapper.toDto(chemicalFiberDeliveryNote);
    }

    @Override
//    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public ChemicalFiberDeliveryNoteDTO create(ChemicalFiberDeliveryNote resources) {
        return chemicalFiberDeliveryNoteMapper.toDto(chemicalFiberDeliveryNoteRepository.save(resources));
    }

    @Override
//    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(ChemicalFiberDeliveryNote resources) {
        CustomerDTO customerDTO = customerService.findById(resources.getCustomerId());
        ChemicalFiberDeliveryNote chemicalFiberDeliveryNote = chemicalFiberDeliveryNoteRepository.findById(resources.getId()).orElseGet(ChemicalFiberDeliveryNote::new);
        ValidationUtil.isNull( chemicalFiberDeliveryNote.getId(),"ChemicalFiberDeliveryNote","id",resources.getId());
        chemicalFiberDeliveryNote.copy(resources);
//        chemicalFiberDeliveryNote.setCreateDate(new Timestamp(System.currentTimeMillis()));
        chemicalFiberDeliveryNote.setCreateUser(SecurityUtils.getUsername());
        chemicalFiberDeliveryNote.setCustomerId(customerDTO.getId());
        chemicalFiberDeliveryNote.setCustomerCode(customerDTO.getCode());
        chemicalFiberDeliveryNote.setCustomerName(customerDTO.getName());
        chemicalFiberDeliveryNote.setCustomerAddress(customerDTO.getAddress());
        chemicalFiberDeliveryNote.setContactPhone(customerDTO.getContactPhone());
        chemicalFiberDeliveryNote.setContacts(customerDTO.getContacts());
        chemicalFiberDeliveryNoteRepository.save(chemicalFiberDeliveryNote);
    }

    @Override
//    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        chemicalFiberDeliveryNoteRepository.deleteById(id);
    }


    @Override
    public void download(List<ChemicalFiberDeliveryNoteDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ChemicalFiberDeliveryNoteDTO chemicalFiberDeliveryNote : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("出库单号", chemicalFiberDeliveryNote.getScanNumber());
            map.put("客户id", chemicalFiberDeliveryNote.getCustomerId());
            map.put("客户名称", chemicalFiberDeliveryNote.getCustomerName());
            map.put("客户编号", chemicalFiberDeliveryNote.getCustomerCode());
            map.put("客户地址", chemicalFiberDeliveryNote.getCustomerAddress());
            map.put("联系人", chemicalFiberDeliveryNote.getContacts());
            map.put("联系电话", chemicalFiberDeliveryNote.getContactPhone());
            map.put("总成本", chemicalFiberDeliveryNote.getTotalCost());
            map.put("总价", chemicalFiberDeliveryNote.getTotalPrice());
            map.put("备注", chemicalFiberDeliveryNote.getRemark());
            map.put("业务员", chemicalFiberDeliveryNote.getSeller());
            map.put("仓管员", chemicalFiberDeliveryNote.getStoreKeeper());
            map.put("制单日期", chemicalFiberDeliveryNote.getCreateDate());
            map.put("制单人", chemicalFiberDeliveryNote.getCreateUser());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public void deliveryNoteStoredProcedure(String scanNumber) {
        chemicalFiberDeliveryNoteRepository.deliveryNoteStoredProcedure(scanNumber);
    }

    @Override
    public void downloadDeliveryNote(Integer id, HttpServletResponse response) {
        ChemicalFiberDeliveryNote chemicalFiberDeliveryNote = chemicalFiberDeliveryNoteRepository.findById(id).orElseGet(ChemicalFiberDeliveryNote::new);
        ChemicalFiberDeliveryDetailQueryCriteria chemicalFiberDeliveryDetailQueryCriteria = new ChemicalFiberDeliveryDetailQueryCriteria();
        chemicalFiberDeliveryDetailQueryCriteria.setScanNumber(chemicalFiberDeliveryNote.getScanNumber());
        List<ChemicalFiberDeliveryDetailDTO> chemicalFiberDeliveryDetailDTOS = chemicalFiberDeliveryDetailService.queryAll(chemicalFiberDeliveryDetailQueryCriteria);
        String templatePath = new TemplateConfig("template/excel", TemplateConfig.ResourceMode.CLASSPATH).getPath() + "/delivery_temp.xls";
        // 加载模板
        TemplateExportParams params = new TemplateExportParams(templatePath);
        // 生成workbook 并导出
        Workbook workbook = null;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("customerName", chemicalFiberDeliveryNote.getCustomerName());
        map.put("customerAddress", chemicalFiberDeliveryNote.getCustomerAddress());
        map.put("contacts", chemicalFiberDeliveryNote.getContacts());
        map.put("contactPhone", chemicalFiberDeliveryNote.getContactPhone());
        map.put("scanNumber", chemicalFiberDeliveryNote.getScanNumber());
        map.put("createDate", chemicalFiberDeliveryNote.getCreateDate());
        map.put("total", chemicalFiberDeliveryNote.getTotalPrice());
        map.put("capitalizationTotal", NumberToCN.number2CNMontrayUnit(chemicalFiberDeliveryNote.getTotalPrice()));
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
        for (ChemicalFiberDeliveryDetailDTO chemicalFiberDeliveryDetailDTO : chemicalFiberDeliveryDetailDTOS) {
            Map<String, String> lm = new HashMap<String, String>();
            lm.put("prodName", chemicalFiberDeliveryDetailDTO.getProdName());
            lm.put("totalBag", chemicalFiberDeliveryDetailDTO.getTotalBag() + "");
            lm.put("totalNumber", "个".equals(chemicalFiberDeliveryDetailDTO.getUnit()) ? chemicalFiberDeliveryDetailDTO.getTotalNumber() + "" : chemicalFiberDeliveryDetailDTO.getTotalWeight() + "");
            lm.put("unit", chemicalFiberDeliveryDetailDTO.getUnit());
            lm.put("sellingPrice", chemicalFiberDeliveryDetailDTO.getSellingPrice() + "");
            lm.put("totalPrice", chemicalFiberDeliveryDetailDTO.getTotalPrice() + "");
            lm.put("remark", chemicalFiberDeliveryDetailDTO.getRemark());
            listMap.add(lm);
        }
        map.put("deliveryList", listMap);
        workbook = ExcelExportUtil.exportExcel(params, map);
        FileUtil.downLoadExcel("生产单导出.xlsx", response, workbook);
    }

    @Override
    public void exportPoundExcel(ChemicalFiberDeliveryNoteExportPoundExcelDto chemicalFiberDeliveryNoteExportPoundExcelDto, HttpServletResponse response) {
        ScanRecordQueryCriteria scanRecordQueryCriteria = new ScanRecordQueryCriteria();
        scanRecordQueryCriteria.setAccurateScanNumber(chemicalFiberDeliveryNoteExportPoundExcelDto.getScanNumber());
        List<ScanRecordDTO> scanRecordDTOS = scanRecordService.queryAll(scanRecordQueryCriteria);
        ScanRecordLabelQueryCriteria scanRecordLabelQueryCriteria = new ScanRecordLabelQueryCriteria();
        scanRecordLabelQueryCriteria.setScanRecordId(scanRecordDTOS.get(0).getId());
        List<ScanRecordLabelDTO> scanRecordLabelDTOS = scanRecordLabelService.queryAll(scanRecordLabelQueryCriteria);
        List<Integer> labelIds = new ArrayList<>();
        for (ScanRecordLabelDTO scanRecordLabelDTO : scanRecordLabelDTOS) {
            labelIds.add(scanRecordLabelDTO.getLabelId());
        }

        ChemicalFiberLabelQueryCriteria chemicalFiberLabelQueryCriteria = new ChemicalFiberLabelQueryCriteria();
        chemicalFiberLabelQueryCriteria.setId(labelIds);
        List<ChemicalFiberLabelDTO> chemicalFiberLabelDTOS = chemicalFiberLabelService.queryAll(chemicalFiberLabelQueryCriteria);
        List<ChemicalFiberLabelDTO> chemicalFiberLabelDTOSTemp = new ArrayList<>();
        for (ChemicalFiberLabelDTO chemicalFiberLabelDTO : chemicalFiberLabelDTOS) {
            if (chemicalFiberLabelDTO.getProductId().equals(chemicalFiberDeliveryNoteExportPoundExcelDto.getProdId())) {
                chemicalFiberLabelDTOSTemp.add(chemicalFiberLabelDTO);
            }
        }

        int tempIndex = 1;
        int sheelAllitem = 144;
        List<Map<String, Object>> list = new ArrayList<>();
        BigDecimal totalTotalWeights=new BigDecimal(0);
        int sheelPages = (chemicalFiberLabelDTOSTemp.size() + sheelAllitem) / sheelAllitem;
        for (int item = 1; item < sheelPages + 1; item++) {
            Map<String, Object> map = new HashMap<String, Object>();
            List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
            Map<String, String> row = initRow();
            BigDecimal rowTotal = new BigDecimal(0);
            BigDecimal total = new BigDecimal(0);
            BigDecimal totalWeight = new BigDecimal(0);
            for (int i = tempIndex; i < chemicalFiberLabelDTOSTemp.size()+1; i++) {
                total = total.add(new BigDecimal(1));
                totalWeight = totalWeight.add(chemicalFiberLabelDTOSTemp.get(i - 1).getNetWeight());
                Integer yu = i % 12;

                BigDecimal cellValue = chemicalFiberLabelDTOSTemp.get(i - 1).getNetWeight();
                rowTotal = rowTotal.add(cellValue);

                if (i % 12 == 0) {
                    row.put("12", cellValue.toString());
                    row.put("total", rowTotal.toString());
                    if (tempIndex == (sheelAllitem * item)) {
                        tempIndex++;
                        break;
                    } else {
                        listMap.add(row);
                        rowTotal = new BigDecimal(0);
                        row = initRow();
                    }
                } else {
                    row.put(yu.toString(), cellValue.toString());
                }
                tempIndex++;
            }
            row.put("total",rowTotal.toString());
            listMap.add(row);
            totalTotalWeights = totalTotalWeights.add(totalWeight);
            map.put("poundList", listMap);
            map.put("customerName", chemicalFiberDeliveryNoteExportPoundExcelDto.getCustomerName());
            map.put("prodName", chemicalFiberDeliveryNoteExportPoundExcelDto.getProdName());
            map.put("createDate", new Timestamp(chemicalFiberDeliveryNoteExportPoundExcelDto.getCreateDate()));
            map.put("total", total);
            map.put("totalWeight", totalWeight);
            map.put("currPage", item);
            map.put("totalPage", sheelPages);
            map.put("totalNumber", chemicalFiberLabelDTOSTemp.size());
//            map.put("weight", totalTotalWeights);
            map.put(SHEET_NAME,"第" + item + "页");
            list.add(map);
        }

        for (Map<String, Object> map : list) {
            map.put("weight", totalTotalWeights);
        }

        String templatePath = new TemplateConfig("template/excel", TemplateConfig.ResourceMode.CLASSPATH).getPath() + "/pound_temp.xls";
//        // 加载模板
        TemplateExportParams params = new TemplateExportParams(templatePath);
        params.setStyle(ExcelStyleType.BORDER.getClazz());
        Map<Integer, List<Map<String, Object>>> realMap = new HashMap<>();
        realMap.put(0, list);
        Workbook book     = ExcelExportUtil.exportExcelClone(realMap, params);
        FileUtil.downLoadExcel("磅码单导出.xls", response, book);
    }

    public List<List<ChemicalFiberLabelDTO>> genPoundTablePages(List<ChemicalFiberLabelDTO> list,Integer pageSize){
        List<List<ChemicalFiberLabelDTO>> result = new ArrayList<>();
        List<ChemicalFiberLabelDTO> page = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if(page.size()<pageSize){
                page.add(list.get(i));
            }else if(page.size()==pageSize){
                page.add(list.get(i));
                result.add(page);
                page = new ArrayList<>();
            }
            if(i == list.size()-1 && page.size()>0){
                result.add(page);
            }
        }
        return result;
    }

    private Map<String,String> initRow(){
        Map<String,String> result=new HashMap<>();
        for (int j = 1; j < 13; j++) {
            result.put(Integer.toString(j),"");
        }
        result.put("total","");
        return result;
    }

    @Override
    public Map<String, Object> getSalesReport(ChemicalFiberDeliveryNoteQueryCriteria criteria, Pageable pageable) {
        if (criteria.getTempStartTime() != null) {
            criteria.setEndTime(new Timestamp(criteria.getTempEndTime()));
            criteria.setStartTime(new Timestamp(criteria.getTempStartTime()));
        }

        List<ChemicalFiberDeliveryNoteSalesReportDTO> chemicalFiberDeliveryNoteSalesReportDTOS = new ArrayList<>();
        Page<ChemicalFiberDeliveryNote> page = chemicalFiberDeliveryNoteRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        for (ChemicalFiberDeliveryNote chemicalFiberDeliveryNote : page.getContent()) {
            ChemicalFiberDeliveryNoteSalesReportDTO chemicalFiberDeliveryNoteSalesReportDTO = new ChemicalFiberDeliveryNoteSalesReportDTO();
            ObjectTransfer.transValue(chemicalFiberDeliveryNote, chemicalFiberDeliveryNoteSalesReportDTO);
            for (ChemicalFiberDeliveryDetail chemicalFiberDeliveryDetail : chemicalFiberDeliveryNoteSalesReportDTO.getChemicalFiberDeliveryDetails()) {
                chemicalFiberDeliveryNoteSalesReportDTO.setOutOfStockPackageNumber(chemicalFiberDeliveryNoteSalesReportDTO.getOutOfStockPackageNumber() + chemicalFiberDeliveryDetail.getTotalBag());
                chemicalFiberDeliveryNoteSalesReportDTO.setOutOfStockFactPerBagNumber(chemicalFiberDeliveryNoteSalesReportDTO.getOutOfStockFactPerBagNumber() + (chemicalFiberDeliveryDetail.getTotalNumber() == null ? 0 : chemicalFiberDeliveryDetail.getTotalNumber()));
                chemicalFiberDeliveryNoteSalesReportDTO.setOutOfStockNetWeight(chemicalFiberDeliveryNoteSalesReportDTO.getOutOfStockNetWeight().add(chemicalFiberDeliveryDetail.getTotalWeight() == null ? new BigDecimal(0.0) : chemicalFiberDeliveryDetail.getTotalWeight()));
                chemicalFiberDeliveryNoteSalesReportDTO.setReceivablePrice(chemicalFiberDeliveryNoteSalesReportDTO.getReceivablePrice().add(chemicalFiberDeliveryDetail.getTotalPrice()));
                chemicalFiberDeliveryNoteSalesReportDTO.setTotalCost(chemicalFiberDeliveryNoteSalesReportDTO.getTotalCost().add(chemicalFiberDeliveryDetail.getTotalCost()));
            }
            chemicalFiberDeliveryNoteSalesReportDTOS.add(chemicalFiberDeliveryNoteSalesReportDTO);
        }
        return PageUtil.toPage(new PageImpl<ChemicalFiberDeliveryNoteSalesReportDTO>(chemicalFiberDeliveryNoteSalesReportDTOS, pageable, page.getTotalElements()));
    }

    @Override
    public Result getSalesReportSummaries(ChemicalFiberDeliveryNoteQueryCriteria criteria) {
        if (criteria.getTempStartTime() != null) {
            criteria.setEndTime(new Timestamp(criteria.getTempEndTime()));
            criteria.setStartTime(new Timestamp(criteria.getTempStartTime()));
        }
        List<ChemicalFiberDeliveryNote> chemicalFiberDeliveryNotes = chemicalFiberDeliveryNoteRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder));

        // 总成本
        BigDecimal totalCost = new BigDecimal(0.0);

        // 总金额
        BigDecimal totalPrice = new BigDecimal(0.0);

        // 总件数
        Integer totalBag = 0;

        // 总数量
        Integer totalNumber = 0;

        // 总重量
        BigDecimal totalWeight = new BigDecimal(0.0);

        for (ChemicalFiberDeliveryNote chemicalFiberDeliveryNote : chemicalFiberDeliveryNotes) {
            for (ChemicalFiberDeliveryDetail chemicalFiberDeliveryDetail : chemicalFiberDeliveryNote.getChemicalFiberDeliveryDetails()) {
                totalCost = totalCost.add(chemicalFiberDeliveryDetail.getTotalCost());
                totalPrice = totalPrice.add(chemicalFiberDeliveryDetail.getTotalPrice());
                totalBag = totalBag + (chemicalFiberDeliveryDetail.getTotalBag() == null ? 0 : chemicalFiberDeliveryDetail.getTotalBag());
                totalNumber = totalNumber + (chemicalFiberDeliveryDetail.getTotalNumber() == null ? 0 : chemicalFiberDeliveryDetail.getTotalNumber());
                totalWeight = totalWeight.add(chemicalFiberDeliveryDetail.getTotalWeight() == null ? new BigDecimal(0.0) : chemicalFiberDeliveryDetail.getTotalWeight());
            }
        }

        List<Object> list = new ArrayList<>();
        list.add("总计");
        list.add("");
        list.add("");
        list.add(totalBag);
        list.add(totalNumber);
        list.add(totalWeight);
        list.add(totalCost);
        list.add(totalPrice);
        list.add("");
        return Result.success(list);
    }

    @Override
    public Result getNoteSumm(ChemicalFiberDeliveryNoteQueryCriteria criteria) {
        if (criteria.getTempStartTime() != null) {
            criteria.setEndTime(new Timestamp(criteria.getTempEndTime()));
            criteria.setStartTime(new Timestamp(criteria.getTempStartTime()));
        }
        List<ChemicalFiberDeliveryNote> chemicalFiberDeliveryNotes = chemicalFiberDeliveryNoteRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder));

        // 总成本
        BigDecimal totalCost = new BigDecimal(0.0);

        // 总金额
        BigDecimal totalPrice = new BigDecimal(0.0);

        // 总件数
        Integer totalBag = 0;

        // 总数量
        Integer totalNumber = 0;

        // 总重量
        BigDecimal totalWeight = new BigDecimal(0.0);
        BigDecimal totalGrossWeight = new BigDecimal(0.0);

        for (ChemicalFiberDeliveryNote chemicalFiberDeliveryNote : chemicalFiberDeliveryNotes) {
            for (ChemicalFiberDeliveryDetail chemicalFiberDeliveryDetail : chemicalFiberDeliveryNote.getChemicalFiberDeliveryDetails()) {
                totalCost = totalCost.add(chemicalFiberDeliveryDetail.getTotalCost());
                totalPrice = totalPrice.add(chemicalFiberDeliveryDetail.getTotalPrice());
                totalBag = totalBag + (chemicalFiberDeliveryDetail.getTotalBag() == null ? 0 : chemicalFiberDeliveryDetail.getTotalBag());
                totalNumber = totalNumber + (chemicalFiberDeliveryDetail.getTotalNumber() == null ? 0 : chemicalFiberDeliveryDetail.getTotalNumber());
                totalWeight = totalWeight.add(chemicalFiberDeliveryDetail.getTotalWeight() == null ? new BigDecimal(0.0) : chemicalFiberDeliveryDetail.getTotalWeight());
                totalGrossWeight = totalGrossWeight.add(chemicalFiberDeliveryDetail.getGrossWeight() == null ? new BigDecimal(0.0) : chemicalFiberDeliveryDetail.getGrossWeight());
            }
        }

        List<Object> list = new ArrayList<>();
        list.add("总计");
        list.add("");
        list.add("");
        list.add(totalCost);
        list.add(totalPrice);
        list.add(totalBag);
        list.add(totalWeight);
        list.add(totalGrossWeight);
        return Result.success(list);
    }
}
