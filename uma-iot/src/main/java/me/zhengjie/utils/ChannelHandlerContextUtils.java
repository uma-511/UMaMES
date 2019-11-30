package me.zhengjie.utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ChannelHandlerContextUtils
{

	private static Logger logger = LoggerFactory.getLogger(ChannelHandlerContextUtils.class);

	public static ChannelFuture writeAndFlush(ChannelHandlerContext ctx, byte[] msg)
	{
		ByteBuf byteBuf = ctx.alloc().buffer();
		byteBuf.writeBytes(msg);
		return ctx.writeAndFlush(byteBuf);
	}

//	public static ChannelFuture writeAndFlushJson(ChannelHandlerContext ctx, Object obj)
//	{
//		String s = JsonMapper.toJsonString(obj);
//		logger.info(s);
//		return writeAndFlush(ctx, s.getBytes());
//	}

}
