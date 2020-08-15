package com.hundanli.gulimall.search;

import com.alibaba.fastjson.JSON;
import com.hundanli.gulimall.search.config.EsClientConfig;
import lombok.Data;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class GulimallSearchApplicationTests {

    @Qualifier("restHighLevelClient")
    @Autowired
    private RestHighLevelClient client;

    @Test
    void testEsRestClient() {
        System.out.println(client);
    }

    @Test
    void testSearchSku() throws IOException {
        SearchRequest searchRequest = new SearchRequest("product");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MatchAllQueryBuilder matchAllQuery = QueryBuilders.matchAllQuery();
        searchSourceBuilder.query(matchAllQuery);
        searchRequest.source(searchSourceBuilder);

        SearchResponse response = client.search(searchRequest, EsClientConfig.COMMON_OPTIONS);
        System.out.println(response);

    }

    @Test
    void testSearch() throws IOException {
        // 1.创建SearchRequest
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("bank");
        // 2.构造SearchSourceBuilder
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 加入查询条件
        searchSourceBuilder.query(QueryBuilders.matchQuery("address", "mill"));
        // 加入聚合操作
        // 按照age分组，并且分组求balance平均值
        TermsAggregationBuilder ageAgg = AggregationBuilders.terms("byAge").field("age").size(100);
        ageAgg.subAggregation(AggregationBuilders.avg("balanceAvg").field("balance"));
        searchSourceBuilder.aggregation(ageAgg);
        // 第二个聚合操作，求年龄平均值
        AvgAggregationBuilder ageAvg = AggregationBuilders.avg("ageAvg").field("age");
        searchSourceBuilder.aggregation(ageAvg);


        // 将其加入查询对象
        searchRequest.source(searchSourceBuilder);
        System.out.println(searchSourceBuilder.toString());

        // 3.执行
        SearchResponse response = client.search(searchRequest, EsClientConfig.COMMON_OPTIONS);
        System.out.println(response.toString());

        // 4.结果分析
        // 命中的全部结果
        SearchHit[] hits = response.getHits().getHits();
        for (SearchHit hit : hits) {
            // 获取源数据并解析成javabean
            String source = hit.getSourceAsString();
            Account account = JSON.parseObject(source, Account.class);
            System.out.println(account);
        }

        // 聚合信息
        Aggregations aggregations = response.getAggregations();
        Terms ageTerms = aggregations.get("byAge");
        for (Terms.Bucket bucket : ageTerms.getBuckets()) {
            String key = bucket.getKeyAsString();
            long docCount = bucket.getDocCount();

            // 子聚合
            Avg balanceAvg = bucket.getAggregations().get("balanceAvg");
            System.out.println("年龄:" + key + "\t结果数:" + docCount+ "\t平均薪资:" + balanceAvg.getValue());

        }
        Avg ageAvgResult = aggregations.get("ageAvg");
        System.out.println("年龄平均值: " + ageAvgResult.getValue());
    }

    @Test
    void testGet() throws IOException {
        GetRequest getRequest = new GetRequest("user", "1");
        GetResponse response = client.get(getRequest, EsClientConfig.COMMON_OPTIONS);
        System.out.println(response.getSource());
    }

    @Test
    void testIndex() throws IOException {
        // 1.构造json串
        User user = new User();
        user.setAge(22);
        user.setName("白晓琪");
        user.setGender("F");
        String jsonString = JSON.toJSONString(user);

        // 2.构造index请求
        IndexRequest indexRequest = new IndexRequest("user").id("1");
        indexRequest.source(jsonString, XContentType.JSON);

        // 3.执行
        IndexResponse response = client.index(indexRequest, EsClientConfig.COMMON_OPTIONS);
        System.out.println(response);

    }


    @Data
    static class User {
        String name;
        Integer age;
        String gender;
    }

    @Data
    static class Account {
        private int account_number;
        private int balance;
        private String firstname;
        private String lastname;
        private int age;
        private String gender;
        private String address;
        private String employer;
        private String email;
        private String city;
        private String state;
    }
}
