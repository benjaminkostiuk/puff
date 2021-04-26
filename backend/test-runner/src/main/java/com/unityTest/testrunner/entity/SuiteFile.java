package com.unityTest.testrunner.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Models a testing code file uploaded to be the base of a test suite
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "SUITE_FILE")
public class SuiteFile extends SourceFile {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "sequence-generator")
    @GenericGenerator(
            name = "sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "SUITE_FILE_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1000"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private int id;

    // Associated test suite's id
    @Column(name = "SUITE_ID")
    @NotNull
    private Integer suiteId;

    public SuiteFile(int id, int suiteId, String fileName, long fileSize, String authorId, byte[] content) {
        super(fileName, fileSize, authorId, content);
        this.id = id;
        this.suiteId = suiteId;
    }

    public SuiteFile(int suiteId, String fileName, long fileSize, String authorId, byte[] content) {
        this(0, suiteId, fileName, fileSize, authorId, content);
    }
}
