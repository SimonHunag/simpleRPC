/**
 * 版权所有@: 杭州铭师堂教育科技发展有限公司
 * 注意：本内容仅限于杭州铭师堂教育科技发展有限公司内部使用，禁止外泄以及用于其他的商业目的
 * CopyRight@: 2018 Hangzhou Mistong Educational Technology Co.,Ltd.
 * All Rights Reserved.
 * Note:Just limited to use by Mistong Educational Technology Co.,Ltd. Others are forbidden.
 */
package cn.simple.client;

import cn.simple.proxy.JdkInvokeProxy;
import cn.simple.service.HelloService;

/**
 * HelloClientTest
 *
 * Created by huapeng.hhp on 2018/5/1.
 */
public class HelloClientTest1 {
	public static void main(String[] args) throws Exception {
		JdkInvokeProxy proxy = new JdkInvokeProxy();
		HelloService server = (HelloService) proxy.newProxy(HelloService.class);
		String result = server.sayHi("Proxy");
		System.out.println(result);
	}
}
