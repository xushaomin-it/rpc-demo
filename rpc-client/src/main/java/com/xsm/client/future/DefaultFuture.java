package com.xsm.client.future;

import com.xsm.common.protocol.RpcResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xsm
 * @Date 2020/5/20 23:27
 * 自定义实现Future
 */
@Slf4j
public class DefaultFuture {

    private RpcResponse rpcResponse;

    private volatile  boolean isSucceed = false;

    private final Object object = new Object();

    public RpcResponse getRpcResponse(int timeOut){
        synchronized (object) {
            while (!isSucceed) {
                try {
                    object.wait();
                } catch (InterruptedException e) {
                    log.error("error", e);
                }
            }
            return rpcResponse;
        }
    }

    public void setRpcResponse(RpcResponse response){
        if (isSucceed) {
            return;
        }
        synchronized (object) {
            this.rpcResponse = response;
            this.isSucceed = true;
            object.notify();
        }
    }

}
