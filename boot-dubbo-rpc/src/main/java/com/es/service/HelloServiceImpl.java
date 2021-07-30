package com.es.service;

import com.es.api.facade.HelloService;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService(interfaceClass = HelloService.class,protocol = "jsonrpc",version = "1.0")
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello() {
        return "rpc001....say....hello";
    }
}
