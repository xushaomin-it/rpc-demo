package com.xsm.client.netty;

import com.xsm.client.future.DefaultFuture;
import com.xsm.common.protocol.RpcRequest;
import com.xsm.common.protocol.RpcResponse;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xsm
 * @Date 2020/5/20 23:35
 */
public class ClientHandler extends ChannelDuplexHandler {

    private final Map<String, DefaultFuture>  futureMap = new ConcurrentHashMap<>();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof RpcResponse) {
            // 获取响应对象
            RpcResponse response = (RpcResponse) msg;
            DefaultFuture defaultFuture = futureMap.get(response.getRequestId());
            defaultFuture.setRpcResponse(response);
        }
        super.channelRead(ctx, msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof RpcRequest) {
            RpcRequest request = (RpcRequest) msg;
            // 发送之前先把请求ID保存下来, 并购一个与响应future的映射关系
            futureMap.putIfAbsent(request.getRequestId(), new DefaultFuture());
        }
        super.write(ctx, msg, promise);
    }

    public RpcResponse getRpcResponse(String requestId){
        try {
            DefaultFuture future = futureMap.get(requestId);
            return future.getRpcResponse(10);
        } finally {
            // 获取成功以后, 从map中移除
            futureMap.remove(requestId);
        }
    }
}
