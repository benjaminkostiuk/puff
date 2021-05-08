package com.unityTest.testrunner.exception;

import com.unityTest.testrunner.models.PLanguage;

public class ProgrammingLanguageMismatch extends RuntimeException {
    public ProgrammingLanguageMismatch(PLanguage expected, PLanguage actual) {
        super(String.format("Could not match expected language %s with actual language %s.", expected, actual));
    }
}
