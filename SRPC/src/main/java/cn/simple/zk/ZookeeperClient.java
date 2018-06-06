/**
 * 版权所有@: 杭州铭师堂教育科技发展有限公司
 * 注意：本内容仅限于杭州铭师堂教育科技发展有限公司内部使用，禁止外泄以及用于其他的商业目的
 * CopyRight@: 2018 Hangzhou Mistong Educational Technology Co.,Ltd.
 * All Rights Reserved.
 * Note:Just limited to use by Mistong Educational Technology Co.,Ltd. Others are forbidden.
 */
package cn.simple.zk;

import java.util.List;

/**
 * ZookeeperClient
 *
 * Created by huapeng.hhp on 2018/6/5.
 */
public interface ZookeeperClient {
	/**
	 * 创建节点
	 * 
	 * @param path
	 *            路径
	 * @param ephemeral
	 *            是否是临时节点
	 */
	void create(String path, boolean ephemeral);

    /**
     * 获取子节点
     * @param path
     * @return
     */
	List<String> getChildren(String path);
}
