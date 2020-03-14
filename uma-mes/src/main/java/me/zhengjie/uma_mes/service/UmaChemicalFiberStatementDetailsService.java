package me.zhengjie.uma_mes.service;

import me.zhengjie.uma_mes.domain.UmaChemicalFiberStatementDetails;
import me.zhengjie.uma_mes.service.dto.UmaChemicalFiberStatementDetailsDTO;
import me.zhengjie.uma_mes.service.dto.UmaChemicalFiberStatementDetailsQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author Xie Ji Biao
* @date 2020-03-11
*/
public interface UmaChemicalFiberStatementDetailsService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(UmaChemicalFiberStatementDetailsQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<UmaChemicalFiberStatementDetailsDTO>
    */
    List<UmaChemicalFiberStatementDetailsDTO> queryAll(UmaChemicalFiberStatementDetailsQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return UmaChemicalFiberStatementDetailsDTO
     */
    UmaChemicalFiberStatementDetailsDTO findById(Integer id);

    UmaChemicalFiberStatementDetailsDTO create(UmaChemicalFiberStatementDetails resources);

    void update(UmaChemicalFiberStatementDetails resources);

    void delete(Integer id);

    void download(List<UmaChemicalFiberStatementDetailsDTO> all, HttpServletResponse response) throws IOException;
}