package me.zhengjie.terminal.terminal;

import lombok.extern.slf4j.Slf4j;
import me.zhengjie.domain.CancelInfo;
import me.zhengjie.domain.ControlPannelInfo;
import me.zhengjie.domain.ReprintInfo;
import me.zhengjie.domain.UserInfo;
import me.zhengjie.server.NettyTcpServer;
import me.zhengjie.service.ControlService;
import me.zhengjie.terminal.GobalSender;
import me.zhengjie.terminal.annotation.Button;
import me.zhengjie.terminal.annotation.Screen;
import me.zhengjie.terminal.annotation.Text;
import me.zhengjie.terminal.command.SendCommand;
import me.zhengjie.uma_mes.domain.ChemicalFiberLabel;
import me.zhengjie.uma_mes.domain.ChemicalFiberProduction;
import me.zhengjie.uma_mes.service.dto.termina.TerminalUploadDataDto;
import me.zhengjie.utils.CoderUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Screen(id = "00 02")
@Slf4j
@Component
public class ControllerPage extends SendCommand {
    private static final Logger logger = LoggerFactory.getLogger(ControllerPage.class);
    @Autowired
    ControlService controlService;

    @Value("${uma.production.createByTerminal}")
    boolean createByTerminal;

    final String screenId = "00 02";
    final String tipId = "00 02";

    public void setLoginInfo(String loginInfo, String ip) {
        NettyTcpServer.terminalMap.get(ip).getControlPannelInfo().setLoginInfo(loginInfo);
    }

    public void setTip(String tip, String ip) {
        NettyTcpServer.terminalMap.get(ip).getControlPannelInfo().setTip(tip);
    }

    public void setProductionNumber(String productionNumber, String ip) {
        NettyTcpServer.terminalMap.get(ip).getControlPannelInfo().setProductionNumber(productionNumber);

    }

    public void setCustomerCode(String customerCode, String ip) {
        Terminal terminal =  NettyTcpServer.terminalMap.get(ip);
        GobalSender gobalSender = terminal.getGobalSender();
        ControlPannelInfo controlPannelInfo = terminal.getControlPannelInfo();
        if (createByTerminal) {
            terminal.setXishaji(false);
            // 洗纱机
            String xishaji = "9999";
            // 废料
            String feiliao = "9998";
            if(customerCode.equals(xishaji)){
                controlPannelInfo.setColor("洗机纱");
                gobalSender.send(sendCustomerCode("洗机纱",ip));
                terminal.setXishaji(true);
            }else if(customerCode.equals(feiliao)){
                controlPannelInfo.setColor("废料");
                controlPannelInfo.setFineness("X");
                gobalSender.addCommand(sendCustomerCode("废料",ip));
                gobalSender.addCommand(sendFineness("X",ip));
                gobalSender.send();
            }else {
                controlPannelInfo.setColor(customerCode);
            }
//            updateProductionId(ip);
            gobalSender.sendImmediate(sendProductionNumber("",ip));
            controlPannelInfo.setProductionNumber("");
        } else {
            controlPannelInfo.setCustomerCode(customerCode);
        }
    }

    public void setFineness(String fineness, String ip) {
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);
        GobalSender gobalSender = terminal.getGobalSender();
        terminal.getControlPannelInfo().setFineness(fineness);
        if (createByTerminal) {
//            updateProductionId(ip);
            ControlPannelInfo controlPannelInfo = terminal.getControlPannelInfo();
            gobalSender.sendImmediate(sendProductionNumber("",ip));
            controlPannelInfo.setProductionNumber("");
        }
    }

    public void setCoreWeight(String coreWeight, String ip) {
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);

        ControlPannelInfo controlPannelInfo = terminal.getControlPannelInfo();
        controlPannelInfo.setCoreWeight(coreWeight);
        if (terminal.getControlPannelInfo().getMode().equals("manual")) {
            competeTare(ip);
        }
    }

    public void setBanci(String banci, String ip) {
        NettyTcpServer.terminalMap.get(ip).getControlPannelInfo().setBanci(banci);
    }

    public void setMachineNumber(String machineNumber, String ip) {
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);
        boolean hadlogin=terminal.checkLoginStatus(screenId,tipId);
        if(!hadlogin){
            return;
        }
        ControlPannelInfo controlPannelInfo = terminal.getControlPannelInfo();
        controlPannelInfo.setMachineNumber(machineNumber);

        if (createByTerminal) {
            GobalSender gobalSender = terminal.getGobalSender();

            String banci = controlPannelInfo.getBanci();
            gobalSender.addCommand(sendBanci(banci + " - " + machineNumber, ip));
            gobalSender.addCommand(sendJitai2(machineNumber, ip));
            // 修改机台号时，查询最后一个标签信息并更新屏幕
            ChemicalFiberLabel label = controlService.getLastLabelByMachine(machineNumber);
            if(label!=null){
                gobalSender.addCommand(sendCustomerCode(label.getColor(),ip));
                gobalSender.addCommand(sendFineness(label.getFineness(),ip));
                gobalSender.addCommand(sendFactPerBagNumber(label.getFactPerBagNumber().toString(),ip));
                gobalSender.addCommand(sendCoreWeight(label.getCoreWeight().toString(),ip));
                gobalSender.addCommand(sendTare(label.getTare().toString(),ip));
                gobalSender.addCommand(sendTotalWeight("",ip));
                gobalSender.addCommand(sendTotalNumber("",ip));
                controlPannelInfo.setColor(label.getColor());
                controlPannelInfo.setFineness(label.getFineness());
                controlPannelInfo.setFactPerBagNumber(label.getFactPerBagNumber().toString());
                controlPannelInfo.setCoreWeight(label.getCoreWeight().toString());
                controlPannelInfo.setTare(label.getTare().toString());
                controlPannelInfo.setTotalWeight("");
                controlPannelInfo.setTotalNumber("");
                updateProductionId(ip);
            }else{
                gobalSender.addCommand(sendProductionNumber("",ip));
                gobalSender.addCommand(sendCustomerCode("",ip));
                gobalSender.addCommand(sendFineness("",ip));
                gobalSender.addCommand(sendFactPerBagNumber("",ip));
                gobalSender.addCommand(sendCoreWeight("",ip));
                gobalSender.addCommand(sendTare("",ip));
                gobalSender.addCommand(sendTotalWeight("",ip));
                gobalSender.addCommand(sendTotalNumber("",ip));
                controlPannelInfo.setProductionNumber("");
                controlPannelInfo.setColor("");
                controlPannelInfo.setFineness("");
                controlPannelInfo.setFactPerBagNumber("");
                controlPannelInfo.setCoreWeight("");
                controlPannelInfo.setTare("");
                controlPannelInfo.setTotalWeight("");
                controlPannelInfo.setTotalNumber("");
            }
//            gobalSender.addCommand(enablePrint());
            gobalSender.send();
        }
    }

    public void setFactPerBagNumber(String factPerBagNumber, String ip) {
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);

        ControlPannelInfo controlPannelInfo = terminal.getControlPannelInfo();
        controlPannelInfo.setFactPerBagNumber(factPerBagNumber);

        if (terminal.getControlPannelInfo().getMode().equals("manual")) {
            competeTare(ip);
        }
    }

    private void competeTare( String ip) {
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);

        ControlPannelInfo controlPannelInfo = terminal.getControlPannelInfo();
        GobalSender gobalSender = terminal.getGobalSender();
        String coreWeightStr = controlPannelInfo.getCoreWeight();
        coreWeightStr = coreWeightStr.equals("") ? "0" : coreWeightStr;
        String factPerBagNumber = controlPannelInfo.getFactPerBagNumber();
        factPerBagNumber = factPerBagNumber.equals("") ? "0" : factPerBagNumber;

        BigDecimal bagNumber = new BigDecimal(factPerBagNumber);
        BigDecimal coreWeight = new BigDecimal(coreWeightStr);
        BigDecimal tare = bagNumber.multiply(coreWeight);

        String tareStr = tare.toString();
        controlPannelInfo.setTare(tareStr);

        gobalSender.sendImmediate(sendTare(tareStr, ip));
    }

    public void updateGrossWeight() {

    }

    public void setTare(String tare, String ip) {
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);
        ControlPannelInfo controlPannelInfo = terminal.getControlPannelInfo();
        Integer manualModeTimes = controlPannelInfo.getManualModeEventTimes();

        if (manualModeTimes > 1) {
            controlPannelInfo.setTare(tare);
        }
        manualModeTimes = manualModeTimes + 1;
        controlPannelInfo.setManualModeEventTimes(manualModeTimes);
    }

    public void setTotalNumber(String totalNumber, String ip) {
        NettyTcpServer.terminalMap.get(ip).getControlPannelInfo().setTotalNumber(totalNumber);
    }

    public void setTotalWeight(String totalWeight, String ip) {
        NettyTcpServer.terminalMap.get(ip).getControlPannelInfo().setTotalWeight(totalWeight);
    }

    public void setNetWeight(String netWeight, String ip) {
        NettyTcpServer.terminalMap.get(ip).getControlPannelInfo().setNetWeight(netWeight);
    }

    public void setJitai2(String jitai2,String ip){
        log.info("setJitai2 do nothing");
    }

    public void setWeights(String weights, String ip) {
        //YM02AABBM0.000P0.000
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);
        GobalSender gobalSender = terminal.getGobalSender();
        try {
            ControlPannelInfo controlPannelInfo = terminal.getControlPannelInfo();
            String[] weightsArray = weights.replace("YM02AABBM", "").split("P");

            String grossWeightStr = weightsArray[0];
            String tareStr = controlPannelInfo.getTare();
            String netWeightStr = "";
            BigDecimal grossWeight = new BigDecimal(grossWeightStr);
            String nul = "\u0000";
            if (StringUtils.isEmpty(tareStr) || nul.equals(tareStr)) {
                tareStr = "0";
            }
            BigDecimal tare = new BigDecimal(tareStr);
            BigDecimal netWeight = grossWeight.subtract(tare);
            netWeightStr = netWeight.toString();

            controlPannelInfo.setGrossWeight(grossWeightStr);
            controlPannelInfo.setNetWeight(netWeightStr);
            controlPannelInfo.setHadGetWeight(true);
            if(afterGetWeight(ip)) {
                if (terminal.isPrint) {
                    controlService.print(ip);
                } else {
                    tareStr = weightsArray[1];
                    controlPannelInfo.setTare(tareStr);
                    gobalSender.sendImmediate(sendTare(tareStr, ip));
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            terminal.addGoControlCommand();
            if (terminal.getUserinfo().getUserName() == null) {
                gobalSender.addCommand(sendTip("与服务器断开连接，请重新登录", ip));
            } else {
                gobalSender.addCommand(sendTip("获取重量失败,请“置零”后重试", ip));
            }
            gobalSender.send();
        }finally {
            terminal.isPrint = false;
        }
    }

    public String sendLoginInfo(String loginInfo, String ip) {
        return setTextValue("00 02", "00 01", loginInfo);
    }

    public String sendTip(String tip, String ip) {
        return setTextValue("00 02", "00 02", tip);
    }

    public String sendProductionNumber(String productionNumber, String ip) {
        return setTextValue("00 02", "00 03", productionNumber);
    }

    public String sendCustomerCode(String customerCode, String ip) {
        return setTextValue("00 02", "00 04", customerCode);
    }

    public String sendFineness(String fineness, String ip) {
        return setTextValue("00 02", "00 05", fineness);
    }

    public String sendCoreWeight(String coreWeight, String ip) {
        return setTextValue("00 02", "00 06", coreWeight);
    }

    public String sendBanci(String banci, String ip) {
        return setTextValue("00 02", "00 07", banci);
    }

    public String sendMachineNumber(String machineNumber, String ip) {
        return setTextValue("00 02", "00 08", machineNumber);
    }

    public String sendFactPerBagNumber(String factPerBagNumber, String ip) {
        return setTextValue("00 02", "00 09", factPerBagNumber);
    }

    public String sendTare(String tare, String ip) {
        return setTextValue("00 02", "00 0a", tare);
    }

    public String sendTotalNumber(String totalNumber, String ip) {
        return setTextValue("00 02", "00 0b", totalNumber);
    }

    public String sendTotalWeight(String totalWeight, String ip) {
        return setTextValue("00 02", "00 0c", totalWeight);
    }

    public String sendNetWeight(String netWeight, String ip) {
        return setTextValue("00 02", "00 0d", netWeight);
    }

    public String sendJitai2(String jitai2, String ip) {
        return setTextValue("00 02", "00 0e", jitai2);
    }

    public void getWeights(String ip) {
        log.info("getWeights");
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);
        GobalSender gobalSender = terminal.getGobalSender();
        gobalSender.send(CoderUtils.stringToHexStr("YM02AABB"));

        if(!terminal.isPrint()){
            gobalSender.sendDeloy(sendTip("", ip), 1000);
        }
    }

    public void getSettingStatus(String ip) {
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);
        GobalSender gobalSender = terminal.getGobalSender();
        gobalSender.send("EE B1 11 00 02 00 2B FF FC FF FF");
    }

    @Text(id = "00 01", handler = "setLoginInfo")
    String loginInfo;
    @Text(id = "00 02", handler = "setTip")
    String tip;
    @Text(id = "00 03", handler = "setProductionNumber")
    String productionNumber;
    @Text(id = "00 04", handler = "setCustomerCode")
    String customerCode;
    @Text(id = "00 05", handler = "setFineness")
    String fineness;
    @Text(id = "00 06", handler = "setCoreWeight")
    String coreWeight;
    @Text(id = "00 07", handler = "setBanci")
    String banci;
    @Text(id = "00 08", handler = "setMachineNumber")
    String machineNumber;
    @Text(id = "00 09", handler = "setFactPerBagNumber")
    String factPerBagNumber;
    @Text(id = "00 0a", handler = "setTare")
    String tare;
    @Text(id = "00 0b", handler = "setTotalNumber")
    String totalNumber;
    @Text(id = "00 0c", handler = "setTotalWeight")
    String totalWeight;
    @Text(id = "00 0d", handler = "setNetWeight")
    String netWeight;

    @Text(id = "00 0e", handler = "setJitai2")
    String jitai2;

    @Button(id = "00 11", handler = "event_print")
    String btn_print;

    @Button(id = "00 12", handler = "event_cancel")
    String btn_cancel;

    @Button(id = "00 13", handler = "event_reprint")
    String btn_reprint;

    @Button(id = "00 10", handler = "event_logout")
    String btn_logout;

    @Button(id = "00 25", handler = "event_zero")
    String btn_zero;

    @Button(id = "00 26", handler = "event_cleanTare")
    String btn_cleanTare;

    @Button(id = "00 2b", handler = "event_manual")
    String btn_manual;

    @Button(id = "00 2c", handler = "event_auto")
    String btn_auto;

    @Button(id = "00 2d", handler = "event_back")
    String btn_back;

    public void event_manual(String buttonId, String ip) {
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);
        GobalSender gobalSender = terminal.getGobalSender();
        ControlPannelInfo controlPannelInfo = terminal.getControlPannelInfo();
        controlPannelInfo.setMode("manual");
        String numberStr = controlPannelInfo.getFactPerBagNumber();
        String coreWeightStr = controlPannelInfo.getCoreWeight();

        BigDecimal number = new BigDecimal(numberStr);
        BigDecimal coreWeight = new BigDecimal(coreWeightStr);
        BigDecimal tare = number.multiply(coreWeight);

        String tareStr = tare.toString();
        controlPannelInfo.setTare(tareStr);
        controlPannelInfo.setManualModeEventTimes(0);
        gobalSender.send(sendTare(tareStr, ip));
    }

    public void event_auto(String button, String ip) {
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);
        GobalSender gobalSender = terminal.getGobalSender();
        ControlPannelInfo controlPannelInfo = terminal.getControlPannelInfo();
        controlPannelInfo.setMode("auto");
        controlPannelInfo.setTare("0");
        gobalSender.send(sendTare("0", ip));
    }

    public void event_print(String buttonId, String ip) {
        log.info("print event");
//        controlService.beforePrint(ip);

        Terminal terminal = NettyTcpServer.terminalMap.get(ip);
        boolean hadlogin=terminal.checkLoginStatus(screenId,tipId);
        if(!hadlogin){
            return;
        }
        terminal.isPrint = true;
        if(beforePrint(ip)) {
            terminal.goPrinting();
            getWeights(ip);
        }
    }

    private boolean beforePrint(String ip){
        boolean canPrint = true;
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);
        GobalSender gobalSender = terminal.getGobalSender();
        ControlPannelInfo controlPannelInfo = terminal.getControlPannelInfo();
        boolean isXishaji = terminal.isXishaji;

        String productionNumber = controlPannelInfo.getProductionNumber();
        if(createByTerminal && StringUtils.isEmpty(productionNumber)) {
            canPrint = false;
            //点击打印时查询/创建订单
            updateProductionId(ip);
            return canPrint;
        }

//        if(isXishaji){
            String fineness = controlPannelInfo.getFineness();
            if(StringUtils.isEmpty(fineness)){
                canPrint = false;
                gobalSender.send(sendTip("打印失败，请输入纤度", ip));

                return canPrint;
            }
//        }

        if(StringUtils.isEmpty(controlPannelInfo.getColor())){
            canPrint = false;
            gobalSender.send(sendTip("打印失败，请输入色号", ip));
        }

        if(StringUtils.isEmpty(controlPannelInfo.getCoreWeight())){
            canPrint = false;
            gobalSender.send(sendTip("打印失败，请输入纸芯重量", ip));

            return canPrint;
        }

        if(StringUtils.isEmpty(controlPannelInfo.getMachineNumber())){
            canPrint = false;
            gobalSender.send(sendTip("打印失败，请输入机台号", ip));

            return canPrint;
        }

        if(StringUtils.isEmpty(controlPannelInfo.getFactPerBagNumber())){
            canPrint = false;
            gobalSender.send(sendTip("打印失败，请输入个数", ip));

            return canPrint;
        }

        if(StringUtils.isEmpty(controlPannelInfo.getTare())){
            canPrint = false;
            gobalSender.send(sendTip("打印失败，请输入皮重", ip));

            return canPrint;
        }
        return canPrint;
    }

    public boolean afterGetWeight(String ip){
        boolean canPrint = true;
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);
        GobalSender gobalSender = terminal.getGobalSender();
        ControlPannelInfo controlPannelInfo = terminal.getControlPannelInfo();
        boolean hadGetWeight = controlPannelInfo.isHadGetWeight();
        if(hadGetWeight) {
            BigDecimal netWeight = new BigDecimal(controlPannelInfo.getNetWeight());

            if (netWeight.compareTo(new BigDecimal("0")) < 0) {
                canPrint = false;
                terminal.addGoControlCommand();
                gobalSender.addCommand(sendTip("重量值不能为负数", ip));
                gobalSender.send();
            } else if (netWeight.compareTo(new BigDecimal("0")) == 0) {
                canPrint = false;
                terminal.addGoControlCommand();
                gobalSender.addCommand(sendTip("重量值不能等于0", ip));
                gobalSender.send();
            }
        }
        // 重置获取重量状态
        controlPannelInfo.setHadGetWeight(false);
        return canPrint;
    }

    public void event_cancel(String buttonId, String ip) {
        log.info("cancel event");
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);

        boolean hadlogin=terminal.checkLoginStatus(screenId,tipId);
        if(!hadlogin){
            return;
        }

        CancelInfo cancelInfo = terminal.getCancelInfo();
        CancelPage cancelPage = new CancelPage();

        GobalSender gobalSender = terminal.getGobalSender();
        gobalSender.addCommand(cancelPage.sendLabelNumber(cancelInfo.getLabelNumber(), ip));
        terminal.addGoCancelCommand();
        gobalSender.send();
    }

    public void event_reprint(String buttonId, String ip) {
        log.info("reprint event");
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);

        boolean hadlogin=terminal.checkLoginStatus(screenId,tipId);
        if(!hadlogin){
            return;
        }

        ReprintInfo reprintInfo = terminal.getReprintInfo();
        ReprintPage reprintPage = new ReprintPage();

        GobalSender gobalSender = terminal.getGobalSender();
        gobalSender.addCommand(reprintPage.sendLabelNumber(reprintInfo.getLabelNumber(), ip));
        terminal.addGoReprintCommand();
        gobalSender.send();
    }

    public void event_logout(String buttonId, String ip) {
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);
        GobalSender gobalSender = terminal.getGobalSender();
        gobalSender.addCommand(switchScreen("00 01"));
        cleanControllerPage(ip);
        cleanControllerPageInfo(ip);
    }

    public void event_cleanTare(String buttonId, String ip) {
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);
        terminal.isPrint = false;
        getWeights(ip);
    }

    public void event_zero(String buttonId, String ip) {
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);
        GobalSender gobalSender = terminal.getGobalSender();
        sendTip("", ip);
        gobalSender.send();
    }

    public void event_back(String buttonId, String ip) {
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);
        GobalSender gobalSender = terminal.getGobalSender();
        gobalSender.addCommand(switchScreen("00 03"));
        cleanControllerPage(ip);
        cleanControllerPageInfo(ip);
    }

    public void load(String ip) {
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);
        UserInfo userInfo = terminal.getUserinfo();
        String loginInfo = userInfo.getUserName() + " " + userInfo.getBanci();
        sendLoginInfo(loginInfo, ip);
    }

    public void cleanControllerPage(String ip) {
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);
        GobalSender gobalSender = terminal.getGobalSender();
        gobalSender.addCommand(sendTip("", ip));
        gobalSender.addCommand(sendMachineNumber("", ip));
        gobalSender.addCommand(sendCustomerCode("", ip));
        gobalSender.addCommand(sendFineness("", ip));
        gobalSender.addCommand(sendCoreWeight("", ip));
        gobalSender.addCommand(sendBanci("", ip));
        gobalSender.addCommand(sendMachineNumber("", ip));
        gobalSender.addCommand(sendFactPerBagNumber("", ip));
        gobalSender.addCommand(sendTare("", ip));
        gobalSender.addCommand(sendTotalNumber("", ip));
        gobalSender.addCommand(sendTotalWeight("", ip));
        gobalSender.addCommand(sendProductionNumber("",ip));
        gobalSender.addCommand(sendJitai2("",ip));
        gobalSender.send();
    }

    public void cleanControllerPageInfo(String ip){
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);
        ControlPannelInfo controlPannelInfo = terminal.getControlPannelInfo();
        controlPannelInfo.setTip("");
        controlPannelInfo.setMachineNumber("");
        controlPannelInfo.setCustomerCode("");
        controlPannelInfo.setFineness("");
        controlPannelInfo.setCoreWeight("");
        controlPannelInfo.setBanci("");
        controlPannelInfo.setFactPerBagNumber("");
        controlPannelInfo.setTare("");
        controlPannelInfo.setTotalWeight("");
        controlPannelInfo.setTotalWeight("");
        controlPannelInfo.setProductionNumber("");
    }

    public void updateProductionId(String ip) {
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);
        GobalSender gobalSender = terminal.getGobalSender();
        ControlPannelInfo controlPannelInfo = terminal.getControlPannelInfo();

        // 设置打印按钮不可用
        gobalSender.addCommand(disablePrint());
        if(terminal.isPrint()) {
            gobalSender.addCommand(sendTip("正在创建订单，请稍候！", ip));
        }else{
            gobalSender.addCommand(sendTip("正在查询订单，请稍候！", ip));
        }
        gobalSender.sendImmediate();
        String color = controlPannelInfo.getColor();
        String fineness = controlPannelInfo.getFineness();
        if(StringUtils.isNotEmpty(color) && StringUtils.isNotEmpty(fineness)) {
            TerminalUploadDataDto terminalUploadDataDto = new TerminalUploadDataDto();
            terminalUploadDataDto.setColor(color);
            terminalUploadDataDto.setFineness(fineness);
            terminalUploadDataDto.setMachineNumber(controlPannelInfo.getMachineNumber());
            ChemicalFiberProduction chemicalFiberProduction = controlService.terminalUploadData(terminalUploadDataDto);

            controlPannelInfo.setProductionId(chemicalFiberProduction.getId());
            controlPannelInfo.setProductId(chemicalFiberProduction.getProdId());
            controlPannelInfo.setProductionNumber(chemicalFiberProduction.getNumber());
            gobalSender.addCommand(sendProductionNumber(chemicalFiberProduction.getNumber(), ip));
            gobalSender.addCommand(sendTip("",ip));
//            String command = sendProductionNumber(chemicalFiberProduction.getNumber(), ip);
            // 设置打印按钮可用
            gobalSender.addCommand(enablePrint());
            gobalSender.send();

            controlService.updateControllerPageTotalFields(ip);

            if(terminal.isPrint()) {
                gobalSender.sendImmediate(sendTip("准备打印，请稍候！",ip));
                event_print("", ip);
            }
        }
    }

    public String enablePrint(){
        return setControlEnable(screenId,"00 11");
    }

    public String disablePrint(){
        return setControlDisable(screenId,"00 11");
    }
}