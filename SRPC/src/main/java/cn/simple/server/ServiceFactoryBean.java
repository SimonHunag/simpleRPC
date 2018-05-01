/**
 * 版权所有@: 杭州铭师堂教育科技发展有限公司
 * 注意：本内容仅限于杭州铭师堂教育科技发展有限公司内部使用，禁止外泄以及用于其他的商业目的
 * CopyRight@: 2018 Hangzhou Mistong Educational Technology Co.,Ltd.
 * All Rights Reserved.
 * Note:Just limited to use by Mistong Educational Technology Co.,Ltd. Others are forbidden.
 */
package cn.simple.server;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import cn.simple.net.NettyServer;

/**
 * ServiceFactoryBean
 *
 * Created by huapeng.hhp on 2018/5/1.
 */
public class ServiceFactoryBean implements InitializingBean, ApplicationContextAware {
	private static final HttpServer httpServer = new NettyServer();

	@Override
	public void afterPropertiesSet() throws Exception {
		httpServer.start();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
	}
}
