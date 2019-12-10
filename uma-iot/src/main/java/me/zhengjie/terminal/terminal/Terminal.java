package me.zhengjie.terminal.terminal;

import io.netty.channel.Channel;
import lombok.Data;
import me.zhengjie.domain.CancelInfo;
import me.zhengjie.domain.ControlPannelInfo;
import me.zhengjie.domain.ReprintInfo;
import me.zhengjie.domain.UserInfo;
import me.zhengjie.server.NettyTcpServer;
import me.zhengjie.terminal.GobalSender;
import me.zhengjie.terminal.command.SendCommand;
import me.zhengjie.uma_mes.domain.ChemicalFiberLabel;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberProductDTO;
import me.zhengjie.uma_mes.service.dto.HeartBeatDTO;
import me.zhengjie.utils.CoderUtils;
import me.zhengjie.utils.SpringContextUtils;

import java.lang.String;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Data
public class Terminal extends SendCommand {
    String ip;

    Channel channel;
    UserInfo userinfo;
    ControlPannelInfo controlPannelInfo;
    CancelInfo cancelInfo;
    ReprintInfo reprintInfo;
    GobalSender gobalSender;
    boolean isPrint = false;
    Integer lossConnectCount = 0;
    HeartBeatDTO heartBeatDTO;

    Map<String, String> banciMap;

    boolean isXishaji = false;

    public Terminal(String ip) {
        setIp(ip);
        this.userinfo = new UserInfo();
        this.controlPannelInfo = new ControlPannelInfo();
        this.cancelInfo = new CancelInfo();
        this.reprintInfo = new ReprintInfo();
        this.banciMap = new HashMap<>();
        initBanciMap();
    }

    private void initBanciMap() {
        banciMap.put("1", "甲");
        banciMap.put("2", "乙");
        banciMap.put("3", "丙");
        banciMap.put("4", "A");
        banciMap.put("5", "B");
        banciMap.put("6", "C");
    }

    public void updateChannel(Channel channel) {
        this.channel = channel;
        this.gobalSender.setChannel(channel);
    }

    public void setIp(String ip) {
        this.ip = ip;
        this.channel = NettyTcpServer.map.get(ip);
        gobalSender = (GobalSender) SpringContextUtils.getBean("gobalSender");
        gobalSender.init(channel);
    }

    public void goLogin() {
        gobalSender.send(switchScreen("00 01"));
    }

    public void addGoLoginCommand() {
        gobalSender.addCommand(switchScreen("00 01"));
    }

    public void goControl() {
        gobalSender.send(switchScreen("00 02"));
    }

    public void addGoControlCommand() {
        gobalSender.addCommand(switchScreen("00 02"));
    }

    public void goMachine() {
        gobalSender.send(switchScreen("00 03"));
    }

    public void addGoMachineCommand() {
        gobalSender.addCommand(switchScreen("00 03"));
    }

    public void goPrinting() {
        gobalSender.send(switchScreen("00 04"));
    }

    public void addGoPrintingCommand() {
        gobalSender.addCommand(switchScreen("00 04"));
    }

    public void goCancel() {
        gobalSender.send(switchScreen("00 05"));
    }

    public void addGoCancelCommand() {
        gobalSender.addCommand(switchScreen("00 05"));
    }

    public void goReprint() {
//         ReprintPage reprintPage = new ReprintPage();
//         reprintPage.sendLabelNumber()
        gobalSender.send(switchScreen("00 06"));
    }

    public void addGoReprintCommand() {
        gobalSender.addCommand(switchScreen("00 06"));
    }

    public void print(String labelNum) {
        SimpleDateFormat myFmt = new SimpleDateFormat("yyyy.MM.dd");
        String ym = myFmt.format(System.currentTimeMillis());
        String printCommand = "PT:\r\n" +
                "SIZE 60 mm,40 mm\r\n" +
                "GAP 2 mm,4 mm\r\n" +
                "CLS\r\n" +
                "DENSITY 7\r\n" +
                "REFERENCE 0,0\r\n" +
                "TEXT 70,6,\"TSS24.BF2\",0,3,2,\"清远市奥锦新材料\" \r\n" +
                "TEXT 70,8,\"TSS24.BF2\",0,3,2,\"清远市奥锦新材料\"\r\n" +
                "TEXT 69,7,\"TSS24.BF2\",0,3,2,\"清远市奥锦新材料\"\r\n" +
                "TEXT 71,7,\"TSS24.BF2\",0,3,2,\"清远市奥锦新材料\"\r\n" +
                "TEXT 45,67,\"TSS24.BF2\",0,2,2,\"色号：\" \r\n" +
                "TEXT 175,67,\"TSS24.BF2\",0,2,2,\"" + controlPannelInfo.getColor() + "\"\r\n" +
                "TEXT 370,67,\"TSS24.BF2\",0,2,2,\"纤度：\"\r\n" +
                "TEXT 500,67,\"TSS24.BF2\",0,2,2,\"" + controlPannelInfo.getFineness() + "\"\r\n" +
                "TEXT 45,124,\"TSS24.BF2\",0,2,2,\"班组：\" \r\n" +
                "TEXT 175,124,\"TSS24.BF2\",0,2,2,\"" + userinfo.getBanci() + " - " + controlPannelInfo.getMachineNumber() + "\"\r\n" +
                "TEXT 370,124,\"TSS24.BF2\",0,2,2,\"数量：\"\r\n" +
                "TEXT 500,124,\"TSS24.BF2\",0,2,2,\"" + controlPannelInfo.getFactPerBagNumber() + "\"\r\n" +
                "TEXT 45,182,\"TSS24.BF2\",0,2,2,\"日期：\" \r\n" +
                "TEXT 175,182,\"TSS24.BF2\",0,1,2,\"" + ym + "\"\r\n" +
                "TEXT 370,182,\"TSS24.BF2\",0,2,2,\"净\"\r\n" +
                "TEXT 440,195,\"TSS24.BF2\",0,3,4,\"" + controlPannelInfo.getNetWeight() + "\"\r\n" +
                "TEXT 440,199,\"TSS24.BF2\",0,3,4,\"" + controlPannelInfo.getNetWeight() + "\"\r\n" +
                "TEXT 438,197,\"TSS24.BF2\",0,3,4,\"" + controlPannelInfo.getNetWeight() + "\"\r\n" +
                "TEXT 442,197,\"TSS24.BF2\",0,3,4,\"" + controlPannelInfo.getNetWeight() + "\"\r\n" +
                "TEXT 45,240,\"TSS24.BF2\",0,2,2,\"毛重：\" \r\n" +
                "TEXT 175,240,\"TSS24.BF2\",0,2,2,\"" + controlPannelInfo.getGrossWeight() + "\"\r\n" +
                "TEXT 370,240,\"TSS24.BF2\",0,2,2,\"重\"\r\n" +
                "1TEXT 430,240,\"2\",0,3,4,\"27.00\"\r\n" +
                "BARCODE 130,296,\"128\",105,1,0,4,4,\"" + labelNum + "\"\r\n" +
                "PRINT 1\r\n";
        gobalSender.send(CoderUtils.stringToHexStr(printCommand));

        goPrinting();
    }

    public void reprint(ChemicalFiberLabel label, ChemicalFiberProductDTO productDTO) {
        addGoPrintingCommand();
        gobalSender.send();

        SimpleDateFormat myFmt = new SimpleDateFormat("yyyy.MM.dd");
        String ym = myFmt.format(label.getPrintTime());
        String printCommand = "PT:\r\n" +
                "SIZE 60 mm,40 mm\r\n" +
                "GAP 2 mm,4 mm\r\n" +
                "CLS\r\n" +
                "DENSITY 7\r\n" +
                "REFERENCE 0,0\r\n" +
                "TEXT 70,6,\"TSS24.BF2\",0,3,2,\"清远市奥锦新材料\" \r\n" +
                "TEXT 70,8,\"TSS24.BF2\",0,3,2,\"清远市奥锦新材料\"\r\n" +
                "TEXT 69,7,\"TSS24.BF2\",0,3,2,\"清远市奥锦新材料\"\r\n" +
                "TEXT 71,7,\"TSS24.BF2\",0,3,2,\"清远市奥锦新材料\"\r\n" +
                "TEXT 45,67,\"TSS24.BF2\",0,2,2,\"色号：\" \r\n" +
                "TEXT 175,67,\"TSS24.BF2\",0,2,2,\"" + productDTO.getColor() + "\"\r\n" +
                "TEXT 370,67,\"TSS24.BF2\",0,2,2,\"纤度：\"\r\n" +
                "TEXT 500,67,\"TSS24.BF2\",0,2,2,\"" + productDTO.getFineness() + "\"\r\n" +
                "TEXT 45,124,\"TSS24.BF2\",0,2,2,\"班组：\" \r\n" +
                "TEXT 175,124,\"TSS24.BF2\",0,2,2,\"" + label.getShifts() + "\"\r\n" +
                "TEXT 370,124,\"TSS24.BF2\",0,2,2,\"数量：\"\r\n" +
                "TEXT 500,124,\"TSS24.BF2\",0,2,2,\"" + label.getFactPerBagNumber() + "\"\r\n" +
                "TEXT 45,182,\"TSS24.BF2\",0,2,2,\"日期：\" \r\n" +
                "TEXT 175,182,\"TSS24.BF2\",0,1,2,\"" + ym + "\"\r\n" +
                "TEXT 370,182,\"TSS24.BF2\",0,2,2,\"净\"\r\n" +
                "TEXT 440,195,\"TSS24.BF2\",0,3,4,\"" + controlPannelInfo.getNetWeight() + "\"\r\n" +
                "TEXT 440,199,\"TSS24.BF2\",0,3,4,\"" + controlPannelInfo.getNetWeight() + "\"\r\n" +
                "TEXT 438,197,\"TSS24.BF2\",0,3,4,\"" + controlPannelInfo.getNetWeight() + "\"\r\n" +
                "TEXT 442,197,\"TSS24.BF2\",0,3,4,\"" + controlPannelInfo.getNetWeight() + "\"\r\n" +
                "TEXT 45,240,\"TSS24.BF2\",0,2,2,\"毛重：\" \r\n" +
                "TEXT 175,240,\"TSS24.BF2\",0,2,2,\"" + label.getGrossWeight() + "\"\r\n" +
                "TEXT 370,240,\"TSS24.BF2\",0,2,2,\"重\"\r\n" +
                "1TEXT 430,240,\"2\",0,3,4,\"27.00\"\r\n" +
                "BARCODE 130,296,\"128\",105,1,0,4,4,\"" + label.getLabelNumber() + "\"\r\n" +
                "PRINT 1\r\n";
        gobalSender.sendDeloy(CoderUtils.stringToHexStr(printCommand), 800);

        ReprintPage reprintPage = new ReprintPage();
        reprintPage.back(ip);
    }
}