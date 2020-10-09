package me.zhengjie.uma_mes.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.hutool.extra.template.TemplateConfig;
import com.lgmn.common.result.Result;
import com.lgmn.common.utils.ObjectTransfer;
import me.zhengjie.uma_mes.domain.*;
import me.zhengjie.uma_mes.repository.*;
import me.zhengjie.uma_mes.service.*;
import me.zhengjie.uma_mes.service.dto.*;
import me.zhengjie.uma_mes.service.dto.statement.CreateStatementDto;
import me.zhengjie.uma_mes.service.dto.statement.StatementDetailsAllListDto;
import me.zhengjie.uma_mes.service.dto.statement.StatementDetailsListDto;
import me.zhengjie.uma_mes.service.mapper.UmaChemicalFiberStatementDetailsMapper;
import me.zhengjie.uma_mes.utils.NumberToCN;
import me.zhengjie.utils.*;
import me.zhengjie.uma_mes.service.mapper.UmaChemicalFiberStatementMapper;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${globalCompanyName}")
    private String globalCompanyName;

    private final UmaChemicalFiberStatementRepository umaChemicalFiberStatementRepository;

    private final UmaChemicalFiberStatementMapper umaChemicalFiberStatementMapper;

    private final ChemicalFiberDeliveryNoteService chemicalFiberDeliveryNoteService;

    private final ChemicalFiberDeliveryDetailService chemicalFiberDeliveryDetailService;

    private final UmaChemicalFiberStatementDetailsService umaChemicalFiberStatementDetailsService;

    private final ChemicalFiberDeliveryNoteRepository chemicalFiberDeliveryNoteRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ChemicalFiberDeliveryDetailRepository chemicalFiberDeliveryDetailRepository;

    @Autowired
    private UmaChemicalFiberStatementDetailsRepository umaChemicalFiberStatementDetailsRepository;

    @Autowired
    private UmaChemicalFiberStatementDetailsMapper umaChemicalFiberStatementDetailsMapper;

    @Autowired
    private ReceiptRepository receiptRepository;

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
        String years = "";
        String months = "";
        Integer yesr;
        if (criteria.getCreateDate() != null) {
            Long createDate = criteria.getCreateDate();
            Date date = new Date(createDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            Integer year = calendar.get(Calendar.YEAR);
            Integer month = calendar.get(Calendar.MONTH) + 1;
            years = year + "";
            if (month < 10) {
                months = "0" + month;
            } else {
                months = month + "";
            }
        }

        String customerName = "";
        String accountCode = "";
        if (criteria.getCustomerName() != null) {
            customerName = criteria.getCustomerName();
        }
        if (criteria.getAccountCode() != null) {
            accountCode = criteria.getAccountCode();
        }
        int start = pageable.getPageSize();
        int end = pageable.getPageNumber();
        int PageNumber = start * end;
        Map<String, Object> map = new HashMap<>();
        List<UmaChemicalFiberStatement> pages = umaChemicalFiberStatementRepository.findadd(PageNumber, start, customerName, accountCode, years + "-" + months);
        List<UmaChemicalFiberStatementDTO> pageList = new ArrayList<>();
        for (UmaChemicalFiberStatement dto : pages) {
            Date date = dto.getCreateDate();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            Integer year = calendar.get(Calendar.MONTH) + 1;
            Integer day1 = calendar.get(Calendar.DAY_OF_MONTH);
            Integer day = 1;
            if (dto.getReconciliations() != null) {
                day = Integer.valueOf(dto.getReconciliations());
                if ( day1 < day) {
                    year -= 1;
                }
            }
            String startTime = getCurrenMonthStartTime(year,day);
            String endTime = getCurrenMonthEndTime(year,day);
            CustomerDTO customer = new CustomerDTO();
            customer = customerService.findByIdWithTotalArrearsStatement(dto.getCustomerId(), startTime, endTime);
            UmaChemicalFiberStatementDTO dtoset = new UmaChemicalFiberStatementDTO();
            ObjectTransfer.transValue(dto, dtoset);
            dtoset.setReceivable(customer.getCurrentArrears());
            dtoset.setAccumulatedArrears(customer.getTotalArrears());
            dtoset.setRemainder(customer.getRemainder());
            dtoset.setAccount(customer.getAccount());
            BigDecimal totalArrears = new BigDecimal(0.00);
            if (customer.getTotalArrears() != null) {
                totalArrears = customer.getCurrentArrears().add(customer.getTotalArrears());
                dtoset.setTotalArrears(totalArrears);
            } else {
                totalArrears = customer.getCurrentArrears();
                dtoset.setTotalArrears(totalArrears);
                dtoset.setAccumulatedArrears(null);
            }
            pageList.add(dtoset);
        }
        Integer ListSeiz = umaChemicalFiberStatementRepository.findSize();
        map.put("content", pageList);
        map.put("totalElements", ListSeiz);
        // Page<UmaChemicalFiberStatement> pages = umaChemicalFiberStatementRepository.findadd();
        return map;
        //return PageUtil.toPage(page.map(umaChemicalFiberStatementMapper::toDto));
    }

    @Override
    //@Cacheable
    public List<UmaChemicalFiberStatementDTO> queryAll(UmaChemicalFiberStatementQueryCriteria criteria){
        return umaChemicalFiberStatementMapper.toDto(umaChemicalFiberStatementRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    //@Cacheable(key = "#p0")
    public UmaChemicalFiberStatementDTO findById(Integer id) {
        UmaChemicalFiberStatement umaChemicalFiberStatement = umaChemicalFiberStatementRepository.findById(id).orElseGet(UmaChemicalFiberStatement::new);
        ValidationUtil.isNull(umaChemicalFiberStatement.getId(),"UmaChemicalFiberStatement","id",id);
        return umaChemicalFiberStatementMapper.toDto(umaChemicalFiberStatement);
    }

    @Override
    //@CacheEvict(allEntries = true)
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

    //@CacheEvict(allEntries = true)
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
        //UmaChemicalFiberStatement statement = umaChemicalFiberStatementRepository.getOneId(note.getCustomerId(), year + "-" + months);

    }

    @Override
    //@CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(UmaChemicalFiberStatement resources) {
        UmaChemicalFiberStatement umaChemicalFiberStatement = umaChemicalFiberStatementRepository.findById(resources.getId()).orElseGet(UmaChemicalFiberStatement::new);
        ValidationUtil.isNull( umaChemicalFiberStatement.getId(),"UmaChemicalFiberStatement","id",resources.getId());
        umaChemicalFiberStatement.copy(resources);
        umaChemicalFiberStatementRepository.save(umaChemicalFiberStatement);
    }

    @Override
    //@CacheEvict(allEntries = true)
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
        List<Boolean> booleanList = new ArrayList<>();
        booleanList.add(Boolean.TRUE);
        if (null != chemicalFiberDeliveryNoteQueryCriteria.getShowUnEnable() && chemicalFiberDeliveryNoteQueryCriteria.getShowUnEnable())
        {
            booleanList.add(Boolean.FALSE);
        }
        chemicalFiberDeliveryNoteQueryCriteria.setEnableList(booleanList);
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
        BigDecimal sumTotalBag = new BigDecimal(0);
        BigDecimal sumNetWeight = new BigDecimal(0);
        BigDecimal sumTotalPrice = new BigDecimal(0);
        BigDecimal sumAdvanceCharge = new BigDecimal(0);
        BigDecimal sumAmountDeducted = new BigDecimal(0);

        for (UmaChemicalFiberStatementDetailsDTO umaChemicalFiberStatementDetailsDTO : umaChemicalFiberStatementDetailsDTOS) {
            sumTotalBag = sumTotalBag.add(umaChemicalFiberStatementDetailsDTO.getTotalBag());
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
        String global = "";
        String templatePath = "";
        if (globalCompanyName.equals("YQ")) {
            global = "高明"+"永琪";
            templatePath = new TemplateConfig("template/excel", TemplateConfig.ResourceMode.CLASSPATH).getPath() + "/statementList_yq.xls";
        } else {
            global = "南海" + "祥琪";
            templatePath = new TemplateConfig("template/excel", TemplateConfig.ResourceMode.CLASSPATH).getPath() + "/statementList_xq.xls";

        }
        UmaChemicalFiberStatement umaChemicalFiberStatement = umaChemicalFiberStatementRepository.findById(id).orElseGet(UmaChemicalFiberStatement::new);
        umaChemicalFiberStatement.setPrintDate(new Timestamp(System.currentTimeMillis()));
        umaChemicalFiberStatementRepository.save(umaChemicalFiberStatement);
        Date date1 = umaChemicalFiberStatement.getCreateDate();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Integer year1 = calendar1.get(Calendar.MONTH) + 1;
        Integer day1 = calendar1.get(Calendar.DAY_OF_MONTH);
        Integer day = 1;
        if (umaChemicalFiberStatement.getReconciliations() != null) {
            day = Integer.valueOf(umaChemicalFiberStatement.getReconciliations());
            if ( day1 < day) {
                year1 -= 1;
            }
        }
        String startTime = getCurrenMonthStartTime(year1, day);
        String endTime = getCurrenMonthEndTime(year1, day);
        CustomerDTO customer = new CustomerDTO();
        customer = customerService.findByIdWithTotalArrearsStatement(umaChemicalFiberStatement.getCustomerId(), startTime, endTime);
        //CustomerDTO customer = customerService.findByIdWithTotalArrears(umaChemicalFiberStatement.getCustomerId());
        umaChemicalFiberStatement.setReceivable(customer.getCurrentArrears());
        umaChemicalFiberStatement.setAccumulatedArrears(customer.getTotalArrears());
        BigDecimal totalArrears = new BigDecimal(0.00);
        if (customer.getTotalArrears() != null) {
            totalArrears = customer.getCurrentArrears().add(customer.getTotalArrears());
            umaChemicalFiberStatement.setTotalArrears(totalArrears);
        } else {
            totalArrears = customer.getCurrentArrears();
            umaChemicalFiberStatement.setTotalArrears(totalArrears);
        }
        UmaChemicalFiberStatementDetailsQueryCriteria umaChemicalFiberStatementDetailsQueryCriteria = new UmaChemicalFiberStatementDetailsQueryCriteria();
        umaChemicalFiberStatementDetailsQueryCriteria.setStatementId(umaChemicalFiberStatement.getId());
        List<UmaChemicalFiberStatementDetailsDTO> umaChemicalFiberStatementDetailsDTOS = umaChemicalFiberStatementDetailsService.queryAll(umaChemicalFiberStatementDetailsQueryCriteria);

        // 加载模板
        TemplateExportParams params = new TemplateExportParams(templatePath);
        params.setReadonly(Boolean.TRUE);
        // 生成workbook 并导出
        Workbook workbook = null;
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
        BigDecimal totalSum = new BigDecimal(0);
        BigDecimal sumTotalBag = new BigDecimal(0);
        BigDecimal sumAmountOfMoney = new BigDecimal(0);
        Timestamp time = umaChemicalFiberStatement.getCreateDate();
        Date date = new Date(time.getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Integer year = calendar.get(Calendar.YEAR);
        Integer month = calendar.get(Calendar.MONTH) + 1;
        Integer month1 = month - 1;
        String months = "";
        String months1 = "";
        if (month < 10) {
            months = "0" + month;
        } else {
            months = month + "";
        }
        if (month1 == 0) {
            year -= 1;
            months1 = "12";
        } else if (month1 < 10) {
            months1 = "0" + month1;
        } else {
            months1 = month1 + "";
        }
        Integer onCredit = chemicalFiberDeliveryNoteRepository.getOnCreditSum(year + "-" + months, umaChemicalFiberStatement.getCustomerId());
        if (onCredit == null) {
            onCredit = 0;
        }
        Integer sumTotal = chemicalFiberDeliveryNoteRepository.getOnCreditSum(year + "-" + (months + 1), umaChemicalFiberStatement.getCustomerId());
        for (int i = 0; i < umaChemicalFiberStatementDetailsDTOS.size(); i++) {
            Map<String, String> lm = new HashMap<String, String>();
            lm.put("index", (i + 1) + "");
            lm.put("scanDate", (umaChemicalFiberStatementDetailsDTOS.get(i).getScanDate() + "").substring(0, 10));
            lm.put("scanNumber", umaChemicalFiberStatementDetailsDTOS.get(i).getScanNumber());
            lm.put("prodName", umaChemicalFiberStatementDetailsDTOS.get(i).getProdName());
            lm.put("totalBag", umaChemicalFiberStatementDetailsDTOS.get(i).getTotalBag() + "");
            lm.put("sellingPrice", umaChemicalFiberStatementDetailsDTOS.get(i).getSellingPrice() + "");
            lm.put("totalPrice", compareToBigDecimal(umaChemicalFiberStatementDetailsDTOS.get(i).getTotalPrice()));
            lm.put("unit", umaChemicalFiberStatementDetailsDTOS.get(i).getUnit());
            lm.put("remark", umaChemicalFiberStatementDetailsDTOS.get(i).getRemark());
            listMap.add(lm);
            totalSum = totalSum.add(umaChemicalFiberStatementDetailsDTOS.get(i).getTotalPrice());
            sumTotalBag = sumTotalBag.add(umaChemicalFiberStatementDetailsDTOS.get(i).getTotalBag());
        }
        List<Map<String, String>> receiptlistMap = new ArrayList<Map<String, String>>();
        List<Receipt> receiptList = receiptRepository.getReceiptList(umaChemicalFiberStatement.getCustomerId(), year + "-" + months);
        for (Receipt receipt : receiptList) {
            Map<String, String> lm = new HashMap<String, String>();
            lm.put("recivedDate", (receipt.getRecivedDate() + "").substring(0, 10));
            lm.put("receiptNumber", receipt.getReceiptNumber() + "");
            lm.put("type", receipt.getRecivedAccount() + "");
            lm.put("recivedNumber", receipt.getRecivedNumber() + "");
            lm.put("amountOfMoney", receipt.getAmountOfMoney() + "");
            lm.put("remark", receipt.getRemark() + "");
            sumAmountOfMoney = sumAmountOfMoney.add(receipt.getAmountOfMoney());
            receiptlistMap.add(lm);
        }
        Timestamp starDate = umaChemicalFiberStatementDetailsDTOS.get(0).getScanDate();
        Timestamp endDate = umaChemicalFiberStatementDetailsDTOS.get(umaChemicalFiberStatementDetailsDTOS.size() - 1).getScanDate();
        String starDateStr = year + "年" + months + "月";
        String endDateStr = year + "年" + months1 + "月";
        map.put("contacts", umaChemicalFiberStatement.getContacts());
        map.put("customerName", umaChemicalFiberStatement.getCustomerName());
        map.put("contactPhone", umaChemicalFiberStatement.getContactPhone());
        map.put("accountCode", umaChemicalFiberStatement.getAccountCode());
        map.put("endDate", endDateStr);
        //map.put("cycle", starDateStr + "--" + endDateStr);
        map.put("starDate", starDateStr);
        map.put("capitTotal", NumberToCN.number2CNMontrayUnit(umaChemicalFiberStatement.getReceivable()));
        map.put("total", umaChemicalFiberStatement.getReceivable() + "");
        //map.put("onCredit", umaChemicalFiberStatement.getAccumulatedArrears() + "");//上期欠款
        map.put("onCredit", umaChemicalFiberStatement.getAccumulatedArrears() + "");//上期欠款
        map.put("sumTotal", umaChemicalFiberStatement.getTotalArrears() + ""); // 总欠款
        map.put("totalSum", umaChemicalFiberStatement.getReceivable() + ""); // 总金额
        map.put("sumAmountOfMoney", sumAmountOfMoney + ""); // 总金额
        map.put("statementLists", listMap);
        map.put("receiptlistMap", receiptlistMap);
        map.put("sumTotalBag", sumTotalBag);
        map.put("global", global);
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

    public void StatementUp(Integer id) {
        try {
            ChemicalFiberDeliveryNote note = chemicalFiberDeliveryNoteRepository.findById(id).orElseGet(ChemicalFiberDeliveryNote::new);
            List<ChemicalFiberDeliveryDetail> detail = chemicalFiberDeliveryDetailRepository.getDetailList(note.getScanNumber());
            Timestamp time = note.getDeliveryDate();
            Date date = new Date(time.getTime());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            CustomerDTO customer = customerService.findByIdWithTotalArrears(note.getCustomerId());
            Integer year = calendar.get(Calendar.YEAR);
            Integer month = calendar.get(Calendar.MONTH) + 1;
            Integer day1 = calendar.get(Calendar.DAY_OF_MONTH);
            Integer day = 1;
            /*if ( day1 < customer.getReconciliation()) {
                month -= 1;
            }*/
            if (customer.getReconciliation() != null) {
                day = Integer.valueOf(customer.getReconciliation());
                if ( day1 < day) {
                    month -= 1;
                }

            }
            String startTime = getCurrenMonthStartTime(month,day);
            String endTime = getCurrenMonthEndTime(month,day);
            UmaChemicalFiberStatement statement = umaChemicalFiberStatementRepository.getOneId(note.getCustomerId(),
                    startTime,
                    endTime);
            String accoun = getAccountCode();
            BigDecimal totalArreares = new BigDecimal(0.00);
            if (statement == null) {
                UmaChemicalFiberStatement statementAdd = new UmaChemicalFiberStatement();
                statementAdd.setAccountCode(accoun);
                statementAdd.setCreateDate(note.getDeliveryDate());
                statementAdd.setCustomerId(note.getCustomerId());
                statementAdd.setCustomerName(note.getCustomerName());
                statementAdd.setContacts(note.getContacts());
                statementAdd.setContactPhone(note.getContactPhone());
                statementAdd.setReconciliations(customer.getReconciliation());
                //statementAdd.setUpDate(new Timestamp(dateUp.getTime()));
                /*statementAdd.setReceivable(customer.getCurrentArrears());
                statementAdd.setAccumulatedArrears(customer.getTotalArrears());
                if (customer.getTotalArrears() != null) {
                    totalArreares = note.getTotalPrice().add(customer.getTotalArrears());
                } else {
                    totalArreares = note.getTotalPrice();
                }
                statementAdd.setTotalArrears(totalArreares);*/
                statementAdd = umaChemicalFiberStatementRepository.save(statementAdd);
                List<UmaChemicalFiberStatementDetails> staementDetail = new ArrayList<>();
                for (ChemicalFiberDeliveryDetail dto : detail) {
                    UmaChemicalFiberStatementDetails add = new UmaChemicalFiberStatementDetails();
                    add.setStatementId(statementAdd.getId());
                    add.setScanNumber(dto.getScanNumber());
                    add.setScanDate(note.getCreateDate());
                    add.setProdName(dto.getProdName());
                    add.setTotalBag(dto.getRealQuantity());
                    add.setSellingPrice(dto.getSellingPrice());
                    add.setTotalPrice(dto.getRealPrice());
                    add.setUnit(dto.getUnit());
                    staementDetail.add(add);
                }
                umaChemicalFiberStatementDetailsRepository.saveAll(staementDetail);
            } else {
                /*BigDecimal big = statement.getReceivable();
                BigDecimal big1 = note.getTotalPrice();
                BigDecimal big2 = big.add(big1);
                statement.setReceivable(big2);
                statement = umaChemicalFiberStatementRepository.save(statement);*/
                List<UmaChemicalFiberStatementDetails> staementDetail = new ArrayList<>();
                for (ChemicalFiberDeliveryDetail dto : detail) {
                    UmaChemicalFiberStatementDetails add = new UmaChemicalFiberStatementDetails();
                    add.setStatementId(statement.getId());
                    add.setScanNumber(dto.getScanNumber());
                    add.setScanDate(note.getCreateDate());
                    add.setProdName(dto.getProdName());
                    add.setTotalBag(dto.getRealQuantity());
                    add.setSellingPrice(dto.getSellingPrice());
                    add.setTotalPrice(dto.getRealPrice());
                    add.setUnit(dto.getUnit());
                    staementDetail.add(add);
                }
                umaChemicalFiberStatementDetailsRepository.saveAll(staementDetail);
            }

        }catch (Exception e){
            System.out.println(e);
        }
    }

    public String getAccountCode () {
        String scanNumber;
        String type = "DZ";
        Map<String, Object> timeMap = monthTimeInMillis();
        String year = timeMap.get("year").toString();
        String month = timeMap.get("month").toString();

        Integer currenCount=umaChemicalFiberStatementRepository.getCurrenNoteCount(year+"-"+month);

        if (currenCount == 0) {
            scanNumber = type + year.substring(2,4) + month + "00001";
        } else {
            Integer number = currenCount+ 1;
            String tempNumberStr = String.format("%5d", number++).replace(" ", "0");
            scanNumber = type + year.substring(2,4) + month + tempNumberStr;
        }
        return scanNumber;
    }


    public Map monthTimeInMillis() {
        // 获取当前日期
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.MONTH, 0);
        // 设置为1号,当前日期既为本月第一天
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Long time = calendar.getTimeInMillis();
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);

        Map<String, Object> map = new HashMap<>();
        map.put("time", time);
        map.put("month", month < 10 ? "0" + month : month);
        map.put("year", year);
        return map;
    }

    private String getCurrenMonthStartTime(Integer month, Integer day){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        String firstday, lastday;
        // 获取前月的第一天
        Calendar cale = Calendar.getInstance();
        cale = Calendar.getInstance();
        cale.set(Calendar.MONTH, month - 1);
        cale.set(Calendar.DAY_OF_MONTH, day);
        return format.format(cale.getTime());
    }

    private String getCurrenMonthEndTime(Integer month, Integer day){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        String firstday, lastday;
        // 获取前月的最后一天
        Calendar cale = Calendar.getInstance();
        cale.set(Calendar.MONTH, month);
        cale.set(Calendar.DAY_OF_MONTH, day);
        return format.format(cale.getTime());
    }


    public void delectStatemen(Integer id){
        ChemicalFiberDeliveryNote note = chemicalFiberDeliveryNoteRepository.findById(id).orElseGet(ChemicalFiberDeliveryNote::new);
        UmaChemicalFiberStatementDetailsQueryCriteria criteria = new UmaChemicalFiberStatementDetailsQueryCriteria();
        criteria.setScanNumber(note.getScanNumber());
        List<UmaChemicalFiberStatementDetailsDTO> statemen = umaChemicalFiberStatementDetailsService.queryAll(criteria);
        List<UmaChemicalFiberStatementDetails> statemens = umaChemicalFiberStatementDetailsMapper.toEntity(statemen);
        umaChemicalFiberStatementDetailsRepository.deleteAll(statemens);
    }

}