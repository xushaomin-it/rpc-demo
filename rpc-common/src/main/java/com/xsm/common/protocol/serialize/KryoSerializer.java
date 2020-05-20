package com.xsm.common.protocol.serialize;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author xsm
 * @Date 2020/5/20 22:20
 * Kryo 序列化方式
 */
public class KryoSerializer implements Serializer {

    private final static ThreadLocal<Kryo> KRYOS = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        // 支持循环引用
        kryo.setReferences(true);
        // 关闭注册行为
        kryo.setRegistrationRequired(false);
        return kryo;
    });

    @Override
    public byte[] serialize(Object object) throws IOException {
        Kryo kryo = KRYOS.get();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Output output = new Output(byteArrayOutputStream);
        kryo.writeClassAndObject(output, object);
        output.close();
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) throws IOException {
        Kryo kryo = KRYOS.get();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Input input = new Input(byteArrayInputStream);
        input.close();
        return (T) kryo.readClassAndObject(input);
    }
}
