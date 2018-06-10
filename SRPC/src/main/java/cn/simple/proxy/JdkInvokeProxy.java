/**
 * 版权所有@: 杭州铭师堂教育科技发展有限公司
 * 注意：本内容仅限于杭州铭师堂教育科技发展有限公司内部使用，禁止外泄以及用于其他的商业目的
 * CopyRight@: 2018 Hangzhou Mistong Educational Technology Co.,Ltd.
 * All Rights Reserved.
 * Note:Just limited to use by Mistong Educational Technology Co.,Ltd. Others are forbidden.
 */
package cn.simple.proxy;

import cn.simple.coder.RpcDecoder;
import cn.simple.coder.RpcEncoder;
import cn.simple.common.RpcRequest;
import cn.simple.common.RpcResponse;
import cn.simple.handler.RpcClientHandler;
import cn.simple.net.URL;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * JdkInvokeProxy
 *
 * Created by huapeng.hhp on 2018/5/11.
 */
public class JdkInvokeProxy implements InvocationHandler {
	/** 代理的类 */
	private Object proxyClass;
	private static final String host = "127.0.0.1";
	private static final Integer port = 51001;
	private Class<?> clazz;
	private List<URL> urls;

	private	Random random =  new Random(1);

	public JdkInvokeProxy() {
	}

	public JdkInvokeProxy(List<URL> urls) {
		this.urls = urls;
	}

	public JdkInvokeProxy(Class<?> clazz) {
		this.clazz = clazz;
	}

	public <T> T newProxy(Class<?> clazz) {
		this.clazz = clazz;
		if (proxyClass != null) {
			return (T) proxyClass;
		}
		proxyClass = Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] { clazz }, this);
		return (T) proxyClass;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		if(urls==null || urls.size()==0){
			return null;
		}

		String methodName = method.getName();
		Class[] paramTypes = method.getParameterTypes();
		if ("toString".equals(methodName) && paramTypes.length == 0) {
			return method.toString();
		} else if ("hashCode".equals(methodName) && paramTypes.length == 0) {
			return method.hashCode();
		}

		EventLoopGroup workerGroup = new NioEventLoopGroup();
		RpcRequest request = new RpcRequest();
		request.setClassName(this.clazz.getName());
		request.setMethodName(methodName);
		request.setParamTypes(paramTypes);
		request.setParams(args);
		final RpcResponse response = new RpcResponse();
		try {
			final CountDownLatch completedSignal = new CountDownLatch(1);
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(workerGroup);
			bootstrap.channel(NioSocketChannel.class);
			bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
			bootstrap.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline channelPipeline = ch.pipeline();
					// channelPipeline.addLast(new HelloClientIntHandler());
					channelPipeline.addLast(new RpcEncoder(RpcRequest.class));
					channelPipeline.addLast(new RpcDecoder(RpcResponse.class));
					channelPipeline.addLast(new RpcClientHandler(response));
				}
			});

			// 随机负载方式


			URL url= urls.get(random.nextInt(urls.size()));

			ChannelFuture future = bootstrap.connect(new InetSocketAddress(url.getHost(), url.getPort())).sync();
			future.channel().writeAndFlush(request).addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture channelFuture) throws Exception {
					completedSignal.countDown();
				}
			});
			// Wait until the asynchronous write return.
			completedSignal.await();
			future.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
		}
		return response.getResult();
	}

	public List<URL> getUrls() {
		return urls;
	}

	public void setUrls(List<URL> urls) {
		this.urls = urls;
	}
}
