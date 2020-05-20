package com.xsm.client;

import com.xsm.client.proxy.ProxyFactory;
import com.xsm.common.service.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author xsm
 * @Date 2020/5/20 23:12
 */
@SpringBootApplication
@Slf4j
public class ClientApplication {
    public static void main(String[] args) throws Exception{
        SpringApplication.run(ClientApplication.class, args);
//        ConfigurableApplicationContext context = SpringApplication.run(ClientApplication.class, args);
//        HelloService helloService = context.getBean(HelloService.class);
        HelloService helloService = ProxyFactory.create(HelloService.class);
        log.info("响应结果“: {}",helloService.hello("pjmike"));
    }
}
