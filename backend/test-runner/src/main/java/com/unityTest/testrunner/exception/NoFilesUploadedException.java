package com.unityTest.testrunner.exception;

/**
 * Models the exception thrown when attempting to upload files without passing any files.
 */
public class NoFilesUploadedException extends RuntimeException {
    public NoFilesUploadedException() {
        super("Failed to find files to upload. Cannot save files.");
    }
}
