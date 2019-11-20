package me.zhengjie.uma_mes.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.UmaDeliveryNote;
import me.zhengjie.uma_mes.service.UmaDeliveryNoteService;
import me.zhengjie.uma_mes.service.dto.UmaDeliveryNoteQueryCriteria;
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
@Api(tags = "UmaDeliveryNote管理")
@RestController
@RequestMapping("/api/umaDeliveryNote")
public class UmaDeliveryNoteController {

    private final UmaDeliveryNoteService umaDeliveryNoteService;

    public UmaDeliveryNoteController(UmaDeliveryNoteService umaDeliveryNoteService) {
        this.umaDeliveryNoteService = umaDeliveryNoteService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('umaDeliveryNote:list')")
    public void download(HttpServletResponse response, UmaDeliveryNoteQueryCriteria criteria) throws IOException {
        umaDeliveryNoteService.download(umaDeliveryNoteService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询UmaDeliveryNote")
    @ApiOperation("查询UmaDeliveryNote")
    @PreAuthorize("@el.check('umaDeliveryNote:list')")
    public ResponseEntity getUmaDeliveryNotes(UmaDeliveryNoteQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(umaDeliveryNoteService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增UmaDeliveryNote")
    @ApiOperation("新增UmaDeliveryNote")
    @PreAuthorize("@el.check('umaDeliveryNote:add')")
    public ResponseEntity create(@Validated @RequestBody UmaDeliveryNote resources){
        return new ResponseEntity<>(umaDeliveryNoteService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改UmaDeliveryNote")
    @ApiOperation("修改UmaDeliveryNote")
    @PreAuthorize("@el.check('umaDeliveryNote:edit')")
    public ResponseEntity update(@Validated @RequestBody UmaDeliveryNote resources){
        umaDeliveryNoteService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除UmaDeliveryNote")
    @ApiOperation("删除UmaDeliveryNote")
    @PreAuthorize("@el.check('umaDeliveryNote:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        umaDeliveryNoteService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}