package com.xsm.server.netty;

import com.xsm.common.protocol.RpcDecoder;
import com.xsm.common.protocol.RpcEncoder;
import com.xsm.common.protocol.RpcRequest;
import com.xsm.common.protocol.RpcResponse;
import com.xsm.common.protocol.serialize.JSONSerializer;
import com.xsm.common.registry.zookeeper.ZkServiceRegistry;
import com.xsm.server.handler.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 * @author xsm
 * @Date 2020/5/20 22:58
 * 服务端
 */
@Component
@Slf4j
public class NettyServer implements InitializingBean {

    private EventLoopGroup boss;

    private EventLoopGroup worker;

    @Autowired
    private ServerHandler serverHandler;


    @Override
    public void afterPropertiesSet() throws Exception {
        // 注册zk
        ZkServiceRegistry registry = new ZkServiceRegistry("127.0.0.1:2181");
        start(registry);
    }

    private void start(ZkServiceRegistry registry) {
        boss = new NioEventLoopGroup();
        worker = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(65535, 0, 4));
                        pipeline.addLast(new RpcEncoder(RpcResponse.class, new JSONSerializer()));
                        pipeline.addLast(new RpcDecoder(RpcRequest.class, new JSONSerializer()));
                        pipeline.addLast(serverHandler);
                    }
                });

        bind(serverBootstrap, 8888);
    }

    /**
     * 如果端口绑定失败, 端口数 +1, 并重新绑定
     * @param serverBootstrap
     * @param port
     */
    private void bind(ServerBootstrap serverBootstrap, int port) {
        serverBootstrap.bind(port).addListener(future -> {
            if (future.isSuccess()) {
                log.info("端口: {}绑定成功", port);
            }
            else {
                log.error("端口: {}绑定失败", port);
                bind(serverBootstrap, port + 1);
            }
        });
    }

    /**
     * 销毁
     * @throws InterruptedException
     */
    @PreDestroy
    public void destory() throws InterruptedException {
        boss.shutdownGracefully().sync();
        worker.shutdownGracefully().sync();
        log.info("关闭netty");
    }
}
