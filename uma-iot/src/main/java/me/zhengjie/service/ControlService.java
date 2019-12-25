package me.zhengjie.service;

import com.lgmn.common.result.Result;
import com.lgmn.common.utils.ObjectTransfer;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.domain.CancelInfo;
import me.zhengjie.domain.ControlPannelInfo;
import me.zhengjie.domain.ReprintInfo;
import me.zhengjie.domain.UserInfo;
import me.zhengjie.server.NettyTcpServer;
import me.zhengjie.terminal.GobalSender;
import me.zhengjie.terminal.terminal.CancelPage;
import me.zhengjie.terminal.terminal.ControllerPage;
import me.zhengjie.terminal.terminal.ReprintPage;
import me.zhengjie.terminal.terminal.Terminal;
import me.zhengjie.uma_mes.domain.ChemicalFiberLabel;
import me.zhengjie.uma_mes.domain.ChemicalFiberProduct;
import me.zhengjie.uma_mes.domain.ChemicalFiberProduction;
import me.zhengjie.uma_mes.service.ChemicalFiberLabelService;
import me.zhengjie.uma_mes.service.ChemicalFiberProductService;
import me.zhengjie.uma_mes.service.ChemicalFiberProductionService;
import me.zhengjie.uma_mes.service.MachineService;
import me.zhengjie.uma_mes.service.dto.*;
import me.zhengjie.uma_mes.service.dto.termina.TerminalUploadDataDto;
import me.zhengjie.uma_mes.service.terminal.TerminalService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Random;

@Slf4j
@Component
public class ControlService {
    private static final Logger logger = LoggerFactory.getLogger(ControlService.class);
    @Value("${uma.label.printedIsIn}")
    boolean printIsIn;

    @Autowired
    MachineService machineService;

    @Autowired
    ChemicalFiberProductionService productionService;

    @Autowired
    ChemicalFiberProductService productService;

    @Autowired
    ChemicalFiberLabelService labelService;

    @Autowired
    TerminalService terminalService;

    @Autowired
    ControllerPage controllerPage;

    @Autowired
    ReprintPage reprintPage;
    /**
     * 获取机台生产单数据
     */
    public void getProdutionByMacchine(String machineId, String ip) {
        updateControlPanelInfo(machineId, ip);
    }

    /**
     * 更新内存数据
     *
     * @param machineId
     * @param ip
     */
    public void updateControlPanelInfo(String machineId, String ip) {
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);
        UserInfo userinfo = terminal.getUserinfo();
        ControlPannelInfo controlPanelInfo = terminal.getControlPannelInfo();
        MachineDTO machineDTO = machineService.findById(Integer.parseInt(machineId));

        if (machineDTO != null) {
            ChemicalFiberProductionDTO productionDTO = productionService.findById(machineDTO.getProductionId());

            BigDecimal tare = productionDTO.getCoreWeight().multiply(new BigDecimal(productionDTO.getPerBagNumber()));
            controlPanelInfo.setProductionNumber(productionDTO.getNumber());
            controlPanelInfo.setCustomerCode(productionDTO.getCustomerCode());
            controlPanelInfo.setFineness(productionDTO.getProdFineness());
            controlPanelInfo.setCoreWeight(productionDTO.getCoreWeight().toString());
            controlPanelInfo.setMachineNumber(productionDTO.getMachineNumber());
            controlPanelInfo.setFactPerBagNumber(productionDTO.getPerBagNumber().toString());

            //手动设置状态下，切换单号，才更新皮重
            if(controlPanelInfo.getMode().equals("manual")) {
                controlPanelInfo.setTare(tare.toString());
            }else{
                controlPanelInfo.setTare("0");
            }

            controlPanelInfo.setProductionId(productionDTO.getId());
            controlPanelInfo.setProductId(productionDTO.getProdId());
            controlPanelInfo.setColor(productionDTO.getProdColor());

            updateControllerPage(Integer.parseInt(machineId), ip);
        } else {
            GobalSender gobalSender = terminal.getGobalSender();
            terminal.addGoControlCommand();
            gobalSender.addCommand(controllerPage.sendTip("当前机号没有生产单", ip));
            controllerPage.cleanControllerPage(ip);
        }
    }

    /**
     * 更新控制界面汇总字段：累计数量，累计重量
     *
     * @param ip
     */
    public void updateControllerPageTotalFields(String ip) {
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);
        ControlPannelInfo controlPanelInfo = terminal.getControlPannelInfo();
        GobalSender gobalSender = terminal.getGobalSender();

        ChemicalFiberLabelTotalDTO labelTotalDTO = labelService.getTotalByProductionId(controlPanelInfo.getProductionId());

        controlPanelInfo.setTotalNumber(labelTotalDTO.getTotalNumber().toString());
        controlPanelInfo.setTotalWeight(labelTotalDTO.getTotalWeight().toString());

        gobalSender.addCommand(controllerPage.sendTotalNumber(controlPanelInfo.getTotalNumber(), ip));
        gobalSender.addCommand(controllerPage.sendTotalWeight(controlPanelInfo.getTotalWeight(), ip));
        gobalSender.addCommand(controllerPage.sendTip("",ip));
        gobalSender.send(2000);
    }

    public void updateControllerPageTotalFieldsAndGoControl(String ip){
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);
        updateControllerPageTotalFields(ip);
        terminal.goControl();
    }

    /**
     * 更新控制界面信息
     *
     * @param machineId
     * @param ip
     */
    public void updateControllerPage(Integer machineId, String ip) {
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);
        ControlPannelInfo controlPanelInfo = terminal.getControlPannelInfo();
        UserInfo userInfo = terminal.getUserinfo();
        GobalSender gobalSender = terminal.getGobalSender();

        String loginInfo = userInfo.getUserName() == null ? "" : userInfo.getUserName();
        gobalSender.addCommand(controllerPage.sendLoginInfo("操作员：" + loginInfo, ip));
        gobalSender.addCommand(controllerPage.sendProductionNumber(controlPanelInfo.getProductionNumber(), ip));
        gobalSender.addCommand(controllerPage.sendCustomerCode(controlPanelInfo.getCustomerCode(), ip));
        gobalSender.addCommand(controllerPage.sendFineness(controlPanelInfo.getFineness(), ip));
        gobalSender.addCommand(controllerPage.sendCoreWeight(controlPanelInfo.getCoreWeight(), ip));
        gobalSender.addCommand(controllerPage.sendMachineNumber(controlPanelInfo.getMachineNumber(), ip));
        gobalSender.addCommand(controllerPage.sendBanci(userInfo.getBanci(), ip));
        gobalSender.addCommand(controllerPage.sendFactPerBagNumber(controlPanelInfo.getFactPerBagNumber(), ip));
        gobalSender.addCommand(controllerPage.sendTare(controlPanelInfo.getTare(), ip));

        updateControllerPageTotalFieldsAndGoControl(ip);
    }

    /**
     * 打印
     */
    public void print(String ip) {
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);

        ControlPannelInfo controlPanelInfo = terminal.getControlPannelInfo();
        UserInfo userInfo = terminal.getUserinfo();

        int status=0;

        if(printIsIn){
            status=1;
        }

        ChemicalFiberLabel label = new ChemicalFiberLabel();
        label.setGrossWeight(new BigDecimal(controlPanelInfo.getGrossWeight()));
        label.setNetWeight(new BigDecimal(controlPanelInfo.getNetWeight()));
        label.setPacker(userInfo.getUserName());
        label.setPrintTime(new Timestamp(System.currentTimeMillis()));
        label.setProductionId(controlPanelInfo.getProductionId());
        label.setProductId(controlPanelInfo.getProductId());
        label.setShifts(userInfo.getBanci());
        label.setStatus(status);
        label.setTare(new BigDecimal(controlPanelInfo.getTare()));
        label.setLabelNumber(generateLabelNumber(controlPanelInfo.getMachineNumber()));
        label.setFactPerBagNumber(Integer.parseInt(controlPanelInfo.getFactPerBagNumber()));
        label.setMachine(controlPanelInfo.getMachineNumber());
        label.setFineness(controlPanelInfo.getFineness());
        label.setColor(controlPanelInfo.getColor());
        label.setCoreWeight(new BigDecimal(controlPanelInfo.getCoreWeight()));

        ChemicalFiberLabelDTO labelDto = labelService.create(label);

        /**
         * todo 打印操作
         */
        log.info("before print");
        terminal.print(labelDto.getLabelNumber());
        terminal.getReprintInfo().setLabelNumber(labelDto.getLabelNumber());
        terminal.getCancelInfo().setLabelNumber(labelDto.getLabelNumber());
        log.info("after print");

        updateControllerPageTotalFieldsAndGoControl(ip);
    }

    /**
     * 作废
     */
    public void cancel(String ip) {
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);
        GobalSender gobalSender = terminal.getGobalSender();
        CancelInfo cancelInfo = terminal.getCancelInfo();
        String labelNumber = cancelInfo.getLabelNumber();
        CancelPage cancelPage = new CancelPage();
        if(StringUtils.isEmpty(labelNumber)){
            gobalSender.sendImmediate(cancelPage.sendTip("请输入标签号",ip));
        }else {
            ChemicalFiberLabel label = labelService.getByLabelNumber(labelNumber);
            if(label==null){
                gobalSender.sendImmediate(cancelPage.sendTip("找不到标签记录",ip));
            }else if(label.getStatus()==2){
                gobalSender.sendImmediate(cancelPage.sendTip("标签已出库,不能作废",ip));
            }else if(label.getStatus()==3){
                gobalSender.sendImmediate(cancelPage.sendTip("标签已作废",ip));
            }else{
                label.setStatus(3);
                labelService.update(label);
                gobalSender.sendImmediate(cancelPage.sendTip("标签作废成功",ip));

                gobalSender.addCommand(cancelPage.sendLabelNumber("",ip));
                gobalSender.addCommand(cancelPage.sendTip("",ip));
                gobalSender.send(2000);
            }
        }

    }

    public void getReprintInfo(String ip){
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);
        ReprintInfo reprintInfo = terminal.getReprintInfo();

        ChemicalFiberLabel label = labelService.getByLabelNumber(reprintInfo.getLabelNumber());

        reprintInfo.setChemicalFiberLabel(label);
        if(label==null){
            reprintInfo.setChemicalFiberProductDTO(null);
        }else{
            ChemicalFiberProductDTO product = productService.findById(label.getProductId());
            reprintInfo.setChemicalFiberProductDTO(product);
        }
    }

    /**
     * 补打
     */
    public void reprint(String ip) {
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);
        ReprintInfo reprintInfo = terminal.getReprintInfo();

        ChemicalFiberLabel label = reprintInfo.getChemicalFiberLabel();
        ChemicalFiberProductDTO product = reprintInfo.getChemicalFiberProductDTO();

        terminal.reprint(label,product);
    }

    private String generateLabelNumber(String jitai) {
        //条码：类型（一位）+年月（4位）+ 6位跳码 + 机台号（四位）
        String type = "1";
        SimpleDateFormat myFmt = new SimpleDateFormat("yyMM");
        String ym = myFmt.format(System.currentTimeMillis());
        String random = randomCode();
        String jt = "000" + jitai;
        jt = jt.substring(jt.length() - 4);
        String labelNumber = type + ym + random + jt;
        return labelNumber;
    }

    public String randomCode() {
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }

    public ChemicalFiberProduction terminalUploadData(TerminalUploadDataDto terminalUploadDataDto){
        ChemicalFiberProduction result = terminalService.terminalUploadData(terminalUploadDataDto);
        return  result;
    }

    public ChemicalFiberLabel getLastLabelByMachine(String machine){
        ChemicalFiberLabel chemicalFiberLabel = labelService.getLastLabelByMachine(machine);
        return chemicalFiberLabel;
    }
}