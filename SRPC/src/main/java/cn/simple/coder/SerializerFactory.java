package cn.simple.coder;

import cn.simple.coder.serializer.FastJsonSerializer;
import cn.simple.coder.serializer.Serializer;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * Created by huapeng.hhp on 2018/5/4.
 */
public class SerializerFactory {
	/**
	 * SPI
	 *
	 * @return
	 */
	public static Serializer load() {
		ServiceLoader<Serializer> serviceLoader = ServiceLoader.load(Serializer.class);
		Iterator<Serializer> iterator = serviceLoader.iterator();
		while (iterator.hasNext()) {
			return iterator.next();
		}
		return new FastJsonSerializer();
	}
}
