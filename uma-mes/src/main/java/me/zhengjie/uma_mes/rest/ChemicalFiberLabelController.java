package me.zhengjie.uma_mes.rest;

import com.lgmn.common.result.Result;
import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.ChemicalFiberLabel;
import me.zhengjie.uma_mes.service.ChemicalFiberLabelService;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberLabelDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberLabelQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Api(tags = "优码MES:化纤-便签管理")
@RestController
@RequestMapping("/api/chemicalFiberLabel")
public class ChemicalFiberLabelController {

    private final ChemicalFiberLabelService chemicalFiberLabelService;

    public ChemicalFiberLabelController(ChemicalFiberLabelService chemicalFiberLabelService) {
        this.chemicalFiberLabelService = chemicalFiberLabelService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('chemicalFiberLabel:list')")
    public void download(HttpServletResponse response, ChemicalFiberLabelQueryCriteria criteria) throws IOException {
        chemicalFiberLabelService.download(chemicalFiberLabelService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询ChemicalFiberLabel")
    @ApiOperation("查询ChemicalFiberLabel")
    @PreAuthorize("@el.check('chemicalFiberLabel:list')")
    public ResponseEntity getChemicalFiberLabels(ChemicalFiberLabelQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(chemicalFiberLabelService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增ChemicalFiberLabel")
    @ApiOperation("新增ChemicalFiberLabel")
    @PreAuthorize("@el.check('chemicalFiberLabel:add')")
    public ResponseEntity create(@Validated @RequestBody ChemicalFiberLabel resources){
        return new ResponseEntity<>(chemicalFiberLabelService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改ChemicalFiberLabel")
    @ApiOperation("修改ChemicalFiberLabel")
    @PreAuthorize("@el.check('chemicalFiberLabel:edit')")
    public ResponseEntity update(@Validated @RequestBody ChemicalFiberLabel resources){
        chemicalFiberLabelService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除ChemicalFiberLabel")
    @ApiOperation("删除ChemicalFiberLabel")
    @PreAuthorize("@el.check('chemicalFiberLabel:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        chemicalFiberLabelService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/getSummaryData")
    @Log("查询ChemicalFiberLabel")
    @ApiOperation("查询ChemicalFiberLabel")
    @PreAuthorize("@el.check('chemicalFiberLabel:list')")
    public Result getSummaryData(@RequestBody ChemicalFiberLabelQueryCriteria criteria) {
        Integer sumFactPerBagNumber = 0;
        BigDecimal sumNetWeight = new BigDecimal(0);
        BigDecimal sumGrossWeight = new BigDecimal(0);
        if (criteria.getTempStartTime() != null) {
            criteria.setStartTime(new Timestamp(criteria.getTempStartTime()));
            criteria.setEndTime(new Timestamp(criteria.getTempEndTime()));
        }
        List<ChemicalFiberLabelDTO> chemicalFiberLabelDTOList = chemicalFiberLabelService.queryAll(criteria);
        for (ChemicalFiberLabelDTO chemicalFiberLabelDTO : chemicalFiberLabelDTOList) {
            sumFactPerBagNumber = sumFactPerBagNumber + chemicalFiberLabelDTO.getFactPerBagNumber();
            sumNetWeight = sumNetWeight.add(chemicalFiberLabelDTO.getNetWeight());
            sumGrossWeight = sumGrossWeight.add(chemicalFiberLabelDTO.getGrossWeight());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("sumFactPerBagNumber", sumFactPerBagNumber);
        map.put("sumNetWeight", sumNetWeight);
        map.put("sumGrossWeight", sumGrossWeight);
        return Result.success(map);
    }

    @PostMapping(value = "/getShifts")
    @Log("查询班次")
    @ApiOperation("查询班次")
    //@PreAuthorize("@el.check('chemicalFiberLabel:add')")
    public ResponseEntity getShifts(){
        return new ResponseEntity<>(chemicalFiberLabelService.getShifts(),HttpStatus.OK);
    }
}