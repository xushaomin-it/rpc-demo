package com.xsm.client.proxy;

import java.lang.reflect.Proxy;

/**
 * @author xsm
 * @Date 2020/5/20 23:13
 */

public class ProxyFactory {

    public static <T> T create(Class<T> interfaceClass) throws Exception{
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[] {interfaceClass}, new RpcClientDynamicProxy<T>(interfaceClass));
    }
}
