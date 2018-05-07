/**
 * 版权所有@: 杭州铭师堂教育科技发展有限公司
 * 注意：本内容仅限于杭州铭师堂教育科技发展有限公司内部使用，禁止外泄以及用于其他的商业目的
 * CopyRight@: 2018 Hangzhou Mistong Educational Technology Co.,Ltd.
 * All Rights Reserved.
 * Note:Just limited to use by Mistong Educational Technology Co.,Ltd. Others are forbidden.
 */
package cn.simple.coder;

import cn.simple.coder.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Decoder
 *
 * Created by huapeng.hhp on 2018/5/4.
 */
public class RpcDecoder extends ByteToMessageDecoder {
	private Class<?> clazz;

	public RpcDecoder(Class<?> clazz) {
		this.clazz = clazz;
	}

	protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list)
			throws Exception {
		// int frameLength = (int) byteBuf.getUnsignedInt(0);// 获取头部
		// // 当ByteBuf没有达到长度时，return null
		// if (byteBuf.readableBytes() < frameLength) {
		// throw new RuntimeException("Insufficient bytes to be read, expected:
		// " + frameLength);
		// }
		// // 舍弃头部
		// byteBuf.skipBytes(4);
		// int index = byteBuf.readerIndex();
		// // 取出自己定义的packet包返回给ChannelRead
		// ByteBuf frame = byteBuf.slice(index, frameLength).retain();
		// //
		// 这一步一定要有，不然其实bytebuf的readerIndex没有变，netty会一直从这里开始读取，将readerIndex移动就相当于把前面的数据处理过了废弃掉了。
		// byteBuf.readerIndex(frameLength);
		//
		// // B test
		// if (frame.readableBytes() < 4) {
		// return;
		// }
		// Serializer serializer = SerializerFactory.load();
		// int length = frame.readInt();
		//// if (frame.readableBytes() < length) {
		//// throw new RuntimeException("Insufficient bytes to be read,
		// expected: " + length);
		//// }
		// byte[] bytes = new byte[frame.readableBytes()];
		// frame.readBytes(bytes);
		// Object object = serializer.deserialize(bytes, clazz);
		// list.add(object);

		// B test
		if (byteBuf.readableBytes() < 4) {
			return;
		}
		Serializer serializer = SerializerFactory.load();
		int length = byteBuf.readInt();
        int beginIndex = byteBuf.readerIndex();

        // 说明包还没有读取完成等待下一个包
		if (byteBuf.readableBytes() < length) {
            byteBuf.readerIndex(beginIndex);
            return;
		}
		byte[] bytes = new byte[length];
		byteBuf.readBytes(bytes);
		Object object = serializer.deserialize(bytes, clazz);
		list.add(object);
	}

	public void setClass(Class<?> clazz) {
		this.clazz = clazz;
	}
}
