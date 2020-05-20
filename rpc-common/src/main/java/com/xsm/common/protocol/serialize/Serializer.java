package com.xsm.common.protocol.serialize;

import java.io.IOException;

/**
 * @author xsm
 * @Date 2020/5/20 22:08
 * 序列化接口
 */
public interface Serializer {

    /**
     * java 对象转换为二进制
     * @return
     * @throws IOException
     */
    byte[] serialize(Object object) throws IOException;

    /**
     * 二进制转换成java对象
     * @param clazz
     * @param bytes
     * @param <T>
     * @return
     * @throws IOException
     */
    <T> T deserialize(Class<T> clazz, byte[] bytes) throws IOException;

}
