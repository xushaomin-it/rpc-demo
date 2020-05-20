package com.xsm.server;

import com.xsm.common.service.HelloService;
import org.springframework.stereotype.Service;

/**
 * @author xsm
 * @Date 2020/5/20 22:47
 */
@Service
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        return "hello, " + name;
     }
}
