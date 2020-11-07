package me.zhengjie.uma_mes.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.hutool.extra.template.TemplateConfig;
import com.lgmn.common.domain.LgmnPage;
import com.lgmn.common.result.Result;
import me.zhengjie.uma_mes.domain.ChemicalFiberLabel;
import me.zhengjie.uma_mes.domain.ChemicalFiberProduction;
import me.zhengjie.uma_mes.domain.Machine;
import me.zhengjie.uma_mes.repository.ChemicalFiberLabelRepository;
import me.zhengjie.uma_mes.repository.MachineRepository;
import me.zhengjie.uma_mes.service.ChemicalFiberProductService;
import me.zhengjie.uma_mes.service.CustomerService;
import me.zhengjie.uma_mes.service.MachineService;
import me.zhengjie.uma_mes.service.dto.*;
import me.zhengjie.uma_mes.service.handheld.HandheldService;
import me.zhengjie.utils.*;
import me.zhengjie.uma_mes.repository.ChemicalFiberProductionRepository;
import me.zhengjie.uma_mes.service.ChemicalFiberProductionService;
import me.zhengjie.uma_mes.service.mapper.ChemicalFiberProductionMapper;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Service
@CacheConfig(cacheNames = "chemicalFiberProduction")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ChemicalFiberProductionServiceImpl implements ChemicalFiberProductionService {

    private final ChemicalFiberProductionRepository chemicalFiberProductionRepository;

    private final ChemicalFiberProductionMapper chemicalFiberProductionMapper;

    private final ChemicalFiberProductService chemicalFiberProductService;

    private final CustomerService customerService;

    private final MachineRepository machineRepository;

    private final ChemicalFiberLabelRepository chemicalFiberLabelRepository;

    @Autowired
    HandheldService handheldService;

    public ChemicalFiberProductionServiceImpl(ChemicalFiberProductionRepository chemicalFiberProductionRepository,
                                              ChemicalFiberProductionMapper chemicalFiberProductionMapper,
                                              ChemicalFiberProductService chemicalFiberProductService,
                                              CustomerService customerService,
                                              MachineRepository machineRepository,
                                              ChemicalFiberLabelRepository chemicalFiberLabelRepository) {
        this.chemicalFiberProductionRepository = chemicalFiberProductionRepository;
        this.chemicalFiberProductionMapper = chemicalFiberProductionMapper;
        this.chemicalFiberProductService = chemicalFiberProductService;
        this.customerService = customerService;
        this.machineRepository = machineRepository;
        this.chemicalFiberLabelRepository = chemicalFiberLabelRepository;
    }

    @Override
//    @Cacheable
    public Map<String,Object> queryAll(ChemicalFiberProductionQueryCriteria criteria, Pageable pageable){
        criteria.setDelFlag(0);
        Page<ChemicalFiberProduction> page = chemicalFiberProductionRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(chemicalFiberProductionMapper::toDto));
    }

    @Override
    public List<ChemicalFiberProductionDTO> queryAll(ChemicalFiberProductionQueryCriteria criteria){
        criteria.setDelFlag(0);
        return chemicalFiberProductionMapper.toDto(chemicalFiberProductionRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
//    @Cacheable(key = "#p0")
    public ChemicalFiberProductionDTO findById(Integer id) {
        ChemicalFiberProduction chemicalFiberProduction = chemicalFiberProductionRepository.findById(id).orElseGet(ChemicalFiberProduction::new);
        ValidationUtil.isNull(chemicalFiberProduction.getId(),"ChemicalFiberProduction","id",id);
        return chemicalFiberProductionMapper.toDto(chemicalFiberProduction);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public ChemicalFiberProductionDTO create(ChemicalFiberProduction resources) {
        ChemicalFiberProductDTO chemicalFiberProductDTO = chemicalFiberProductService.findById(resources.getProdId());
        if (resources.getCustomerId() != null) {
            CustomerDTO customerDTO = customerService.findById(resources.getCustomerId());
            resources.setCustomerId(customerDTO.getId());
            resources.setCustomerName(customerDTO.getName());
            resources.setCustomerCode(customerDTO.getCode());
            resources.setCustomerAddress(customerDTO.getAddress());
            resources.setCustomerContacts(customerDTO.getContacts());
            resources.setCustomerContactPhone(customerDTO.getContactPhone());
        }

        resources.setNumber(getChemicalFiberProductionNumber(resources.getCustomerOrderNumber()));
        resources.setProdId(chemicalFiberProductDTO.getId());
        resources.setProdModel(chemicalFiberProductDTO.getModel());
        resources.setProdName(chemicalFiberProductDTO.getName());
        resources.setProdColor(chemicalFiberProductDTO.getColor());
        resources.setProdFineness(chemicalFiberProductDTO.getFineness());
        resources.setStatus(0);
        resources.setCreateTime(new Timestamp(System.currentTimeMillis()));
        resources.setCreateUser(SecurityUtils.getUsername());
        resources.setDelFlag(0);
        resources.setFlowingWater(0);
        return chemicalFiberProductionMapper.toDto(chemicalFiberProductionRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(ChemicalFiberProduction resources) {
        if (resources.getCustomerId() != null) {
            CustomerDTO customerDTO = customerService.findById(resources.getCustomerId());
            resources.setCustomerId(customerDTO.getId());
            resources.setCustomerName(customerDTO.getName());
            resources.setCustomerCode(customerDTO.getCode());
            resources.setCustomerAddress(customerDTO.getAddress());
            resources.setCustomerContactPhone(customerDTO.getContactPhone());
            resources.setCustomerContacts(customerDTO.getContacts());
        }
        ChemicalFiberProductDTO chemicalFiberProductDTO = chemicalFiberProductService.findById(resources.getProdId());
        resources.setProdId(chemicalFiberProductDTO.getId());
        resources.setProdModel(chemicalFiberProductDTO.getModel());
        resources.setProdName(chemicalFiberProductDTO.getName());
        resources.setProdColor(chemicalFiberProductDTO.getColor());
        resources.setProdFineness(chemicalFiberProductDTO.getFineness());

        ChemicalFiberProduction chemicalFiberProduction = chemicalFiberProductionRepository.findById(resources.getId()).orElseGet(ChemicalFiberProduction::new);
        ValidationUtil.isNull( chemicalFiberProduction.getId(),"ChemicalFiberProduction","id",resources.getId());
        chemicalFiberProduction.copy(resources);
        chemicalFiberProductionRepository.save(chemicalFiberProduction);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        ChemicalFiberProduction chemicalFiberProduction = chemicalFiberProductionRepository.findById(id).orElseGet(ChemicalFiberProduction::new);
        chemicalFiberProduction.setDelFlag(1);
        chemicalFiberProductionRepository.save(chemicalFiberProduction);
    }


    @Override
    public void download(List<ChemicalFiberProductionDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ChemicalFiberProductionDTO chemicalFiberProduction : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("订单编号", chemicalFiberProduction.getNumber());
            map.put("产品id", chemicalFiberProduction.getProdId());
            map.put("产品型号", chemicalFiberProduction.getProdModel());
            map.put("产品名称", chemicalFiberProduction.getProdName());
            map.put("产品颜色", chemicalFiberProduction.getProdColor());
            map.put("产品纤度", chemicalFiberProduction.getProdFineness());
            map.put("客户id", chemicalFiberProduction.getCustomerId());
            map.put("客户名称", chemicalFiberProduction.getCustomerName());
            map.put("联系电话", chemicalFiberProduction.getCustomerContactPhone());
            map.put("联系人", chemicalFiberProduction.getCustomerContacts());
            map.put("客户地址", chemicalFiberProduction.getCustomerAddress());
            map.put("客户编号", chemicalFiberProduction.getCustomerCode());
            map.put("纸芯重量", chemicalFiberProduction.getCoreWeight());
            map.put("每袋个数", chemicalFiberProduction.getPerBagNumber());
            map.put("计划生产数量", chemicalFiberProduction.getPlanNumber());
            map.put("完成数量", chemicalFiberProduction.getQuantityCompeted());
            map.put("生产进度(%)", chemicalFiberProduction.getProductionSchedule());
            map.put("交货日期", chemicalFiberProduction.getDeliveryDate());
            map.put("机器编号", chemicalFiberProduction.getMachineNumber());
            map.put("备注", chemicalFiberProduction.getRemark());
            map.put("状态", chemicalFiberProduction.getStatus());
            map.put("制单时间", chemicalFiberProduction.getCreateTime());
            map.put("制单人", chemicalFiberProduction.getCreateUser());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ChemicalFiberProduction setMachines(ChemicalFiberProductionSetMachinesDTO resources) {
        Machine machine = machineRepository.findById(resources.getMachinesId()).orElseGet(Machine::new);
        ChemicalFiberProduction chemicalFiberProduction = chemicalFiberProductionRepository.findById(resources.getProductionId()).orElseGet(ChemicalFiberProduction::new);
        chemicalFiberProduction.setMachineNumber(machine.getNumber());
        chemicalFiberProduction.setStatus(1);
        machine.setStatus(1);
        machine.setProductionId(chemicalFiberProduction.getId());
        machineRepository.save(machine);
        chemicalFiberProductionRepository.save(chemicalFiberProduction);
        return chemicalFiberProduction;
    }

    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ChemicalFiberProduction setProductionStatus(ChemicalFiberProductionSetProductionStatusDTO resources) {
        ChemicalFiberProduction chemicalFiberProduction = chemicalFiberProductionRepository.findById(resources.getProductionId()).orElseGet(ChemicalFiberProduction::new);
        if (!StringUtils.isEmpty(chemicalFiberProduction.getMachineNumber())) {
            Machine machine = machineRepository.findById(Integer.parseInt(chemicalFiberProduction.getMachineNumber())).orElseGet(Machine::new);
            switch (resources.getStatus()) {
                case 0:
                    machine.setStatus(0);
                    chemicalFiberProduction.setMachineNumber("");
                    break;
                case 1:
                    machine.setStatus(1);
                    break;
                case 2:
                    chemicalFiberProduction.setMachineNumber("");
                    machine.setStatus(0);
                    break;
                default:
                    machine.setStatus(0);
                    chemicalFiberProduction.setMachineNumber("");
            }
            machineRepository.save(machine);
        }
        switch (resources.getStatus()) {
            case 0:
                chemicalFiberProduction.setStatus(2);
                break;
            case 1:
                chemicalFiberProduction.setStatus(1);
                break;
            case 2:
                chemicalFiberProduction.setStatus(4);
                break;
            default:
                chemicalFiberProduction.setStatus(3);
        }
        chemicalFiberProductionRepository.save(chemicalFiberProduction);
        return chemicalFiberProduction;
    }

    @Override
    public Map<String, Object> getProductionReport(ChemicalFiberProductionQueryCriteria criteria, Pageable pageable) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String data1 = simpleDateFormat.format(criteria.getTempStartTime());
        String data2 = simpleDateFormat.format(criteria.getTempEndTime());
        Pageable tempPageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize());
        return PageUtil.toPage(chemicalFiberProductionRepository.getProductionReport(
                                                                                        data1,
                                                                                        data2,
                                                                                        StringUtils.isEmpty(criteria.getProdColor()) ? "" : criteria.getProdColor(),
                                                                                        StringUtils.isEmpty(criteria.getProdFineness()) ? "" : criteria.getProdFineness(),
                                                                                        StringUtils.isEmpty(criteria.getMachineNumber()) ? "" : criteria.getMachineNumber(),
                                                                                        StringUtils.isEmpty(criteria.getShifts()) ? "" : criteria.getShifts(),
                                                                                        tempPageable));

    }

    @Override
    public Result getProductionReportSummaries(ChemicalFiberProductionQueryCriteria criteria) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String data1 = simpleDateFormat.format(criteria.getTempStartTime());
        String data2 = simpleDateFormat.format(criteria.getTempEndTime());
        List<Map<String, Object>> maps = chemicalFiberProductionRepository.getProductionReportSummaries(
                                                                        data1,
                                                                        data2,
                                                                        StringUtils.isEmpty(criteria.getProdColor()) ? "" : criteria.getProdColor(),
                                                                        StringUtils.isEmpty(criteria.getProdFineness()) ? "" : criteria.getProdFineness(),
                                                                        StringUtils.isEmpty(criteria.getMachineNumber()) ? "" : criteria.getMachineNumber(),
                                                                        StringUtils.isEmpty(criteria.getShifts()) ? "" : criteria.getShifts());



        // 入库包数
        Integer warehousingPacketNumber = 0;
        // 实际每袋个数
        Integer warehousingFactPerBagNumber = 0;

        // 净重
        BigDecimal warehousingNetWeight = new BigDecimal(0.0);

        // 毛重
        BigDecimal warehousingGrossWeight = new BigDecimal(0.0);

        // 出库
        // 包数
        Integer outOfstockPacketNumber = 0;

        // 实际每袋个数
        Integer outOfstockFactPerBagNumber = 0;

        // 净重
        BigDecimal outOfstockNetWeight = new BigDecimal(0.0);

        // 毛重
        BigDecimal outOfstockGrossWeight = new BigDecimal(0.0);

        // 作废
        // 包数
        Integer toVoidPacketNumber = 0;

        // 实际每袋个数
        Integer toVoidFactPerBagNumber = 0;

        // 净重
        BigDecimal toVoidNetWeight = new BigDecimal(0.0);

        // 毛重
        BigDecimal toVoidGrossWeight = new BigDecimal(0.0);
        List<Object> list = new ArrayList<>();
        list.add("总计");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        for (Map<String, Object> map : maps) {
            warehousingPacketNumber = warehousingPacketNumber + Integer.parseInt(map.get("in_stock_pack").toString());
            warehousingFactPerBagNumber = warehousingFactPerBagNumber + Integer.parseInt(map.get("in_stock_number").toString());
            warehousingNetWeight = warehousingNetWeight.add(new BigDecimal(map.get("in_net_weight").toString()));
            warehousingGrossWeight = warehousingGrossWeight.add(new BigDecimal(map.get("in_gross_weight").toString()));

            outOfstockPacketNumber = outOfstockPacketNumber + Integer.parseInt(map.get("out_stock_pack").toString());
            outOfstockFactPerBagNumber = outOfstockFactPerBagNumber + Integer.parseInt(map.get("out_stock_number").toString());
            outOfstockNetWeight = outOfstockNetWeight.add(new BigDecimal(map.get("out_net_weight").toString()));
            outOfstockGrossWeight = outOfstockGrossWeight.add(new BigDecimal(map.get("out_gross_weight").toString()));

            /*toVoidPacketNumber = toVoidPacketNumber + Integer.parseInt(map.get("cancel_stock_pack").toString());
            toVoidFactPerBagNumber = toVoidFactPerBagNumber + Integer.parseInt(map.get("cancel_stock_number").toString());
            toVoidNetWeight = toVoidNetWeight.add(new BigDecimal(map.get("cancel_net_weight").toString()));
            toVoidGrossWeight = toVoidGrossWeight.add(new BigDecimal(map.get("cancel_gross_weight").toString()));*/
        }

        list.add(warehousingPacketNumber);
        list.add(warehousingFactPerBagNumber);
        list.add(warehousingNetWeight);
        list.add(warehousingGrossWeight);
        list.add(outOfstockPacketNumber);
        list.add(outOfstockFactPerBagNumber);
        list.add(outOfstockNetWeight);
        list.add(outOfstockGrossWeight);
    /*    list.add("");
        list.add("");
        list.add(toVoidPacketNumber);
        list.add(toVoidFactPerBagNumber);
        list.add(toVoidNetWeight);
        list.add(toVoidGrossWeight);*/
        return Result.success(list);
    }

    private String getChemicalFiberProductionNumber(String st) {
        String productionNumber;
        ChemicalFiberProductionQueryCriteria criteria = new ChemicalFiberProductionQueryCriteria();
        Map<String, Object> timeMap = handheldService.monthTimeInMillis();
        String yea = timeMap.get("year").toString();
        String year = yea.substring(2 );
        String month = timeMap.get("month").toString();
        String day = timeMap.get("day").toString();
        //criteria.setStartTime(new Timestamp(Long.parseLong(timeMap.get("time").toString())));
        //criteria.setEndTime(new Timestamp(System.currentTimeMillis()));
        criteria.setCustomerOrderNumber(st);
        List<ChemicalFiberProductionDTO> chemicalFiberProductionDTOS = chemicalFiberProductionMapper.toDto(chemicalFiberProductionRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));

        if (chemicalFiberProductionDTOS.size() == 0) {
            productionNumber = st + "01";
        } else {
            Integer chemicalFiberProductionDTOSSize =  chemicalFiberProductionDTOS.size();
            String tempNumberStr = String.format("%2d", (chemicalFiberProductionDTOSSize + 1)).replace(" ", "0");
            productionNumber = st + tempNumberStr;
        }
        return productionNumber;
    }

    //定义方法
    public static String chufa(int a,int b) {
        //“0.00000000”确定精度
        DecimalFormat dF = new DecimalFormat("0.00");
        return dF.format((float)a/b);
    }

    public void downloadProduct(ChemicalFiberProductionQueryCriteria criteria,  Pageable pageable, HttpServletResponse response) throws IOException {
        // 入库包数
        Integer warehousingPacketNumber = 0;
        // 实际每袋个数
        Integer warehousingFactPerBagNumber = 0;

        // 净重
        BigDecimal warehousingNetWeight = new BigDecimal(0.0);

        // 毛重
        BigDecimal warehousingGrossWeight = new BigDecimal(0.0);

        // 出库
        // 包数
        Integer outOfstockPacketNumber = 0;

        // 实际每袋个数
        Integer outOfstockFactPerBagNumber = 0;

        // 净重
        BigDecimal outOfstockNetWeight = new BigDecimal(0.0);

        // 毛重
        BigDecimal outOfstockGrossWeight = new BigDecimal(0.0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String data1 = simpleDateFormat.format(criteria.getTempStartTime());
        String data2 = simpleDateFormat.format(criteria.getTempEndTime());
        Pageable tempPageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize());
        List<Map<String, Object>> ProdctionList = chemicalFiberProductionRepository.getProductionReportSummaries(data1, data2,
                StringUtils.isEmpty(criteria.getProdColor()) ? "" : criteria.getProdColor(),
                StringUtils.isEmpty(criteria.getProdFineness()) ? "" : criteria.getProdFineness(),
                StringUtils.isEmpty(criteria.getMachineNumber()) ? "" : criteria.getMachineNumber(),
                StringUtils.isEmpty(criteria.getShifts()) ? "" : criteria.getShifts());
        List<Map<String, Object>> listMap = new ArrayList<>();
        for (int i = 1; i <= ProdctionList.size(); i++) {
            Map<String, Object> dto = new HashMap<>();
            Map<String, Object> dto2 = ProdctionList.get(i - 1);
            dto.putAll(dto2);
            dto.put("index" ,i);
            warehousingPacketNumber = warehousingPacketNumber + Integer.parseInt(dto.get("in_stock_pack").toString());
            warehousingFactPerBagNumber = warehousingFactPerBagNumber + Integer.parseInt(dto.get("in_stock_number").toString());
            warehousingNetWeight = warehousingNetWeight.add(new BigDecimal(dto.get("in_net_weight").toString()));
            warehousingGrossWeight = warehousingGrossWeight.add(new BigDecimal(dto.get("in_gross_weight").toString()));

            outOfstockPacketNumber = outOfstockPacketNumber + Integer.parseInt(dto.get("out_stock_pack").toString());
            outOfstockFactPerBagNumber = outOfstockFactPerBagNumber + Integer.parseInt(dto.get("out_stock_number").toString());
            outOfstockNetWeight = outOfstockNetWeight.add(new BigDecimal(dto.get("out_net_weight").toString()));
            outOfstockGrossWeight = outOfstockGrossWeight.add(new BigDecimal(dto.get("out_gross_weight").toString()));
            listMap.add(dto);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("ProdctionList", listMap);
        map.put("warehousingPacketNumber", warehousingPacketNumber);
        map.put("warehousingFactPerBagNumber", warehousingFactPerBagNumber);
        map.put("warehousingNetWeight", warehousingNetWeight);
        map.put("warehousingGrossWeight", warehousingGrossWeight);
        map.put("outOfstockPacketNumber", outOfstockPacketNumber);
        map.put("outOfstockFactPerBagNumber", outOfstockFactPerBagNumber);
        map.put("outOfstockNetWeight", outOfstockNetWeight);
        map.put("outOfstockGrossWeight", outOfstockGrossWeight);
        Workbook workbook = null;
        String templatePath = new TemplateConfig("template/excel", TemplateConfig.ResourceMode.CLASSPATH).getPath() + "/prodtion.xls";
        // 加载模板
        TemplateExportParams params = new TemplateExportParams(templatePath);
        workbook = ExcelExportUtil.exportExcel(params, map);
        FileUtil.downLoadExcel("生产报表导出.xls", response, workbook);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateList(List<ChemicalFiberProduction> resources) {
        /*ChemicalFiberProduction ChemicalFiberProduction = chemicalFiberProductionRepository.findById(resources.getId()).orElseGet(ChemicalFiberCustomerOrder::new);
        ValidationUtil.isNull( chemicalFiberCustomerOrder.getId(),"ChemicalFiberCustomerOrder","id",resources.getId());
        chemicalFiberCustomerOrder.copy(resources);*/
        chemicalFiberProductionRepository.saveAll(resources);
    }


}
