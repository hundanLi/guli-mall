package com.hundanli.gulimall.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2020/11/1 15:03
 */
@Configuration
public class EsClientConfig {

    public static RequestOptions COMMON_OPTIONS = RequestOptions.DEFAULT;
    @Bean
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));
    }

//    @Bean
//    public RestHighLevelClient restHighLevelClient() {
//        return new RestHighLevelClient(RestClient.builder(new HttpHost("ubuntu.wsl", 9200, "http")));
//    }
}
