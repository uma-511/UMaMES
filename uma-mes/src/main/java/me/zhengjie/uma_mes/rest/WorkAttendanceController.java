package me.zhengjie.uma_mes.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.WorkAttendance;
import me.zhengjie.uma_mes.service.WorkAttendanceService;
import me.zhengjie.uma_mes.service.dto.WorkAttendanceQueryCriteria;
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
* @author wave
* @date 2020-09-07
*/
@Api(tags = "WorkAttendance管理")
@RestController
@RequestMapping("/api/workAttendance")
public class WorkAttendanceController {

    private final WorkAttendanceService workAttendanceService;

    public WorkAttendanceController(WorkAttendanceService workAttendanceService) {
        this.workAttendanceService = workAttendanceService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    //@PreAuthorize("@el.check('workAttendance:list')")
    public void download(HttpServletResponse response, WorkAttendanceQueryCriteria criteria) throws IOException {
        workAttendanceService.download(workAttendanceService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询WorkAttendance")
    @ApiOperation("查询WorkAttendance")
    //@PreAuthorize("@el.check('workAttendance:list')")
    public ResponseEntity getWorkAttendances(WorkAttendanceQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(workAttendanceService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增WorkAttendance")
    @ApiOperation("新增WorkAttendance")
    //@PreAuthorize("@el.check('workAttendance:add')")
    public ResponseEntity create(@Validated @RequestBody WorkAttendance resources){
        return new ResponseEntity<>(workAttendanceService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改WorkAttendance")
    @ApiOperation("修改WorkAttendance")
    //@PreAuthorize("@el.check('workAttendance:edit')")
    public ResponseEntity update(@Validated @RequestBody WorkAttendance resources){
        workAttendanceService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除WorkAttendance")
    @ApiOperation("删除WorkAttendance")
    //@PreAuthorize("@el.check('workAttendance:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        workAttendanceService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}