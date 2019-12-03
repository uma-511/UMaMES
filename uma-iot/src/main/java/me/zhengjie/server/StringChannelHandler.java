package me.zhengjie.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
@Slf4j
public class StringChannelHandler extends SimpleChannelInboundHandler<Object> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof String) {
            System.out.println("----------XXX-----"+(String) msg);
            ctx.writeAndFlush("我是XXX服务端");
        }else {
            ctx.fireChannelRead(msg);
        }
    }
}