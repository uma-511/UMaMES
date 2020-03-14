package me.zhengjie.uma_mes.service;

import com.lgmn.common.result.Result;
import me.zhengjie.uma_mes.domain.UmaChemicalFiberStatement;
import me.zhengjie.uma_mes.service.dto.UmaChemicalFiberStatementDTO;
import me.zhengjie.uma_mes.service.dto.UmaChemicalFiberStatementQueryCriteria;
import me.zhengjie.uma_mes.service.dto.statement.CreateStatementDto;
import me.zhengjie.uma_mes.service.dto.statement.StatementDetailsAllListDto;
import me.zhengjie.uma_mes.service.dto.statement.StatementDetailsListDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author Xie Ji Biao
* @date 2020-03-11
*/
public interface UmaChemicalFiberStatementService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(UmaChemicalFiberStatementQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<UmaChemicalFiberStatementDTO>
    */
    List<UmaChemicalFiberStatementDTO> queryAll(UmaChemicalFiberStatementQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return UmaChemicalFiberStatementDTO
     */
    UmaChemicalFiberStatementDTO findById(Integer id);

    UmaChemicalFiberStatementDTO create(CreateStatementDto createStatementDto);

    void update(UmaChemicalFiberStatement resources);

    void delete(Integer id);

    void download(List<UmaChemicalFiberStatementDTO> all, HttpServletResponse response) throws IOException;

    Map<String,Object> getStatementDetailsList(StatementDetailsListDto statementDetailsListDto);

    Map<String,Object> getStatementDetailsAllList(StatementDetailsAllListDto statementDetailsAllListDto);

    Result getSums(StatementDetailsAllListDto statementDetailsAllListDto);

    void exportStatement(HttpServletResponse response, Integer id);
}