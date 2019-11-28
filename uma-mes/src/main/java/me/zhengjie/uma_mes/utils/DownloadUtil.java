package me.zhengjie.uma_mes.utils;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelStyleType;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import me.zhengjie.uma_mes.domain.ChemicalFiberDeliveryNote;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberDeliveryDetailDTO;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DownloadUtil {

    public static void downloadDeliveryNoteExcel(ChemicalFiberDeliveryNote chemicalFiberDeliveryNote, List<ChemicalFiberDeliveryDetailDTO> chemicalFiberDeliveryDetailDTOS, HttpServletResponse response) throws IOException, IllegalAccessException {
//        TemplateConfig templateConfig = new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH);
//        String path = templateConfig.getPath() + "/excel/delivery_temp.xls";

//        File templateFile=new File("/Volumes/back/Lgmn/java/UMaMES/eladmin-system/src/main/resources/template/excel/temp.xlsx");
//        if(!templateFile.exists()){
//            System.out.println("找不到模板文件");
//            try {
//                throw new FileNotFoundException("找不到模板文件");
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//        }

//        TemplateExportParams params = new TemplateExportParams(templateFile.getPath(), true);
//        params.setHeadingRows(2);
//        params.setHeadingStartRow(2);
//        params.setStyle(ExcelStyleType.BORDER.getClazz());

//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("customerName", chemicalFiberDeliveryNote.getCustomerName());
//        map.put("customerAddress", chemicalFiberDeliveryNote.getCustomerAddress());
//        map.put("contacts", chemicalFiberDeliveryNote.getContacts());
//        map.put("contactPhone", chemicalFiberDeliveryNote.getContactPhone());
//        map.put("scanNumber", chemicalFiberDeliveryNote.getScanNumber());
//        map.put("createDate", chemicalFiberDeliveryNote.getCreateDate());
//        map.put("total", chemicalFiberDeliveryNote.getTotalPrice());
//        map.put("capitalizationTotal", "大写");
//        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
//        for (ChemicalFiberDeliveryDetailDTO chemicalFiberDeliveryDetailDTO : chemicalFiberDeliveryDetailDTOS) {
//            Map<String, String> lm = new HashMap<String, String>();
//            lm.put("prodName", chemicalFiberDeliveryDetailDTO.getProdName());
//            lm.put("totalBag", chemicalFiberDeliveryDetailDTO.getTotalBag() + "");
//            lm.put("totalNumber", chemicalFiberDeliveryDetailDTO.getTotalNumber() + "");
//            lm.put("unit", chemicalFiberDeliveryDetailDTO.getUnit());
//            lm.put("sellingPrice", chemicalFiberDeliveryDetailDTO.getSellingPrice() + "");
//            lm.put("totalPrice", chemicalFiberDeliveryDetailDTO.getTotalPrice() + "");
//
//            listMap.add(lm);
//        }
//        map.put("deliveryList", listMap);

//        DeliveryVo deliveryVo = new DeliveryVo();
//        deliveryVo.setCustomerName(chemicalFiberDeliveryNote.getCustomerName());
//        deliveryVo.setCustomerAddress(chemicalFiberDeliveryNote.getCustomerAddress());
//        deliveryVo.setContacts(chemicalFiberDeliveryNote.getContacts());
//        deliveryVo.setContactPhone(chemicalFiberDeliveryNote.getContactPhone());
//        deliveryVo.setScanNumber(chemicalFiberDeliveryNote.getScanNumber());
//        deliveryVo.setCreateDate("0000");
//        deliveryVo.setTotal(chemicalFiberDeliveryNote.getTotalPrice() + "");
//        deliveryVo.setCapitalizationTotal("大写");
//        List<DeliveryListVo> deliveryListVos = new ArrayList<>();
//        for (ChemicalFiberDeliveryDetailDTO chemicalFiberDeliveryDetailDTO : chemicalFiberDeliveryDetailDTOS) {
//            DeliveryListVo deliveryListVo = new DeliveryListVo();
//            deliveryListVo.setProdName(chemicalFiberDeliveryDetailDTO.getProdName());
//            deliveryListVo.setTotalBag(chemicalFiberDeliveryDetailDTO.getTotalBag() + "");
//            deliveryListVo.setTotalNumber(chemicalFiberDeliveryDetailDTO.getTotalNumber() + "");
//            deliveryListVo.setUnit(chemicalFiberDeliveryDetailDTO.getUnit());
//            deliveryListVo.setSellingPrice(chemicalFiberDeliveryDetailDTO.getSellingPrice() + "");
//            deliveryListVo.setTotalPrice(chemicalFiberDeliveryDetailDTO.getTotalPrice() + "");
//
//            deliveryListVos.add(deliveryListVo);
//        }
//        deliveryVo.setDeliveryList(deliveryListVos);
//        Workbook workbook = ExcelExportUtil.exportExcel(params, objectToMap(deliveryVo));
//        File savefile = new File("/Users/xjb/Desktop/PS/");
//        if (!savefile.exists()) {
//            savefile.mkdirs();
//        }
//        FileOutputStream fos = new FileOutputStream("/Users/xjb/Desktop/PS/出货单导出.xls");
//        workbook.write(fos);
//        fos.close();

//        String filePath = ExcelUtils.exportLabelExcel(tempPath,exportPath,vo);

        // 加载模板
//        TemplateExportParams params = new TemplateExportParams(templateFile.getPath());
//        // 生成workbook 并导出
//        Workbook workbook = null;
//        try {
//            workbook = ExcelExportUtil.exportExcel(params, objectToMap(deliveryVo));
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//        String savePath = "/Users/xjb/Desktop/PS/";
//        File savefile = new File(savePath);
//        if (!savefile.exists()) {
//            boolean result = savefile.mkdirs();
//            System.out.println("目录不存在,进行创建,创建" + (result ? "成功!" : "失败！"));
//        }
//        String filePath = savePath + "出货单导出_19110001.xls";
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(filePath);
//            workbook.write(fos);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                fos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//
//        File file=new File(filePath);
//        // 如果文件名存在，则进行下载
//        if (file.exists()) {
//
//            // 配置文件下载
//            response.setHeader("content-type", "application/octet-stream");
//            response.setContentType("application/octet-stream");
//            // 下载文件能正常显示中文
//            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
//
//            // 实现文件下载
//            byte[] buffer = new byte[1024];
//            FileInputStream fis = null;
//            BufferedInputStream bis = null;
//            try {
//                fis = new FileInputStream(file);
//                bis = new BufferedInputStream(fis);
//                OutputStream os = response.getOutputStream();
//                int i = bis.read(buffer);
//                while (i != -1) {
//                    os.write(buffer, 0, i);
//                    i = bis.read(buffer);
//                }
//                System.out.println("Download the song successfully!");
//            }
//            catch (Exception e) {
//                System.out.println("Download the song failed!");
//            }
//            finally {
//                if (bis != null) {
//                    try {
//                        bis.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                if (fis != null) {
//                    try {
//                        fis.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }

        File templateFile=new File("/Volumes/back/Lgmn/java/UMaMES/eladmin-system/src/main/resources/template/excel/temp.xlsx");
        if(!templateFile.exists()){
            System.out.println("找不到模板文件");
            try {
                throw new FileNotFoundException("找不到模板文件");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        TemplateExportParams params = new TemplateExportParams(templateFile.getPath());
        //excel導出數據
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("customerName", chemicalFiberDeliveryNote.getCustomerName());
        map.put("customerAddress", chemicalFiberDeliveryNote.getCustomerAddress());
        map.put("contacts", chemicalFiberDeliveryNote.getContacts());
        map.put("contactPhone", chemicalFiberDeliveryNote.getContactPhone());
        map.put("scanNumber", chemicalFiberDeliveryNote.getScanNumber());
        map.put("createDate", chemicalFiberDeliveryNote.getCreateDate());
        map.put("total", chemicalFiberDeliveryNote.getTotalPrice());
        map.put("capitalizationTotal", "大写");
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
        for (ChemicalFiberDeliveryDetailDTO chemicalFiberDeliveryDetailDTO : chemicalFiberDeliveryDetailDTOS) {
            Map<String, String> lm = new HashMap<String, String>();
            lm.put("prodName", chemicalFiberDeliveryDetailDTO.getProdName());
            lm.put("totalBag", chemicalFiberDeliveryDetailDTO.getTotalBag() + "");
            lm.put("totalNumber", chemicalFiberDeliveryDetailDTO.getTotalNumber() + "");
            lm.put("unit", chemicalFiberDeliveryDetailDTO.getUnit());
            lm.put("sellingPrice", chemicalFiberDeliveryDetailDTO.getSellingPrice() + "");
            lm.put("totalPrice", chemicalFiberDeliveryDetailDTO.getTotalPrice() + "");

            listMap.add(lm);
        }
        map.put("deliveryList", listMap);
        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        FileUtil.downLoadExcel("kmye.xls", response, workbook);
    }

    /**
     * 对象转换为Map<String, Object>的工具类
     *
     * @param obj
     *            要转换的对象
     * @return map
     * @throws IllegalAccessException
     */
    private static Map<String, Object> objectToMap(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>(16);
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            /*
             * Returns the value of the field represented by this {@code Field}, on the
             * specified object. The value is automatically wrapped in an object if it
             * has a primitive type.
             * 注:返回对象该该属性的属性值，如果该属性的基本类型，那么自动转换为包装类
             */
            Object value = field.get(obj);
            map.put(fieldName, value);
        }
        return map;
    }
}
