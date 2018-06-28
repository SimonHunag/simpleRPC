/**
 * 版权所有@: 杭州铭师堂教育科技发展有限公司
 * 注意：本内容仅限于杭州铭师堂教育科技发展有限公司内部使用，禁止外泄以及用于其他的商业目的
 * CopyRight@: 2018 Hangzhou Mistong Educational Technology Co.,Ltd.
 * All Rights Reserved.
 * Note:Just limited to use by Mistong Educational Technology Co.,Ltd. Others are forbidden.
 */
package cn.simple.zk;

import cn.simple.zk.conf.ZKConfig;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;

import java.util.List;

/**
 * AbstractZookeeperClient
 *
 * Created by huapeng.hhp on 2018/6/5.
 */
public abstract class AbstractZookeeperClient implements ZookeeperClient {
	private final CuratorFramework curatorFramework;

	public AbstractZookeeperClient(ZKConfig zkConfig) {
		curatorFramework = CuratorFrameworkFactory.newClient(zkConfig.getAddress(), zkConfig.getSessionTimeOut(),
				zkConfig.getConnectionTimeOut(), new RetryNTimes(Integer.MAX_VALUE, 1000));
		curatorFramework.start();
	}

	@Override
	public void create(String path, boolean ephemeral) {
		int i = path.lastIndexOf('/');
		if (i > 0) {
			create(path.substring(0, i), false);
		}
		if (ephemeral) {
			createEphemeral(path);
		} else {
			createPersistent(path);
		}
	}

	@Override
	public List<String> getChildren(String path) {
		try {
			return curatorFramework.getChildren().forPath(path);
		} catch (KeeperException e) {
			return null;
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	public void createPersistent(String path) {
		try {
			getCuratorFramework().create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
		} catch (KeeperException e) {
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	public void createEphemeral(String path) {
		try {
			getCuratorFramework().create().withMode(CreateMode.EPHEMERAL).forPath(path);
		} catch (KeeperException e) {
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	public CuratorFramework getCuratorFramework() {
		return curatorFramework;
	}
}
