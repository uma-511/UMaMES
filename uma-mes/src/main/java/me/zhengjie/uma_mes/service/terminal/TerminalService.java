package me.zhengjie.uma_mes.service.terminal;

import com.lgmn.common.result.Result;
import com.lgmn.common.utils.ObjectTransfer;
import me.zhengjie.uma_mes.domain.ChemicalFiberProduct;
import me.zhengjie.uma_mes.domain.ChemicalFiberProduction;
import me.zhengjie.uma_mes.domain.ChemicalFiberStock;
import me.zhengjie.uma_mes.repository.ChemicalFiberProductionRepository;
import me.zhengjie.uma_mes.service.ChemicalFiberProductService;
import me.zhengjie.uma_mes.service.ChemicalFiberStockService;
import me.zhengjie.uma_mes.service.dto.*;
import me.zhengjie.uma_mes.service.dto.termina.TerminalUploadDataDto;
import me.zhengjie.uma_mes.service.handheld.HandheldService;
import me.zhengjie.uma_mes.service.mapper.ChemicalFiberProductionMapper;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TerminalService {

    @Autowired
    HandheldService handheldService;

    private final ChemicalFiberProductionRepository chemicalFiberProductionRepository;

    private final ChemicalFiberProductService chemicalFiberProductService;

    private final ChemicalFiberStockService chemicalFiberStockService;

    private final ChemicalFiberProductionMapper chemicalFiberProductionMapper;

    public TerminalService(ChemicalFiberProductService chemicalFiberProductService,
                           ChemicalFiberStockService chemicalFiberStockService,
                           ChemicalFiberProductionMapper chemicalFiberProductionMapper,
                           ChemicalFiberProductionRepository chemicalFiberProductionRepository) {
        this.chemicalFiberProductService = chemicalFiberProductService;
        this.chemicalFiberStockService = chemicalFiberStockService;
        this.chemicalFiberProductionMapper = chemicalFiberProductionMapper;
        this.chemicalFiberProductionRepository = chemicalFiberProductionRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public ChemicalFiberProduction terminalUploadData(TerminalUploadDataDto terminalUploadDataDto, Boolean print) {

        ChemicalFiberProduct chemicalFiberProduct = new ChemicalFiberProduct();
        String modelAndName = terminalUploadDataDto.getColor() + "-" + terminalUploadDataDto.getFineness();

        ChemicalFiberProductQueryCriteria chemicalFiberProductQueryCriteria = new ChemicalFiberProductQueryCriteria();
        chemicalFiberProductQueryCriteria.setModelAccurate(modelAndName);
        List<ChemicalFiberProductDTO> chemicalFiberProductDTOS = chemicalFiberProductService.queryAll(chemicalFiberProductQueryCriteria);
        if (chemicalFiberProductDTOS.size() <= 0) {
            // 添加产品
            chemicalFiberProduct.setModel(modelAndName);
            chemicalFiberProduct.setName(modelAndName);
            chemicalFiberProduct.setColor(terminalUploadDataDto.getColor());
            chemicalFiberProduct.setFineness(terminalUploadDataDto.getFineness());
            chemicalFiberProduct.setCreateUser("admin");
            chemicalFiberProduct.setCreateDate(new Timestamp(System.currentTimeMillis()));
            chemicalFiberProduct.setDelFlag(0);
            chemicalFiberProductService.createForTerminal(chemicalFiberProduct);


            // 添加库存
            saveChemicalFiberStock(chemicalFiberProduct);
        } else {
            ChemicalFiberProductDTO chemicalFiberProductDTO = chemicalFiberProductDTOS.get(0);
            ObjectTransfer.transValue(chemicalFiberProductDTO, chemicalFiberProduct);
        }
        ChemicalFiberProduction chemicalFiberProduction = getChemicalFiberProduction(chemicalFiberProduct, terminalUploadDataDto, print);
        return chemicalFiberProduction;
    }

    private ChemicalFiberProduction getChemicalFiberProduction(ChemicalFiberProduct chemicalFiberProduct, TerminalUploadDataDto terminalUploadDataDto, Boolean print) {
        ChemicalFiberProduction production = null;
        List<ChemicalFiberProduction> productions = chemicalFiberProductionRepository.findByProdIdAndMachineNumber(chemicalFiberProduct.getId(),terminalUploadDataDto.getMachineNumber());
        if(productions==null || productions.size()==0 || print) {
            // 添加生产单
            ChemicalFiberProduction chemicalFiberProduction = new ChemicalFiberProduction();
            chemicalFiberProduction.setNumber(getChemicalFiberProductionNumber());
            chemicalFiberProduction.setProdId(chemicalFiberProduct.getId());
            chemicalFiberProduction.setProdModel(chemicalFiberProduct.getModel());
            chemicalFiberProduction.setProdName(chemicalFiberProduct.getName());
            chemicalFiberProduction.setProdFineness(chemicalFiberProduct.getFineness());
            chemicalFiberProduction.setProdColor(chemicalFiberProduct.getColor());
            chemicalFiberProduction.setCoreWeight(new BigDecimal(0.1));
            chemicalFiberProduction.setPerBagNumber(1);
            chemicalFiberProduction.setPlanNumber(new BigDecimal(999));
            chemicalFiberProduction.setDeliveryDate(new Timestamp(System.currentTimeMillis()));
            chemicalFiberProduction.setMachineNumber(terminalUploadDataDto.getMachineNumber());
            chemicalFiberProduction.setStatus(1);
            chemicalFiberProduction.setCreateTime(new Timestamp(System.currentTimeMillis()));
            chemicalFiberProduction.setCreateUser("admin");
            chemicalFiberProduction.setDelFlag(0);
            production = chemicalFiberProductionRepository.save(chemicalFiberProduction);
        }else {
            production = productions.get(0);
        }
        return production;
    }

    public void saveChemicalFiberStock(ChemicalFiberProduct chemicalFiberProduct) {
        ChemicalFiberStock chemicalFiberStock = new ChemicalFiberStock();
        chemicalFiberStock.setProdId(chemicalFiberProduct.getId());
        chemicalFiberStock.setProdName(chemicalFiberProduct.getName());
        chemicalFiberStock.setProdModel(chemicalFiberProduct.getModel());
        chemicalFiberStock.setProdColor(chemicalFiberProduct.getColor());
        chemicalFiberStock.setProdFineness(chemicalFiberProduct.getFineness());
        ChemicalFiberStock fiberStock = chemicalFiberStockService.findByColorAndFineness(chemicalFiberProduct.getColor(),chemicalFiberProduct.getFineness());
        if(fiberStock==null) {
            chemicalFiberStockService.create(chemicalFiberStock);
            chemicalFiberStockService.stockTask();
        }
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

}
