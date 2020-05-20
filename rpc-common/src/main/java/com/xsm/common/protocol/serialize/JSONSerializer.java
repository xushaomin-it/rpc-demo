package com.xsm.common.protocol.serialize;

import com.alibaba.fastjson.JSON;

import java.io.IOException;

/**
 * @author xsm
 * @Date 2020/5/20 22:19
 * fastJson 作为序列化框架
 */
public class JSONSerializer implements Serializer {


    @Override
    public byte[] serialize(Object object) throws IOException {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) throws IOException {
        return JSON.parseObject(bytes, clazz);
    }
}
