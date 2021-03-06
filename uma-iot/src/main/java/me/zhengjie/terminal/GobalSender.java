package me.zhengjie.terminal;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.terminal.command.SendCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Vector;

@Slf4j
@Component
public class GobalSender extends SendCommand {
    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Value("${uma.commond.delay:100}")
    long delay;

    Channel channel;
    StringBuilder commandBuffer;

    public void init(Channel channel){
        this.channel=channel;
        commandBuffer = new StringBuilder();
    }

    public void setChannel(Channel channel){
        this.channel = channel;
    }

    public void sendImmediate(String command){
        channel.writeAndFlush(command);
    }

    public void sendImmediate(byte[] command){
        channel.writeAndFlush(command);
    }

    public void sendImmediate(Vector<Byte> command){
        channel.writeAndFlush(command);
    }
    
    public void send(String command){
        sendDelay(command,delay);
    }

    public void send(Vector<Byte> command){
        sendDelay(command,delay);
    }

    public void sendDelay(String command, long times){
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
    }

    public void sendDelay(Vector<Byte> command, long times){
        try {
            Thread.sleep(times);
            channel.writeAndFlush(command);
        } catch (InterruptedException e) {
            log.error("发送指令延时任务");
            e.printStackTrace();
        }
    }

    public void send(List<String> commands){
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

    public void send(){
        send(commandBuffer.toString());
    }

    public void send(long times){
        sendDelay(commandBuffer.toString(),times);
    }
    public void sendImmediate(){
        sendImmediate(commandBuffer.toString());
    }
}