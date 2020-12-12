package com.unityTest.courseManagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Models the enrollment of a user in a course
 */
@Data
@ApiModel(value = "AssignmentEnrollment", description = "Enrollment of a user in a assignment")
@Entity
@Table(name = "ASSIGNMENT_ENROLLMENT")
public class AssignmentEnrollment {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "sequence-generator")
    @GenericGenerator(
            name = "sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "ASSIGNMENT_ENROLLMENT_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1000"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private int id;

    // Assignment user is enrolled in
//    @ApiModelProperty(value = "Enrolled assignment")
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "ASSIGNMENT_ID", referencedColumnName = "ID")
//    private Assignment course;

    // User id
    @JsonIgnore
    @Column(name = "USER_ID")
    private String userId;

    // True if user has pinned the course
    @ApiModelProperty(value = "True if assignment is pinned for user", required = true, example = "false")
    @Column(name = "PINNED")
    private boolean isPinned = false;
}
