package cn.simple.coder.serializer;

/**
 * Created by huapeng.hhp on 2018/5/4.
 */
public interface Serializer {

    byte[] serialize(Object object);

    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
