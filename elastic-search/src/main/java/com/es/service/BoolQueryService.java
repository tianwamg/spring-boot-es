package com.es.service;

import com.es.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 布尔查询
 */
@Slf4j
@Service
public class BoolQueryService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    public void boolQuery(){
        try{
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            boolQueryBuilder.must(QueryBuilders.termsQuery("address.keyword","xxx","xxx"))
                    .filter().add(QueryBuilders.rangeQuery("birthDate").format("yyyy").gte("1990").lte("1995"));

            SearchSourceBuilder builder = new SearchSourceBuilder();
            builder.query(boolQueryBuilder);

            SearchRequest request = new SearchRequest(Constant.INDEX);
            request.source(builder);

            SearchResponse response = restHighLevelClient.search(request,RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
