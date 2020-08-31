package me.zhengjie.uma_mes.rest;

import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.Car;
import me.zhengjie.uma_mes.service.CarService;
import me.zhengjie.uma_mes.service.dto.CarQueryCriteria;
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
* @date 2020-08-29
*/
@Api(tags = "Car管理")
@RestController
@RequestMapping("/api/car")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    //@PreAuthorize("@el.check('car:list')")
    public void download(HttpServletResponse response, CarQueryCriteria criteria) throws IOException {
        carService.download(carService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询Car")
    @ApiOperation("查询Car")
    //@PreAuthorize("@el.check('car:list')")
    public ResponseEntity getCars(CarQueryCriteria criteria, Pageable pageable){
        //return new ResponseEntity(HttpStatus.OK);
        return new ResponseEntity<>(carService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增Car")
    @ApiOperation("新增Car")
    //@PreAuthorize("@el.check('car:add')")
    public ResponseEntity create(@Validated @RequestBody Car resources){
        return new ResponseEntity<>(carService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改Car")
    @ApiOperation("修改Car")
    //@PreAuthorize("@el.check('car:edit')")
    public ResponseEntity update(@Validated @RequestBody Car resources){
        carService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除Car")
    @ApiOperation("删除Car")
    //@PreAuthorize("@el.check('car:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        carService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}