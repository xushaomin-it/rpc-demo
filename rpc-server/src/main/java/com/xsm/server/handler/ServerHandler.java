package com.xsm.server.handler;

import com.sun.org.apache.regexp.internal.RE;
import com.xsm.common.protocol.RpcRequest;
import com.xsm.common.protocol.RpcResponse;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

/**
 * @author xsm
 * @Date 2020/5/20 22:47
 */
@Component
@Slf4j
@ChannelHandler.Sharable
public class ServerHandler extends SimpleChannelInboundHandler<RpcRequest> implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    /**
     * 处理netty读事件
     * @param channelHandlerContext
     * @param rpcRequest
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest msg) throws Exception {
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setRequestId(msg.getRequestId());
        try {
            Object handler = handler(msg);
            log.info("获取返回结果: {}", handler);
            rpcResponse.setResult(handler);
        } catch (Exception e) {
            log.error("error", e);
            rpcResponse.setError(e.toString());
        }

        channelHandlerContext.writeAndFlush(rpcResponse);

    }

    /**
     * 服务端使用代理处理请求
     * @param msg
     * @return
     */
    private Object handler(RpcRequest request) throws ClassNotFoundException, InvocationTargetException {
        // 加载Class文件
        Class<?> clazz = Class.forName(request.getClassName());
        Object serviceBean = applicationContext.getBean(clazz);
        log.info("serviceBean: {}", serviceBean);
        Class<?> serviceBeanClass = serviceBean.getClass();
        log.info("serviceBeanClass: {}", serviceBeanClass);
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();
        // 使用CGLIB Reflect
        FastClass fastClass = FastClass.create(serviceBeanClass);
        FastMethod fastMethod = fastClass.getMethod(methodName, parameterTypes);
        log.info("开始调用CGLIB动态代理执行服务端方法...");
        return fastMethod.invoke(serviceBean, parameters);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
