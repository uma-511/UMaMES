package me.zhengjie.terminal.terminal;

import lombok.extern.slf4j.Slf4j;
import me.zhengjie.domain.ReprintInfo;
import me.zhengjie.server.NettyTcpServer;
import me.zhengjie.service.ControlService;
import me.zhengjie.terminal.GobalSender;
import me.zhengjie.terminal.annotation.Button;
import me.zhengjie.terminal.annotation.Screen;
import me.zhengjie.terminal.annotation.Text;
import me.zhengjie.terminal.command.SendCommand;
import me.zhengjie.uma_mes.domain.ChemicalFiberLabel;
import me.zhengjie.uma_mes.service.dto.ChemicalFiberProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Screen(id="00 06")
@Component
public class ReprintPage extends SendCommand {
    @Autowired
    ControlService controlService;

    @Text(id="00 02",handler = "setLabelNumber")
    String labelNumber;

    public void setLabelNumber(String labelNumber,String ip){
        NettyTcpServer.terminalMap.get(ip).getReprintInfo().setLabelNumber(labelNumber);
    }

    public String sendLabelNumber(String labelNumber,String ip) {
        return setTextValue("00 06","00 02",labelNumber);
    }

    @Text(id="00 07",handler = "setTip")
    String tip;

    public void setTip(String tip,String ip){

    }

    public String sendTip(String tip,String ip) {
        return setTextValue("00 06","00 07",tip);
    }

    @Button(id="00 04",handler = "event_confirm")
    String btn_confirm;

    public void event_confirm(String button_id,String ip){
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);
        GobalSender gobalSender = terminal.getGobalSender();
        controlService.getReprintInfo(ip);

        ReprintInfo reprintInfo = terminal.getReprintInfo();
        ChemicalFiberLabel chemicalFiberLabel = reprintInfo.getChemicalFiberLabel();
        ChemicalFiberProductDTO chemicalFiberProductDTO = reprintInfo.getChemicalFiberProductDTO();
        if(chemicalFiberLabel == null){
            gobalSender.send(sendTip("找不到标签记录",ip));
        }else if(chemicalFiberLabel.getStatus()==3){
            gobalSender.send(sendTip("标签条码号已作废，不能补打",ip));
        }else if(chemicalFiberLabel != null && chemicalFiberProductDTO==null){
            gobalSender.send(sendTip("标签条码号异常，找不到产品信息",ip));
        }else if(chemicalFiberLabel != null && chemicalFiberProductDTO!=null){
            controlService.reprint(ip);
        }
    }

    @Button(id="00 05",handler = "event_clean")
    String btn_clean;

    public void event_clean(String buttonId,String ip){
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);
        GobalSender gobalSender = terminal.getGobalSender();
        gobalSender.addCommand(sendLabelNumber("",ip));
        gobalSender.addCommand(sendTip("",ip));
        gobalSender.send();
    }

    @Button(id="00 06",handler = "event_back")
    String btn_back;

    public void event_back(String buttonId,String ip){
        Terminal terminal= NettyTcpServer.terminalMap.get(ip);
        terminal.goControl();
    }

    public void back(String ip){
        log.info("reprint back");
        Terminal terminal= NettyTcpServer.terminalMap.get(ip);
        GobalSender gobalSender = terminal.getGobalSender();

        terminal.addGoReprintCommand();
        gobalSender.addCommand(sendTip("补打成功",ip));
        gobalSender.send(2000);

        log.info("reprint clean");
        gobalSender.addCommand(sendTip("",ip));
        gobalSender.addCommand(sendLabelNumber("",ip));
        gobalSender.send(2000);
    }
}