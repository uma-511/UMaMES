package me.zhengjie.uma_mes.service.impl;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.hutool.extra.template.TemplateConfig;
import me.zhengjie.uma_mes.domain.ChemicalFiberCustomerOrder;
import me.zhengjie.uma_mes.domain.ChemicalFiberProduction;
import me.zhengjie.uma_mes.repository.ChemicalFiberCustomerOrderRepository;
import me.zhengjie.uma_mes.repository.ChemicalFiberProductionRepository;
import me.zhengjie.uma_mes.service.ChemicalFiberCustomerOrderService;
import me.zhengjie.uma_mes.service.ChemicalFiberProductionService;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberCustomerOrderDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberCustomerOrderQueryCriteria;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberProductionDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberProductionQueryCriteria;
import me.zhengjie.uma_mes.service.handheld.HandheldService;
import me.zhengjie.uma_mes.service.mapper.ChemicalFiberCustomerOrderMapper;
import me.zhengjie.utils.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.krysalis.barcode4j.HumanReadablePlacement;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author Xie Ji Biao
* @date 2020-10-30
*/
@Service
@CacheConfig(cacheNames = "chemicalFiberCustomerOrder")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ChemicalFiberCustomerOrderServiceImpl implements ChemicalFiberCustomerOrderService {

    private final ChemicalFiberCustomerOrderRepository chemicalFiberCustomerOrderRepository;

    private final ChemicalFiberCustomerOrderMapper chemicalFiberCustomerOrderMapper;

    @Autowired
    private ChemicalFiberProductionService chemicalFiberProductionService;

    @Autowired
    private ChemicalFiberProductionRepository chemicalFiberProductionRepository;

    @Autowired
    private HandheldService handheldService;

    public ChemicalFiberCustomerOrderServiceImpl(ChemicalFiberCustomerOrderRepository chemicalFiberCustomerOrderRepository, ChemicalFiberCustomerOrderMapper chemicalFiberCustomerOrderMapper) {
        this.chemicalFiberCustomerOrderRepository = chemicalFiberCustomerOrderRepository;
        this.chemicalFiberCustomerOrderMapper = chemicalFiberCustomerOrderMapper;
    }

    @Override
    public Map<String,Object> queryAll(ChemicalFiberCustomerOrderQueryCriteria criteria, Pageable pageable){
        criteria.setDelFlag(0);
        Page<ChemicalFiberCustomerOrder> page = chemicalFiberCustomerOrderRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(chemicalFiberCustomerOrderMapper::toDto));
    }

    @Override
    public List<ChemicalFiberCustomerOrderDTO> queryAll(ChemicalFiberCustomerOrderQueryCriteria criteria){
        return chemicalFiberCustomerOrderMapper.toDto(chemicalFiberCustomerOrderRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    public ChemicalFiberCustomerOrderDTO findById(Integer id) {
        ChemicalFiberCustomerOrder chemicalFiberCustomerOrder = chemicalFiberCustomerOrderRepository.findById(id).orElseGet(ChemicalFiberCustomerOrder::new);
        ValidationUtil.isNull(chemicalFiberCustomerOrder.getId(),"ChemicalFiberCustomerOrder","id",id);
        return chemicalFiberCustomerOrderMapper.toDto(chemicalFiberCustomerOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ChemicalFiberCustomerOrderDTO create(ChemicalFiberCustomerOrder resources) {
        resources.setCustomerNumber(getNumber());
        resources.setDelFlag(0);
        resources.setCreateUser(SecurityUtils.getUsername());
        resources.setWarehousingDate(new Timestamp(System.currentTimeMillis()));
        return chemicalFiberCustomerOrderMapper.toDto(chemicalFiberCustomerOrderRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ChemicalFiberCustomerOrder resources) {
        ChemicalFiberCustomerOrder chemicalFiberCustomerOrder = chemicalFiberCustomerOrderRepository.findById(resources.getId()).orElseGet(ChemicalFiberCustomerOrder::new);
        ValidationUtil.isNull( chemicalFiberCustomerOrder.getId(),"ChemicalFiberCustomerOrder","id",resources.getId());
        chemicalFiberCustomerOrder.copy(resources);
        chemicalFiberCustomerOrderRepository.save(chemicalFiberCustomerOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        ChemicalFiberCustomerOrder chemicalFiberCustomerOrder = chemicalFiberCustomerOrderRepository.findById(id).orElseGet(ChemicalFiberCustomerOrder::new);
        chemicalFiberCustomerOrder.setDelFlag(1);
        chemicalFiberCustomerOrderRepository.save(chemicalFiberCustomerOrder);
        ChemicalFiberProductionQueryCriteria car = new ChemicalFiberProductionQueryCriteria();
        car.setCustomerOrderNumber(chemicalFiberCustomerOrder.getCustomerNumber());
        car.setDelFlag(0);
        List<ChemicalFiberProduction> prodction = chemicalFiberProductionRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,car,criteriaBuilder));

        for (int i = 0; i < prodction.size(); i++) {
            prodction.get(i).setDelFlag(1);
        }
        chemicalFiberProductionRepository.saveAll(prodction);
    }


    @Override
    public void download(Integer id, HttpServletResponse response) throws IOException {
        ChemicalFiberCustomerOrder chemicalFiberCustomerOrder = chemicalFiberCustomerOrderRepository.findById(id).orElseGet(ChemicalFiberCustomerOrder::new);
        ChemicalFiberProductionQueryCriteria car = new ChemicalFiberProductionQueryCriteria();
        car.setCustomerOrderNumber(chemicalFiberCustomerOrder.getCustomerNumber());
        car.setDelFlag(0);
        List<ChemicalFiberProduction> prodction = chemicalFiberProductionRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,car,criteriaBuilder));
        // 模板的地址
        String templatePath = new TemplateConfig("template/excel", TemplateConfig.ResourceMode.CLASSPATH).getPath() + "/customerOrder.xls";
        // 加载模板
        TemplateExportParams params = new TemplateExportParams(templatePath);
        // 生成workbook 并导出
        Workbook workbook = null;
        Map<String, Object> map = new HashMap<>();
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
        for (int i = 0; i < prodction.size(); i++) {
            Map<String, String> lm = new HashMap<String, String>();
            lm.put("index", (i + 1) + "");
            lm.put("customerName", prodction.get(i).getCustomerName());
            lm.put("prodColor", prodction.get(i).getProdColor());
            lm.put("prodFineness", prodction.get(i).getProdFineness());
            lm.put("number", prodction.get(i).getNumber());
            lm.put("planNumber", prodction.get(i).getPlanNumber().toString());
            lm.put("remark", prodction.get(i).getRemark());
            lm.put("model", "PP Yarm");
            listMap.add(lm);
        }
        map.put("list",listMap);
        map.put("customerNumber",chemicalFiberCustomerOrder.getCustomerNumber());
        map.put("contactNumber",chemicalFiberCustomerOrder.getContactNumber());
        String Time = new SimpleDateFormat("yyyy-MM-dd").format(chemicalFiberCustomerOrder.getWarehousingDate());
        map.put("warehousingDate",Time);
        map.put("remark",chemicalFiberCustomerOrder.getRemark());
        map.put("customerName",chemicalFiberCustomerOrder.getCustomerName());
        map.put("createUser",chemicalFiberCustomerOrder.getCreateUser());
        String Time1 = new SimpleDateFormat("yyyy-MM-dd").format(chemicalFiberCustomerOrder.getDeviceDate());
        map.put("deviceDate", Time1);
        workbook = ExcelExportUtil.exportExcel(params, map);
        Sheet sheet1 = workbook.getSheet("Sheet1");
        Drawing patriarch = sheet1.createDrawingPatriarch();
        for (int i = 0; i < prodction.size(); i++) {
            ClientAnchor anchor = new HSSFClientAnchor(0, 20, 0, 230,(short) 6 , (5 + i) , (short) 7 , (5 + i)  );
            patriarch.createPicture(anchor, workbook.addPicture(
                    generateBarCode128(prodction.get(i).getNumber(), 10D, 5D, false, true), HSSFWorkbook.PICTURE_TYPE_JPEG));
        }
        //anchor.setAnchorType(3);
        FileUtil.downLoadExcel("排产单导出.xlsx", response, workbook);

    }

    @Override
    public List<ChemicalFiberProductionDTO> getProdction(ChemicalFiberCustomerOrder resources) {
        ChemicalFiberProductionQueryCriteria criteria = new ChemicalFiberProductionQueryCriteria();
        criteria.setCustomerOrderNumber(resources.getCustomerNumber());
        criteria.setDelFlag(0);
        return chemicalFiberProductionService.queryAll(criteria);

    }

    public String getNumber() {
        String productionNumber;
        Map<String, Object> timeMap = handheldService.monthTimeInMillis();
        String year = timeMap.get("year").toString();
        String month = timeMap.get("month").toString();
        String day = timeMap.get("day").toString();
        //List<ChemicalFiberProductionDTO> chemicalFiberProductionDTOS = chemicalFiberProductionMapper.toDto(chemicalFiberProductionRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
        List<ChemicalFiberCustomerOrder> chemicalFiberProductionDTOS = chemicalFiberCustomerOrderRepository.get(year + "-" + month);

        if (chemicalFiberProductionDTOS.size() == 0) {
            productionNumber = year + month + "01";
        } else {
            Integer chemicalFiberProductionDTOSSize =  chemicalFiberProductionDTOS.size();
            String tempNumberStr = String.format("%2d", (chemicalFiberProductionDTOSSize + 1)).replace(" ", "0");
            productionNumber = year + month + tempNumberStr;
        }
        return productionNumber;

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
}
