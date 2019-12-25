package me.zhengjie.uma_mes.service;

import com.lgmn.common.result.Result;
import me.zhengjie.uma_mes.domain.ChemicalFiberDeliveryNote;
import me.zhengjie.uma_mes.service.dto.*;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
public interface ChemicalFiberDeliveryNoteService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(ChemicalFiberDeliveryNoteQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<ChemicalFiberDeliveryNoteDTO>
    */
    List<ChemicalFiberDeliveryNoteDTO> queryAll(ChemicalFiberDeliveryNoteQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return ChemicalFiberDeliveryNoteDTO
     */
    ChemicalFiberDeliveryNoteDTO findById(Integer id);

    ChemicalFiberDeliveryNoteDTO create(ChemicalFiberDeliveryNote resources);

    void update(ChemicalFiberDeliveryNote resources);

    void delete(Integer id);

    void download(List<ChemicalFiberDeliveryNoteDTO> all, HttpServletResponse response) throws IOException;

    void deliveryNoteStoredProcedure(String scanNumber);

    void downloadDeliveryNote(Integer id, HttpServletResponse response);

    void exportPoundExcel(ChemicalFiberDeliveryNoteExportPoundExcelDto chemicalFiberDeliveryNoteExportPoundExcelDto, HttpServletResponse response);

    Map<String,Object> getSalesReport(ChemicalFiberDeliveryNoteQueryCriteria criteria, Pageable pageable);

    Result getSalesReportSummaries(ChemicalFiberDeliveryNoteQueryCriteria criteria);
}