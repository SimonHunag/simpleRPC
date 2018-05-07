/**
 * 版权所有@: 杭州铭师堂教育科技发展有限公司
 * 注意：本内容仅限于杭州铭师堂教育科技发展有限公司内部使用，禁止外泄以及用于其他的商业目的
 * CopyRight@: 2018 Hangzhou Mistong Educational Technology Co.,Ltd.
 * All Rights Reserved.
 * Note:Just limited to use by Mistong Educational Technology Co.,Ltd. Others are forbidden.
 */
package cn.simple.handler;

import cn.simple.common.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * RpcClientHandler 处理返回的respone Created by huapeng.hhp on 2018/5/4.
 */
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponse> {
	private RpcResponse response;

	public RpcClientHandler(RpcResponse response) {
		this.response = response;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RpcResponse rpcResponse) throws Exception {
		System.out.println("RpcClientHandler - response: " + rpcResponse);
		response.setRequestId(rpcResponse.getRequestId());
		response.setError(rpcResponse.getError());
		response.setResult(rpcResponse.getResult());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	public RpcResponse getResponse() {
		return response;
	}
}
