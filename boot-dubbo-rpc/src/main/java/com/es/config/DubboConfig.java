package com.es.config;

import org.apache.dubbo.config.ProtocolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DubboConfig {

    @Value("${server.port}")
    private Integer port;

    @Bean
    public ProtocolConfig jsonRpc(){
        return new ProtocolConfig("jsonrpc",port);
    }
}
