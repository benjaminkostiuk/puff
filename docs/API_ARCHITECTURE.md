
# Endpoints
This document is for outlining ideas about the api structure for unity-test.

# Course-Management Endpoints
Course management microservice endpoints

## Course
**POST** `/course`: Create or update a course

body={Class}

**GET** `/course`: Get courses

?id={course id}
?code={course code}
?level={course level}
?term={course term}
?academicYear={course academic year}

**DELETE** `/course/{courseId}`: Delete a course

**POST** `/course/{courseId}/attr`: Create or update a course attribute

body={CourseAttribute}

**GET** `course/{courseId}/attr`: Get attriibute(s) for a course

?id={course attribute id}
?name={course attribute name}

**DELETE** `course/attr/{courseAttributeId}`: Delete a course attribute

## Assignments
**POST** `/assignment`: Create an assignment

body={Assignment}

**GET** `/assignment`: Get assignments

?id={assignment_id}
?name={assignment_name}
?due_date={assignment_due_date}
?code={class_code}
?term={class_term}

**DELETE** `/assignment/{assignmentId}`: Delete an assignment

**POST** `/assignment/{assignmentId}/attr`: Create an assignment attribute

body={AssignmentAttribute}

**GET** `/assignment/{assignmentId}/attr`: Get attributes for an 
assignment

?id={assignment_attribute_id}
?name={assignment_attribute_name}

**DELETE** `/assignment/attr/{assignmentAttributeId}`: Delete an assignment attribute

## Engagement

### Vote
**POST** `/engagement/{source}/{id}/vote`: Vote on a source item

body={Vote}

**DELETE** `/engagement/{source}/{id}/vote`: Remove a vote for a source item

### Comment
**POST** `/engagement/{source}/{id}/comment`: Comment on a source item

body={Comment}

**GET** `/engagement/{source}/{id}/comment`: Get comments on a source item

?id={commentId}
?author={authorId}

**PUT** `/engagement/{source}/{id}/comment`: Update a comment on a source item (Optional)

body={Comment}

**DELETE** `/engagement/{source}/{id}/comment/{commentId}`: Delete a comment on a source item (Restricted)

?id={comment_id}

### Stats
**GET** `/engagement/{source}/{id}/stats`: Get the statistics of a source item

## User
**GET** `/user/assignments`: Get the user's assignments

?id={assignmentId}
?name={assignmentName}
?dueDate={assignmentDueDate}
?code={classCode}
?term={classTerm}
?academicYear={academicYear}
?pinned={true | false}

**POST** `/user/assignment/{assignmentId}/add`: Add an assignment to the user's workload

**PUT** `/user/assignment/{assignmentId}/add`: Update the preferences of the user's assigned assignment

**GET** `/user/engagement/{source}/{id}/vote`: Get the user's vote on a source item

# Test Runner Endpoints

## Test Suite
**POST** `/suite`: Create a test suite for an assignment

body={Suite}

**GET** `/suite`: Get test suites

?assignmentId={assignment_id}
?name={suite name}
?lang={programming language}

**DELETE** `/suite/{suiteId}`: Delete a test suite (Restricted)

**POST** `/suite/run`: Run all test cases in test suite

?id={test suite id}

## Test Case
**POST** `/case`: Create a test case for a test suite

body={Case}

**GET** `/case`: Retrieve a pageable view of test cases

?suiteId={test suite id}
?lang={Programming language}

**PUT** `/case`: Update a test case (if you are the author or ADMIN)

body={Case}

Updateable fields
* code
* description

**DELETE** `/case/{caseId}`: Delete a test case (if you are the author or ADMIN)

**POST**: `/case/compile`: Compile test case code in its language

body={Case}

**POST**: `/case/run`: Run test case(s) on a source code submission

?ids={List of case ids to run}
?submissionId={Id of submission}e]

## Submission

**POST** `/upload`: Upload source files as part of a submission 

## User
**GET** `/user/cases`: Get test cases written by a user

**GET** `/user/uploads`: Get source code submissions uploaded by the user
