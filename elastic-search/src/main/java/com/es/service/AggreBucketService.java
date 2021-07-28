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
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * bucket桶聚合查询
 */
@Slf4j
@Service
public class AggreBucketService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 按岁数进行聚合分桶
     */
    public void aggreBucketTerms(){
        try {
            AggregationBuilder aggr = AggregationBuilders.terms("age_bucket").field("age");
            SearchSourceBuilder builder = new SearchSourceBuilder();
            builder.size(10);
            builder.aggregation(aggr);

            SearchRequest request = new SearchRequest(Constant.INDEX);
            request.source(builder);

            SearchResponse response = restHighLevelClient.search(request,RequestOptions.DEFAULT);
            Aggregations aggregations = response.getAggregations();
            if (RestStatus.OK.equals(response.status())) {
                //分桶
                Terms terms = aggregations.get("Age_bucket");
                List<? extends Terms.Bucket> buckets = terms.getBuckets();
                for(Terms.Bucket bucket : buckets){
                    log.info("桶名:{} | 总数:{}",bucket.getKeyAsString(),bucket.getDocCount());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 按工资范围进行聚合分桶
     */
    public void aggrBucketRange(){
        try{
            AggregationBuilder aggr = AggregationBuilders.range("salary_range_bucket")
                    .field("salary")
                    .addUnboundedTo("lower level",3000)
                    .addRange("middle level",5000,9000)
                    .addUnboundedFrom("high level",9000);

            SearchSourceBuilder builder = new SearchSourceBuilder();
            builder.size(0);
            builder.aggregation(aggr);

            SearchRequest request = new SearchRequest(Constant.INDEX);
            request.source(builder);

            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            Aggregations aggregations = response.getAggregations();
            if(RestStatus.OK.equals(response.status())){
                Range range = aggregations.get("salary_range_bucket");
                List<? extends Range.Bucket> buckets = range.getBuckets();
                for(Range.Bucket bucket : buckets){
                    log.info("桶名:{} | 总数{}",bucket.getKeyAsString(),bucket.getDocCount());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void multiOperate(){
        //时间范围分桶
        AggregationBuilder ad = AggregationBuilders.dateRange("date_range_bucket")
                .field("birthDate")
                .format("yyyy")
                .addRange("1985-1990","1985","1990")
                .addRange("1990-1995","1990","1995");

        //按工资多少聚合分桶
        AggregationBuilder as = AggregationBuilders.histogram("salary_histogram")
                .field("salary")
                .extendedBounds(0,12000)
                .interval(3000);

        //按出生日期进行分桶
        AggregationBuilder ab = AggregationBuilders.dateHistogram("birthday_histogram")
                .field("birthDate")
                .interval(1)
                .dateHistogramInterval(DateHistogramInterval.YEAR)
                .format("yyyy");

    }
}
