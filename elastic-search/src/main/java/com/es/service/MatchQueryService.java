package com.es.service;

import com.alibaba.fastjson.JSON;
import com.es.domain.UserInfo;
import com.es.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 匹配查询
 */
@Slf4j
@Service
public class MatchQueryService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 匹配查询符合条件的索引数据，并设置分页
     */
    public void matchAllQuery(){
        try{
            //构建查询条件
            MatchAllQueryBuilder builder = QueryBuilders.matchAllQuery();
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.query(builder);
            //设置分页
            sourceBuilder.from(0);
            sourceBuilder.size(10);
            //设置排序
            sourceBuilder.sort("salary", SortOrder.ASC);
            SearchRequest request = new SearchRequest(Constant.INDEX);
            request.source(sourceBuilder);
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            if(RestStatus.OK.equals(response.status()) && response.getHits().totalHits > 0){
                SearchHits hits = response.getHits();
                for(SearchHit hit: hits){
                    UserInfo userInfo = JSON.parseObject(hit.getSourceAsString(),UserInfo.class);
                    log.info("匹配查询：{}",userInfo.toString());
                }
            }
        } catch (IOException e) {
            log.error("",e);
        }
    }

    /**
     * 匹配查询数据
     */
    public void matchQuery(){
        try{
            SearchSourceBuilder builder = new SearchSourceBuilder();
            builder.query(QueryBuilders.matchQuery("address","*XXX"));
            SearchRequest request = new SearchRequest(Constant.INDEX);
            request.source(builder);
            SearchResponse response = restHighLevelClient.search(request,RequestOptions.DEFAULT);
            if(RestStatus.OK.equals(response.status()) && response.getHits().totalHits > 0){

            }
        } catch (IOException e) {

        }
    }

    /**
     * 词语匹配查询
     */
    public void matchPhraseQuery(){
        try{
            SearchSourceBuilder builder = new SearchSourceBuilder();
            builder.query(QueryBuilders.matchPhraseQuery("address","XXX"));
            SearchRequest request = new SearchRequest(Constant.INDEX);
            request.source(builder);
            SearchResponse response = restHighLevelClient.search(request,RequestOptions.DEFAULT);
            //XXX
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 内容在多字段中进行查询
     */
    public void matchMultiQuery(){
        try{
            SearchSourceBuilder builder = new SearchSourceBuilder();
            builder.query(QueryBuilders.multiMatchQuery("xxx","address","remark"));

            SearchRequest request = new SearchRequest(Constant.INDEX);
            request.source(builder);

            SearchResponse response = restHighLevelClient.search(request,RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
