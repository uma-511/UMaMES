package me.zhengjie.terminal;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.server.NettyTcpServer;
import me.zhengjie.terminal.command.SendCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Vector;

@Slf4j
@Component
public class GobalSender extends SendCommand {
    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    Channel channel;
    StringBuilder commandBuffer;

    public void init(Channel channel){
        this.channel=channel;
        commandBuffer = new StringBuilder();
    }

    public void setChannel(Channel channel){
        this.channel = channel;
    }

   /* public void sendImmediate(String command){
        channel.writeAndFlush(command);
    }*/

    public void sendImmediate(String command, String ip){
        Channel chann1 = NettyTcpServer.map.get(ip);
        chann1.writeAndFlush(command);
    }

    public void sendImmediate(byte[] command, String ip){
        Channel chann1 = NettyTcpServer.map.get(ip);
        chann1.writeAndFlush(command);
    }

    public void sendImmediate(Vector<Byte> command, String ip){
        Channel chann1 = NettyTcpServer.map.get(ip);
        chann1.writeAndFlush(command);
    }
    
    /*public void send(String command){
        sendDeloy(command,300);
    }*/

    public void send(String command, String ip){
        sendDeloy(command,300, ip);
    }

    public void send(Vector<Byte> command, String ip){
        sendDeloy(command,1500, ip);
    }

   /* public void sendDeloy(String command,long times){
        try {
            Thread.sleep(times);
            channel.writeAndFlush(command);
            commandBuffer.setLength(0);
        } catch (InterruptedException e) {
            log.error("发送指令延时任务");
            e.printStackTrace();
        }
//        threadPoolTaskExecutor.execute(() -> {
//            try {
//                Thread.sleep(times);
//            } catch (InterruptedException e) {
//                log.error("发送指令延时任务");
//                e.printStackTrace();
//            }finally {
//                channel.writeAndFlush(command);
//                commandBuffer.setLength(0);
//            }
//        });
    }*/

    public void sendDeloy(String command,long times, String ip){
        try {
            Thread.sleep(times);
            Channel chann1 = NettyTcpServer.map.get(ip);
            chann1.writeAndFlush(command);
            commandBuffer.setLength(0);
        } catch (InterruptedException e) {
            log.error("发送指令延时任务");
            e.printStackTrace();
        }
    }

    public void sendDeloy(Vector<Byte> command,long times, String ip){
        try {
            Thread.sleep(times);
            Channel chann1 = NettyTcpServer.map.get(ip);
            chann1.writeAndFlush(command);
        } catch (InterruptedException e) {
            log.error("发送指令延时任务");
            e.printStackTrace();
        }
    }

    public void send(List<String> commands, String ip){
        StringBuilder sb=new StringBuilder();
        for (String command:commands) {
            sb.append(command+ " 0D 0A ");
        }
        send(sb.toString());
    }

    public void addCommand(String command){
        commandBuffer.append(command).append(" 0D 0A ");
    }

    public void unshiftCommand(String command){
        commandBuffer.insert(0,command + " 0D 0A ");
    }

    public void send(String ip){
        send(commandBuffer.toString(), ip);
    }
    /*public void send(String ip){
        send(commandBuffer.toString(), ip);
    }*/

    /*public void send(long times){
        sendDeloy(commandBuffer.toString(),times);
    }*/

    public void send(long times, String ip){
        sendDeloy(commandBuffer.toString(),times, ip);
    }
    /*public void sendImmediate(){
        sendImmediate(commandBuffer.toString());
    }*/

    public void sendImmediate(String ip){
        sendImmediate(commandBuffer.toString(), ip);
    }
}