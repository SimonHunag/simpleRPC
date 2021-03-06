/**
 * 版权所有@: 杭州铭师堂教育科技发展有限公司
 * 注意：本内容仅限于杭州铭师堂教育科技发展有限公司内部使用，禁止外泄以及用于其他的商业目的
 * CopyRight@: 2018 Hangzhou Mistong Educational Technology Co.,Ltd.
 * All Rights Reserved.
 * Note:Just limited to use by Mistong Educational Technology Co.,Ltd. Others are forbidden.
 */
package cn.simple.client;

import cn.simple.coder.RpcDecoder;
import cn.simple.coder.RpcEncoder;
import cn.simple.common.RpcRequest;
import cn.simple.common.RpcResponse;
import cn.simple.handler.RpcClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;

/**
 * HelloClientTest
 *
 * Created by huapeng.hhp on 2018/5/1.
 */
public class HelloClientTest {
	public void connect(String host, int port) throws Exception {
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		RpcRequest request = new RpcRequest();
		request.setClassName("cn.simple.service.HelloService");
		request.setMethodName("sayHi");
		request.setParamTypes(new Class[]{String.class});
		request.setParams(new String[]{"go"});
		try {
			final CountDownLatch completedSignal = new CountDownLatch(1);
			final RpcResponse response = new RpcResponse();
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(workerGroup);
			bootstrap.channel(NioSocketChannel.class);
			bootstrap.option(ChannelOption.SO_KEEPALIVE, false);
			bootstrap.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline channelPipeline = ch.pipeline();
					//channelPipeline.addLast(new HelloClientIntHandler());
					channelPipeline.addLast(new RpcEncoder(RpcRequest.class));
					channelPipeline.addLast(new RpcDecoder(RpcResponse.class));
					channelPipeline.addLast(new RpcClientHandler(response));
				}
			});
			ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port)).sync();
			future.channel().writeAndFlush(request).addListener(new ChannelFutureListener() {
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
	}

	public static void main(String[] args) throws Exception {
		HelloClientTest client = new HelloClientTest();
		client.connect("127.0.0.1", 51001);
	}
}
