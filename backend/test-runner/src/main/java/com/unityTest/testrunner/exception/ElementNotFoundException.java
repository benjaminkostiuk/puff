package com.unityTest.testrunner.exception;

import com.unityTest.testrunner.utils.Utils;
import org.springframework.util.StringUtils;

import java.util.Map;

public class ElementNotFoundException extends RuntimeException {

    public ElementNotFoundException(Class clazz, Object... queryParamsMap) {
        super(ElementNotFoundException.generateMessage(clazz.getSimpleName(), Utils.toMap(String.class, String.class, queryParamsMap)));
    }

    private static String generateMessage(String entity, Map<String, String> searchParams) {
        return String.format("Could not find %s for query parameters %s", StringUtils.capitalize(entity), searchParams);
    }
}
