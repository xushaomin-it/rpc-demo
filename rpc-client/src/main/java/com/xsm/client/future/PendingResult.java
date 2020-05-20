package com.xsm.client.future;

import com.xsm.common.protocol.RpcResponse;

import javax.xml.ws.soap.Addressing;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author xsm
 * @Date 2020/5/20 23:31
 */
public class PendingResult {

    private Map<String, ResultFuture> map = new ConcurrentHashMap<>();

    public void add(String id, ResultFuture future){
        this.map.put(id, future);
    }

    public void set(String id, RpcResponse response){
        ResultFuture resultFuture = this.map.get(id);
        if (resultFuture != null) {
            resultFuture.setSuccess(response);
            this.map.remove(id);
        }

    }


}
