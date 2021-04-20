package com.unityTest.testrunner.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 *  Models a code file uploaded by a user
 */
@Data
@NoArgsConstructor
public class SourceFile {
    // Name of source file
    @Column(name = "FILE_NAME")
    private String fileName;

    // Size of source file
    @Column(name = "FILE_SIZE")
    private long fileSize;

    @Column(name = "AUTHOR_ID")
    private String authorId;

    @Column(name = "CONTENT")
    private byte[] content;

    public SourceFile(String fileName, long fileSize, String authorId, byte[] content) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.authorId = authorId;
        this.content = content;
    }
}