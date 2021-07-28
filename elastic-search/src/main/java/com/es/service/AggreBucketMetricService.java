package com.es.service;

import com.es.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.tophits.ParsedTopHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * metric与bucket聚合分析
 */
@Service
@Slf4j
public class AggreBucketMetricService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * tophits按岁数分桶，然后统计每个员工工资最高值
     */
    public void aggregationTophits(){
        try{
            AggregationBuilder top = AggregationBuilders.topHits("salary_max_user")
                    .size(1)
                    .sort("salary",SortOrder.DESC);
            AggregationBuilder salaryBucket = AggregationBuilders.terms("salary_bucket")
                    .field("age")
                    .size(10);
            salaryBucket.subAggregation(top);

            SearchSourceBuilder builder = new SearchSourceBuilder();
            builder.size(0);
            builder.aggregation(salaryBucket);

            SearchRequest request = new SearchRequest(Constant.INDEX);
            request.source(builder);

            SearchResponse response = restHighLevelClient.search(request,RequestOptions.DEFAULT);

            Aggregations aggregations = response.getAggregations();
            if(response.status().equals(RestStatus.OK)){
                Terms terms = aggregations.get("salary_bucket");
                List<? extends Terms.Bucket> buckets = terms.getBuckets();
                for(Terms.Bucket bucket:buckets){
                    log.info("桶名:{}",bucket.getKeyAsString());
                    ParsedTopHits topHits = bucket.getAggregations().get("salary_max_user");
                    for(SearchHit hit : topHits.getHits()){
                        log.info(hit.getSourceAsString());
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
