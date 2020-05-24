package com.hundanli.gulimall.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author li
 * 1.整合Mybatis plus
 *      1.1 在common模块添加Mybatis-plus和数据库驱动
 *      1.2 在yml中配置数据源
 *      1.3 配置Mybatis-plus
 *
 * 2.nacos服务注册与发现
 *      2.0 下载并启动nacos服务器
 *      2.1 gulimall-common模块导入nacos-discovery依赖
 *      2.2 开启注解@EnableDiscoveryClient
 *      2.3 在application.yml中指定
 *          spring.cloud.nacos.discovery.server-addr: 127.0.0.1:8848
 *          application.name: gulimall-member
 *
 * 3.openfeign远程调用流程
 *      3.1 导入openfeign依赖并开启注解@EnableFeignClients
 *      3.2 编写接口并使用@FeignClient声明调用的服务名称
 *      3.3 接口中的方法与远程方法的签名需一一对应
 *
 * 4.nacos配置中心
 *      4.0 启动nacos服务器
 *      4.1 gulimall-common模块导入nacos-config依赖
 *      4.2 在bootstrap.properties/bootstrap.yml文件中配置应用名称和nacos配置中心地址，
 *          它将优先于application.yml被加载
 *      4.3 在本地application.properties或者在nacos配置中心创建${应用名称}.properties文件并写入配置
 *      4.4 Controller上添加注解@RefreshScope
 *      4.5 使用spring中的@Value注解获取配置中心的值，配置中心的优先级更高
 *
 * 5.nacos配置中心命名空间和配置分组
 *      5.1 命名空间： 用于进行环境隔离和区分，默认为public，通常有两种区分方式
 *          1.按开发、测试、生产三种命名空间进行环境区分
 *          2.按各个微服务有自己独立的命名空间来进行环境区分
 *          3.指定方式：修改bootstrap.yml中的cloud.nacos.config.namespace值
 *      5.2 配置集和配置集ID（Data ID）：即.properties文件中的所有配置和它的文件名
 *      5.3 配置组：即每个命名空间下的分组，默认为DEFAULT_GROUP
 *          可以通过bootstrap.yml中的cloud.nacos.config.group指定
 *      5.4 最佳实践
 *          通常为每个微服务建一个namespace，在这个namespace下创建dev，test，prod三个分组分别用于开发、测试和部署
 * 6.多配置集组合
 *      微服务的任何配置都可以放到配置中心中，只要在bootstrap.yml中指定即可，如：
 *         # 多配置集组合
 *         extension-configs:
 *           - dataId: datasource.yml
 *             group: dev
 *             refresh: true
 *           - dataId: mybatis-plus.yml
 *             group: dev
 *             refresh: true
 *           - dataId: others.yml
 *             group: dev
 *             refresh: true
 *       这样一来，将会优先加载配置中心的配置信息。
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.hundanli.gulimall.member.feign")
public class GulimallMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallMemberApplication.class, args);
    }

}
