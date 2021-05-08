package com.unityTest.testrunner.exception;

public class NoSuiteFileException extends RuntimeException {
    public NoSuiteFileException(int suiteId) {
        super(String.format("Could not find suite file for suite with id %d.", suiteId));
    }
}
