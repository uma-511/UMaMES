package me.zhengjie.uma_mes.service.impl;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
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
    @Cacheable
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

        resources.setNumber(getChemicalFiberProductionNumber());
        resources.setProdId(chemicalFiberProductDTO.getId());
        resources.setProdModel(chemicalFiberProductDTO.getModel());
        resources.setProdName(chemicalFiberProductDTO.getName());
        resources.setProdColor(chemicalFiberProductDTO.getColor());
        resources.setProdFineness(chemicalFiberProductDTO.getFineness());
        resources.setStatus(0);
        resources.setCreateTime(new Timestamp(System.currentTimeMillis()));
        resources.setCreateUser(SecurityUtils.getUsername());
        resources.setDelFlag(0);
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
        if (criteria.getTempStartTime() != null) {
            criteria.setStartTime(new Timestamp(criteria.getTempStartTime()));
            criteria.setEndTime(new Timestamp(criteria.getTempEndTime()));
        }
        criteria.setDelFlag(0);
        Page<ChemicalFiberProduction> page = chemicalFiberProductionRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        List<ChemicalFiberProductionReportDTO> chemicalFiberProductionReportDTOS = new ArrayList<>();
        for (ChemicalFiberProduction chemicalFiberProduction : page.getContent()) {
            ChemicalFiberProductionReportDTO chemicalFiberProductionReportDTO = new ChemicalFiberProductionReportDTO();
            chemicalFiberProductionReportDTO.setNumber(chemicalFiberProduction.getNumber());
            chemicalFiberProductionReportDTO.setProdColor(chemicalFiberProduction.getProdColor());
            chemicalFiberProductionReportDTO.setProdFineness(chemicalFiberProduction.getProdFineness());
            for (ChemicalFiberLabel chemicalFiberLabel : chemicalFiberProduction.getChemicalFiberLabels()) {
                // 0：待入库 1：入库 2：出库 3：作废 4：退库 5：退货
                switch (chemicalFiberLabel.getStatus()) {
                    case 1:
                        chemicalFiberProductionReportDTO.setWarehousingPacketNumber(chemicalFiberProductionReportDTO.getWarehousingPacketNumber() + 1);
                        chemicalFiberProductionReportDTO.setWarehousingFactPerBagNumber(chemicalFiberProductionReportDTO.getWarehousingFactPerBagNumber() + chemicalFiberLabel.getFactPerBagNumber());
                        chemicalFiberProductionReportDTO.setWarehousingNetWeight(chemicalFiberProductionReportDTO.getWarehousingNetWeight().add(chemicalFiberLabel.getNetWeight()));
                        chemicalFiberProductionReportDTO.setWarehousingGrossWeight(chemicalFiberProductionReportDTO.getWarehousingGrossWeight().add(chemicalFiberLabel.getGrossWeight()));
                        break;
                    case 2:
                        chemicalFiberProductionReportDTO.setOutOfstockPacketNumber(chemicalFiberProductionReportDTO.getOutOfstockPacketNumber() + 1);
                        chemicalFiberProductionReportDTO.setOutOfstockFactPerBagNumber(chemicalFiberProductionReportDTO.getOutOfstockFactPerBagNumber() + chemicalFiberLabel.getFactPerBagNumber());
                        chemicalFiberProductionReportDTO.setOutOfstockNetWeight(chemicalFiberProductionReportDTO.getOutOfstockNetWeight().add(chemicalFiberLabel.getNetWeight()));
                        chemicalFiberProductionReportDTO.setOutOfstockGrossWeight(chemicalFiberProductionReportDTO.getOutOfstockGrossWeight().add(chemicalFiberLabel.getGrossWeight()));
                        break;
                    case 3:
                        chemicalFiberProductionReportDTO.setToVoidPacketNumber(chemicalFiberProductionReportDTO.getToVoidPacketNumber() + 1);
                        chemicalFiberProductionReportDTO.setToVoidFactPerBagNumber(chemicalFiberProductionReportDTO.getToVoidFactPerBagNumber() + chemicalFiberLabel.getFactPerBagNumber());
                        chemicalFiberProductionReportDTO.setToVoidNetWeight(chemicalFiberProductionReportDTO.getToVoidNetWeight().add(chemicalFiberLabel.getNetWeight()));
                        chemicalFiberProductionReportDTO.setToVoidGrossWeight(chemicalFiberProductionReportDTO.getToVoidGrossWeight().add(chemicalFiberLabel.getGrossWeight()));
                        break;
                }
            }
            double tempWarehousingPacketRatio = chemicalFiberProductionReportDTO.getWarehousingFactPerBagNumber() == 0 ? 0.0 : Double.parseDouble(chufa((chemicalFiberProductionReportDTO.getWarehousingFactPerBagNumber()), chemicalFiberProductionReportDTO.getWarehousingFactPerBagNumber() + chemicalFiberProductionReportDTO.getOutOfstockFactPerBagNumber()));
            BigDecimal tempWarehousingNetWeightRatio = chemicalFiberProductionReportDTO.getWarehousingNetWeight().compareTo(BigDecimal.ZERO) == 0 ? new BigDecimal(0.0) : chemicalFiberProductionReportDTO.getWarehousingNetWeight().divide(chemicalFiberProductionReportDTO.getWarehousingNetWeight().add(chemicalFiberProductionReportDTO.getOutOfstockNetWeight()), 2, BigDecimal.ROUND_HALF_UP);
            chemicalFiberProductionReportDTO.setWarehousingPacketRatio(tempWarehousingPacketRatio * 100);
            chemicalFiberProductionReportDTO.setWarehousingNetWeightRatio(tempWarehousingNetWeightRatio.multiply(new BigDecimal(100)));
            chemicalFiberProductionReportDTOS.add(chemicalFiberProductionReportDTO);
        }
        return PageUtil.toPage(new PageImpl<ChemicalFiberProductionReportDTO>(chemicalFiberProductionReportDTOS, pageable, page.getTotalElements()));

//        Page<ChemicalFiberLabel> page = chemicalFiberLabelRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
//        List<ChemicalFiberProductionReportDTO> chemicalFiberProductionReportDTOS = new ArrayList<>();
//        for (ChemicalFiberLabel chemicalFiberLabel : page.getContent()) {
//            ChemicalFiberProductionReportDTO chemicalFiberProductionReportDTO = new ChemicalFiberProductionReportDTO();
//            chemicalFiberProductionReportDTO.setNumber(chemicalFiberLabel.getLabelNumber());
//            chemicalFiberProductionReportDTO.setProdColor(chemicalFiberLabel.getColor());
//            chemicalFiberProductionReportDTO.setProdFineness(chemicalFiberLabel.getFineness());
////            for (ChemicalFiberLabel chemicalFiberLabel : chemicalFiberLabel.getChemicalFiberLabels()) {
////
////            }
//            // 0：待入库 1：入库 2：出库 3：作废 4：退库 5：退货
//            switch (chemicalFiberLabel.getStatus()) {
//                case 1:
//                    chemicalFiberProductionReportDTO.setWarehousingPacketNumber(chemicalFiberProductionReportDTO.getWarehousingPacketNumber() + 1);
//                    chemicalFiberProductionReportDTO.setWarehousingFactPerBagNumber(chemicalFiberProductionReportDTO.getWarehousingFactPerBagNumber() + chemicalFiberLabel.getFactPerBagNumber());
//                    chemicalFiberProductionReportDTO.setWarehousingNetWeight(chemicalFiberProductionReportDTO.getWarehousingNetWeight().add(chemicalFiberLabel.getNetWeight()));
//                    chemicalFiberProductionReportDTO.setWarehousingGrossWeight(chemicalFiberProductionReportDTO.getWarehousingGrossWeight().add(chemicalFiberLabel.getGrossWeight()));
//                    break;
//                case 2:
//                    chemicalFiberProductionReportDTO.setOutOfstockPacketNumber(chemicalFiberProductionReportDTO.getOutOfstockPacketNumber() + 1);
//                    chemicalFiberProductionReportDTO.setOutOfstockFactPerBagNumber(chemicalFiberProductionReportDTO.getOutOfstockFactPerBagNumber() + chemicalFiberLabel.getFactPerBagNumber());
//                    chemicalFiberProductionReportDTO.setOutOfstockNetWeight(chemicalFiberProductionReportDTO.getOutOfstockNetWeight().add(chemicalFiberLabel.getNetWeight()));
//                    chemicalFiberProductionReportDTO.setOutOfstockGrossWeight(chemicalFiberProductionReportDTO.getOutOfstockGrossWeight().add(chemicalFiberLabel.getGrossWeight()));
//                    break;
//                case 3:
//                    chemicalFiberProductionReportDTO.setToVoidPacketNumber(chemicalFiberProductionReportDTO.getToVoidPacketNumber() + 1);
//                    chemicalFiberProductionReportDTO.setToVoidFactPerBagNumber(chemicalFiberProductionReportDTO.getToVoidFactPerBagNumber() + chemicalFiberLabel.getFactPerBagNumber());
//                    chemicalFiberProductionReportDTO.setToVoidNetWeight(chemicalFiberProductionReportDTO.getToVoidNetWeight().add(chemicalFiberLabel.getNetWeight()));
//                    chemicalFiberProductionReportDTO.setToVoidGrossWeight(chemicalFiberProductionReportDTO.getToVoidGrossWeight().add(chemicalFiberLabel.getGrossWeight()));
//                    break;
//            }
//            double tempWarehousingPacketRatio = chemicalFiberProductionReportDTO.getWarehousingFactPerBagNumber() == 0 ? 0.0 : Double.parseDouble(chufa((chemicalFiberProductionReportDTO.getWarehousingFactPerBagNumber()), chemicalFiberProductionReportDTO.getWarehousingFactPerBagNumber() + chemicalFiberProductionReportDTO.getOutOfstockFactPerBagNumber()));
//            BigDecimal tempWarehousingNetWeightRatio = chemicalFiberProductionReportDTO.getWarehousingNetWeight().compareTo(BigDecimal.ZERO) == 0 ? new BigDecimal(0.0) : chemicalFiberProductionReportDTO.getWarehousingNetWeight().divide(chemicalFiberProductionReportDTO.getWarehousingNetWeight().add(chemicalFiberProductionReportDTO.getOutOfstockNetWeight()), 2, BigDecimal.ROUND_HALF_UP);
//            chemicalFiberProductionReportDTO.setWarehousingPacketRatio(tempWarehousingPacketRatio * 100);
//            chemicalFiberProductionReportDTO.setWarehousingNetWeightRatio(tempWarehousingNetWeightRatio.multiply(new BigDecimal(100)));
//            chemicalFiberProductionReportDTOS.add(chemicalFiberProductionReportDTO);
//        }
//        return PageUtil.toPage(new PageImpl<ChemicalFiberProductionReportDTO>(chemicalFiberProductionReportDTOS, pageable, page.getTotalElements()));

    }

    @Override
    public Result getProductionReportSummaries(ChemicalFiberProductionQueryCriteria criteria) {
        if (criteria.getTempStartTime() != null) {
            criteria.setStartTime(new Timestamp(criteria.getTempStartTime()));
            criteria.setEndTime(new Timestamp(criteria.getTempEndTime()));
        }
        criteria.setDelFlag(0);
        List<ChemicalFiberProductionDTO> chemicalFiberProductionDTOS = chemicalFiberProductionMapper.toDto(chemicalFiberProductionRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
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
        for (ChemicalFiberProductionDTO chemicalFiberProductionDTO : chemicalFiberProductionDTOS) {
            for (ChemicalFiberLabel chemicalFiberLabel : chemicalFiberProductionDTO.getChemicalFiberLabels()) {
                switch (chemicalFiberLabel.getStatus()) {
                    case 1:
                        warehousingPacketNumber = warehousingPacketNumber + 1;
                        warehousingFactPerBagNumber = warehousingFactPerBagNumber + chemicalFiberLabel.getFactPerBagNumber();
                        warehousingNetWeight = warehousingNetWeight.add(chemicalFiberLabel.getNetWeight());
                        warehousingGrossWeight = warehousingGrossWeight.add(chemicalFiberLabel.getGrossWeight());
                        break;
                    case 2:
                        outOfstockPacketNumber = outOfstockPacketNumber + 1;
                        outOfstockFactPerBagNumber = outOfstockFactPerBagNumber + chemicalFiberLabel.getFactPerBagNumber();
                        outOfstockNetWeight = outOfstockNetWeight.add(chemicalFiberLabel.getNetWeight());
                        outOfstockGrossWeight = outOfstockGrossWeight.add(chemicalFiberLabel.getGrossWeight());
                        break;
                    case 3:
                        toVoidPacketNumber = toVoidPacketNumber + 1;
                        toVoidFactPerBagNumber = toVoidFactPerBagNumber + chemicalFiberLabel.getFactPerBagNumber();
                        toVoidNetWeight = toVoidNetWeight.add(chemicalFiberLabel.getNetWeight());
                        toVoidGrossWeight = toVoidGrossWeight.add(chemicalFiberLabel.getGrossWeight());
                        break;
                }
            }
        }
        list.add(warehousingPacketNumber);
        list.add(warehousingFactPerBagNumber);
        list.add(warehousingNetWeight);
        list.add(warehousingGrossWeight);
        list.add(outOfstockPacketNumber);
        list.add(outOfstockFactPerBagNumber);
        list.add(outOfstockNetWeight);
        list.add(outOfstockGrossWeight);
        list.add("");
        list.add("");
        list.add(toVoidPacketNumber);
        list.add(toVoidFactPerBagNumber);
        list.add(toVoidNetWeight);
        list.add(toVoidGrossWeight);
        return Result.success(list);
    }

    private String getChemicalFiberProductionNumber() {
        String productionNumber;
        ChemicalFiberProductionQueryCriteria criteria = new ChemicalFiberProductionQueryCriteria();
        Map<String, Object> timeMap = handheldService.monthTimeInMillis();
        String year = timeMap.get("year").toString();
        String month = timeMap.get("month").toString();
        criteria.setStartTime(new Timestamp(Long.parseLong(timeMap.get("time").toString())));
        criteria.setEndTime(new Timestamp(System.currentTimeMillis()));
        List<ChemicalFiberProductionDTO> chemicalFiberProductionDTOS = chemicalFiberProductionMapper.toDto(chemicalFiberProductionRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));

        if (chemicalFiberProductionDTOS.size() == 0) {
            productionNumber = year + month + "0001";
        } else {
            Integer chemicalFiberProductionDTOSSize =  chemicalFiberProductionDTOS.size();
            String tempNumberStr = String.format("%4d", (chemicalFiberProductionDTOSSize + 1)).replace(" ", "0");
            productionNumber = year + month + tempNumberStr;
        }
        return productionNumber;
    }

    //定义方法
    public static String chufa(int a,int b) {
        //“0.00000000”确定精度
        DecimalFormat dF = new DecimalFormat("0.00");
        return dF.format((float)a/b);
    }


}