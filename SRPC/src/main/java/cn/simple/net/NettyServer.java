/**
 * 版权所有@: 杭州铭师堂教育科技发展有限公司
 * 注意：本内容仅限于杭州铭师堂教育科技发展有限公司内部使用，禁止外泄以及用于其他的商业目的
 * CopyRight@: 2018 Hangzhou Mistong Educational Technology Co.,Ltd.
 * All Rights Reserved.
 * Note:Just limited to use by Mistong Educational Technology Co.,Ltd. Others are forbidden.
 */
package cn.simple.net;

import cn.simple.coder.RpcDecoder;
import cn.simple.coder.RpcEncoder;
import cn.simple.common.NamedThreadFactory;
import cn.simple.common.RpcRequest;
import cn.simple.common.RpcResponse;
import cn.simple.handler.RpcServerHandler;
import cn.simple.server.HttpServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * NettyJaxrsServer
 *
 * Created by huapeng.hhp on 2018/5/1.
 */
public class NettyServer implements HttpServer {
	/** 服务启动器 */
	protected ServerBootstrap bootstrap = new ServerBootstrap();
	private EventLoopGroup eventLoopGroup;
	private EventLoopGroup eventExecutor;
	protected String hostname = "127.0.0.1";
	protected int port = 51001;
	/** 物理机核数 */
	private int ioWorkerCount = Runtime.getRuntime().availableProcessors() * 2;
	/** 总线程数 */
	private int executorThreadCount = 16;
	/** 可连接队列 */
	private int backlog = 128;
	/** 是否是守护线程 */
	protected boolean daemon = true;
	/** 是否测试长连接的状态 */
	protected boolean keepAlive = false;
	private int maxRequestSize = 1024 * 1024 * 10;

	@Override
	public void start() {
		// 处理请求IO，建立连接
		eventLoopGroup = new NioEventLoopGroup(ioWorkerCount, new NamedThreadFactory("SRPC-IO-" + port, daemon));
		// 处理执行事件，就是处理业务
		eventExecutor = new NioEventLoopGroup(executorThreadCount, new NamedThreadFactory("SRPC-BIZ-" + port, daemon));
		bootstrap.group(eventExecutor, eventLoopGroup).channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, backlog).childOption(ChannelOption.SO_KEEPALIVE, keepAlive)
				.childHandler(createChannelInitializer());
		final InetSocketAddress socketAddress;
		if (null == hostname || hostname.isEmpty()) {
			socketAddress = new InetSocketAddress(port);
		} else {
			socketAddress = new InetSocketAddress(hostname, port);
		}
		bootstrap.bind(socketAddress).syncUninterruptibly();
	}

	private ChannelHandler createChannelInitializer() {
		return new ChannelInitializer<SocketChannel>() {
			// 为accept
			// channel的pipeline预添加的inboundhandler
			// 当新连接accept的时候，这个方法会调用
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline channelPipeline = ch.pipeline();
				channelPipeline.addLast(new RpcEncoder(RpcResponse.class));
				channelPipeline.addLast(new RpcDecoder(RpcRequest.class));
				//channelPipeline.addLast(new HelloServerInHandler());
				channelPipeline.addLast(new RpcServerHandler());
			}
		};
	}

	@Override
	public void stop() {
		try {
			eventLoopGroup.shutdownGracefully().sync();
			eventExecutor.shutdownGracefully().sync();
		} catch (Exception ignore) { // NOPMD
		}
	}
	/** ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */
}
