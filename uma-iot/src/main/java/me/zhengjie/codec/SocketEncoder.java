package me.zhengjie.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import me.zhengjie.utils.ChannelHandlerContextUtils;
import me.zhengjie.utils.CoderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: SocketResponseEncoder
 * @Function: 消息编码
 * @date: May 22, 2017 10:49:11 AM
 * 
 * @author Joker
 * @version
 * @since JDK 1.8
 */
//@Sharable
public class SocketEncoder extends MessageToByteEncoder
{
	 private static final Logger logger = LoggerFactory.getLogger(SocketEncoder.class.getName());
//
//	@Override
//	protected void encode(ChannelHandlerContext ctx, byte[] msg, List<Object> out) throws Exception
//	{
//		logger.warn("服务器发送数据：" + FrameUtils.toString(msg));
//		ChannelHandlerContextUtils.writeAndFlush(ctx, msg);
//	}

	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
		String msgStr= msg.toString();
		logger.info("服务器发送数据：" + msgStr);
		ChannelHandlerContextUtils.writeAndFlush(ctx, CoderUtils.cvtStr2Hex1(msgStr));
	}
}
