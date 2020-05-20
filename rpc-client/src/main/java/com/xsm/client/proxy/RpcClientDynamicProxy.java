package com.xsm.client.proxy;

import com.xsm.client.netty.NettyClient;
import com.xsm.common.protocol.RpcRequest;
import com.xsm.common.protocol.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.UUID;


/**
 * @author xsm
 * @Date 2020/5/20 23:15
 */
@Slf4j
public class RpcClientDynamicProxy<T> implements InvocationHandler {

    private Class<T> clazz;

    public RpcClientDynamicProxy(Class<T> clazz) throws Exception {
        this.clazz = clazz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String requestId = UUID.randomUUID().toString();
        String className = method.getDeclaringClass().getName();
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setRequestId(requestId);
        rpcRequest.setClassName(className);
        rpcRequest.setMethodName(methodName);
        rpcRequest.setParameterTypes(parameterTypes);
        rpcRequest.setParameters(args);
        log.info("请求内容: {}", rpcRequest);
        NettyClient nettyClient = new NettyClient("127.0.0.1", 8888);
        log.info("开始连接服务端: {}", new Date());
        nettyClient.connect();
        RpcResponse send = nettyClient.send(rpcRequest);
        log.info("请求调用返回结果: {}", send.getResult());
        return send.getResult();

    }
}
