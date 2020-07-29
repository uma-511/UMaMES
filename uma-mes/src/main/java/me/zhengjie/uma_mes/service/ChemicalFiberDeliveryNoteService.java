package me.zhengjie.uma_mes.service;

import com.lgmn.common.result.Result;
import me.zhengjie.uma_mes.domain.ChemicalFiberDeliveryNote;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberDeliveryNoteDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberDeliveryNoteExportPoundExcelDto;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberDeliveryNoteQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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

    void sendOut(Integer id);

    void recived(Integer id);
}