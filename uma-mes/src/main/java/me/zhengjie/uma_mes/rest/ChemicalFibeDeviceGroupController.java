package me.zhengjie.uma_mes.rest;

import com.lgmn.common.utils.ObjectTransfer;
import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.ChemicalFibeDevice;
import me.zhengjie.uma_mes.domain.ChemicalFibeDeviceGroup;
import me.zhengjie.uma_mes.service.ChemicalFibeDeviceGroupService;
import me.zhengjie.uma_mes.service.ChemicalFibeDeviceService;
import me.zhengjie.uma_mes.service.dto.ChemicalFibeDeviceDTO;
import me.zhengjie.uma_mes.service.dto.ChemicalFibeDeviceGroupQueryCriteria;
import me.zhengjie.uma_mes.service.dto.ChemicalFibeDeviceQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

/**
* @author Xie Ji Biao
* @date 2020-04-16
*/
@Api(tags = "ChemicalFibeDeviceGroup管理")
@RestController
@RequestMapping("/api/chemicalFibeDeviceGroup")
public class ChemicalFibeDeviceGroupController {

    private final ChemicalFibeDeviceGroupService chemicalFibeDeviceGroupService;

    private final ChemicalFibeDeviceService chemicalFibeDeviceService;

    public ChemicalFibeDeviceGroupController(ChemicalFibeDeviceGroupService chemicalFibeDeviceGroupService, ChemicalFibeDeviceService chemicalFibeDeviceService) {
        this.chemicalFibeDeviceGroupService = chemicalFibeDeviceGroupService;
        this.chemicalFibeDeviceService = chemicalFibeDeviceService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('chemicalFibeDeviceGroup:list')")
    public void download(HttpServletResponse response, ChemicalFibeDeviceGroupQueryCriteria criteria) throws IOException {
        chemicalFibeDeviceGroupService.download(chemicalFibeDeviceGroupService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询ChemicalFibeDeviceGroup")
    @ApiOperation("查询ChemicalFibeDeviceGroup")
    @PreAuthorize("@el.check('chemicalFibeDeviceGroup:list')")
    public ResponseEntity getChemicalFibeDeviceGroups(ChemicalFibeDeviceGroupQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(chemicalFibeDeviceGroupService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增ChemicalFibeDeviceGroup")
    @ApiOperation("新增ChemicalFibeDeviceGroup")
    @PreAuthorize("@el.check('chemicalFibeDeviceGroup:add')")
    public ResponseEntity create(@Validated @RequestBody ChemicalFibeDeviceGroup resources){
        resources.setStatus(1);
        return new ResponseEntity<>(chemicalFibeDeviceGroupService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改ChemicalFibeDeviceGroup")
    @ApiOperation("修改ChemicalFibeDeviceGroup")
    @PreAuthorize("@el.check('chemicalFibeDeviceGroup:edit')")
    public ResponseEntity update(@Validated @RequestBody ChemicalFibeDeviceGroup resources){
        ChemicalFibeDeviceQueryCriteria chemicalFibeDeviceQueryCriteria = new ChemicalFibeDeviceQueryCriteria();
        chemicalFibeDeviceQueryCriteria.setGroupId(resources.getId());
        List<ChemicalFibeDeviceDTO> chemicalFibeDeviceDTOS = chemicalFibeDeviceService.queryAll(chemicalFibeDeviceQueryCriteria);
        for (ChemicalFibeDeviceDTO chemicalFibeDeviceDTO : chemicalFibeDeviceDTOS) {
            ChemicalFibeDevice chemicalFibeDevice = new ChemicalFibeDevice();
            ObjectTransfer.transValue(chemicalFibeDeviceDTO, chemicalFibeDevice);
            chemicalFibeDevice.setGroupName(resources.getName());
            chemicalFibeDeviceService.update(chemicalFibeDevice);
        }
        chemicalFibeDeviceGroupService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除ChemicalFibeDeviceGroup")
    @ApiOperation("删除ChemicalFibeDeviceGroup")
    @PreAuthorize("@el.check('chemicalFibeDeviceGroup:del')")
    public ResponseEntity delete(@PathVariable Integer id){
        chemicalFibeDeviceGroupService.delete(id);
        ChemicalFibeDeviceQueryCriteria chemicalFibeDeviceQueryCriteria = new ChemicalFibeDeviceQueryCriteria();
        chemicalFibeDeviceQueryCriteria.setGroupId(id);
        List<ChemicalFibeDeviceDTO> chemicalFibeDeviceDTOS = chemicalFibeDeviceService.queryAll(chemicalFibeDeviceQueryCriteria);
        for (ChemicalFibeDeviceDTO chemicalFibeDeviceDTO : chemicalFibeDeviceDTOS) {
            chemicalFibeDeviceService.delete(chemicalFibeDeviceDTO.getId());
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}