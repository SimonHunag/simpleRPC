/**
 * 版权所有@: 杭州铭师堂教育科技发展有限公司
 * 注意：本内容仅限于杭州铭师堂教育科技发展有限公司内部使用，禁止外泄以及用于其他的商业目的
 * CopyRight@: 2018 Hangzhou Mistong Educational Technology Co.,Ltd.
 * All Rights Reserved.
 * Note:Just limited to use by Mistong Educational Technology Co.,Ltd. Others are forbidden.
 */
package cn.simple;

import java.util.List;

import cn.simple.zk.CuratorZookeeperClient;
import cn.simple.zk.ZookeeperClient;
import cn.simple.zk.conf.ZKConfig;

/**
 * ZookeeperClientTest
 *
 * Created by huapeng.hhp on 2018/6/5.
 */
public class ZookeeperClientTest {
	public static void main(String[] args) {
		ZKConfig config = new ZKConfig();
		config.setAddress("10.0.0.28:2181");
		config.setAppName("SRPC");
		ZookeeperClient zkClient = new CuratorZookeeperClient(config);

		zkClient.create("/test/srpc", false);

        List<String> strlist = zkClient.getChildren("/test/srpc");
        System.out.println(strlist);
		zkClient.create("/test/srpc/ppp", true);
        strlist = zkClient.getChildren("/test/srpc");
		System.out.println(strlist);
	}
}
