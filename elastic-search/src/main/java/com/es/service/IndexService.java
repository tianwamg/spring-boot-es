package com.es.service;

import com.es.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class IndexService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 创建索引
     */
    public void createIndex(){
        try {
            XContentBuilder mapping = XContentFactory.jsonBuilder()
                    .startObject()
                    .field("dynamic",true)
                    .startObject("properties")
                    .startObject("name")
                    .field("type","text")
                    .startObject("fields")
                    .startObject("keyword")
                    .field("type","keyword")
                    .endObject()
                    .endObject()
                    .endObject()
                    .startObject("address")
                    .field("type","text")
                    .startObject("fields")
                    .startObject("keyword")
                    .field("type","keyword")
                    .endObject()
                    .endObject()
                    .endObject()
                    .startObject("remark")
                    .field("type","text")
                    .startObject("fields")
                    .startObject("keyword")
                    .field("type","keyword")
                    .endObject()
                    .endObject()
                    .endObject()
                    .startObject("age")
                    .field("type","integer")
                    .endObject()
                    .startObject("salary")
                    .field("type","float")
                    .endObject()
                    .startObject("birthDate")
                    .field("type","date")
                    .field("format", "yyyy-MM-dd")
                    .endObject()
                    .startObject("createTime")
                    .field("type","date")
                    .endObject()
                    .endObject()
                    .endObject();
            //创建索引配置信息
            Settings settings = Settings.builder()
                    .put("index.number_of_shards",1)
                    .put("index.number_of_replicas",0)
                    .build();
            //新建创建索引请求对象，然后设置索引类型（7不存在）和mapping与index配置
            CreateIndexRequest request = new CreateIndexRequest(Constant.INDEX,settings);
            request.mapping("doc",mapping);
            //RestHighLevelClient执行创建索引
            CreateIndexResponse response = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
            //判断是否创建成功
            boolean isCreated = response.isAcknowledged();
            log.info("是否创建成功:{}",isCreated);
        } catch (IOException e) {
            log.error("",e);
        }

    }

    /**
     * 删除索引
     */
    public void deleteIndex(){
        try {
            //新建删除索引创建请求
            DeleteIndexRequest request = new DeleteIndexRequest(Constant.INDEX);
            //执行索引删除
            AcknowledgedResponse response = restHighLevelClient.indices().delete(request,RequestOptions.DEFAULT);
            //判断是否删除成功
            boolean isDeleted = response.isAcknowledged();
            log.info("是否删除成功:{}",isDeleted);
        } catch (IOException e) {
            log.error("",e);
        }
    }
}
