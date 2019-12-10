package me.zhengjie.uma_mes.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.HeartBeat;
import me.zhengjie.uma_mes.service.HeartBeatService;
import me.zhengjie.uma_mes.service.dto.HeartBeatQueryCriteria;
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
* @author Xie Ji Biao
* @date 2019-12-04
*/
@Api(tags = "HeartBeat管理")
@RestController
@RequestMapping("/api/heartBeat")
public class HeartBeatController {

    private final HeartBeatService heartBeatService;

    public HeartBeatController(HeartBeatService heartBeatService) {
        this.heartBeatService = heartBeatService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('heartBeat:list')")
    public void download(HttpServletResponse response, HeartBeatQueryCriteria criteria) throws IOException {
        heartBeatService.download(heartBeatService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询HeartBeat")
    @ApiOperation("查询HeartBeat")
    @PreAuthorize("@el.check('heartBeat:list')")
    public ResponseEntity getHeartBeats(HeartBeatQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(heartBeatService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增HeartBeat")
    @ApiOperation("新增HeartBeat")
    @PreAuthorize("@el.check('heartBeat:add')")
    public ResponseEntity create(@Validated @RequestBody HeartBeat resources){
        return new ResponseEntity<>(heartBeatService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改HeartBeat")
    @ApiOperation("修改HeartBeat")
    @PreAuthorize("@el.check('heartBeat:edit')")
    public ResponseEntity update(@Validated @RequestBody HeartBeat resources){
        heartBeatService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除HeartBeat")
    @ApiOperation("删除HeartBeat")
    @PreAuthorize("@el.check('heartBeat:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        heartBeatService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}