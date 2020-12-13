
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
**POST** `/engagement/source/{sourceId}/vote`: Vote on a source

body={Vote}

**GET** `/engagement/source/{sourceId}/vote`: Get the votes for a source

?id={vote_id}
?action={VoteAction}
?author={id}

**DELETE** `/engagement/source/{sourceId}/vote`: Remove a vote from a source

?id={vote_id}

### Comment
**POST** `/engagement/source/{sourceId}/comment`: Comment on a source

body={Comment}

**GET** `/engagement/source/{sourceId}/comment`: Get comment for a source

?id={comment_id}

**DELETE** `/engagement/source/{sourceId}/comment`: Delete a comment on a source

?id={comment_id}

## User
**GET** `/user/{user_id}/assignments`: Get a user's assignments

?id={assignmentId}
?name={assignmentName}
?dueDate={assignmentDueDate}
?code={classCode}
?term={classTerm}
?academicYear={academicYear}
?pinned={true | false}

**POST** `/user/assignment/{assignmentId}/add`: Add an assignment to a user's workload

**PUT** `/user/assignment/{assignmentId}/add`: Update the preferences of a user's assigned assignment

# Test Runner Endpoints
TBD

## Testing
**POST** `/test/assignment/{assignment_id}/case`: Create a test case for an assignment

body={TestCase}

**GET** `/test/assignment/{assignment_id}/case`: Get test cases for an assignment

?id={assignment_id}
?voteScore={vote_score}

**PUT** `/test/assignment/{assignment_id}/case`: Update a test case for an assignment

body={TestCase}

**DELETE** `/test/assignment/{assignment_id}/case`: Delete a test case for an assignment

**POST** `/test/case/{case_id}/run`: Run a test case 

body={Some way to pass assignment code}
return={TestResult}

**POST** `/compile/codeblock`: Compile a code block

body={CodeBlock}

## User
**GET** `/user/{user_id}/testcase`: Get test cases written by a user

?id={test_case_id}
?assignment={assignment_id}
?assignment_name={assignment_name}

**GET** `/user/{user_id}/testresults`: Get test results ran by a user