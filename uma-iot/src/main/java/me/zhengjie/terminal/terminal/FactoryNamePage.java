package me.zhengjie.terminal.terminal;

import me.zhengjie.server.NettyTcpServer;
import me.zhengjie.service.LoginService;
import me.zhengjie.terminal.GobalSender;
import me.zhengjie.terminal.annotation.Button;
import me.zhengjie.terminal.annotation.Screen;
import me.zhengjie.terminal.command.SendCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Screen(id="00 03")
@Component
public class FactoryNamePage extends SendCommand {

    @Autowired
    LoginService loginService;

    @Autowired
    ControllerPage controllerPage;

    @Button(id = "00 01" ,handler = "event_one")
    String btn_one;
    @Button(id = "00 02" ,handler = "event_one")
    String btn_two;
    @Button(id = "00 03" ,handler = "event_one")
    String btn_three;
    @Button(id = "00 04" ,handler = "event_one")
    String btn_four;
    @Button(id = "00 05" ,handler = "event_one")
    String btn_five;
    @Button(id = "00 26" ,handler = "event_login")
    String btn_login;


    public void event_login(String button, String ip) {
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);
        GobalSender gobalSender = terminal.getGobalSender();
        gobalSender.addCommand(switchScreen("00 02"));
        //cleanControllerPage(ip);
        //cleanControllerPageInfo(ip);
        gobalSender.send(ip);
    }
    public void event_one(String button, String ip) {
        Integer id = Integer.valueOf(button.substring(4, 5));
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);
        String factory = loginService.getFactory(id);
        terminal.getUserinfo().setFactory(factory);
        GobalSender gobalSender = terminal.getGobalSender();
        gobalSender.addCommand(controllerPage.sendFactory(factory, ip));
        gobalSender.addCommand(switchScreen("00 02"));
        gobalSender.send(ip);
    }

}
