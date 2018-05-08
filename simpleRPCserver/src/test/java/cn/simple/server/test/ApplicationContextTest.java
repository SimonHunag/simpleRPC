/**
 * 版权所有@: 杭州铭师堂教育科技发展有限公司
 * 注意：本内容仅限于杭州铭师堂教育科技发展有限公司内部使用，禁止外泄以及用于其他的商业目的
 * CopyRight@: 2018 Hangzhou Mistong Educational Technology Co.,Ltd.
 * All Rights Reserved.
 * Note:Just limited to use by Mistong Educational Technology Co.,Ltd. Others are forbidden.
 */
package cn.simple.server.test;

import cn.simple.service.HelloService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.Test;

/**
 * ApplicationContextTest
 *
 * Created by huapeng.hhp on 2018/5/7.
 */
public class ApplicationContextTest {
	@Test
	public void testApplicationContext() {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		HelloService manager = (HelloService) context.getBean("helloService");
		manager.sayHi("Simon");
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
