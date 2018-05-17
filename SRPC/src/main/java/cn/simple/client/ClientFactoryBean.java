/**
 * 版权所有@: 杭州铭师堂教育科技发展有限公司
 * 注意：本内容仅限于杭州铭师堂教育科技发展有限公司内部使用，禁止外泄以及用于其他的商业目的
 * CopyRight@: 2018 Hangzhou Mistong Educational Technology Co.,Ltd.
 * All Rights Reserved.
 * Note:Just limited to use by Mistong Educational Technology Co.,Ltd. Others are forbidden.
 */
package cn.simple.client;

import cn.simple.annotation.SRpcClinet;
import cn.simple.proxy.JdkInvokeProxy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Field;

/**
 * ClientFactoryBean
 *
 * Created by huapeng.hhp on 2018/5/17.
 */
public class ClientFactoryBean implements InitializingBean, ApplicationContextAware {
    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        if (beanNames != null && beanNames.length > 0) {
            for (String beanName : beanNames) {
                Object bean = applicationContext.getBean(beanName);
                for (Field field : bean.getClass().getDeclaredFields()) {
                    if (field.isAnnotationPresent(SRpcClinet.class)) {
                        JdkInvokeProxy proxy = new JdkInvokeProxy();
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
}
