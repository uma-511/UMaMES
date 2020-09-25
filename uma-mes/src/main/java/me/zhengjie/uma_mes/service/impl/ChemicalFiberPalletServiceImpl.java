package me.zhengjie.uma_mes.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelStyleType;
import cn.hutool.extra.template.TemplateConfig;
import com.lgmn.common.utils.ObjectTransfer;
import me.zhengjie.uma_mes.domain.ChemicalFiberPallet;
import me.zhengjie.uma_mes.domain.ChemicalFiberPalletDetail;
import me.zhengjie.uma_mes.repository.ChemicalFiberPalletDetailRepository;
import me.zhengjie.uma_mes.repository.ChemicalFiberPalletRepository;
import me.zhengjie.uma_mes.service.ChemicalFiberPalletService;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberPalletDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberPalletQueryCeiteria;
import me.zhengjie.uma_mes.service.mapper.ChemicalFiberPalletMapper;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.krysalis.barcode4j.HumanReadablePlacement;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

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
        if (criteria.getTempStartTime() != null) {
            criteria.setStartTime(new Timestamp(criteria.getTempStartTime()));
            criteria.setEndTime(new Timestamp(criteria.getTempEndTime()));
        }
        Page<ChemicalFiberPallet> page = chemicalFiberPalletRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        List<ChemicalFiberPallet> list = page.getContent();
        List<Map<String, Object>> number = chemicalFiberPalletRepository.getNumbar();
        Map<String, ChemicalFiberPalletDTO> numberDTO = new HashMap();
        for (Map<String, Object> number1 : number) {
            ChemicalFiberPalletDTO dto = new ChemicalFiberPalletDTO();
            String totalBagStr = number1.get("total_bag").toString();
            Integer totalBag = Integer.parseInt(totalBagStr);
            dto.setTotalBag(totalBag);

            String totalNumberStr = number1.get("total_number").toString();
            Integer totalNumber = Integer.parseInt(totalNumberStr);
            dto.setTotalNumber(totalNumber);
            dto.setGrossWeight((BigDecimal)number1.get("total_gross_weight"));
            dto.setNetWeight((BigDecimal)number1.get("total_net_weight"));
            dto.setTare((BigDecimal)number1.get("total_tare"));
            String warehousingBagStr = number1.get("warehousing_bag").toString();
            Integer warehousingBag = Integer.parseInt(warehousingBagStr);
            dto.setWarehousingBag(warehousingBag);

            String warehousingNumberStr = number1.get("warehousing_number").toString();
            Integer warehousingNumber = Integer.parseInt(warehousingNumberStr);
            dto.setWarehousingNumber(warehousingNumber);
            numberDTO.put((String)number1.get("pallet_id"), dto);
        }
        List<ChemicalFiberPalletDTO> pageDTO = new ArrayList<>();
        for (ChemicalFiberPallet pallet : list) {
            ChemicalFiberPalletDTO Pages = new ChemicalFiberPalletDTO();
            ObjectTransfer.transValue(pallet, Pages);
            ChemicalFiberPalletDTO getnumber = numberDTO.get(pallet.getPalletNumber());
            ObjectTransfer.transValue(getnumber, Pages);
            pageDTO.add(Pages);
        }
        /*return PageUtil.toPage(page.map(chemicalFiberPalletMapper::toDto));*/
        return PageUtil.toPage(new PageImpl(pageDTO, pageable, page.getTotalElements()));

    }

    @Override
    public void exportPoundExcel(ChemicalFiberPallet chemicalFiberPallet, HttpServletResponse response) {
        List<ChemicalFiberPalletDetail> PalletDetail = chemicalFiberPalletDetailRepository.getPalletDateil(chemicalFiberPallet.getPalletNumber());
        if ( chemicalFiberPallet.getPrintStatus() == 0 ) {
            chemicalFiberPallet.setPrintStatus(1);
            chemicalFiberPallet.setPrintNumber(1);
            chemicalFiberPallet.setPrintTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        } else {
            chemicalFiberPallet.setPrintNumber(chemicalFiberPallet.getPrintNumber() + 1);
            chemicalFiberPallet.setPrintEndTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        }
        chemicalFiberPalletRepository.save(chemicalFiberPallet);

        Workbook workbook = null;

        int tempIndex = 1;
        int sheelAllitem = 100;
        List<Map<String, Object>> list = new ArrayList<>();
        BigDecimal totalTotalWeights=new BigDecimal(0);
        int sheelPages = (PalletDetail.size() + sheelAllitem) / sheelAllitem;

        Map<String, Object> map = new HashMap<>();
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
        Map<String, String> row = initRow();
        BigDecimal rowTotal = new BigDecimal(0);
        BigDecimal total = new BigDecimal(0);
        BigDecimal totalWeight = new BigDecimal(0);
        for (int i = tempIndex; i < PalletDetail.size() + 1; i++) {
            total = total.add(new BigDecimal(1));
            totalWeight = totalWeight.add(PalletDetail.get(i - 1).getNetWeight());
            Integer yu = i % 10;

            BigDecimal cellValue = PalletDetail.get(i - 1).getNetWeight();
            rowTotal = rowTotal.add(cellValue);

            if (i % 10 == 0) {
                row.put("10", cellValue.toString());
                row.put("total", rowTotal.toString());
                listMap.add(row);
                rowTotal = new BigDecimal(0);
                row = initRow();
            } else {
                row.put(yu.toString(), cellValue.toString());
            }
        }
        row.put("total", rowTotal.toString());
        listMap.add(row);
        totalTotalWeights = totalTotalWeights.add(totalWeight);
        map.put("poundList", listMap);
       /* map.put("customerName", chemicalFiberDeliveryNoteExportPoundExcelDto.getCustomerName());
        map.put("prodName", chemicalFiberDeliveryNoteExportPoundExcelDto.getProdName());
        map.put("createDate", new Timestamp(chemicalFiberDeliveryNoteExportPoundExcelDto.getCreateDate()));*/
        map.put("total", total);
        map.put("totalWeight", totalWeight);
        map.put("totalPage", sheelPages);
        map.put("palletNumber", chemicalFiberPallet.getPalletNumber());
        map.put("prodFineness", chemicalFiberPallet.getProdFineness());
        map.put("prodColor", chemicalFiberPallet.getProdColor());

        String templatePath = new TemplateConfig("template/excel", TemplateConfig.ResourceMode.CLASSPATH).getPath() + "/hqexcal.xls";
        // 加载模板
        TemplateExportParams params = new TemplateExportParams(templatePath);
        /*params.setReadonly(Boolean.TRUE);*/
        workbook = ExcelExportUtil.exportExcel(params, map);
        Sheet sheet1 = workbook.getSheet("Sheet1");
        Drawing patriarch = sheet1.createDrawingPatriarch();
        ClientAnchor anchor = new HSSFClientAnchor(0, 0, 255, 255,(short) 9, 2, (short) 13, 2);
        //anchor.setAnchorType(3);
        patriarch.createPicture(anchor, workbook.addPicture(
                generateBarCode128(chemicalFiberPallet.getPalletNumber(), 10D, 5D, false, true), HSSFWorkbook.PICTURE_TYPE_JPEG));
        FileUtil.downLoadExcel("磅码单导出.xls", response, workbook);

    }

    private Map<String,String> initRow(){
        Map<String,String> result=new HashMap<>();
        for (int j = 1; j < 11; j++) {
            result.put(Integer.toString(j),"");
        }
        result.put("total","");
        return result;
    }

    public byte[] generateBarCode128(String message, Double height, Double width, boolean withQuietZone, boolean hideText) {

        Code128Bean bean = new Code128Bean();

        int dpi = 300;

        bean.doQuietZone(withQuietZone);

        bean.setBarHeight((double) ObjectUtils.defaultIfNull(height,9.0D));
        if (width != null) {
            bean.setModuleWidth(width);
        }

        if (hideText) {
            bean.setMsgPosition(HumanReadablePlacement.HRP_NONE);
        }

        String format = "image/png";

        ByteArrayOutputStream ous = new ByteArrayOutputStream();

        BitmapCanvasProvider canvas = new BitmapCanvasProvider(ous, format, dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);

        bean.generateBarcode(canvas, message);
        try {
            canvas.finish();
        } catch (IOException e) {
        }

        return ous.toByteArray();
    }

    public List<ChemicalFiberPallet> getPallet(String palletNumber){
        ChemicalFiberPalletQueryCeiteria pallet = new ChemicalFiberPalletQueryCeiteria();
        pallet.setPalletNumber(palletNumber);
        return chemicalFiberPalletRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,pallet,criteriaBuilder));
    }
}
