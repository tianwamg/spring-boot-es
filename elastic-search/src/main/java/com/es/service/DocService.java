package com.es.service;

import com.alibaba.fastjson.JSON;
import com.es.domain.UserInfo;
import com.es.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

@Slf4j
@Service
public class DocService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 增加文档信息
     */
    public void addDocument(){
        try {
            IndexRequest indexRequest = new IndexRequest(Constant.INDEX, Constant.Doc, "1");
            //IndexRequest indexRequest = new IndexRequest(Constant.INDEX);
            UserInfo userInfo = UserInfo.builder()
                    .name("张三")
                    .age(29)
                    .salary(100.00f)
                    .address("上海")
                    .remark("来自大山深处的老王")
                    .createTime(new Date())
                    .birthDate("2021-01-01")
                    .build();
            //将对象转换为byte数组
            byte[] json = JSON.toJSONBytes(userInfo);
            //设置文档内容
            indexRequest.source(json, XContentType.JSON);
            //执行增加文档
            IndexResponse response = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            log.info("创建文档状态：{}",response.status());
        }catch (Exception e){
            log.error("",e);
        }
    }

    /**
     * 获取文档信息
     */
    public void geDocument(){
        try {
            //获取请求对象
            GetRequest request = new GetRequest(Constant.INDEX,Constant.Doc,"1");
            //获取文档信息
            GetResponse response = restHighLevelClient.get(request,RequestOptions.DEFAULT);
            //json转换对象
            if(response.isExists()){
                UserInfo userInfo = JSON.parseObject(response.getSourceAsBytes(),UserInfo.class);
                log.info("人员信息:{}",userInfo);
            }
        } catch (IOException e) {
            log.error("",e);
        }
    }

    /**
     * 更新文档信息
     */
    public void updateDocument(){
        try{
            UpdateRequest request = new UpdateRequest(Constant.INDEX,Constant.Doc,"1");
            //设置人员更新信息
            UserInfo userInfo = UserInfo.builder()
                    .name("老王")
                    .salary(200.01f)
                    .build();
            byte[] json = JSON.toJSONBytes(userInfo);
            request.doc(json,XContentType.JSON);
            UpdateResponse response = restHighLevelClient.update(request,RequestOptions.DEFAULT);
            log.info("更新文档状态:{}",response.status());
        } catch (IOException e) {
            log.error("",e);
        }
    }

    /**
     * 删除文档信息
     */
    public void deleteDocument(){
        try{
            DeleteRequest request = new DeleteRequest(Constant.INDEX,Constant.Doc,"1");
            DeleteResponse response = restHighLevelClient.delete(request,RequestOptions.DEFAULT);
            log.info("删除状态:{}",response.status());
        } catch (IOException e) {
            log.error("",e);
        }
    }
}
