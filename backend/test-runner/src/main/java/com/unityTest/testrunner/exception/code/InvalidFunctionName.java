package com.unityTest.testrunner.exception.code;

public class InvalidFunctionName extends RuntimeException {
    public InvalidFunctionName(String functionName, String reason) {
        super(String.format("Invalid function name %s. %s", functionName, reason));
    }
}
