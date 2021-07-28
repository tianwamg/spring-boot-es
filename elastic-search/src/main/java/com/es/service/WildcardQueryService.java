package com.es.service;

import com.es.util.Constant;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * 通配符查询
 */
public class WildcardQueryService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    public void wildcardQuery(){
        try{
            SearchSourceBuilder builder = new SearchSourceBuilder();
            builder.query(QueryBuilders.wildcardQuery("name.keyword","*xxx"));

            SearchRequest request = new SearchRequest(Constant.INDEX);
            request.source(builder);

            SearchResponse response = restHighLevelClient.search(request,RequestOptions.DEFAULT);
            //TODO
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
