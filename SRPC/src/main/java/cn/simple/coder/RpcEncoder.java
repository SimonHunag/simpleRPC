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
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * RpcEncoder
 *
 * Created by huapeng.hhp on 2018/5/4.
 */
public class RpcEncoder extends MessageToByteEncoder {
	private Class<?> clazz;

	public RpcEncoder(Class<?> clazz) {
		this.clazz = clazz;
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
		Serializer serializer = SerializerFactory.load();
		byte[] bytes = serializer.serialize(msg);
		int len = bytes.length;
		out.writeInt(len);
		out.writeBytes(bytes);
	}
}
