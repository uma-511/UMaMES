package me.zhengjie.uma_mes.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.ChemicalFiberDeliveryNote;
import me.zhengjie.uma_mes.service.ChemicalFiberDeliveryNoteService;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberDeliveryNoteQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author Tan Jun Ming
* @date 2019-11-20
*/
@Api(tags = "优码MES:化纤-出货单管理")
@RestController
@RequestMapping("/api/chemicalFiberDeliveryNote")
public class ChemicalFiberDeliveryNoteController {

    private final ChemicalFiberDeliveryNoteService chemicalFiberDeliveryNoteService;

    public ChemicalFiberDeliveryNoteController(ChemicalFiberDeliveryNoteService chemicalFiberDeliveryNoteService) {
        this.chemicalFiberDeliveryNoteService = chemicalFiberDeliveryNoteService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('chemicalFiberDeliveryNote:list')")
    public void download(HttpServletResponse response, ChemicalFiberDeliveryNoteQueryCriteria criteria) throws IOException {
        chemicalFiberDeliveryNoteService.download(chemicalFiberDeliveryNoteService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询ChemicalFiberDeliveryNote")
    @ApiOperation("查询ChemicalFiberDeliveryNote")
    @PreAuthorize("@el.check('chemicalFiberDeliveryNote:list')")
    public ResponseEntity getChemicalFiberDeliveryNotes(ChemicalFiberDeliveryNoteQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(chemicalFiberDeliveryNoteService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增ChemicalFiberDeliveryNote")
    @ApiOperation("新增ChemicalFiberDeliveryNote")
    @PreAuthorize("@el.check('chemicalFiberDeliveryNote:add')")
    public ResponseEntity create(@Validated @RequestBody ChemicalFiberDeliveryNote resources){
        return new ResponseEntity<>(chemicalFiberDeliveryNoteService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改ChemicalFiberDeliveryNote")
    @ApiOperation("修改ChemicalFiberDeliveryNote")
    @PreAuthorize("@el.check('chemicalFiberDeliveryNote:edit')")
    public ResponseEntity update(@Validated @RequestBody ChemicalFiberDeliveryNote resources){
        chemicalFiberDeliveryNoteService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除ChemicalFiberDeliveryNote")
    @ApiOperation("删除ChemicalFiberDeliveryNote")
    @PreAuthorize("@el.check('chemicalFiberDeliveryNote:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        chemicalFiberDeliveryNoteService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}