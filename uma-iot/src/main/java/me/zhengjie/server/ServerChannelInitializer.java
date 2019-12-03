package me.zhengjie.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import me.zhengjie.codec.SocketDecoder;
import me.zhengjie.codec.SocketEncoder;
import me.zhengjie.config.HeartbeatConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Autowired
    ServerChannelHandler serverChannelHandler;

    @Autowired
    StringChannelHandler stringChannelHandler;

    @Autowired
    HeartbeatConfig heartbeatConfig;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //IdleStateHandler心跳机制,如果超时触发Handle中userEventTrigger()方法
        pipeline.addLast("idleStateHandler",
                new IdleStateHandler(heartbeatConfig.getReaderIdleTime(), heartbeatConfig.getWritererIdleTime(), heartbeatConfig.getAllIdleTime(), heartbeatConfig.getTimeUnit()));
        //添加 IdleStateHandler 传播的 IdleStateEvent 的处理器
//        pipeline.addLast(heartbeatHandler);
        //字符串编解码器
        pipeline.addLast(
//                new StringDecoder(),
//                new StringEncoder(),
                new SocketDecoder(),
                new SocketEncoder()
        );
        //自定义Handler
        pipeline.addLast("serverChannelHandler", serverChannelHandler);
    }
}