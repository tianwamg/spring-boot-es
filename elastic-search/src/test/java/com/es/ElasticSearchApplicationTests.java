package com.es;

import com.es.service.DocService;
import com.es.service.IndexService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticSearchApplicationTests {

    @Test
    public void contextLoads() {
        System.out.println(123);
    }

    @Autowired
    IndexService indexService;

    @Test
    public void indexTest(){
        indexService.createIndex();
    }

    @Autowired
    DocService docService;

    @Test
    public void docTest(){
        //docService.addDocument();
        docService.geDocument();
    }

}
