/**
 * 版权所有@: 杭州铭师堂教育科技发展有限公司
 * 注意：本内容仅限于杭州铭师堂教育科技发展有限公司内部使用，禁止外泄以及用于其他的商业目的
 * CopyRight@: 2018 Hangzhou Mistong Educational Technology Co.,Ltd.
 * All Rights Reserved.
 * Note:Just limited to use by Mistong Educational Technology Co.,Ltd. Others are forbidden.
 */
package cn.simple;

import cn.simple.zk.CuratorZookeeperClient;
import cn.simple.zk.ZookeeperClient;
import cn.simple.zk.conf.ZKConfig;

import java.util.List;

/**
 * ZKClientQuery
 *
 * Created by huapeng.hhp on 2018/6/24.
 */
public class ZKClientQuery {

    public static void main(String[] args) {
        ZKConfig config = new ZKConfig();
        config.setAddress("10.0.0.28:2181");
        config.setAppName("SRPC");
        ZookeeperClient zkClient = new CuratorZookeeperClient(config);

        while (true){
            List<String> strlist = zkClient.getChildren("/srpc/cn.simple.service.HelloService");
            System.out.println(strlist);

            try {
                Thread.sleep(1000l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }




    }
}
