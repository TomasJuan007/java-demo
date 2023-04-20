package com.example.cache.common.utils;

import com.esotericsoftware.kryo.Kryo;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;

public class KryoUtils {

    public static <T> T copy(T source, ParameterizedType type) {
        Kryo kryo = commonKryo();
        Type[] typeArguments = type.getActualTypeArguments();
        for (Type typeArgument : typeArguments) {
            Class<?> typeArgClass = (Class<?>) typeArgument;
            kryo.register(typeArgClass);
        }
        return kryo.copy(source);
    }

    public static <T> T copy(T source) {
        Kryo kryo = commonKryo();
        return kryo.copy(source);
    }

    private static Kryo commonKryo() {
        Kryo kryo = new Kryo();
        kryo.register(ArrayList.class);
        kryo.register(HashSet.class);
        return kryo;
    }
}
