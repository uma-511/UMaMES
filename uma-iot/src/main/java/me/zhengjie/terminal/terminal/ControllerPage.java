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
import me.zhengjie.utils.CoderUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Screen(id = "00 02")
@Slf4j
@Component
public class ControllerPage extends SendCommand {
    @Autowired
    ControlService controlService;

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
        NettyTcpServer.terminalMap.get(ip).getControlPannelInfo().setCustomerCode(customerCode);
    }

    public void setFineness(String fineness, String ip) {
        NettyTcpServer.terminalMap.get(ip).getControlPannelInfo().setFineness(fineness);
    }

    public void setCoreWeight(String coreWeight, String ip) {
        NettyTcpServer.terminalMap.get(ip).getControlPannelInfo().setCoreWeight(coreWeight);
    }

    public void setBanci(String banci, String ip) {
        NettyTcpServer.terminalMap.get(ip).getControlPannelInfo().setBanci(banci);
    }

    public void setMachineNumber(String machineNumber, String ip) {
        NettyTcpServer.terminalMap.get(ip).getControlPannelInfo().setMachineNumber(machineNumber);
    }

    public void setFactPerBagNumber(String factPerBagNumber, String ip) {
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);

        ControlPannelInfo controlPannelInfo = terminal.getControlPannelInfo();
        controlPannelInfo.setFactPerBagNumber(factPerBagNumber);

        if(terminal.getControlPannelInfo().getMode().equals("manual")) {
            GobalSender gobalSender = terminal.getGobalSender();
            String coreWeightStr = controlPannelInfo.getCoreWeight();

            BigDecimal bagNumber = new BigDecimal(factPerBagNumber);
            BigDecimal coreWeight = new BigDecimal(coreWeightStr);
            BigDecimal tare = bagNumber.multiply(coreWeight);

            String tareStr = tare.toString();
            controlPannelInfo.setTare(tareStr);

            gobalSender.sendImmediate(sendTare(tareStr, ip));
        }
    }

    public void updateGrossWeight() {

    }

    public void setTare(String tare, String ip) {
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);
        ControlPannelInfo controlPannelInfo = terminal.getControlPannelInfo();
        Integer manualModeTimes=controlPannelInfo.getManualModeEventTimes();

        if(manualModeTimes > 1) {
            controlPannelInfo.setTare(tare);
        }
        manualModeTimes = manualModeTimes+1;
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

    public void setWeights(String weights, String ip) {
        //YM02AABBM0.000P0.000
        try {
            Terminal terminal = NettyTcpServer.terminalMap.get(ip);
            ControlPannelInfo controlPannelInfo = terminal.getControlPannelInfo();
            String[] weightsArray = weights.replace("YM02AABBM", "").split("P");

//            DecimalFormat df2 =new DecimalFormat("#0.00");
            String grossWeightStr = weightsArray[0];
//            String tareStr = weightsArray[1];
            String tareStr = controlPannelInfo.getTare();
            String netWeightStr = "";
            BigDecimal grossWeight = new BigDecimal(grossWeightStr);
            String nul = "\u0000";
            if(StringUtils.isEmpty(tareStr) || nul.equals(tareStr)){
                tareStr = "0";
            }
            BigDecimal tare = new BigDecimal(tareStr);
            BigDecimal netWeight = grossWeight.subtract(tare);
            netWeightStr = netWeight.toString();

            controlPannelInfo.setGrossWeight(grossWeightStr);
//            controlPannelInfo.setTare(tareStr);
            controlPannelInfo.setNetWeight(netWeightStr);
            if(terminal.isPrint) {
                controlService.print(ip);
            }else{
                tareStr = weightsArray[1];
                controlPannelInfo.setTare(tareStr);
                terminal.gobalSender.sendImmediate(sendTare(tareStr,ip));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            Terminal terminal = NettyTcpServer.terminalMap.get(ip);
            GobalSender gobalSender = terminal.getGobalSender();
            terminal.addGoControlCommand();
            if(terminal.getUserinfo().getUserName()==null){
                gobalSender.addCommand(sendTip("与服务器断开连接，请重新登录", ip));
            }else {
                gobalSender.addCommand(sendTip("获取重量失败,请“置零”后重试", ip));
            }
            gobalSender.send();
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

    public void getWeights(String ip) {
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);
        GobalSender gobalSender = terminal.getGobalSender();
        gobalSender.sendImmediate(CoderUtils.stringToHexStr("YM02AABB"));

        gobalSender.sendDeloy(sendTip("",ip),1000);
//        gobalSender.send();
        log.info("getWeights");
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
    @Text(id = "00 0c", handler = "setNetWeight")
    String netWeight;

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

    @Button(id="00 2b", handler = "event_manual")
    String btn_manual;

    @Button(id="00 2c", handler = "event_auto")
    String btn_auto;

    @Button(id = "00 2d", handler = "event_back")
    String btn_back;

    public void event_manual(String buttonId,String ip){
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
        gobalSender.send(sendTare(tareStr,ip));
    }

    public void event_auto(String button,String ip){
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);
        GobalSender gobalSender = terminal.getGobalSender();
        ControlPannelInfo controlPannelInfo = terminal.getControlPannelInfo();
        controlPannelInfo.setMode("auto");
        controlPannelInfo.setTare("0");
        gobalSender.send(sendTare("0",ip));
    }

    public void event_print(String buttonId, String ip) {
        log.info("print event");
//        controlService.beforePrint(ip);

        Terminal terminal = NettyTcpServer.terminalMap.get(ip);
        terminal.isPrint = true;
        getWeights(ip);
        terminal.goPrinting();
    }

    public void event_cancel(String buttonId, String ip) {
        log.info("cancel event");
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);
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
    }

    public void event_cleanTare(String buttonId, String ip) {
        Terminal terminal= NettyTcpServer.terminalMap.get(ip);
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
        gobalSender.send();
    }
}