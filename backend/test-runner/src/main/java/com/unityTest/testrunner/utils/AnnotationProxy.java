package com.unityTest.testrunner.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Accessors(fluent = true)
public class AnnotationProxy implements Annotation, InvocationHandler {
    @Getter
    private final Class<? extends Annotation> annotationType;
    private final Map<String, Object> values;

    public static <A extends Annotation> A of(Class<A> annotation, Map<String, Object> values) {
        return (A) Proxy.newProxyInstance(
            annotation.getClassLoader(),
            new Class[]{annotation},
            new AnnotationProxy(
                annotation,
                new HashMap<String, Object>(values) {{
                    put("annotationType", annotation); // Required because getDefaultValue() returns null for this call
                }}
            )
        );
    }

    public Object invoke(Object proxy, Method method, Object[] args) {
        return values.getOrDefault(method.getName(), method.getDefaultValue());
    }
}