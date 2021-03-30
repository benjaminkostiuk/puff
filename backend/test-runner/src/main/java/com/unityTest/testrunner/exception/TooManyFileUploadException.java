package com.unityTest.testrunner.exception;

public class TooManyFileUploadException extends RuntimeException {

    public TooManyFileUploadException(int numberOfFiles) {
        super(String.format("Maximum number of files uploadable exceeded. Cannot upload %d files at once.", numberOfFiles));
    }
}
