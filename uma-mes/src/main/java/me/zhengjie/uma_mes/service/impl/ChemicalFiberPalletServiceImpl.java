package me.zhengjie.uma_mes.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelStyleType;
import cn.hutool.extra.template.TemplateConfig;
import me.zhengjie.uma_mes.domain.ChemicalFiberPallet;
import me.zhengjie.uma_mes.domain.ChemicalFiberPalletDetail;
import me.zhengjie.uma_mes.repository.ChemicalFiberPalletDetailRepository;
import me.zhengjie.uma_mes.repository.ChemicalFiberPalletRepository;
import me.zhengjie.uma_mes.service.ChemicalFiberPalletService;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberPalletQueryCeiteria;
import me.zhengjie.uma_mes.service.mapper.ChemicalFiberPalletMapper;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import static cn.afterturn.easypoi.excel.ExcelExportUtil.SHEET_NAME;

@Service
@CacheConfig(cacheNames = "chemicalFiberPallet")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ChemicalFiberPalletServiceImpl implements ChemicalFiberPalletService {

    @Autowired
    private ChemicalFiberPalletRepository chemicalFiberPalletRepository;

    @Autowired
    private ChemicalFiberPalletDetailRepository chemicalFiberPalletDetailRepository;

    @Autowired
    private ChemicalFiberPalletMapper chemicalFiberPalletMapper;

    public Map<String, Object> queryAll(ChemicalFiberPalletQueryCeiteria criteria, Pageable pageable) {
        /*if (criteria.getTempStartTime() != null) {
            criteria.setStartTime(new Timestamp(criteria.getTempStartTime()));
            criteria.setEndTime(new Timestamp(criteria.getTempEndTime()));
        }*/
        Page<ChemicalFiberPallet> page = chemicalFiberPalletRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(chemicalFiberPalletMapper::toDto));

    }

    @Override
    public void exportPoundExcel(ChemicalFiberPallet chemicalFiberPallet, HttpServletResponse response) {
        List<ChemicalFiberPalletDetail> PalletDetail = chemicalFiberPalletDetailRepository.getPalletDateil(chemicalFiberPallet.getPalletNumber());

        int tempIndex = 1;
        int sheelAllitem = 100;
        List<Map<String, Object>> list = new ArrayList<>();
        BigDecimal totalTotalWeights=new BigDecimal(0);
        int sheelPages = (PalletDetail.size() + sheelAllitem) / sheelAllitem;
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
        Map<String, String> row = initRow();
        BigDecimal rowTotal = new BigDecimal(0);
        BigDecimal total = new BigDecimal(0);
        BigDecimal totalWeight = new BigDecimal(0);
        for (int i = tempIndex; i < PalletDetail.size()+1; i++) {
            total = total.add(new BigDecimal(1));
            totalWeight = totalWeight.add(PalletDetail.get(i - 1).getNetWeight());
            Integer yu = i % 10;

            BigDecimal cellValue = PalletDetail.get(i - 1).getNetWeight();
            rowTotal = rowTotal.add(cellValue);

            if (i % 10 == 0) {
                row.put("10", cellValue.toString());
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
/*
        for (Map<String, Object> map : list) {
            map.put("weight", totalTotalWeights);
        }*/

        String templatePath = new TemplateConfig("template/excel", TemplateConfig.ResourceMode.CLASSPATH).getPath() + "/pound_temp.xls";
//        // 加载模板
        TemplateExportParams params = new TemplateExportParams(templatePath);
        Workbook book = ExcelExportUtil.exportExcelClone(map, params);
        FileUtil.downLoadExcel("磅码单导出.xls", response, book);

    }

    private Map<String,String> initRow(){
        Map<String,String> result=new HashMap<>();
        for (int j = 1; j < 11; j++) {
            result.put(Integer.toString(j),"");
        }
        result.put("total","");
        return result;
    }
}
