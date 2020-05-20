package com.xsm.common.registry;

/**
 * @author xsm
 * @Date 2020/5/20 22:38
 * 服务注册
 */
public interface ServiceRegistry {
    /**
     * 服务注册
     * @param data
     * @throws Exception
     */
    void registry(String data) throws Exception;
}

