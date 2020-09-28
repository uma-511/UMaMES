package me.zhengjie.uma_mes.service;

import com.lgmn.common.result.Result;
import me.zhengjie.uma_mes.domain.ChemicalFiberLabel;
import me.zhengjie.uma_mes.domain.ChemicalFiberProductionReport;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberProductionReportDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberProductionReportQueryCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Map;

public interface ChemicalFiberProductionReportService {

    Map<String, Object> queryAll(ChemicalFiberProductionReportQueryCriteria criteria, Pageable pageable);


    ChemicalFiberProductionReport getReport(Timestamp time, String shifts, String machine, Integer productionId);

    void update(ChemicalFiberProductionReport report);

    void create(ChemicalFiberProductionReportDTO reportDTO);

    void delectReport(ChemicalFiberLabel label);

    Result getProductionReportSummaries(ChemicalFiberProductionReportQueryCriteria criteria);

    void downloadProduct(ChemicalFiberProductionReportQueryCriteria criteria,  Pageable pageable, HttpServletResponse response) throws IOException;


}
