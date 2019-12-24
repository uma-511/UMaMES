package me.zhengjie.terminal.terminal;

import me.zhengjie.server.NettyTcpServer;
import me.zhengjie.terminal.annotation.Button;
import me.zhengjie.terminal.annotation.Screen;
import org.springframework.stereotype.Component;

@Screen(id="00 04")
@Component
public class PrintingPage {
    @Button(id="00 01", handler = "event_back")
    String btn_back;

    public void event_back(String buttonId, String ip){
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);
        terminal.goControl();
        terminal.isPrint = false;
    }
}