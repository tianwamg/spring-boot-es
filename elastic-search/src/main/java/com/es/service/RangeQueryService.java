package com.es.service;

import com.es.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 范围查询
 */
@Slf4j
@Service
public class RangeQueryService {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    /**
     * 查询年龄>=30的数据
     */
    public void rangeQuery(){
        try{
            SearchSourceBuilder builder = new SearchSourceBuilder();
            builder.query(QueryBuilders.rangeQuery("age").gte(30));
            SearchRequest request = new SearchRequest(Constant.INDEX);
            request.source(builder);

            SearchResponse response = restHighLevelClient.search(request,RequestOptions.DEFAULT);
            //TODO
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dateRangeQuery(){
        try{
            SearchSourceBuilder builder = new SearchSourceBuilder();
            //includeLower(是否包含下边界),includeUpper(是否包含上边界)
            builder.query(QueryBuilders.rangeQuery("birthDate")
                .gte("now-30y").includeLower(true).includeUpper(true));

            SearchRequest request = new SearchRequest(Constant.INDEX);
            request.source(builder);
            SearchResponse response = restHighLevelClient.search(request,RequestOptions.DEFAULT);
            //TODO
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
