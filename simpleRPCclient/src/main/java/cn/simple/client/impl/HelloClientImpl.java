/**
 * 版权所有@: 杭州铭师堂教育科技发展有限公司
 * 注意：本内容仅限于杭州铭师堂教育科技发展有限公司内部使用，禁止外泄以及用于其他的商业目的
 * CopyRight@: 2018 Hangzhou Mistong Educational Technology Co.,Ltd.
 * All Rights Reserved.
 * Note:Just limited to use by Mistong Educational Technology Co.,Ltd. Others are forbidden.
 */
package cn.simple.client.impl;

import cn.simple.annotation.SRpcClinet;
import cn.simple.client.HelloClient;
import cn.simple.service.HelloService;

/**
 * HelloClientImpl
 *
 * Created by huapeng.hhp on 2018/5/15.
 */
public class HelloClientImpl implements HelloClient{

    @SRpcClinet(value = HelloService.class)
    private HelloService helloService;

    @Override
    public String say(String name) {
        return helloService.sayHi(name);
    }
}
