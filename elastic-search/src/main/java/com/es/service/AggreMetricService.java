package com.es.service;

import com.es.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.stats.ParsedStats;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 聚合查询
 */
@Service
@Slf4j
public class AggreMetricService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 统计员工总数，工资最高值，最低值，平均值，工资总和
     */
    public void aggregationStats(){

        try{
            AggregationBuilder aggr = AggregationBuilders.stats("salary_stats").field("salary");
            SearchSourceBuilder builder = new SearchSourceBuilder();
            builder.aggregation(aggr);
            //设置查询结果不返回，只返回聚合结果
            builder.size(0);

            SearchRequest request = new SearchRequest(Constant.INDEX);
            request.source(builder);

            SearchResponse response = restHighLevelClient.search(request,RequestOptions.DEFAULT);
            Aggregations aggregations = response.getAggregations();
            if(RestStatus.OK.equals(response.status()) || aggregations != null){
                ParsedStats stats = aggregations.get("salary_stats");
                log.info("-----------------------");
                log.info("聚合信息");
                log.info("count:{}",stats.getCount());
                log.info("avg:{}",stats.getAvg());
                log.info("max:{}",stats.getMax());
                log.info("min:{}",stats.getMin());
                log.info("sum:{}",stats.getSum());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void aggregationMin(){
        AggregationBuilder aggr = AggregationBuilders.min("salary_min").field("salary");
    }
}
