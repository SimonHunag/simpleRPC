/**
 * 版权所有@: 杭州铭师堂教育科技发展有限公司
 * 注意：本内容仅限于杭州铭师堂教育科技发展有限公司内部使用，禁止外泄以及用于其他的商业目的
 * CopyRight@: 2018 Hangzhou Mistong Educational Technology Co.,Ltd.
 * All Rights Reserved.
 * Note:Just limited to use by Mistong Educational Technology Co.,Ltd. Others are forbidden.
 */
package cn.simple;

import cn.simple.net.NettyServer;

/**
 * NettyServerTest
 *
 * Created by huapeng.hhp on 2018/5/1.
 */
public class NettyServerTest {
	public static void main(String[] args) {
		NettyServer httpServer = new NettyServer();
		httpServer.start();
		System.out.println("123132");
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
