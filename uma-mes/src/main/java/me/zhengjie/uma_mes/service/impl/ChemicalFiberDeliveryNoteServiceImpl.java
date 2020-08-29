package me.zhengjie.uma_mes.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelStyleType;
import cn.hutool.extra.template.TemplateConfig;
import com.lgmn.common.result.Result;
import com.lgmn.common.utils.ObjectTransfer;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.uma_mes.domain.ChemicalFiberDeliveryDetail;
import me.zhengjie.uma_mes.domain.ChemicalFiberDeliveryNote;
import me.zhengjie.uma_mes.domain.UmaChemicalFiberStatement;
import me.zhengjie.uma_mes.domain.UmaChemicalFiberStatementDetails;
import me.zhengjie.uma_mes.repository.ChemicalFiberDeliveryDetailRepository;
import me.zhengjie.uma_mes.repository.ChemicalFiberDeliveryNoteRepository;
import me.zhengjie.uma_mes.repository.UmaChemicalFiberStatementDetailsRepository;
import me.zhengjie.uma_mes.repository.UmaChemicalFiberStatementRepository;
import me.zhengjie.uma_mes.service.*;
import me.zhengjie.uma_mes.service.dto.*;
import me.zhengjie.uma_mes.service.mapper.ChemicalFiberDeliveryDetailMapper;
import me.zhengjie.uma_mes.service.mapper.ChemicalFiberDeliveryNoteMapper;
import me.zhengjie.uma_mes.service.mapper.ChemicalFiberStockMapper;
import me.zhengjie.uma_mes.service.mapper.CustomerMapper;
import me.zhengjie.uma_mes.utils.NumberToCN;
import me.zhengjie.utils.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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

    private final ChemicalFiberDeliveryDetailMapper chemicalFiberDeliveryDetailMapper;

    private final ChemicalFiberStockService chemicalFiberStockService;

    private final ChemicalFiberStockMapper chemicalFiberStockMapper;

    private final CustomerMapper customerMapper;

    @Autowired
    ChemicalFiberDeliveryNotePayDetailService chemicalFiberDeliveryNotePayDetailService;

    @Value("${globalCompanyName}")
    private String globalCompanyName;

    @Autowired
    private UmaChemicalFiberStatementRepository umaChemicalFiberStatementRepository;

    @Autowired
    private UmaChemicalFiberStatementDetailsRepository umaChemicalFiberStatementDetailsRepository;

    @Autowired
    private ChemicalFiberDeliveryDetailRepository chemicalFiberDeliveryDetailRepository;


    public ChemicalFiberDeliveryNoteServiceImpl(ChemicalFiberDeliveryNoteRepository chemicalFiberDeliveryNoteRepository,
                                                ChemicalFiberDeliveryNoteMapper chemicalFiberDeliveryNoteMapper,
                                                CustomerService customerService,
                                                ChemicalFiberDeliveryDetailService chemicalFiberDeliveryDetailService,
                                                ScanRecordService scanRecordService,
                                                ScanRecordLabelService scanRecordLabelService,
                                                ChemicalFiberLabelService chemicalFiberLabelService,
                                                 ChemicalFiberDeliveryDetailMapper chemicalFiberDeliveryDetailMapper,
                                                ChemicalFiberStockService chemicalFiberStockService,
                                                ChemicalFiberStockMapper chemicalFiberStockMapper,
                                                CustomerMapper customerMapper){
        this.chemicalFiberDeliveryNoteRepository = chemicalFiberDeliveryNoteRepository;
        this.chemicalFiberDeliveryNoteMapper = chemicalFiberDeliveryNoteMapper;
        this.customerService = customerService;
        this.chemicalFiberDeliveryDetailService = chemicalFiberDeliveryDetailService;
        this.scanRecordService = scanRecordService;
        this.scanRecordLabelService = scanRecordLabelService;
        this.chemicalFiberLabelService = chemicalFiberLabelService;
        this.chemicalFiberDeliveryDetailMapper = chemicalFiberDeliveryDetailMapper;
        this.chemicalFiberStockService = chemicalFiberStockService;
        this.chemicalFiberStockMapper = chemicalFiberStockMapper;
        this.customerMapper = customerMapper;
    }

    @Override
//    @Cacheable
    public Map<String,Object> queryAll(ChemicalFiberDeliveryNoteQueryCriteria criteria, Pageable pageable){
        if (criteria.getTempStartTime() != null) {
            criteria.setStartTime(new Timestamp(criteria.getTempStartTime()));
            criteria.setEndTime(new Timestamp(criteria.getTempEndTime()));
        }
        List<Boolean> booleanList = new ArrayList<>();
        booleanList.add(Boolean.TRUE);
        if (null != criteria.getShowUnEnable() && criteria.getShowUnEnable())
        {
            booleanList.add(Boolean.FALSE);
        }
        criteria.setEnableList(booleanList);
        Page<ChemicalFiberDeliveryNote> page = chemicalFiberDeliveryNoteRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(chemicalFiberDeliveryNoteMapper::toDto));
    }

    @Override
//    @Cacheable
    public List<ChemicalFiberDeliveryNoteDTO> queryAll(ChemicalFiberDeliveryNoteQueryCriteria criteria){
        return chemicalFiberDeliveryNoteMapper.toDto(chemicalFiberDeliveryNoteRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
//    @Cacheable(key = "#p0")
    public ChemicalFiberDeliveryNoteDTO findById(Integer id) {
        ChemicalFiberDeliveryNote chemicalFiberDeliveryNote = chemicalFiberDeliveryNoteRepository.findById(id).orElseGet(ChemicalFiberDeliveryNote::new);
        ValidationUtil.isNull(chemicalFiberDeliveryNote.getId(),"ChemicalFiberDeliveryNote","id",id);
        return chemicalFiberDeliveryNoteMapper.toDto(chemicalFiberDeliveryNote);
    }

    @Override
    public ChemicalFiberDeliveryNoteDTO findByScanNumber(String scanNumber) {
        ChemicalFiberDeliveryNote chemicalFiberDeliveryNote = chemicalFiberDeliveryNoteRepository.getByScanNumber(scanNumber);
        ValidationUtil.isNull(chemicalFiberDeliveryNote.getScanNumber(),"ChemicalFiberDeliveryNote","scanNumber",scanNumber);
        return chemicalFiberDeliveryNoteMapper.toDto(chemicalFiberDeliveryNote);
    }

    @Override
//    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public ChemicalFiberDeliveryNoteDTO create(ChemicalFiberDeliveryNote resources) {
        if(null == resources.getNoteStatus()){
            resources.setNoteStatus(1);
        }
        CustomerDTO customerDTO = customerService.findById(resources.getCustomerId());
        resources.setCustomerName(customerDTO.getName());
        resources.setCustomerAddress(customerDTO.getAddress());
        resources.setCustomerCode(customerDTO.getCode());
        resources.setContacts(customerDTO.getContacts());
        resources.setContactPhone(customerDTO.getContactPhone());
        resources.setCreateDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        resources.setCreateUser(chemicalFiberDeliveryNoteRepository.getRealNameByUserName(SecurityUtils.getUsername()));
        resources.setScanNumber(getScanNumberWithMaxNumber());
        resources.setInvalid(0);
        resources.setEnable(Boolean.TRUE);
        return chemicalFiberDeliveryNoteMapper.toDto(chemicalFiberDeliveryNoteRepository.save(resources));
    }

    public String getScanNumber () {
        String scanNumber;
        String type = globalCompanyName;
        Map<String, Object> timeMap = monthTimeInMillis();
        String year = timeMap.get("year").toString();
        String month = timeMap.get("month").toString();

        Integer currenCount=chemicalFiberDeliveryNoteRepository.getCurrenNoteCount(year+"-"+month);

        if (currenCount == 0) {
            scanNumber = type + year.substring(2,4) + month + "00001";
        } else {
            Integer number = currenCount+ 1;
            String tempNumberStr = String.format("%5d", number++).replace(" ", "0");
            scanNumber = type + year.substring(2,4) + month + tempNumberStr;
        }
        return scanNumber;
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

    public String getScanNumberWithMaxNumber () {
        String scanNumber;
        String type = globalCompanyName;
        Map<String, Object> timeMap = monthTimeInMillis();
        String year = timeMap.get("year").toString();
        String month = timeMap.get("month").toString();

        String currenNumber=chemicalFiberDeliveryNoteRepository.getCurrenNoteCountWithMaxNumber(year+"-"+month);

        if (null == currenNumber || currenNumber.equals("")) {
            scanNumber = type + year.substring(2,4) + month + "00001";
        } else {
            Integer lastNumber = Integer.parseInt(currenNumber.substring(currenNumber.length()-5,currenNumber.length()));
            Integer number = lastNumber+ 1;
            String tempNumberStr = String.format("%5d", number++).replace(" ", "0");
            scanNumber = type + year.substring(2,4) + month + tempNumberStr;
        }
        return scanNumber;
    }

    public Map monthTimeInMillis() {
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
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

    @Override
//    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(ChemicalFiberDeliveryNote resources) {
        CustomerDTO customerDTO = customerService.findById(resources.getCustomerId());
        ChemicalFiberDeliveryNote chemicalFiberDeliveryNote = chemicalFiberDeliveryNoteRepository.findById(resources.getId()).orElseGet(ChemicalFiberDeliveryNote::new);
        ValidationUtil.isNull( chemicalFiberDeliveryNote.getId(),"ChemicalFiberDeliveryNote","id",resources.getId());
        chemicalFiberDeliveryNote.copy(resources);
//        chemicalFiberDeliveryNote.setCreateDate(new Timestamp(System.currentTimeMillis()));
        chemicalFiberDeliveryNote.setCreateUser(chemicalFiberDeliveryNoteRepository.getRealNameByUserName(SecurityUtils.getUsername()));
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
    public void sendOut(Integer id) {
        ChemicalFiberDeliveryNote chemicalFiberDeliveryNote = chemicalFiberDeliveryNoteRepository.findById(id).orElseGet(ChemicalFiberDeliveryNote::new);
        chemicalFiberDeliveryNote.setNoteStatus(3);
        update(chemicalFiberDeliveryNote);
    }

    @Override
    public void recived(Integer id) {
        try{
            ChemicalFiberDeliveryNote chemicalFiberDeliveryNote = chemicalFiberDeliveryNoteRepository.findById(id).orElseGet(ChemicalFiberDeliveryNote::new);
            ChemicalFiberDeliveryDetailQueryCriteria chemicalFiberDeliveryDetailQueryCriteria = new ChemicalFiberDeliveryDetailQueryCriteria();
            chemicalFiberDeliveryDetailQueryCriteria.setScanNumber(chemicalFiberDeliveryNote.getScanNumber());
            List<ChemicalFiberDeliveryDetailDTO> chemicalFiberDeliveryDetailDTOS = chemicalFiberDeliveryDetailService.queryAll(chemicalFiberDeliveryDetailQueryCriteria);
            ChemicalFiberStockQueryCriteria chemicalFiberStockQueryCriteria;
            BigDecimal detailPrise;
            BigDecimal realTotalPrise = new BigDecimal(0);
            for (ChemicalFiberDeliveryDetailDTO chemicalFiberDeliveryDetailDTO : chemicalFiberDeliveryDetailDTOS) {
                if(null == chemicalFiberDeliveryDetailDTO.getRealQuantity() || chemicalFiberDeliveryDetailDTO.getRealQuantity().equals(0)) {
                    //若实收数量为空则自动填入计划数
                    chemicalFiberDeliveryDetailDTO.setRealQuantity(chemicalFiberDeliveryDetailDTO.getTotalNumber());
                    detailPrise = new BigDecimal(0);
                    detailPrise = chemicalFiberDeliveryDetailDTO.getSellingPrice().multiply(chemicalFiberDeliveryDetailDTO.getRealQuantity());
                    realTotalPrise = realTotalPrise.add(detailPrise);
                    chemicalFiberDeliveryDetailDTO.setRealPrice(detailPrise);
                }else{
                    realTotalPrise=realTotalPrise.add(chemicalFiberDeliveryDetailDTO.getRealPrice());
                }
                chemicalFiberDeliveryDetailService.update(chemicalFiberDeliveryDetailMapper.toEntity(chemicalFiberDeliveryDetailDTO));
                //处理库存
                chemicalFiberStockQueryCriteria = new ChemicalFiberStockQueryCriteria();
                chemicalFiberStockQueryCriteria.setProdName(chemicalFiberDeliveryDetailDTO.getProdName());
                chemicalFiberStockQueryCriteria.setProdId(chemicalFiberDeliveryDetailDTO.getProdId());
                chemicalFiberStockQueryCriteria.setProdUnit(chemicalFiberDeliveryDetailDTO.getUnit());
                List<ChemicalFiberStockDTO> chemicalFiberStockDTOs = chemicalFiberStockService.queryAll(chemicalFiberStockQueryCriteria);
                for (ChemicalFiberStockDTO chemicalFiberStockDTO : chemicalFiberStockDTOs){
                    if(null == chemicalFiberStockDTO.getTotalNumber()){
                        chemicalFiberStockDTO.setTotalNumber(new BigDecimal(0));
                    }
                    chemicalFiberStockDTO.setTotalNumber(chemicalFiberStockDTO.getTotalNumber().subtract(chemicalFiberDeliveryDetailDTO.getRealQuantity()));
                    chemicalFiberStockService.update(chemicalFiberStockMapper.toEntity(chemicalFiberStockDTO));
                }
            }
            chemicalFiberDeliveryNote.setNoteStatus(4);
            chemicalFiberDeliveryNote.setBalance(realTotalPrise);
            chemicalFiberDeliveryNote.setTotalPrice(realTotalPrise);
            update(chemicalFiberDeliveryNote);
            StatementUp(id);
        }catch (Exception e){
            throw new BadRequestException("签收失败，请校验订单数据");
        }

    }

    @Override
    public void doInvalid(Integer id) {
        ChemicalFiberDeliveryNote chemicalFiberDeliveryNote = chemicalFiberDeliveryNoteRepository.findById(id).orElseGet(ChemicalFiberDeliveryNote::new);
        List<ChemicalFiberDeliveryNotePayDetailDTO> payDetailList
                =chemicalFiberDeliveryNotePayDetailService.findListByScanNumber(chemicalFiberDeliveryNote.getScanNumber());
        BigDecimal payedMoney = new BigDecimal(0);
        for(ChemicalFiberDeliveryNotePayDetailDTO payDetail:payDetailList){
            payedMoney = payedMoney.add(payDetail.getAmount());
            chemicalFiberDeliveryNotePayDetailService.delete(payDetail.getId());
        }
        CustomerDTO customerDTO = customerService.findById(chemicalFiberDeliveryNote.getCustomerId());
        customerDTO.setAccount(customerDTO.getAccount().add(payedMoney));
        customerService.save(customerMapper.toEntity(customerDTO));
        chemicalFiberDeliveryNote.setRemainder(new BigDecimal(0));
        chemicalFiberDeliveryNote.setEnable(Boolean.FALSE);
        chemicalFiberDeliveryNote.setNoteStatus(2);
        update(chemicalFiberDeliveryNote);
    }

    @Override
    public void unInvalid(Integer id) {
        ChemicalFiberDeliveryNote chemicalFiberDeliveryNote = chemicalFiberDeliveryNoteRepository.findById(id).orElseGet(ChemicalFiberDeliveryNote::new);
        chemicalFiberDeliveryNote.setEnable(Boolean.TRUE);
        update(chemicalFiberDeliveryNote);
    }

    @Override
    public void updateBalance(ChemicalFiberDeliveryNote chemicalFiberDeliveryNote) {
        chemicalFiberDeliveryNoteRepository.save(chemicalFiberDeliveryNote);
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
    @Transactional(rollbackFor = Exception.class)
    public void downloadDeliveryNote(Integer id, HttpServletResponse response) {
        DecimalFormat df = new DecimalFormat("0.00");
        ChemicalFiberDeliveryNote chemicalFiberDeliveryNote = chemicalFiberDeliveryNoteRepository.findById(id).orElseGet(ChemicalFiberDeliveryNote::new);
        ChemicalFiberDeliveryDetailQueryCriteria chemicalFiberDeliveryDetailQueryCriteria = new ChemicalFiberDeliveryDetailQueryCriteria();
        chemicalFiberDeliveryDetailQueryCriteria.setScanNumber(chemicalFiberDeliveryNote.getScanNumber());
        List<ChemicalFiberDeliveryDetailDTO> chemicalFiberDeliveryDetailDTOS = chemicalFiberDeliveryDetailService.queryAll(chemicalFiberDeliveryDetailQueryCriteria);
        String templatePath = new TemplateConfig("template/excel", TemplateConfig.ResourceMode.CLASSPATH).getPath() + "/delivery_temp.xls";
        // 加载模板
        TemplateExportParams params = new TemplateExportParams(templatePath);
        // 生成workbook 并导出
        Workbook workbook = null;
        if (null == chemicalFiberDeliveryNote.getDeliveryDate() || chemicalFiberDeliveryNote.getDeliveryDate().equals("")) {
            chemicalFiberDeliveryNote.setDeliveryDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));
            update(chemicalFiberDeliveryNote);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("customerName", chemicalFiberDeliveryNote.getCustomerName());
        map.put("customerAddress", chemicalFiberDeliveryNote.getCustomerAddress());
        map.put("contacts", chemicalFiberDeliveryNote.getContacts());
        map.put("contactPhone", chemicalFiberDeliveryNote.getContactPhone());
        map.put("scanNumber", chemicalFiberDeliveryNote.getScanNumber());
        map.put("createDate", chemicalFiberDeliveryNote.getCreateDate());
        map.put("deliveryDate", chemicalFiberDeliveryNote.getDeliveryDate());
        map.put("createUser", chemicalFiberDeliveryNote.getCreateUser());
        map.put("driverMain",chemicalFiberDeliveryNote.getDriverMain());
        map.put("driverDeputy",chemicalFiberDeliveryNote.getDriverDeputy());
        map.put("loaderOne",chemicalFiberDeliveryNote.getLoaderOne());
        map.put("loaderTwo",chemicalFiberDeliveryNote.getLoaderTwo());
        map.put("carNumber",chemicalFiberDeliveryNote.getCarNumber());
        map.put("seller",chemicalFiberDeliveryNote.getSeller());
        map.put("remark",chemicalFiberDeliveryNote.getRemark());
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
        BigDecimal totalPriceWhitRealQuantity = new BigDecimal(0);
        for (ChemicalFiberDeliveryDetailDTO chemicalFiberDeliveryDetailDTO : chemicalFiberDeliveryDetailDTOS) {
            if (null == chemicalFiberDeliveryDetailDTO.getTotalNumber() || chemicalFiberDeliveryDetailDTO.getTotalNumber().equals(0) ) {
                throw new BadRequestException("请补充计划数量");
            }
            if (null == chemicalFiberDeliveryDetailDTO.getSellingPrice() || chemicalFiberDeliveryDetailDTO.getSellingPrice().compareTo(BigDecimal.ZERO) == 0) {
                throw new BadRequestException("请补充单价");
            }
            Map<String, String> lm = new HashMap<String, String>();
            lm.put("prodName", chemicalFiberDeliveryDetailDTO.getProdName());
            lm.put("detailNumber", chemicalFiberDeliveryDetailDTO.getDetailNumber()+"");
            lm.put("totalBag", chemicalFiberDeliveryDetailDTO.getTotalBag() + "");
            lm.put("totalNumber", chemicalFiberDeliveryDetailDTO.getTotalNumber() + "");
            lm.put("unit", chemicalFiberDeliveryDetailDTO.getUnit());
            lm.put("sellingPrice", chemicalFiberDeliveryDetailDTO.getSellingPrice() + "");
            if(null != chemicalFiberDeliveryDetailDTO.getRealQuantity() && !chemicalFiberDeliveryDetailDTO.getRealQuantity().equals(0)){
                BigDecimal detailTotalPrice = chemicalFiberDeliveryDetailDTO.getSellingPrice().multiply(chemicalFiberDeliveryDetailDTO.getRealQuantity());
                chemicalFiberDeliveryDetailDTO.setTotalPrice(detailTotalPrice);
                totalPriceWhitRealQuantity = totalPriceWhitRealQuantity.add(detailTotalPrice);
                lm.put("totalPrice", df.format(chemicalFiberDeliveryDetailDTO.getTotalPrice()) + "");
                lm.put("realQuantity", chemicalFiberDeliveryDetailDTO.getRealQuantity() + "");
            }else{
                lm.put("realQuantity","");
                lm.put("totalPrice","");
            }
            lm.put("remark", chemicalFiberDeliveryDetailDTO.getRemark());
            lm.put("detailNumber", chemicalFiberDeliveryDetailDTO.getDetailNumber()+"");
            listMap.add(lm);
        }
        if(totalPriceWhitRealQuantity.compareTo(BigDecimal.ZERO) == 0){
            map.put("total", "");
            map.put("capitalizationTotal","");
        }else {
            map.put("total",df.format(totalPriceWhitRealQuantity) + "");
            map.put("capitalizationTotal", NumberToCN.number2CNMontrayUnit(totalPriceWhitRealQuantity));
        }
        map.put("deliveryList", listMap);
        workbook = ExcelExportUtil.exportExcel(params, map);
        FileUtil.downLoadExcel(chemicalFiberDeliveryNote.getScanNumber()+"-送货单导出.xlsx", response, workbook);
        if(null != chemicalFiberDeliveryNote.getNoteStatus() && chemicalFiberDeliveryNote.getNoteStatus().equals(1)) {
            chemicalFiberDeliveryNote.setNoteStatus(2);
        }
        update(chemicalFiberDeliveryNote);
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
                chemicalFiberDeliveryNoteSalesReportDTO.setOutOfStockPackageNumber(chemicalFiberDeliveryNoteSalesReportDTO.getOutOfStockPackageNumber().add(chemicalFiberDeliveryDetail.getTotalBag()));
                chemicalFiberDeliveryNoteSalesReportDTO.setOutOfStockFactPerBagNumber(chemicalFiberDeliveryNoteSalesReportDTO.getOutOfStockFactPerBagNumber().add((chemicalFiberDeliveryDetail.getTotalNumber() == null ? new BigDecimal(0) : chemicalFiberDeliveryDetail.getTotalNumber())));
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
        BigDecimal totalBag = new BigDecimal(0.0);

        // 总数量
        BigDecimal totalNumber = new BigDecimal(0.0);

        // 总重量
        BigDecimal totalWeight = new BigDecimal(0.0);

        for (ChemicalFiberDeliveryNote chemicalFiberDeliveryNote : chemicalFiberDeliveryNotes) {
            for (ChemicalFiberDeliveryDetail chemicalFiberDeliveryDetail : chemicalFiberDeliveryNote.getChemicalFiberDeliveryDetails()) {
                totalCost = totalCost.add(chemicalFiberDeliveryDetail.getTotalCost());
                totalPrice = totalPrice.add(chemicalFiberDeliveryDetail.getTotalPrice());
                totalBag = totalBag.add((chemicalFiberDeliveryDetail.getTotalBag() == null ? new BigDecimal(0) : chemicalFiberDeliveryDetail.getTotalBag()));
                totalNumber = totalNumber.add((chemicalFiberDeliveryDetail.getTotalNumber() == null ? new BigDecimal(0) : chemicalFiberDeliveryDetail.getTotalNumber()));
                totalWeight = totalWeight.add(chemicalFiberDeliveryDetail.getTotalWeight() == null ? new BigDecimal(0.0) : chemicalFiberDeliveryDetail.getTotalWeight());
            }
        }

        List<Object> list = new ArrayList<>();
        list.add("总计");
        list.add("");
        list.add(totalBag);
        list.add(totalNumber);
        list.add(totalWeight);
        list.add(totalCost);
        list.add(totalPrice);
        list.add("");
        return Result.success(list);
    }

    public void StatementUp(Integer id) {
        try {
            ChemicalFiberDeliveryNote note = chemicalFiberDeliveryNoteRepository.findById(id).orElseGet(ChemicalFiberDeliveryNote::new);
            List<ChemicalFiberDeliveryDetail> detail = chemicalFiberDeliveryDetailRepository.getDetailList(note.getScanNumber());
            Timestamp time = note.getDeliveryDate();
            Date date = new Date(time.getTime());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            Integer year = calendar.get(Calendar.YEAR);
            Integer month = calendar.get(Calendar.MONTH) + 1;
            Integer da = 0;
            Integer month1 = month - 1;
            Integer day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
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
            String dateTime = year + "-" + months + "-" + day;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dateUp = simpleDateFormat.parse(dateTime);
            UmaChemicalFiberStatement statement = umaChemicalFiberStatementRepository.getOneId(note.getCustomerId(), year + "-" + months);
            String accoun = getAccountCode();
            if (statement == null) {
                UmaChemicalFiberStatement statementAdd = new UmaChemicalFiberStatement();
                statementAdd.setAccountCode(accoun);
                statementAdd.setCreateDate(note.getDeliveryDate());
                statementAdd.setCustomerId(note.getCustomerId());
                statementAdd.setCustomerName(note.getCustomerName());
                statementAdd.setContacts(note.getContacts());
                statementAdd.setContactPhone(note.getContactPhone());
                statementAdd.setUpDate(new Timestamp(dateUp.getTime()));
                //statementAdd.setTotalArrears(note.getBalance());
                //statementAdd.setAccumulatedArrears(statement1.getTotalArrears());
                statementAdd.setReceivable(note.getTotalPrice());
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
                BigDecimal big = statement.getReceivable();
                BigDecimal big1 = note.getTotalPrice();
                BigDecimal big2 = big.add(big1);
                statement.setReceivable(big2);
                statement = umaChemicalFiberStatementRepository.save(statement);
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
}