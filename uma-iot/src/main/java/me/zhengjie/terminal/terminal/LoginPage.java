package me.zhengjie.terminal.terminal;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.domain.ControlPannelInfo;
import me.zhengjie.domain.UserInfo;
import me.zhengjie.server.NettyTcpServer;
import me.zhengjie.service.LoginService;
import me.zhengjie.terminal.GobalSender;
import me.zhengjie.terminal.annotation.Button;
import me.zhengjie.terminal.annotation.Screen;
import me.zhengjie.terminal.annotation.Text;
import me.zhengjie.terminal.command.SendCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

@Screen(id = "00 01")
@Slf4j
@Component
public class LoginPage extends SendCommand {
    private static final Logger logger = LoggerFactory.getLogger(LoginPage.class);

    @Value("${uma.production.createByTerminal}")
    boolean createByTerminal;

    @Autowired
    ControllerPage controllerPage;

    @Autowired
    LoginService loginService;

    final String screenId = "00 01";

    int status = 0;
    boolean loginClicked = false;

    @Text(id = "00 01", handler = "setUserName")
    String userName;

    public void setUserName(String userName, String ip) {
        log.info("setUserName");
        NettyTcpServer.terminalMap.get(ip).getUserinfo().setUserName(userName);
        status = status + 1;
        login(ip);
    }

    @Text(id = "00 02", handler = "setPassword")
    String password;

    public void setPassword(String password, String ip) {
        log.info("setPassword");
        NettyTcpServer.terminalMap.get(ip).getUserinfo().setPassword(password);
        status = status + 1;
        login(ip);
    }

    @Text(id = "00 03", handler = "setBanci")
    String banci;

    public void setBanci(String banci, String ip) {
        log.info("setBanci");
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);
        GobalSender gobalSender = terminal.getGobalSender();
        Map<String, String> map = terminal.banciMap;
        String bc = "";
        if (map.containsKey(banci)) {
            bc = map.get(banci);
        } else {
            bc = banci;
        }
        String factory = loginService.getFactory(1);
//        gobalSender.sendImmediate(setTextValue("00 01", "00 03", bc));
        terminal.getUserinfo().setBanci(bc);
        terminal.getUserinfo().setFactory(factory);
        status = status + 1;
        login(ip);
    }

    @Text(id = "00 04", handler = "setJitai")
    String jitai;

    public void setJitai(String jitai, String ip) {
        log.info("setJitai");
        NettyTcpServer.terminalMap.get(ip).getUserinfo().setJitai(jitai);
    }

    @Text(id = "00 05", handler = "setTip")
    String tip;

    public void setTip(String tip, String ip) {
        log.info("setJitai");
    }

    @Button(id = "00 06", handler = "event_login", send = "send_login")
    String btn_login;

    @Button(id = "00 07", handler = "event_cancel", send = "send_cancel")
    String btn_cancel;

    public void event_login(String buttonId, String ip) {
        log.info("login click");
        loginClicked = true;
        syncFromTerminal(ip);

        login(ip);
    }

    private void login(String ip) {
        if(status >= 3 && loginClicked) {
            status = 0;
            loginClicked = false;
            Terminal terminal = NettyTcpServer.terminalMap.get(ip);
            Channel chann = NettyTcpServer.map.get(ip);
            GobalSender gobalSender = terminal.getGobalSender();

            UserInfo userInfo = terminal.getUserinfo();

            RestTemplate restTemplate = getRestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            JSONObject requestBody = new JSONObject();
            requestBody.put("username", userInfo.getUserName());
            requestBody.put("password", userInfo.getPassword());

            HttpEntity<JSONObject> requestEntity = new HttpEntity<JSONObject>(requestBody, headers);
            JSONObject resp = restTemplate.exchange("http://localhost:8000/auth/handsetlogin", HttpMethod.POST, requestEntity, JSONObject.class).getBody();
            log.info(resp.toJSONString());
            String loginTip = "未知错误，请重启设备";
            GobalSender gobalSender1 = terminal.gobalSender;
            if (resp.containsKey("code") && resp.get("code").equals("200")) {
//            terminal.goMachine();

                if (createByTerminal) {
                    ControlPannelInfo controlPannelInfo = terminal.getControlPannelInfo();
                    controlPannelInfo.setBanci(userInfo.getBanci());

                   // gobalSender.sendImmediate(switchScreen("00 02"));
                    //Thread.sleep(300);
                    //gobalSender.sendImmediate1(switchScreen("00 02"), chann);
                    gobalSender.sendDeloy(switchScreen("00 02", ip), 400, ip);
                    gobalSender.addCommand(controllerPage.sendBanci(userInfo.getBanci(), ip));
                    gobalSender.addCommand(controllerPage.sendFactory(userInfo.getFactory(), ip));
                } else {
                    gobalSender.addCommand(switchScreen("00 03",ip));
                }

                resetLogin(ip);
            } else if (resp.containsKey("code") && !resp.get("code").equals("200")) {
                loginTip = resp.getString("message");
                gobalSender1.send(setTextValue("00 01", "00 05", loginTip));
            } else if (resp.containsKey("status") && resp.containsKey("message")) {
                loginTip = resp.getString("message");

                loginTip = loginTip.replaceAll("password:", "密码").replaceAll("username:", "账号");

                if (loginTip.startsWith("User with name") && loginTip.endsWith("does not exist")) {
                    loginTip = "账号不存在";
                }

                gobalSender1.addCommand(setTextValue("00 01", "00 05", loginTip));
                resetLogin(ip);
            } else {
                gobalSender1.send(setTextValue("00 01", "00 05", loginTip));
            }
        }
    }

    public void send_login() {
        log.info("send_login");
    }

    public void event_cancel(String buttonId, String ip) {
        log.info("cancel click");
        resetLogin(ip);
    }

    public void send_cancel() {
        log.info("cancel_login");
    }

    public void syncFromTerminal(String ip){
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);
        GobalSender gobalSender = terminal.getGobalSender();
        gobalSender.addCommand(setTextValue("00 01", "00 05", "登录中，请稍后！"));
        gobalSender.addCommand(getTextValue(screenId,"00 01"));
        gobalSender.addCommand(getTextValue(screenId,"00 02"));
        gobalSender.addCommand(getTextValue(screenId,"00 03"));
        gobalSender.sendImmediate(ip);
    }

    private void resetLogin(String ip) {
        Terminal terminal = NettyTcpServer.terminalMap.get(ip);
        GobalSender gobalSender = terminal.getGobalSender();

        gobalSender.addCommand(setTextValue(screenId, "00 01", ""));
        gobalSender.addCommand(setTextValue(screenId, "00 02", ""));
        gobalSender.addCommand(setTextValue(screenId, "00 03", ""));
        gobalSender.addCommand(setTextValue(screenId, "00 04", ""));
        gobalSender.addCommand(setTextValue(screenId, "00 05", ""));
        gobalSender.send(ip);
    }

    private RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            // Ignore 400
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getRawStatusCode() != 400 && response.getRawStatusCode() != 401 && response.getRawStatusCode() != 402 && response.getRawStatusCode() != 403 && response.getRawStatusCode() != 405 && response.getRawStatusCode() != 500) {
                    super.handleError(response);
                }
            }
        });
        return restTemplate;
    }
}
