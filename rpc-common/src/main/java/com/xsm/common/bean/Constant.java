package com.xsm.common.bean;

/**
 * @author xsm
 * @Date 2020/5/20 22:36
 */
public class Constant {
    private Constant(){}

    public static final int ZK_SESSION_TIMEOUT = 5000;
    public static final int ZK_CONNECTION_TIMEOUT = 5000;
    public static final String ZK_REGISTRY_PATH = "/registry";
    public static final String ZK_CHILDREN_PATH = ZK_REGISTRY_PATH + "/data";
}
