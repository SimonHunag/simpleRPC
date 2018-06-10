/**
 * 版权所有@: 杭州铭师堂教育科技发展有限公司
 * 注意：本内容仅限于杭州铭师堂教育科技发展有限公司内部使用，禁止外泄以及用于其他的商业目的
 * CopyRight@: 2018 Hangzhou Mistong Educational Technology Co.,Ltd.
 * All Rights Reserved.
 * Note:Just limited to use by Mistong Educational Technology Co.,Ltd. Others are forbidden.
 */
package cn.simple.zk.conf;

import java.io.Serializable;

/**
 * ZKConfig
 *
 * Created by huapeng.hhp on 2018/6/5.
 */
public class ZKConfig implements Serializable {
	private static final long serialVersionUID = 3710479934608370978L;
	/** appname */
	private String appName;
	/** zk地址 */
	private String address;

	/** 是netty的端口号不是zk的*/
	private String appPort;
	/** 连接超时时间 */
	private Integer connectionTimeOut = 5000;
	/** session超时时间 */
	private Integer sessionTimeOut = 60 * 1000;

	public String getAppPort() {
		return appPort;
	}

	public void setAppPort(String appPort) {
		this.appPort = appPort;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getConnectionTimeOut() {
		return connectionTimeOut;
	}

	public void setConnectionTimeOut(Integer connectionTimeOut) {
		this.connectionTimeOut = connectionTimeOut;
	}

	public Integer getSessionTimeOut() {
		return sessionTimeOut;
	}

	public void setSessionTimeOut(Integer sessionTimeOut) {
		this.sessionTimeOut = sessionTimeOut;
	}
}
