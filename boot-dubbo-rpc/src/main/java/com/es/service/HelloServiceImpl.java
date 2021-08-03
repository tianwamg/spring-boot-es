package com.es.service;

import com.es.api.facade.HelloService;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService(interfaceClass = HelloService.class)
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello() {
        return "rpc001....say....hello";
    }
}
