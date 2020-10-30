package me.zhengjie.uma_mes.rest.terminal;

import com.lgmn.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.aop.log.Log;
import me.zhengjie.uma_mes.domain.ChemicalFiberProduction;
import me.zhengjie.uma_mes.service.dto.termina.TerminalUploadDataDto;
import me.zhengjie.uma_mes.service.terminal.TerminalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "优码MES:终端机API")
@RestController
@RequestMapping("/api/terminal")
public class TerminalController {

    @Autowired
    TerminalService terminalService;

    @Log("终端机添加产品，库存，生产单")
    @ApiOperation("终端机添加产品，库存，生产单")
    @PostMapping(value = "/terminalUploadData")
    @AnonymousAccess()
    public ChemicalFiberProduction terminalUploadData(@RequestBody TerminalUploadDataDto terminalUploadDataDto) {
        Boolean a = true;
        return terminalService.terminalUploadData(terminalUploadDataDto, a);
    }

}
