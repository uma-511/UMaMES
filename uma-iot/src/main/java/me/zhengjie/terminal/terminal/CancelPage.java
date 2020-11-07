package me.zhengjie.terminal.terminal;

import me.zhengjie.server.NettyTcpServer;
import me.zhengjie.service.ControlService;
import me.zhengjie.terminal.GobalSender;
import me.zhengjie.terminal.annotation.Button;
import me.zhengjie.terminal.annotation.Screen;
import me.zhengjie.terminal.annotation.Text;
import me.zhengjie.terminal.command.SendCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Screen(id="00 05")
@Component
public class CancelPage  extends SendCommand {
    @Autowired
    ControlService controlService;

    @Text(id="00 02",handler = "setLabelNumber")
    String labelNumber;

    public void setLabelNumber(String labelNumber,String ip){
        NettyTcpServer.terminalMap.get(ip).getCancelInfo().setLabelNumber(labelNumber);
        NettyTcpServer.terminalMap.get(ip).getReprintInfo().setLabelNumber(labelNumber);
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);
        GobalSender gobalSender = terminal.getGobalSender();
        gobalSender.send(sendLabelNumber(labelNumber,ip),ip);
    }

    public String sendLabelNumber(String labelNumber,String ip) {
        return setTextValue("00 05","00 02",labelNumber);
    }

    @Text(id="00 07",handler = "setTip")
    String tip;

    public void setTip(String tip,String ip){
    }

    public String sendTip(String tip,String ip) {
        return setTextValue("00 05","00 07",tip);
    }

    @Button(id="00 04",handler = "event_confirm")
    String btn_confirm;

    public void event_confirm(String buttonId,String ip){
        controlService.cancel(ip);
    }

    @Button(id="00 05",handler = "event_clean")
    String btn_clean;

    public void event_clean(String buttonId,String ip){
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);
        GobalSender gobalSender = terminal.getGobalSender();
        gobalSender.addCommand(sendLabelNumber("",ip));
        gobalSender.addCommand(sendTip("",ip));
        gobalSender.send(ip);
    }

    @Button(id="00 06",handler = "event_back")
    String btn_back;

    public void event_back(String buttonId,String ip){
        controlService.updateControllerPageTotalFieldsAndGoControl(ip);
    }
}
