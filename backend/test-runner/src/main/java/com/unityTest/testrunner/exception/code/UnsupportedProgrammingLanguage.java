package com.unityTest.testrunner.exception.code;

import com.unityTest.testrunner.models.PLanguage;

public class UnsupportedProgrammingLanguage extends RuntimeException {
    public UnsupportedProgrammingLanguage(PLanguage pLanguage) {
        super(String.format("programming language %s is not supported."));
    }
}
