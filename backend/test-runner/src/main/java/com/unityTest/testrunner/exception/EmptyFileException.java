package com.unityTest.testrunner.exception;

/**
 * Models the exception thrown when attempting to upload a file with no contents.
 */
public class EmptyFileException extends RuntimeException {

    public EmptyFileException(String filename) {
        super(String.format("Cannot save empty file %s.", filename));
    }
}
