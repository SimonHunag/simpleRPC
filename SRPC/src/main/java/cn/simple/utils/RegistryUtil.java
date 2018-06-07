/**
 * 版权所有@: 杭州铭师堂教育科技发展有限公司
 * 注意：本内容仅限于杭州铭师堂教育科技发展有限公司内部使用，禁止外泄以及用于其他的商业目的
 * CopyRight@: 2018 Hangzhou Mistong Educational Technology Co.,Ltd.
 * All Rights Reserved.
 * Note:Just limited to use by Mistong Educational Technology Co.,Ltd. Others are forbidden.
 */
package cn.simple.utils;

/**
 * RegistryUtils
 *
 * Created by huapeng.hhp on 2018/6/7.
 */
public class RegistryUtil {
	public static final String servicePath = "/srpc";

	public static String getServicePath() {
		return servicePath;
	}

	public static String getChildPath(String childPath) {
		return new StringBuffer(servicePath).append("/").append(childPath).toString();
	}
}
