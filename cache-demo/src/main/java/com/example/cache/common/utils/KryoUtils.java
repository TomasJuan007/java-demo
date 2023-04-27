package com.example.cache.common.utils;

import com.esotericsoftware.kryo.Kryo;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class KryoUtils {

    public static <T> T copy(T source, Type type) {
        Kryo kryo = commonKryo();
        Set<Class<?>> classSet = new HashSet<>();
        getClassesForType(type, classSet);
        for (Class<?> c : classSet) {
            kryo.register(c);
        }
        return kryo.copy(source);
    }

    private static Kryo commonKryo() {
        Kryo kryo = new Kryo();
        // 方法返回值类型为实际类型父类，无法通过反射获取到实际类型
        kryo.register(ArrayList.class);
        kryo.register(HashSet.class);
        return kryo;
    }

    private static Set<Class<?>> getClassesForType(Type type, Set<Class<?>> classSet) {
        if (type instanceof Class) {
            classSet.add((Class<?>) type);
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            classSet.add((Class<?>) rawType);
            Type[] typeArguments = parameterizedType.getActualTypeArguments();
            for (Type typeArgument : typeArguments) {
                getClassesForType(typeArgument, classSet);
            }
        }
        return classSet;
    }
}
