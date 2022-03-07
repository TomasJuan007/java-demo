package com.example.cache.common.invoke.invocation;

import java.lang.reflect.Method;

public interface InvocationRegistry {

    void registerInvocation(Object invokeBean, Method invokeMethod, Object[] invocationArguments, String cacheName);
}
