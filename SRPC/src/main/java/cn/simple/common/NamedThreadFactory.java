/**
 * 版权所有@: 杭州铭师堂教育科技发展有限公司
 * 注意：本内容仅限于杭州铭师堂教育科技发展有限公司内部使用，禁止外泄以及用于其他的商业目的
 * CopyRight@: 2018 Hangzhou Mistong Educational Technology Co.,Ltd.
 * All Rights Reserved.
 * Note:Just limited to use by Mistong Educational Technology Co.,Ltd. Others are forbidden.
 */
package cn.simple.common;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * NamedThreadFactory
 *
 * Created by huapeng.hhp on 2018/5/1.
 */
public class NamedThreadFactory implements ThreadFactory {
	/**
	 * 系统全局线程池计数器
	 */
	private static final AtomicInteger POOL_COUNT = new AtomicInteger();
	/**
	 * 当前线程池计数器
	 */
	final AtomicInteger threadCount = new AtomicInteger(1);
	/**
	 * 线程组
	 */
	private final ThreadGroup group;
	/**
	 * 线程名前缀
	 */
	private final String namePrefix;
	/**
	 * 是否守护线程，true的话随主线程退出而退出，false的话则要主动退出
	 */
	private final boolean isDaemon;

	/**
	 * 构造函数，默认非守护线程
	 *
	 * @param prefix
	 *            前缀，后面会自动加上-T-
	 */
	public NamedThreadFactory(String prefix) {
		this(prefix, false);
	}

	/**
	 * 构造函数
	 *
	 * @param prefix
	 *            前缀，后面会自动加上-T-
	 * @param daemon
	 *            是否守护线程，true的话随主线程退出而退出，false的话则要主动退出
	 */
	public NamedThreadFactory(String prefix, boolean daemon) {
		SecurityManager s = System.getSecurityManager();
		group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
		namePrefix = prefix + "-" + POOL_COUNT.getAndIncrement() + "-T";
		isDaemon = daemon;
	}

	@Override
	public Thread newThread(Runnable r) {
		Thread t = new Thread(group, r, namePrefix + threadCount.getAndIncrement(), 0);
		t.setDaemon(isDaemon);
		if (t.getPriority() != Thread.NORM_PRIORITY) {
			t.setPriority(Thread.NORM_PRIORITY);
		}
		return t;
	}
}
