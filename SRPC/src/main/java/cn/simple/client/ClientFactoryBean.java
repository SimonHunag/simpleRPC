/**
 * 版权所有@: 杭州铭师堂教育科技发展有限公司
 * 注意：本内容仅限于杭州铭师堂教育科技发展有限公司内部使用，禁止外泄以及用于其他的商业目的
 * CopyRight@: 2018 Hangzhou Mistong Educational Technology Co.,Ltd.
 * All Rights Reserved.
 * Note:Just limited to use by Mistong Educational Technology Co.,Ltd. Others are forbidden.
 */
package cn.simple.client;

import cn.simple.annotation.SRpcClinet;
import cn.simple.conf.PropertyPlaceholder;
import cn.simple.net.URL;
import cn.simple.proxy.JdkInvokeProxy;
import cn.simple.utils.RegistryUtil;
import cn.simple.zk.CuratorZookeeperClient;
import cn.simple.zk.ZookeeperClient;
import cn.simple.zk.conf.ZKConfig;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.utils.ZKPaths;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClientFactoryBean
 *
 * Created by huapeng.hhp on 2018/5/17.
 */
public class ClientFactoryBean implements InitializingBean, ApplicationContextAware {
	private ZookeeperClient client;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (client == null) {
			ZKConfig config = new ZKConfig();
			config.setAddress(PropertyPlaceholder.getStrProperty("zookeeper.address"));
			config.setAppName(PropertyPlaceholder.getStrProperty("app.name"));
			config.setAppPort(PropertyPlaceholder.getStrProperty("simple.port"));
			client = new CuratorZookeeperClient(config);
		}
		String[] beanNames = applicationContext.getBeanDefinitionNames();
		if (beanNames != null && beanNames.length > 0) {
			for (String beanName : beanNames) {
				Object bean = applicationContext.getBean(beanName);
				for (Field field : bean.getClass().getDeclaredFields()) {
					if (field.isAnnotationPresent(SRpcClinet.class)) {
						PathChildrenCache pathChildrenCache = new PathChildrenCache(client.getCuratorFramework(),
								RegistryUtil.getChildPath(field.getType().getName()), true);
						List<URL> urls = new ArrayList<>();
						List<String> providerList = client
								.getChildren(RegistryUtil.getChildPath(field.getType().getName()));
						for (String provider : providerList) {
							urls.add(buildURL(provider));
						}
						JdkInvokeProxy proxy = new JdkInvokeProxy(urls);
						try {
							pathChildrenCache.start();
							addPathChildListener(pathChildrenCache, proxy);
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							// try {
							// pathChildrenCache.close();
							// } catch (IOException e) {
							// e.printStackTrace();
							// }
						}
						field.setAccessible(true);
						try {
							field.set(bean, proxy.newProxy(field.getType()));
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	private URL buildURL(String provider) {
		String[] protocol = StringUtils.split(provider, ":");
		try {
			URL url = new URL(protocol[0], Integer.valueOf(protocol[1]));
			return url;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void addPathChildListener(PathChildrenCache cache, final JdkInvokeProxy proxy) {
		// a PathChildrenCacheListener is optional. Here, it's used just to log
		// changes
		PathChildrenCacheListener listener = new PathChildrenCacheListener() {
			@Override
			public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
				String path = ZKPaths.getNodeFromPath(event.getData().getPath());
				byte[] data = event.getData().getData();
				String dataStr = new String(data);
				switch (event.getType()) {
				case CHILD_ADDED: {
					List<URL> urls = proxy.getUrls();
					if (urls == null || urls.size() == 0) {
						urls = new ArrayList<>();
						urls.add(buildURL(path));
					} else {
						Map<String, URL> urlMap = new HashMap<>();
						for (URL url : urls) {
							urlMap.put(url.getHost() + ":" + url.getPort(), url);
						}
						URL url = buildURL(path);
						urlMap.put(url.getHost() + ":" + url.getPort(), url);
						List<URL> newUrls = new ArrayList<>();
						for (Map.Entry<String, URL> urlEntry : urlMap.entrySet()) {
							newUrls.add(urlEntry.getValue());
						}
						urls = newUrls;
					}
					proxy.setUrls(urls);
					System.out.println("Node added: " + path + " data " + dataStr);
					break;
				}
				case CHILD_UPDATED: {
					System.out.println("Node changed: " + path + " data " + dataStr);
					break;
				}
				case CHILD_REMOVED: {
					List<URL> urls = proxy.getUrls();
					if (urls == null || urls.size() == 0) {
						break;
					}
					Map<String, URL> urlMap = new HashMap<>();
					for (URL url : urls) {
						urlMap.put(url.getHost() + ":" + url.getPort(), url);
					}
					URL url = buildURL(path);
					urlMap.remove(url.getHost() + ":" + url.getPort());
					List<URL> newUrls = new ArrayList<>();
					for (Map.Entry<String, URL> urlEntry : urlMap.entrySet()) {
						newUrls.add(urlEntry.getValue());
					}
					urls = newUrls;
					proxy.setUrls(urls);
					System.out.println("Node removed: " + path + " data " + dataStr);
					break;
				}
				}
			}
		};
		cache.getListenable().addListener(listener);
	}
}
