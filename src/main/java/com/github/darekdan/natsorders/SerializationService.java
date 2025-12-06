package com.github.darekdan.natsorders;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Service
public class SerializationService {

    private final KryoPool kryoPool;

    public SerializationService(KryoPool kryoPool) {
        this.kryoPool = kryoPool;
    }

    public byte[] serialize(Object object) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Output output = new Output(baos);
        Kryo kryo = kryoPool.get();
        kryo.writeObject(output, object);
        output.close();
        return baos.toByteArray();
    }

    public <T> T deserialize(byte[] data, Class<T> type) {
        Input input = new Input(new ByteArrayInputStream(data));
        Kryo kryo = kryoPool.get();
        T object = kryo.readObject(input, type);
        input.close();
        return object;
    }
}
