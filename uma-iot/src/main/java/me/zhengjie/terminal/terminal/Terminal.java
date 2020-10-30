package me.zhengjie.terminal.terminal;

import com.lgmn.utils.SvgUtils;
import com.lgmn.utils.printer.Command;
import com.lgmn.utils.printer.PrintProps;
import io.netty.channel.Channel;
import lombok.Data;
import me.zhengjie.domain.CancelInfo;
import me.zhengjie.domain.ControlPannelInfo;
import me.zhengjie.domain.ReprintInfo;
import me.zhengjie.domain.UserInfo;
import me.zhengjie.server.NettyTcpServer;
import me.zhengjie.terminal.GobalSender;
import me.zhengjie.terminal.PrintData;
import me.zhengjie.terminal.PrintDataHolder;
import me.zhengjie.terminal.PrintExector;
import me.zhengjie.terminal.command.SendCommand;
import me.zhengjie.uma_mes.domain.ChemicalFiberLabel;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberProductDTO;
import me.zhengjie.uma_mes.service.dto.HeartBeatDTO;
import me.zhengjie.utils.CoderUtils;
import me.zhengjie.utils.SpringContextUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.lang.String;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

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

    boolean rp = false;

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

    public void goLogin(String ip) {
        gobalSender.send(switchScreen("00 01"), ip);
    }

    public void addGoLoginCommand() {
        gobalSender.addCommand(switchScreen("00 01"));
    }

    public void goControl(String ip) {
//        gobalSender.send(2000);
        gobalSender.send(switchScreen("00 02"), ip);
    }

    public void addGoControlCommand() {
        gobalSender.addCommand(switchScreen("00 02"));
    }

    public void goMachine(String ip) {
        gobalSender.send(switchScreen("00 03"), ip);
    }

    public void addGoMachineCommand(String ip) {
        gobalSender.addCommand(switchScreen("00 03"));
    }

    public void goPrinting(String ip) {
//        gobalSender.sendImmediate(switchScreen("00 04"));
        // 需还原
        gobalSender.send(switchScreen("00 04"), ip);
    }

    public void addGoPrintingCommand() {
        gobalSender.addCommand(switchScreen("00 04"));
    }

    public void goCancel(String ip) {
        gobalSender.send(switchScreen("00 05"), ip);
    }

    public void addGoCancelCommand() {
        gobalSender.addCommand(switchScreen("00 05"));
    }

    public void goReprint(String ip) {
//         ReprintPage reprintPage = new ReprintPage();
//         reprintPage.sendLabelNumber()
        gobalSender.send(switchScreen("00 06"), ip);
    }

    public void addGoReprintCommand() {
        gobalSender.addCommand(switchScreen("00 06"));
    }

    public void print(String labelNum) throws UnsupportedEncodingException {
        SimpleDateFormat myFmt = new SimpleDateFormat("yyyy.MM.dd");
        String ym = myFmt.format(System.currentTimeMillis());

//        Map<String, String> dataMap = new HashMap<>();
//        dataMap.put("color",controlPannelInfo.getColor() );
//        dataMap.put("banci",userinfo.getBanci() + " - " + controlPannelInfo.getMachineNumber());
//        dataMap.put("date",ym);
//        dataMap.put("grossWeight",controlPannelInfo.getGrossWeight());
//        dataMap.put("fineness",controlPannelInfo.getFineness());
//        dataMap.put("factPerBagNumber",controlPannelInfo.getFactPerBagNumber());
//        dataMap.put("netWeight",controlPannelInfo.getNetWeight());
//        dataMap.put("labelNumber",labelNum);
//
//        PrintProps printProps = new PrintProps();
//        printProps.setWidth(50);
//        printProps.setHeight(40);
//
//        Vector<Byte> data = new  SvgUtils().getTeminalPrintData("E:\\项目资料\\优码\\清远\\清远.svg",printProps,dataMap);
//
//        Vector<Vector<Byte>> frames = new Vector<>();
//
//
//
//        String pt = "PT:\r\n";
//
//        byte[] ptByte = pt.getBytes("GB2312");
//
//
//        int frameLen = 768 - ptByte.length;
//        Vector<Byte> frame = new Vector<>();
//        for (byte b : ptByte) {
//            frame.add(b);
//        }
//
//        for (int i = 0; i < data.size(); i++) {
//            if(i%frameLen==0){
//                frames.add(frame);
//                frame=new Vector<>();
//                for (byte b : ptByte) {
//                    frame.add(b);
//                }
//            }
//            if (i==data.size()-2){
//                frames.add(frame);
//            }
//            frame.add(data.get(i));
//        }
//
//        PrintData printData = new PrintData();
//        printData.setCurr(0);
//        printData.setFrames(frames);
//        printData.setGobalSender(gobalSender);
//        printData.setIp(ip);
//
//        PrintDataHolder.getInstance().addData(ip,printData);
//
//        PrintExector.getInstance().print(ip);

//        for (Vector<Byte> bytes : frames) {
//            gobalSender.sendImmediate(bytes);
//        }
//        Command command = Command.getInstance();
//        command.addInitializeTeminalPrinter(printProps.getWidth(),printProps.getHeight(),printProps.getGap_m(),printProps.getGap_n());
//        command.addPrint();
//        Vector<Byte> data =  command.getCommand();
//        gobalSender.sendDeloy(data,2000);
        String printCommand = "PT:\r\n" +
                "SIZE 60 mm,40 mm\r\n" +
                "GAP 2 mm,4 mm\r\n" +
                "CLS\r\n" +
                "DENSITY 7\r\n" +
                "REFERENCE 0,0\r\n" +
                /*"TEXT 70,6,\"TSS24.BF2\",0,2,2,\"清远市清城区翼德织造厂\" \r\n" +
                "TEXT 70,8,\"TSS24.BF2\",0,2,2,\"清远市清城区翼德织造厂\"\r\n" +
                "TEXT 69,7,\"TSS24.BF2\",0,2,2,\"清远市清城区翼德织造厂\"\r\n" +
                "TEXT 71,7,\"TSS24.BF2\",0,2,2,\"清远市清城区翼德织造厂\"\r\n" +*/
                "TEXT 45,47,\"TSS24.BF2\",0,2,2,\"色号：\" \r\n" +
                "TEXT 175,46,\"TSS24.BF2\",0,2,2,\"" + controlPannelInfo.getColor() + "\"\r\n" +
                "TEXT 175,48,\"TSS24.BF2\",0,2,2,\"" + controlPannelInfo.getColor() + "\"\r\n" +
                "TEXT 174,47,\"TSS24.BF2\",0,2,2,\"" + controlPannelInfo.getColor() + "\"\r\n" +
                "TEXT 176,47,\"TSS24.BF2\",0,2,2,\"" + controlPannelInfo.getColor() + "\"\r\n" +
                "TEXT 370,47,\"TSS24.BF2\",0,2,2,\"纤度：\"\r\n" +
                "TEXT 500,47,\"TSS24.BF2\",0,2,2,\"" + controlPannelInfo.getFineness() + "\"\r\n" +
                "TEXT 45,104,\"TSS24.BF2\",0,2,2,\"数量：\" \r\n" +
                "TEXT 175,104,\"TSS24.BF2\",0,2,2,\"" + controlPannelInfo.getFactPerBagNumber()   + "\"\r\n" +
                "TEXT 370,104,\"TSS24.BF2\",0,2,2,\"班组：\"\r\n" +
                "TEXT 500,104,\"TSS24.BF2\",0,2,2,\"" + userinfo.getBanci() + " - " + controlPannelInfo.getMachineNumber() + "\"\r\n" +
                "TEXT 45,162,\"TSS24.BF2\",0,2,2,\"日期：\" \r\n" +
                "TEXT 175,162,\"TSS24.BF2\",0,1,2,\"" + ym + "\"\r\n" +
                "TEXT 370,162,\"TSS24.BF2\",0,2,2,\"净\"\r\n" +
                "TEXT 440,175,\"TSS24.BF2\",0,3,4,\"" + controlPannelInfo.getNetWeight() + "\"\r\n" +
                "TEXT 440,179,\"TSS24.BF2\",0,3,4,\"" + controlPannelInfo.getNetWeight() + "\"\r\n" +
                "TEXT 438,177,\"TSS24.BF2\",0,3,4,\"" + controlPannelInfo.getNetWeight() + "\"\r\n" +
                "TEXT 442,177,\"TSS24.BF2\",0,3,4,\"" + controlPannelInfo.getNetWeight() + "\"\r\n" +
                "TEXT 45,220,\"TSS24.BF2\",0,2,2,\"毛重：\" \r\n" +
                "TEXT 175,220,\"TSS24.BF2\",0,2,2,\"" + controlPannelInfo.getGrossWeight() + "\"\r\n" +
                "TEXT 370,220,\"TSS24.BF2\",0,2,2,\"重\"\r\n" +
                "1TEXT 430,220,\"2\",0,3,4,\"27.00\"\r\n" +
                "BARCODE 80,276,\"128\",105,1,0,4,4,\"" + labelNum + "\"\r\n" +
                "PRINT 1\r\n";
        gobalSender.sendImmediate(CoderUtils.stringToHexStr(printCommand), ip);

        goPrinting(ip);
    }

    public void reprint(ChemicalFiberLabel label, ChemicalFiberProductDTO productDTO) {
        addGoPrintingCommand();
        gobalSender.send(ip);

        SimpleDateFormat myFmt = new SimpleDateFormat("yyyy.MM.dd");
        String ym = myFmt.format(label.getPrintTime());

//        Map<String, String> dataMap = new HashMap<>();
//        dataMap.put("color",label.getColor() );
//        dataMap.put("banci", label.getShifts() + " - " + label.getMachine());
//        dataMap.put("date",ym);
//        dataMap.put("grossWeight",label.getGrossWeight().toString());
//        dataMap.put("fineness",label.getFineness());
//        dataMap.put("factPerBagNumber",label.getFactPerBagNumber().toString());
//        dataMap.put("netWeight",label.getNetWeight().toString());
//        dataMap.put("labelNumber",label.getLabelNumber());

//        PrintProps printProps = new PrintProps();
//        printProps.setWidth(60);
//        printProps.setHeight(40);
//
//        Vector<Byte> data = new SvgUtils().getTeminalPrintData("E:\\项目资料\\优码\\清远\\清远.svg",printProps,dataMap);
//        gobalSender.sendDeloy(data,800);

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
                "TEXT 175,66,\"TSS24.BF2\",0,2,2,\"" + label.getColor() + "\"\r\n" +
                "TEXT 175,68,\"TSS24.BF2\",0,2,2,\"" + label.getColor() + "\"\r\n" +
                "TEXT 174,67,\"TSS24.BF2\",0,2,2,\"" + label.getColor() + "\"\r\n" +
                "TEXT 176,67,\"TSS24.BF2\",0,2,2,\"" + label.getColor() + "\"\r\n" +
                "TEXT 370,67,\"TSS24.BF2\",0,2,2,\"纤度：\"\r\n" +
                "TEXT 500,67,\"TSS24.BF2\",0,2,2,\"" + label.getFineness() + "\"\r\n" +
                "TEXT 45,124,\"TSS24.BF2\",0,2,2,\"班组：\" \r\n" +
                "TEXT 175,124,\"TSS24.BF2\",0,2,2,\"" + label.getShifts() + " - " + label.getMachine() + "\"\r\n" +
                "TEXT 370,124,\"TSS24.BF2\",0,2,2,\"数量：\"\r\n" +
                "TEXT 500,124,\"TSS24.BF2\",0,2,2,\"" + label.getFactPerBagNumber() + "\"\r\n" +
                "TEXT 45,182,\"TSS24.BF2\",0,2,2,\"日期：\" \r\n" +
                "TEXT 175,182,\"TSS24.BF2\",0,1,2,\"" + ym + "\"\r\n" +
                "TEXT 370,182,\"TSS24.BF2\",0,2,2,\"净\"\r\n" +
                "TEXT 440,195,\"TSS24.BF2\",0,3,4,\"" + label.getNetWeight() + "\"\r\n" +
                "TEXT 440,199,\"TSS24.BF2\",0,3,4,\"" + label.getNetWeight() + "\"\r\n" +
                "TEXT 438,197,\"TSS24.BF2\",0,3,4,\"" + label.getNetWeight() + "\"\r\n" +
                "TEXT 442,197,\"TSS24.BF2\",0,3,4,\"" + label.getNetWeight() + "\"\r\n" +
                "TEXT 45,240,\"TSS24.BF2\",0,2,2,\"毛重：\" \r\n" +
                "TEXT 175,240,\"TSS24.BF2\",0,2,2,\"" + label.getGrossWeight() + "\"\r\n" +
                "TEXT 370,240,\"TSS24.BF2\",0,2,2,\"重\"\r\n" +
                "1TEXT 430,240,\"2\",0,3,4,\"27.00\"\r\n" +
                "BARCODE 80,296,\"128\",105,1,0,4,4,\"" + label.getLabelNumber() + "\"\r\n" +
                "PRINT 1\r\n";
        String command = CoderUtils.stringToHexStr(printCommand);
        gobalSender.sendDeloy(command,600, ip);
//        checkPrintStatus(command);
        ReprintPage reprintPage = new ReprintPage();
        reprintPage.back(ip);
    }

    public boolean checkLoginStatus(String screenId,String tipId){
        boolean result = true;
        String username=userinfo.getUserName();
        String password=userinfo.getPassword();
        String tip = "登录状态失效，请重新登录";
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password) ){
            result = false;
            gobalSender.sendImmediate(setTextValue(screenId,tipId,tip), ip);
        }
        return result;
    }

    public void checkPrintStatus(String command){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
//            log.error("发送指令延时任务");
            e.printStackTrace();
        }
    }
}
