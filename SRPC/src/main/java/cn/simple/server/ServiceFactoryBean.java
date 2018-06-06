/**
 * 版权所有@: 杭州铭师堂教育科技发展有限公司
 * 注意：本内容仅限于杭州铭师堂教育科技发展有限公司内部使用，禁止外泄以及用于其他的商业目的
 * CopyRight@: 2018 Hangzhou Mistong Educational Technology Co.,Ltd.
 * All Rights Reserved.
 * Note:Just limited to use by Mistong Educational Technology Co.,Ltd. Others are forbidden.
 */
package cn.simple.server;

import cn.simple.annotation.SRpcService;
import cn.simple.net.NettyServer;
import cn.simple.utils.NetUtils;
import cn.simple.zk.CuratorZookeeperClient;
import cn.simple.zk.ZookeeperClient;
import cn.simple.zk.conf.ZKConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * ServiceFactoryBean
 *
 * Created by huapeng.hhp on 2018/5/1.
 */
public class ServiceFactoryBean implements InitializingBean, ApplicationContextAware {
	private ApplicationContext applicationContext;
	private Map<String, Object> handlerMap = new HashMap<String, Object>();
	private volatile HttpServer httpServer;

	@Override
	public void afterPropertiesSet() throws Exception {
		httpServer = new NettyServer(handlerMap);
		httpServer.start();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		Map<String, Object> serviceMap = applicationContext.getBeansWithAnnotation(SRpcService.class);
		if (CollectionUtils.isEmpty(serviceMap)) {
			return;
		}
		for (Object bean : serviceMap.values()) {
			String interfaceName = bean.getClass().getAnnotation(SRpcService.class).value().getName();
			handlerMap.put(interfaceName, bean);
		}
		// 注册到zookeeper
		doRegister();
	}

	private void doRegister() {
		ZKConfig config = new ZKConfig();
		config.setAddress("10.0.0.28:2181");
		config.setAppName("SRPC");
		config.setAppPort("51001");
		ZookeeperClient client = new CuratorZookeeperClient(config);
		String servicePath = "/srpc/" + config.getAppName();
		client.create(servicePath, false);
		InetAddress inetAddress = NetUtils.getLocalAddress();
		for (Map.Entry<String, Object> hardler : handlerMap.entrySet()) {
			String childPath = servicePath + "/" + hardler.getKey() + "@" + inetAddress.getHostAddress() + ":"
					+ config.getAppPort();
			client.create(childPath, true);
		}
		System.out.println(client.getChildren(servicePath));
	}
}
