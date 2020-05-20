package com.xsm.common.protocol;

import lombok.Data;

/**
 * @author xsm
 * @Date 2020/5/20 22:27
 * rpc response
 */
@Data
public class RpcResponse {

    /** 响应id*/
    private String requestId;

    /** 错误信息*/
    private String error;

    /** 返回的结果*/
    private Object result;

}
