package com.unityTest.testrunner.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 *  Models a code file uploaded by a user
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class SourceFile {
    // Name of source file
    @Column(name = "FILE_NAME")
    private String fileName;

    // Size of source file
    @Column(name = "FILE_SIZE")
    private long fileSize;

    @Column(name = "AUTHOR_ID")
    private String authorId;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "CONTENT")
    private byte[] content;
}