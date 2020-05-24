package com.hundanli.gulimall.thirdparty;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;

@SpringBootTest
class GulimallThirdpartyApplicationTests {

    @Autowired
    OSSClient ossClient;

    @Test
    void testOssUpload() {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "oss-cn-shenzhen.aliyuncs.com";
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = "LTAI4Fy1FtkHieQ4kpzG5NP6";
        String accessKeySecret = "NaMsN2T2BFtmJoYWLadkdzJ1p1kaU8";
        String bucketName = "gulimall-hundanli";
        // <yourObjectName>上传文件到OSS时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。
        String objectName = "hello-oss.txt";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 上传内容到指定的存储空间（bucketName）并保存为指定的文件名称（objectName）。
        String content = "Hello OSS";
        ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(content.getBytes()));


        // 关闭OSSClient。
        ossClient.shutdown();
        System.out.println("上传成功！");
    }


    /**
     * SpringBoot整合OSS：
     * 1. 导入依赖
     * 2. 注入OSSClient实例
     * 3. 上传
     */
    @Test
    void testOssUploadBoot() {
        String objectName = "hello-boot.txt";
        String bucketName = "gulimall-hundanli";

        // 上传内容到指定的存储空间（bucketName）并保存为指定的文件名称（objectName）。
        String content = "Hello OSS Boot";
        ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(content.getBytes()));
        System.out.println("上传完成！");
    }

}
