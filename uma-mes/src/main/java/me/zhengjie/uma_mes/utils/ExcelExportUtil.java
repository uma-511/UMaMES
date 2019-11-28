package me.zhengjie.uma_mes.utils;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.excel.export.ExcelBatchExportService;
import cn.afterturn.easypoi.excel.export.ExcelExportService;
import cn.afterturn.easypoi.excel.export.template.ExcelExportOfTemplateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ExcelExportUtil {
    private ExcelExportUtil() {
    }

    /**
     * @param entity
     *            表格標題屬性
     * @param pojoClass
     *            Excel對象Class
     * @param dataSet
     *            Excel對象數據List
     */
    public static Workbook exportBigExcel(ExportParams entity, Class<?> pojoClass,
                                          Collection<?> dataSet) {
        ExcelBatchExportService batchService = ExcelBatchExportService
                .getExcelBatchExportService(entity, pojoClass);
        return batchService.appendData(dataSet);
    }

    public static Workbook exportBigExcel(ExportParams entity, List<ExcelExportEntity> excelParams,
                                          Collection<?> dataSet) {
        ExcelBatchExportService batchService = ExcelBatchExportService
                .getExcelBatchExportService(entity, excelParams);
        return batchService.appendData(dataSet);
    }

    public static void closeExportBigExcel() {
        ExcelBatchExportService batchService = ExcelBatchExportService.getCurrentExcelBatchExportService();
        if(batchService != null) {
            batchService.closeExportBigExcel();
        }
    }

    /**
     * @param entity
     *            表格標題屬性
     * @param pojoClass
     *            Excel對象Class
     * @param dataSet
     *            Excel對象數據List
     */
    public static Workbook exportExcel(ExportParams entity, Class<?> pojoClass,
                                       Collection<?> dataSet) {
        Workbook workbook = getWorkbook(entity.getType(),dataSet.size());
        new ExcelExportService().createSheet(workbook, entity, pojoClass, dataSet);
        return workbook;
    }

    private static Workbook getWorkbook(ExcelType type, int size) {
        if (ExcelType.HSSF.equals(type)) {
            return new HSSFWorkbook();
        } else if (size < 100000) {
            return new XSSFWorkbook();
        } else {
            return new SXSSFWorkbook();
        }
    }

    /**
     * 根據Map創建對應的Excel
     * @param entity
     *            表格標題屬性
     * @param entityList
     *            Map對象列表
     * @param dataSet
     *            Excel對象數據List
     */
    public static Workbook exportExcel(ExportParams entity, List<ExcelExportEntity> entityList,
                                       Collection<?> dataSet) {
        Workbook workbook = getWorkbook(entity.getType(),dataSet.size());;
        new ExcelExportService().createSheetForMap(workbook, entity, entityList, dataSet);
        return workbook;
    }

    /**
     * 一個excel 創建多個sheet
     *
     * @param list
     *            多個Map key title 對應表格Title key entity 對應表格對應實體 key data
     *            Collection 數據
     * @return
     */
    public static Workbook exportExcel(List<Map<String, Object>> list, ExcelType type) {
        Workbook workbook = getWorkbook(type,0);
        for (Map<String, Object> map : list) {
            ExcelExportService service = new ExcelExportService();
            service.createSheet(workbook, (ExportParams) map.get("title"),
                    (Class<?>) map.get("entity"), (Collection<?>) map.get("data"));
        }
        return workbook;
    }

    /**
     * 導出文件通過模板解析,不推薦這個了,推薦全部通過模板來執行處理
     *
     * @param params
     *            導出參數類
     * @param pojoClass
     *            對應實體
     * @param dataSet
     *            實體集合
     * @param map
     *            模板集合
     * @return
     */
    @Deprecated
    public static Workbook exportExcel(TemplateExportParams params, Class<?> pojoClass,
                                       Collection<?> dataSet, Map<String, Object> map) {
        return new ExcelExportOfTemplateUtil().createExcleByTemplate(params, pojoClass, dataSet,
                map);
    }

    /**
     * 導出文件通過模板解析只有模板,沒有集合
     *
     * @param params
     *            導出參數類
     * @param map
     *            模板集合
     * @return
     */
    public static Workbook exportExcel(TemplateExportParams params, Map<String, Object> map) {
        return new ExcelExportOfTemplateUtil().createExcleByTemplate(params, null, null, map);
    }

    /**
     * 導出文件通過模板解析只有模板,沒有集合
     * 每個sheet對應一個map,導出到處,key是sheet的NUM
     * @param params
     *            導出參數類
     * @param map
     *            模板集合
     * @return
     */
    public static Workbook exportExcel(Map<Integer, Map<String, Object>> map,
                                       TemplateExportParams params) {
        return new ExcelExportOfTemplateUtil().createExcleByTemplate(params, map);
    }
}
