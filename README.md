#实现一个简单的RPC

看了许许多多的RPC框架，很多的同学只知道怎么去用，但是真正的原理以及如何去实现一个PRC框架没有一个概念，因此想去写一个简单PRC框架抛砖引玉一下。或者那一天我们需要自己去实现符合我们自己的业务的PRC框架，期待有怎么一天^_^


##说说思路

![设计一个PRC框架的思路](http://sinom21.oss-cn-hangzhou.aliyuncs.com/%E5%A6%82%E4%BD%95%E5%86%99%E4%B8%80%E4%B8%AARPC%E6%A1%86%E6%9E%B6.png)

确定一些研发基础环境

    JDK 8 + spring framework 5.x


---
##step1.服务端与客户端之间的通讯模型

1.简单的完成服务之间的通讯问题，使用netty进行实现。

##step2.序列化

1.进行序列化，fastjson与kryo

##step3.服务注册与服务调用

1.完成服务的注册与服务的调用
    
    查看用例的方法：ApplicationContextTest与HelloClientTest这两个类

现在刚刚完成，是比较粗糙的，没有做线程安全的控制已经异常流程的控制，但是这个可以之后慢慢的去不足，写到这里。大家对RPC应该有了比较初步的认识了。

2.客户端通过proxy执行

* proxy的原理需要去了解InvocationHandler与Proxy的一些原理
##

