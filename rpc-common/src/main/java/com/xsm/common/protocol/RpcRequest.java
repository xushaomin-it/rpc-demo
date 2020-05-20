package com.xsm.common.protocol;

import lombok.Data;
import lombok.ToString;

/**
 * @author xsm
 * @Date 2020/5/20 22:25
 * rpc request
 */
@Data
public class RpcRequest {

    /** 请求对象id*/
    private String requestId;

    /** 类名*/
    private String className;

    /** 方法名*/
    private String methodName;

    /** 参数类型*/
    private Class<?>[] parameterTypes;

    /** 入参*/
    private Object[] parameters;

}
