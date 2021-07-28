package com.es.service;

import com.alibaba.fastjson.JSON;
import com.es.domain.UserInfo;
import com.es.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 精确查询
 */
@Slf4j
@Service
public class TermQueryService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 精确查询(查询条件不会进行分词，但是查询内容可能会分词，导致查询不到)
     */
    public void termQuery(){
        try{
            //构建查询条件
            SearchSourceBuilder builder = new SearchSourceBuilder();
            builder.query(QueryBuilders.termQuery("address.keyword","xxxx"));
            //创建查询请求对象，将查询对象匹配到其中
            SearchRequest request = new SearchRequest(Constant.INDEX);
            request.source(builder);
            //执行查询，然后处理响应结果
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            if(RestStatus.OK.equals(response.status())  && response.getHits().totalHits > 0){
                SearchHits hits = response.getHits();
                for(SearchHit hit : hits){
                    UserInfo userInfo = JSON.parseObject(hit.getSourceAsString(),UserInfo.class);
                    log.info("精确查询信息:{}",userInfo.toString());
                }
            }

        } catch (IOException e) {
            log.error("",e);
        }
    }

    /**
     * 多个内容在一个字段中进行查询
     */
    public void termsQuery(){
        try{
            //构建查询条件
            SearchSourceBuilder builder = new SearchSourceBuilder();
            builder.query(QueryBuilders.termsQuery("address.keyword","xxx","xxxxxx"));
            //创建查询请求对象
            SearchRequest request = new SearchRequest(Constant.INDEX);
            request.source(builder);

            SearchResponse response = restHighLevelClient.search(request,RequestOptions.DEFAULT);
            if(RestStatus.OK.equals(response.status()) && response.getHits().totalHits >0){
                SearchHits hits = response.getHits();
                for(SearchHit hit : hits){
                    UserInfo userInfo = JSON.parseObject(hit.getSourceAsString(),UserInfo.class);
                    log.info("多内容精确查询:{}",userInfo);
                }
            }
        } catch (IOException e) {
            log.error("",e);
        }
    }
}
