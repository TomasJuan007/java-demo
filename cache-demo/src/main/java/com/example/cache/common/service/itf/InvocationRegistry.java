package com.example.cache.common.service.itf;

import java.lang.reflect.Method;

public interface InvocationRegistry {

    void registerInvocation(Object invokeBean, Method invokeMethod, Object[] invocationArguments, String cacheName);
}
