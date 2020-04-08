package me.zhengjie.utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.Vector;

public abstract class ChannelHandlerContextUtils
{

	private static Logger logger = LoggerFactory.getLogger(ChannelHandlerContextUtils.class);

	public static ChannelFuture writeAndFlush(ChannelHandlerContext ctx, byte[] msg)
	{
		ByteBuf byteBuf = ctx.alloc().buffer();
		byteBuf.writeBytes(msg);
		return ctx.writeAndFlush(byteBuf);
	}

	public static ChannelFuture writeAndFlush(ChannelHandlerContext ctx, Vector<Byte> msg)
	{
		byte[] bytes = new byte[msg.size()];
		for (int i = 0; i < msg.size(); i++) {
			bytes[i]=msg.get(i).byteValue();
		}

		// 定义一个发送消息协议格式：|--header:4 byte--|--content:10MB--|
		// 获取一个4字节长度的协议体头
//		byte[] dataLength = intToByteArray(4, bytes.length);
//		// 和请求的数据组成一个请求数据包
//		byte[] requestMessage = combineByteArray(dataLength, bytes);

//		System.out.println(FrameUtils.toString(bytes));
		ByteBuf byteBuf = ctx.alloc().buffer();
		byteBuf.writeBytes(bytes);
		return ctx.writeAndFlush(byteBuf);
	}

//	public static ChannelFuture writeAndFlushJson(ChannelHandlerContext ctx, Object obj)
//	{
//		String s = JsonMapper.toJsonString(obj);
//		logger.info(s);
//		return writeAndFlush(ctx, s.getBytes());
//	}
	private static byte[] intToByteArray(int byteLength, int intValue) {
		return ByteBuffer.allocate(byteLength).putInt(intValue).array();
	}

	private static byte[] combineByteArray(byte[] array1, byte[] array2) {
		byte[] combined = new byte[array1.length + array2.length];
		System.arraycopy(array1, 0, combined, 0, array1.length);
		System.arraycopy(array2, 0, combined, array1.length, array2.length);
		return combined;
	}
}
