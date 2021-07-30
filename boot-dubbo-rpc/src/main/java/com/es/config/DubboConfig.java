package com.es.config;

import org.apache.dubbo.config.ProtocolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DubboConfig {

    @Bean
    public ProtocolConfig jsonRpc(){
        return new ProtocolConfig("jsonrpc",10082);
    }
}
