package me.zhengjie.uma_mes.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.hutool.extra.template.TemplateConfig;
import com.lgmn.common.result.Result;
import com.lgmn.common.utils.ObjectTransfer;
import me.zhengjie.uma_mes.domain.ChemicalFiberDeliveryDetail;
import me.zhengjie.uma_mes.domain.ChemicalFiberDeliveryNote;
import me.zhengjie.uma_mes.domain.UmaChemicalFiberStatement;
import me.zhengjie.uma_mes.domain.UmaChemicalFiberStatementDetails;
import me.zhengjie.uma_mes.repository.ChemicalFiberDeliveryNoteRepository;
import me.zhengjie.uma_mes.service.ChemicalFiberDeliveryDetailService;
import me.zhengjie.uma_mes.service.ChemicalFiberDeliveryNoteService;
import me.zhengjie.uma_mes.service.UmaChemicalFiberStatementDetailsService;
import me.zhengjie.uma_mes.service.dto.*;
import me.zhengjie.uma_mes.service.dto.statement.CreateStatementDto;
import me.zhengjie.uma_mes.service.dto.statement.StatementDetailsAllListDto;
import me.zhengjie.uma_mes.service.dto.statement.StatementDetailsListDto;
import me.zhengjie.uma_mes.utils.NumberToCN;
import me.zhengjie.utils.*;
import me.zhengjie.uma_mes.repository.UmaChemicalFiberStatementRepository;
import me.zhengjie.uma_mes.service.UmaChemicalFiberStatementService;
import me.zhengjie.uma_mes.service.mapper.UmaChemicalFiberStatementMapper;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author Xie Ji Biao
* @date 2020-03-11
*/
@Service
@CacheConfig(cacheNames = "umaChemicalFiberStatement")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UmaChemicalFiberStatementServiceImpl implements UmaChemicalFiberStatementService {

    private final UmaChemicalFiberStatementRepository umaChemicalFiberStatementRepository;

    private final UmaChemicalFiberStatementMapper umaChemicalFiberStatementMapper;

    private final ChemicalFiberDeliveryNoteService chemicalFiberDeliveryNoteService;

    private final ChemicalFiberDeliveryDetailService chemicalFiberDeliveryDetailService;

    private final UmaChemicalFiberStatementDetailsService umaChemicalFiberStatementDetailsService;

    private final ChemicalFiberDeliveryNoteRepository chemicalFiberDeliveryNoteRepository;

    public UmaChemicalFiberStatementServiceImpl(UmaChemicalFiberStatementRepository umaChemicalFiberStatementRepository,
                                                UmaChemicalFiberStatementMapper umaChemicalFiberStatementMapper,
                                                ChemicalFiberDeliveryNoteService chemicalFiberDeliveryNoteService,
                                                ChemicalFiberDeliveryDetailService chemicalFiberDeliveryDetailService,
                                                UmaChemicalFiberStatementDetailsService umaChemicalFiberStatementDetailsService,
                                                ChemicalFiberDeliveryNoteRepository chemicalFiberDeliveryNoteRepository) {
        this.umaChemicalFiberStatementRepository = umaChemicalFiberStatementRepository;
        this.umaChemicalFiberStatementMapper = umaChemicalFiberStatementMapper;
        this.chemicalFiberDeliveryNoteService = chemicalFiberDeliveryNoteService;
        this.chemicalFiberDeliveryDetailService = chemicalFiberDeliveryDetailService;
        this.umaChemicalFiberStatementDetailsService = umaChemicalFiberStatementDetailsService;
        this.chemicalFiberDeliveryNoteRepository = chemicalFiberDeliveryNoteRepository;
    }

    @Override
//    @Cacheable
    public Map<String,Object> queryAll(UmaChemicalFiberStatementQueryCriteria criteria, Pageable pageable){
        //Page<UmaChemicalFiberStatement> page = umaChemicalFiberStatementRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        Long createDate = criteria.getCreateDate();
        Date date = new Date(createDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Integer year = calendar.get(Calendar.YEAR);
        Integer month = calendar.get(Calendar.MONTH) + 2;
        String months = "";
        String days = "";
        String dateTime = "";
        if (month < 10) {
            months = "0" + month;
        } else {
            months = month + "";
        }
        int start = pageable.getPageSize();
        int end = pageable.getPageNumber();
        int PageNumber = start * end;
        Map<String, Object> map = new HashMap<>();
        List<UmaChemicalFiberStatement> pages = umaChemicalFiberStatementRepository.findadd(PageNumber, start, year + "-" + months);
        Integer ListSeiz = umaChemicalFiberStatementRepository.findSize();
        map.put("content", pages);
        map.put("totalElements", ListSeiz);
        // Page<UmaChemicalFiberStatement> pages = umaChemicalFiberStatementRepository.findadd();
        return map;
        //return PageUtil.toPage(page.map(umaChemicalFiberStatementMapper::toDto));
    }

    @Override
    @Cacheable
    public List<UmaChemicalFiberStatementDTO> queryAll(UmaChemicalFiberStatementQueryCriteria criteria){
        return umaChemicalFiberStatementMapper.toDto(umaChemicalFiberStatementRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public UmaChemicalFiberStatementDTO findById(Integer id) {
        UmaChemicalFiberStatement umaChemicalFiberStatement = umaChemicalFiberStatementRepository.findById(id).orElseGet(UmaChemicalFiberStatement::new);
        ValidationUtil.isNull(umaChemicalFiberStatement.getId(),"UmaChemicalFiberStatement","id",id);
        return umaChemicalFiberStatementMapper.toDto(umaChemicalFiberStatement);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public UmaChemicalFiberStatementDTO create1(CreateStatementDto createStatementDto) {
        Calendar now = Calendar.getInstance();
        createStatementDto.getUmaChemicalFiberStatement().setCreateUser(SecurityUtils.getUsername());
        createStatementDto.getUmaChemicalFiberStatement().setCreateDate(new Timestamp(System.currentTimeMillis()));
        createStatementDto.getUmaChemicalFiberStatement().setAccountCode(
                        "AC" +
                        now.get(Calendar.YEAR) +
                        (now.get(Calendar.MONTH) + 1) + "" +
                        now.get(Calendar.DAY_OF_MONTH) +
                        now.get(Calendar.HOUR_OF_DAY) +
                        now.get(Calendar.MINUTE) +
                        now.get(Calendar.SECOND) +
                        (now.getTimeInMillis() + "").substring(8));
        createStatementDto.getUmaChemicalFiberStatement().setReceivable(new BigDecimal(0));
        createStatementDto.getUmaChemicalFiberStatement().setAccumulatedArrears(new BigDecimal(0));
        createStatementDto.getUmaChemicalFiberStatement().setTotalArrears(new BigDecimal(0));
        UmaChemicalFiberStatementDTO chemicalFiberStatementDTO = umaChemicalFiberStatementMapper.toDto(umaChemicalFiberStatementRepository.save(createStatementDto.getUmaChemicalFiberStatement()));

        List<ChemicalFiberDeliveryDetailDTO> chemicalFiberDeliveryDetailDTOS = getStatementDetailsAllList(createStatementDto);
        for (ChemicalFiberDeliveryDetailDTO chemicalFiberDeliveryDetailDTO : chemicalFiberDeliveryDetailDTOS) {
            UmaChemicalFiberStatementDetails umaChemicalFiberStatementDetails = new UmaChemicalFiberStatementDetails();
            umaChemicalFiberStatementDetails.setStatementId(chemicalFiberStatementDTO.getId());
            umaChemicalFiberStatementDetails.setScanNumber(chemicalFiberDeliveryDetailDTO.getScanNumber());
            umaChemicalFiberStatementDetails.setScanDate(chemicalFiberDeliveryDetailDTO.getCreateDate());
            umaChemicalFiberStatementDetails.setProdName(chemicalFiberDeliveryDetailDTO.getProdName());
            umaChemicalFiberStatementDetails.setTotalBag(chemicalFiberDeliveryDetailDTO.getTotalBag());
            umaChemicalFiberStatementDetails.setNetWeight(chemicalFiberDeliveryDetailDTO.getTotalWeight());
            umaChemicalFiberStatementDetails.setSellingPrice(chemicalFiberDeliveryDetailDTO.getSellingPrice());
            umaChemicalFiberStatementDetails.setTotalPrice(chemicalFiberDeliveryDetailDTO.getTotalPrice());
            umaChemicalFiberStatementDetails.setRemark(chemicalFiberDeliveryDetailDTO.getRemark());
            umaChemicalFiberStatementDetails.setAdvanceCharge(new BigDecimal(0));
            umaChemicalFiberStatementDetails.setAmountDeducted(new BigDecimal(0));
            umaChemicalFiberStatementDetailsService.create(umaChemicalFiberStatementDetails);
        }
        return chemicalFiberStatementDTO;
    }

    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void create(Integer id) {
        ChemicalFiberDeliveryNote note = chemicalFiberDeliveryNoteRepository.findById(id).orElseGet(ChemicalFiberDeliveryNote::new);
        Date date = note.getDeliveryDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Integer year = calendar.get(Calendar.YEAR);
        Integer month = calendar.get(Calendar.MONTH) + 1;
        String months = "";
        String dateTime = "";
        if (month < 10) {
            months = "0" + month;
        } else {
            months = month + "";
        }
        UmaChemicalFiberStatement statement = umaChemicalFiberStatementRepository.getOneId(note.getCustomerId(), year + "-" + months);

    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(UmaChemicalFiberStatement resources) {
        UmaChemicalFiberStatement umaChemicalFiberStatement = umaChemicalFiberStatementRepository.findById(resources.getId()).orElseGet(UmaChemicalFiberStatement::new);
        ValidationUtil.isNull( umaChemicalFiberStatement.getId(),"UmaChemicalFiberStatement","id",resources.getId());
        umaChemicalFiberStatement.copy(resources);
        umaChemicalFiberStatementRepository.save(umaChemicalFiberStatement);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        umaChemicalFiberStatementRepository.deleteById(id);
        UmaChemicalFiberStatementDetailsQueryCriteria umaChemicalFiberStatementDetailsQueryCriteria = new UmaChemicalFiberStatementDetailsQueryCriteria();
        umaChemicalFiberStatementDetailsQueryCriteria.setStatementId(id);
        List<UmaChemicalFiberStatementDetailsDTO> umaChemicalFiberStatementDetailsDTOS = umaChemicalFiberStatementDetailsService.queryAll(umaChemicalFiberStatementDetailsQueryCriteria);
        for (UmaChemicalFiberStatementDetailsDTO umaChemicalFiberStatementDetailsDTO : umaChemicalFiberStatementDetailsDTOS) {
            umaChemicalFiberStatementDetailsService.delete(umaChemicalFiberStatementDetailsDTO.getId());
        }
    }


    @Override
    public void download(List<UmaChemicalFiberStatementDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UmaChemicalFiberStatementDTO umaChemicalFiberStatement : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("对账单号", umaChemicalFiberStatement.getAccountCode());
            map.put("创建日期", umaChemicalFiberStatement.getCreateDate());
            map.put("创建人", umaChemicalFiberStatement.getCreateUser());
            map.put("客户ID", umaChemicalFiberStatement.getCustomerId());
            map.put("客户名称", umaChemicalFiberStatement.getCustomerName());
            map.put("客户联系人", umaChemicalFiberStatement.getContacts());
            map.put("客户联系电话", umaChemicalFiberStatement.getContactPhone());
            map.put("应收金额", umaChemicalFiberStatement.getReceivable());
            map.put("上期欠款", umaChemicalFiberStatement.getAccumulatedArrears());
            map.put("总欠金额", umaChemicalFiberStatement.getTotalArrears());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public Map<String, Object> getStatementDetailsList(StatementDetailsListDto statementDetailsListDto) {
        Map<String, Timestamp> scanNumberTempMap = new HashMap<>();
        List<String> scanNumbers = new ArrayList<>();
        ChemicalFiberDeliveryNoteQueryCriteria chemicalFiberDeliveryNoteQueryCriteria = new ChemicalFiberDeliveryNoteQueryCriteria();
        chemicalFiberDeliveryNoteQueryCriteria.setCustomerId(statementDetailsListDto.getCustomerId());
        chemicalFiberDeliveryNoteQueryCriteria.setStartTime(new Timestamp(statementDetailsListDto.getTempStartTime()));
        chemicalFiberDeliveryNoteQueryCriteria.setEndTime(new Timestamp(statementDetailsListDto.getTempEndTime()));
        List<ChemicalFiberDeliveryNoteDTO> chemicalFiberDeliveryNoteDTOs = chemicalFiberDeliveryNoteService.queryAll(chemicalFiberDeliveryNoteQueryCriteria);
        for (ChemicalFiberDeliveryNoteDTO chemicalFiberDeliveryNoteDTO : chemicalFiberDeliveryNoteDTOs) {
            scanNumbers.add(chemicalFiberDeliveryNoteDTO.getScanNumber());
            scanNumberTempMap.put(chemicalFiberDeliveryNoteDTO.getScanNumber(), chemicalFiberDeliveryNoteDTO.getCreateDate());
        }

        if (scanNumbers.size() <= 0) {
            Map<String, Object> temp = new HashMap<>();
            temp.put("content", new ArrayList<>());
            temp.put("totalElements", 0);
            return temp;
        }

        ChemicalFiberDeliveryDetailQueryCriteria chemicalFiberDeliveryDetailQueryCriteria = new ChemicalFiberDeliveryDetailQueryCriteria();
        chemicalFiberDeliveryDetailQueryCriteria.setScanNumbers(scanNumbers);
        Pageable pageable = PageRequest.of(statementDetailsListDto.getPageNumber(), statementDetailsListDto.getPageSize());
        Map<String,Object> chemicalFiberDeliveryDetailMap = chemicalFiberDeliveryDetailService.queryStatementDetailsAll(chemicalFiberDeliveryDetailQueryCriteria, pageable);
        List<ChemicalFiberDeliveryDetailDTO> tempList = (List<ChemicalFiberDeliveryDetailDTO>)chemicalFiberDeliveryDetailMap.get("content");
        for (ChemicalFiberDeliveryDetailDTO chemicalFiberDeliveryDetailDTO : tempList) {
            chemicalFiberDeliveryDetailDTO.setCreateDate(scanNumberTempMap.get(chemicalFiberDeliveryDetailDTO.getScanNumber()));
        }
        chemicalFiberDeliveryDetailMap.put("content", tempList);
        return chemicalFiberDeliveryDetailMap;
    }

    @Override
    public Map<String, Object> getStatementDetailsAllList(StatementDetailsAllListDto statementDetailsAllListDto) {
        UmaChemicalFiberStatementDetailsQueryCriteria umaChemicalFiberStatementDetailsQueryCriteria = new UmaChemicalFiberStatementDetailsQueryCriteria();
        umaChemicalFiberStatementDetailsQueryCriteria.setStatementId(statementDetailsAllListDto.getStatementId());
        if (statementDetailsAllListDto.getTempEndTime() != null) {
            umaChemicalFiberStatementDetailsQueryCriteria.setStartTime(new Timestamp(statementDetailsAllListDto.getTempStartTime()));
            umaChemicalFiberStatementDetailsQueryCriteria.setEndTime(new Timestamp(statementDetailsAllListDto.getTempEndTime()));
        }
        Pageable pageable = PageRequest.of(statementDetailsAllListDto.getPageNumber(), statementDetailsAllListDto.getPageSize());
        return umaChemicalFiberStatementDetailsService.queryAll(umaChemicalFiberStatementDetailsQueryCriteria, pageable);
    }

    @Override
    public Result getSums(StatementDetailsAllListDto statementDetailsAllListDto) {
        UmaChemicalFiberStatementDetailsQueryCriteria umaChemicalFiberStatementDetailsQueryCriteria = new UmaChemicalFiberStatementDetailsQueryCriteria();
        umaChemicalFiberStatementDetailsQueryCriteria.setStatementId(statementDetailsAllListDto.getStatementId());
        List<UmaChemicalFiberStatementDetailsDTO> umaChemicalFiberStatementDetailsDTOS = umaChemicalFiberStatementDetailsService.queryAll(umaChemicalFiberStatementDetailsQueryCriteria);
        Integer sumTotalBag = 0;
        BigDecimal sumNetWeight = new BigDecimal(0);
        BigDecimal sumTotalPrice = new BigDecimal(0);
        BigDecimal sumAdvanceCharge = new BigDecimal(0);
        BigDecimal sumAmountDeducted = new BigDecimal(0);

        for (UmaChemicalFiberStatementDetailsDTO umaChemicalFiberStatementDetailsDTO : umaChemicalFiberStatementDetailsDTOS) {
            sumTotalBag = sumTotalBag + umaChemicalFiberStatementDetailsDTO.getTotalBag();
            //sumNetWeight = sumNetWeight.add(umaChemicalFiberStatementDetailsDTO.getNetWeight());
            sumTotalPrice = sumTotalPrice.add(umaChemicalFiberStatementDetailsDTO.getTotalPrice());
            //sumAdvanceCharge = sumAdvanceCharge.add(umaChemicalFiberStatementDetailsDTO.getAdvanceCharge());
            //sumAmountDeducted = sumAmountDeducted.add(umaChemicalFiberStatementDetailsDTO.getAmountDeducted());
        }
        List<Object> tempList = new ArrayList<>();
        tempList.add("合计");
        tempList.add("");
        tempList.add("");
        tempList.add("");
        tempList.add(sumTotalBag);
        tempList.add("");
        tempList.add(sumTotalPrice);
        tempList.add("");
        List<Timestamp> cycleDate = new ArrayList<>();
        cycleDate.add(umaChemicalFiberStatementDetailsDTOS.get(0).getScanDate());
        cycleDate.add(umaChemicalFiberStatementDetailsDTOS.get(umaChemicalFiberStatementDetailsDTOS.size() - 1).getScanDate());
        Map<String, Object> map = new HashMap<>();
        map.put("sums", tempList);
        map.put("cycleDate", cycleDate);
        return Result.success(map);
    }

    @Override
    public void exportStatement(HttpServletResponse response, Integer id) {
        UmaChemicalFiberStatement umaChemicalFiberStatement = umaChemicalFiberStatementRepository.findById(id).orElseGet(UmaChemicalFiberStatement::new);
        UmaChemicalFiberStatementDetailsQueryCriteria umaChemicalFiberStatementDetailsQueryCriteria = new UmaChemicalFiberStatementDetailsQueryCriteria();
        umaChemicalFiberStatementDetailsQueryCriteria.setStatementId(umaChemicalFiberStatement.getId());
        List<UmaChemicalFiberStatementDetailsDTO> umaChemicalFiberStatementDetailsDTOS = umaChemicalFiberStatementDetailsService.queryAll(umaChemicalFiberStatementDetailsQueryCriteria);
        String templatePath = new TemplateConfig("template/excel", TemplateConfig.ResourceMode.CLASSPATH).getPath() + "/statement.xls";
        // 加载模板
        TemplateExportParams params = new TemplateExportParams(templatePath);
        // 生成workbook 并导出
        Workbook workbook = null;
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
        BigDecimal totalSum = new BigDecimal(0);
        BigDecimal totalAdvanceCharge = new BigDecimal(0);
        BigDecimal totalAmountDeducted = new BigDecimal(0);
        BigDecimal sumNetWeight = new BigDecimal(0);
        int sumTotalBag = 0;
        for (int i = 0; i < umaChemicalFiberStatementDetailsDTOS.size(); i++) {
            Map<String, String> lm = new HashMap<String, String>();
            lm.put("index", (i + 1) + "");
            lm.put("scanDate", (umaChemicalFiberStatementDetailsDTOS.get(i).getScanDate() + "").substring(0, 10));
            lm.put("scanNumber", umaChemicalFiberStatementDetailsDTOS.get(i).getScanNumber());
            lm.put("prodName", umaChemicalFiberStatementDetailsDTOS.get(i).getProdName());
            lm.put("totalBag", umaChemicalFiberStatementDetailsDTOS.get(i).getTotalBag() + "");
            lm.put("netWeight", umaChemicalFiberStatementDetailsDTOS.get(i).getNetWeight() + "");
            lm.put("sellingPrice", umaChemicalFiberStatementDetailsDTOS.get(i).getSellingPrice() + "");
            lm.put("totalPrice", compareToBigDecimal(umaChemicalFiberStatementDetailsDTOS.get(i).getTotalPrice()));
            lm.put("advanceCharge", compareToBigDecimal(umaChemicalFiberStatementDetailsDTOS.get(i).getAdvanceCharge()));
            lm.put("amountDeducted", compareToBigDecimal(umaChemicalFiberStatementDetailsDTOS.get(i).getAmountDeducted()));
            lm.put("remark", umaChemicalFiberStatementDetailsDTOS.get(i).getRemark());
            listMap.add(lm);
            totalSum = totalSum.add(umaChemicalFiberStatementDetailsDTOS.get(i).getTotalPrice());
            totalAdvanceCharge = totalAdvanceCharge.add(umaChemicalFiberStatementDetailsDTOS.get(i).getAdvanceCharge());
            totalAmountDeducted = totalAmountDeducted.add(umaChemicalFiberStatementDetailsDTOS.get(i).getAmountDeducted());
            sumNetWeight = sumNetWeight.add(umaChemicalFiberStatementDetailsDTOS.get(i).getNetWeight());
            sumTotalBag = sumTotalBag + umaChemicalFiberStatementDetailsDTOS.get(i).getTotalBag();
        }
        Timestamp starDate = umaChemicalFiberStatementDetailsDTOS.get(0).getScanDate();
        Timestamp endDate = umaChemicalFiberStatementDetailsDTOS.get(umaChemicalFiberStatementDetailsDTOS.size() - 1).getScanDate();
        String starDateStr = (starDate + "").substring(0, 10);
        String endDateStr = (endDate + "").substring(0, 10);
        map.put("contacts", umaChemicalFiberStatement.getContacts());
        map.put("customerName", umaChemicalFiberStatement.getCustomerName());
        map.put("contactPhone", umaChemicalFiberStatement.getContactPhone());
        map.put("accountCode", umaChemicalFiberStatement.getAccountCode());
        map.put("cycle", starDateStr + "--" + endDateStr);
        map.put("capitTotal", NumberToCN.number2CNMontrayUnit(umaChemicalFiberStatement.getReceivable()));
        map.put("total", umaChemicalFiberStatement.getReceivable() + "");
        map.put("starDate", getLastDay(starDate.getTime()));
        map.put("onCredit", umaChemicalFiberStatement.getAccumulatedArrears() + "");
        map.put("endDate", endDateStr);
        map.put("sumTotal", umaChemicalFiberStatement.getTotalArrears() + ""); // 总欠款
        map.put("totalSum", totalSum + ""); // 总金额
        map.put("totalAdvanceCharge", compareToBigDecimal(totalAdvanceCharge));
        map.put("totalAmountDeducted", compareToBigDecimal(totalAmountDeducted));
        map.put("statementList", listMap);
        map.put("sumTotalBag", sumTotalBag);
        map.put("sumNetWeight", sumNetWeight);
        workbook = ExcelExportUtil.exportExcel(params, map);
        FileUtil.downLoadExcel("对账单导出.xlsx", response, workbook);
    }

    private List<ChemicalFiberDeliveryDetailDTO> getStatementDetailsAllList(CreateStatementDto createStatementDto) {
        Map<String, Timestamp> scanNumberTempMap = new HashMap<>();
        List<String> scanNumbers = new ArrayList<>();
        ChemicalFiberDeliveryNoteQueryCriteria chemicalFiberDeliveryNoteQueryCriteria = new ChemicalFiberDeliveryNoteQueryCriteria();
        chemicalFiberDeliveryNoteQueryCriteria.setEndTime(new Timestamp(createStatementDto.getCreateStatementDetailsDto().getTempEndTime()));
        chemicalFiberDeliveryNoteQueryCriteria.setStartTime(new Timestamp(createStatementDto.getCreateStatementDetailsDto().getTempStartTime()));
        chemicalFiberDeliveryNoteQueryCriteria.setCustomerId(createStatementDto.getCreateStatementDetailsDto().getCustomerId());
        List<ChemicalFiberDeliveryNoteDTO> chemicalFiberDeliveryNoteDTOs = chemicalFiberDeliveryNoteService.queryAll(chemicalFiberDeliveryNoteQueryCriteria);
        for (ChemicalFiberDeliveryNoteDTO chemicalFiberDeliveryNoteDTO : chemicalFiberDeliveryNoteDTOs) {
            scanNumbers.add(chemicalFiberDeliveryNoteDTO.getScanNumber());
            scanNumberTempMap.put(chemicalFiberDeliveryNoteDTO.getScanNumber(), chemicalFiberDeliveryNoteDTO.getCreateDate());
        }

        if (scanNumbers.size() <= 0) {
            return null;
        }

        ChemicalFiberDeliveryDetailQueryCriteria chemicalFiberDeliveryDetailQueryCriteria = new ChemicalFiberDeliveryDetailQueryCriteria();
        chemicalFiberDeliveryDetailQueryCriteria.setScanNumbers(scanNumbers);
        List<ChemicalFiberDeliveryDetailDTO> tempList = chemicalFiberDeliveryDetailService.queryAll(chemicalFiberDeliveryDetailQueryCriteria);
        for (ChemicalFiberDeliveryDetailDTO chemicalFiberDeliveryDetailDTO : tempList) {
            chemicalFiberDeliveryDetailDTO.setCreateDate(scanNumberTempMap.get(chemicalFiberDeliveryDetailDTO.getScanNumber()));
        }
        return tempList;
    }


    private static String getLastDay(Long time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date date= new Date(time);
        calendar.setTime(date);
        int day=calendar.get(Calendar.DATE);
        calendar.set(Calendar.DATE,day-1);
        String lastDay = sdf.format(calendar.getTime());
        return lastDay;
    }

    private String compareToBigDecimal(BigDecimal value) {
        if (value.compareTo(new BigDecimal(0)) == 0) {
            return "";
        }
        return value + "";
    }

}