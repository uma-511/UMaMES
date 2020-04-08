package me.zhengjie.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.config.HeartbeatConfig;
import me.zhengjie.service.HeartBeatConsumer;
import me.zhengjie.terminal.GobalListener;
import me.zhengjie.terminal.PrintData;
import me.zhengjie.terminal.PrintDataHolder;
import me.zhengjie.terminal.PrintExector;
import me.zhengjie.terminal.command.BaseCommand;
import me.zhengjie.terminal.terminal.ControllerPage;
import me.zhengjie.terminal.terminal.Terminal;
import me.zhengjie.uma_mes.service.dto.HeartBeatDTO;
import me.zhengjie.utils.CoderUtils;
import me.zhengjie.utils.TerminalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@ChannelHandler.Sharable
@Slf4j
public class ServerChannelHandler extends SimpleChannelInboundHandler<Object> {

    @Autowired
    HeartbeatConfig heartbeatConfig;

    @Autowired
    GobalListener gobalListener;

    @Autowired
    BaseCommand baseCommand;

    @Autowired
    ControllerPage controllerPage;

    @Autowired
    HeartBeatConsumer heartBeatConsumer;

    /**
     * 拿到传过来的msg数据，开始处理
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Netty tcp server receive msg : " + msg);

        String text = msg.toString().toUpperCase().replace("[", "").replace("]", "");
//        String decodeText=CoderUtils.decoder(text.replaceAll(" ",""));
        String ip = getIPString(ctx);

        if (!checkTerminal(ip)) {
            initTerminal(ctx);
        }

        String hb = CoderUtils.stringToHexStr(heartbeatConfig.getHeartbeatMsg()).trim();
        String sc = CoderUtils.stringToHexStr(baseCommand.getScanValue()).trim();
        String rp = CoderUtils.stringToHexStr(baseCommand.getRevicePrintCommand()).trim();
        if (text.startsWith(baseCommand.getGetValue())) {
            if (text.endsWith(baseCommand.getInputEventEndingFrame())) {
                String[] texts = text.split(" 00 FF FC FF FF EE B1 11 ");
                for (String t : texts) {
                    String screenId = TerminalUtils.getScreenId(t, baseCommand.getGetValue(), baseCommand.getInputEventEndingFrame());
                    String controlId = TerminalUtils.getControlId(t, baseCommand.getGetValue(), baseCommand.getInputEventEndingFrame());
                    String value = TerminalUtils.getTextValue(t, baseCommand.getGetValue(), baseCommand.getInputEventEndingFrame());
                    gobalListener.gateWay(screenId, controlId, value, "text", ip);
                }
                log.info("input event");
            } else if (text.endsWith(baseCommand.getButtonEventEndingFrame())) {
                String screenId = TerminalUtils.getScreenId(text, baseCommand.getGetValue(), baseCommand.getButtonEventEndingFrame());
                String controlId = TerminalUtils.getControlId(text, baseCommand.getGetValue(), baseCommand.getButtonEventEndingFrame());
                gobalListener.gateWay(screenId, controlId, controlId, "button", ip);
                log.info("button event");
            }
        } else if (text.startsWith(sc)) {
            log.info("scan event");
//            scanHandle();
        } else if (text.startsWith(baseCommand.getGetWeights())) {
            String message = CoderUtils.decoder(text.replaceAll(" ", ""));
            log.info("getWeights event:" + message);
            controllerPage.setWeights(message, ip);
        } else if (text.startsWith(hb)) {
            log.info("heartbeat event");
            Terminal terminal = NettyTcpServer.terminalMap.get(ip);
            terminal.setLossConnectCount(0);
            heartBeatConsumer.updateResponseTime(terminal.getHeartBeatDTO());
        } else if(text.startsWith(rp)) {
            log.info("revice print command event");
            Terminal terminal = NettyTcpServer.terminalMap.get(ip);
            terminal.setRp(true);
//            terminal.setLossConnectCount(0);
//            heartBeatConsumer.updateResponseTime(terminal.getHeartBeatDTO());

//            PrintData printData = PrintDataHolder.getInstance().getTask(ip);
//            printData.addCurr();
//            PrintExector.getInstance().print(ip);
        } else {
            log.info("unknow event:");
//            unknowEvent();
        }

//        ctx.channel().writeAndFlush(" response msg ").syncUninterruptibly();
    }

    /**
     * 活跃的、有效的通道
     * 第一次连接成功后进入的方法
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        log.info("tcp client " + getRemoteAddress(ctx) + " connect success");
        //往channel map中添加channel信息

        initTerminal(ctx);
    }

    private void initTerminal(ChannelHandlerContext ctx) {
        String ip = getIPString(ctx);
        String port = getIPPort(ctx);
        log.info("terminalIp:" + ip);
        Channel channel = ctx.channel();

        Boolean updateChannel = false;

        if (NettyTcpServer.map.containsKey(ip)) {
            NettyTcpServer.map.get(ip).close();
            updateChannel = true;
        }

        NettyTcpServer.map.put(ip, channel);
        NettyTcpServer.ipPortMap.put(ip, port);
        if (updateChannel) {
            NettyTcpServer.terminalMap.get(ip).updateChannel(channel);
        } else {
            NettyTcpServer.terminalMap.put(ip, new Terminal(ip));
        }
    }

    private boolean checkTerminal(String ip) {
        return NettyTcpServer.map.containsKey(ip);
    }

    /**
     * 不活动的通道
     * 连接丢失后执行的方法（client端可据此实现断线重连）
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //删除Channel Map中的失效Client
        NettyTcpServer.map.remove(getIPString(ctx));
        NettyTcpServer.terminalMap.remove(getIPString(ctx));
        NettyTcpServer.ipPortMap.remove(getIPString(ctx));
        ctx.close();
    }

    /**
     * 异常处理
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        //发生异常，关闭连接
        log.error("引擎 {} 的通道发生异常，即将断开连接", getRemoteAddress(ctx));
        ctx.close();//再次建议close
    }

    /**
     * 心跳机制，超时处理
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        System.out.println("客户端循环心跳监测发送: " + new Date());
        if (evt instanceof IdleStateEvent) {
            //当捕获到 IdleStateEvent，发送心跳到远端，并添加一个监听器，如果发送失败就关闭服务端连接
            String ip = getIPString(ctx);
            String port = getIPPort(ctx);
            Terminal terminal = NettyTcpServer.terminalMap.get(ip);
            HeartBeatDTO heartBeatDTO = heartBeatConsumer.createHeartBeatRecord(ip,port);
            ctx.writeAndFlush(CoderUtils.stringToHexStr(heartbeatConfig.getHeartbeatMsg()));
            heartBeatConsumer.updateSendTime(heartBeatDTO);
            terminal.setHeartBeatDTO(heartBeatDTO);
            Integer lcc = terminal.getLossConnectCount();
            terminal.setLossConnectCount(lcc+1);
            if (lcc > heartbeatConfig.getReconnectTimes()) {
                System.out.println("关闭这个不活跃通道！");
                ctx.channel().close();
            }
        } else {
//如果捕获到的时间不是一个 IdleStateEvent，就将该事件传递到下一个处理器
            super.userEventTriggered(ctx, evt);
        }
//        String socketString = ctx.channel().remoteAddress().toString();
//        if (evt instanceof IdleStateEvent) {
//            IdleStateEvent event = (IdleStateEvent) evt;
//            if (event.state() == IdleState.READER_IDLE) {
//                log.info("Client: " + socketString + " READER_IDLE 读超时");
//                ctx.disconnect();//断开
//            } else if (event.state() == IdleState.WRITER_IDLE) {
//                log.info("Client: " + socketString + " WRITER_IDLE 写超时");
//                ctx.disconnect();
//            } else if (event.state() == IdleState.ALL_IDLE) {
//                log.info("Client: " + socketString + " ALL_IDLE 总超时");
//                ctx.disconnect();
//            }
//        }
    }

    /**
     * 获取client对象：ip+port
     *
     * @param ctx
     * @return
     */
    public String getRemoteAddress(ChannelHandlerContext ctx) {
        String socketString = "";
        socketString = ctx.channel().remoteAddress().toString();
        return socketString;
    }

    /**
     * 获取client的ip
     *
     * @param ctx
     * @return
     */
    public String getIPString(ChannelHandlerContext ctx) {
        String ipString = "";
        String socketString = ctx.channel().remoteAddress().toString();
        int colonAt = socketString.indexOf(":");
        ipString = socketString.substring(1, colonAt);
        return ipString;
    }

    public String getIPPort(ChannelHandlerContext ctx) {
        String ipPort = "";
        String socketString = ctx.channel().remoteAddress().toString();
        int colonAt = socketString.indexOf(":");
        ipPort = socketString.substring(colonAt + 1);
        return ipPort;
    }
}