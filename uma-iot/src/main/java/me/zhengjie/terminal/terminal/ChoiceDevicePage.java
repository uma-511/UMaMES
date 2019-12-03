package me.zhengjie.terminal.terminal;

import lombok.extern.slf4j.Slf4j;
import me.zhengjie.server.NettyTcpServer;
import me.zhengjie.service.ControlService;
import me.zhengjie.terminal.annotation.Button;
import me.zhengjie.terminal.annotation.Screen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Screen(id = "00 03")
@Slf4j
@Component
public class ChoiceDevicePage {
    @Autowired
    ControlService controlService;
    @Button(id = "00 01", handler = "event_m1")
    String m1;
    @Button(id = "00 02", handler = "event_m1")
    String m2;
    @Button(id = "00 03", handler = "event_m1")
    String m3;
    @Button(id = "00 04", handler = "event_m1")
    String m4;
    @Button(id = "00 05", handler = "event_m1")
    String m5;
    @Button(id = "00 06", handler = "event_m1")
    String m6;
    @Button(id = "00 07", handler = "event_m1")
    String m7;
    @Button(id = "00 08", handler = "event_m1")
    String m8;
    @Button(id = "00 09", handler = "event_m1")
    String m9;
    @Button(id = "00 0a", handler = "event_m1")
    String m10;
    @Button(id = "00 0b", handler = "event_m1")
    String m11;
    @Button(id = "00 0c", handler = "event_m1")
    String m12;
    @Button(id = "00 0d", handler = "event_m1")
    String m13;
    @Button(id = "00 0e", handler = "event_m1")
    String m14;
    @Button(id = "00 0f", handler = "event_m1")
    String m15;
    @Button(id = "00 10", handler = "event_m1")
    String m16;

    @Button(id = "00 11", handler = "event_waste")
    String waste;

    @Button(id = "00 12", handler = "event_washing")
    String washing;

    public void event_m1(String buttonId, String ip) {
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);
        terminal.goControl();
        int id=Integer.parseInt(buttonId.replaceAll("00 ", ""), 16);
        log.info(buttonId + ":" + id);
        controlService.getProdutionByMacchine(Integer.toString(id),ip);
    }

    @Button(id = "00 26", handler = "event_logout")
    String logout;

    public void event_logout(String buttonId, String ip) {
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);
        terminal.goLogin();
    }
}