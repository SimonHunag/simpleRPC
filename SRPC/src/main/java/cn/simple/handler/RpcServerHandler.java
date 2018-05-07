/**
 * 版权所有@: 杭州铭师堂教育科技发展有限公司
 * 注意：本内容仅限于杭州铭师堂教育科技发展有限公司内部使用，禁止外泄以及用于其他的商业目的
 * CopyRight@: 2018 Hangzhou Mistong Educational Technology Co.,Ltd.
 * All Rights Reserved.
 * Note:Just limited to use by Mistong Educational Technology Co.,Ltd. Others are forbidden.
 */
package cn.simple.handler;

import cn.simple.common.RpcRequest;
import cn.simple.common.RpcResponse;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * RpcServerHandler 处理对应的请求类 返回一个respones
 *
 * Created by huapeng.hhp on 2018/5/4.
 */
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RpcRequest rpcRequest) throws Exception {
		System.out.println("RpcServerHandler request: " + rpcRequest);
		RpcResponse response = new RpcResponse();
		response.setRequestId(rpcRequest.getRequestId());
		response.setError(null);
		// Object result = handle(rpcRequest);
		response.setResult(null);
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
