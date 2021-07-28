package com.es.service;

import com.alibaba.fastjson.JSON;
import com.es.domain.UserInfo;
import com.es.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.directory.SearchResult;
import java.io.IOException;

/**
 * 模糊查询
 */
@Slf4j
@Service
public class FuzzyQueryService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 模糊查询所有以xx结尾的姓名
     * @return
     */
    public void fuzzyQuery(){
        try{
            SearchSourceBuilder builder = new SearchSourceBuilder();
            builder.query(QueryBuilders.fuzzyQuery("name","xxx").fuzziness(Fuzziness.AUTO));
            SearchRequest request = new SearchRequest(Constant.INDEX);
            request.source(builder);
            SearchResponse response = restHighLevelClient.search(request,RequestOptions.DEFAULT);
            if(RestStatus.OK.equals(response.status()) && response.getHits().totalHits > 0){
                SearchHits hits = response.getHits();
                for(SearchHit hit : hits){
                    UserInfo info = JSON.parseObject(hit.getSourceAsString(),UserInfo.class);
                    log.info("模糊信息查询:{}",info.toString());
                }
            }
        } catch (IOException e) {
            log.error("",e);
        }

    }
}
