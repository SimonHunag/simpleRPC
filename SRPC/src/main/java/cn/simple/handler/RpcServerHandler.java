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
import cn.simple.exception.SRpcException;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * RpcServerHandler 处理对应的请求类 返回一个respones
 *
 * Created by huapeng.hhp on 2018/5/4.
 */
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
	private Map<String, Object> serviceHandle;

	public RpcServerHandler(Map<String, Object> serviceHandle) {
		this.serviceHandle = serviceHandle;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RpcRequest rpcRequest) throws Exception {
		System.out.println("RpcServerHandler request: " + rpcRequest);
		RpcResponse response = new RpcResponse();
		response.setRequestId(rpcRequest.getRequestId());
		response.setError(null);
		Object result = handle(rpcRequest);
		response.setResult(result);
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

	/** ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */
	private Object handle(RpcRequest rpcRequest) {
		String className = rpcRequest.getClassName();
		Object beanClass = this.serviceHandle.get(className);
		if (beanClass == null) {
			throw new SRpcException("没有找到对应的类");
		}
		Method[] methods = beanClass.getClass().getDeclaredMethods();
		return invokeMedthod(beanClass,methods, rpcRequest);
	}

	private Object invokeMedthod(Object beanClass, Method[] methods, RpcRequest rpcRequest) {
		String methodName = rpcRequest.getMethodName();
		if (methods == null || methods.length <= 0) {
			throw new SRpcException("类没有对应的方法");
		}
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				try {
					return method.invoke(beanClass, rpcRequest.getParams());
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}
}
