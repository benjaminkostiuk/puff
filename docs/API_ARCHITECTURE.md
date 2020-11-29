<!-- This document is for outlining ideas about the api structure for unity-test -->
# Endpoints

## User
**POST** `/user/{user_id}/class/{class_id}/enroll`: Enroll a user in a class

**PUT** `/user/{user_id}/class/{class_id}/enroll`: Update a user's enrollment for a class

body={class_enrollment}

**GET** `/user/{user_id}/class`: Get a user's classes

?id={class_id}
?code={class_code}
?level={class_level}
?term={class_term}
?year={class_academic_year}
?pinned={true | false}

**POST** `/user/{user_id}/assignments/{assignment_id}/enroll`: Assign an assignment to a user

**PUT** `/user/{user_id}/assignment/{assignment_id}/enroll`: Update a user's enrollment for an assignment

body={assignment_enrollment}

**GET** `/user/{user_id}/assignments`: Get a user's assignments

?id={assignment_id}
?name={assignment_name}
?due_date={assignment_due_date}
?code={class_code}
?term={class_term}
?pinned={true | false}

**GET** `/user/{user_id}/testcase`: Get test cases written by a user

?id={test_case_id}
?assignment={assignment_id}
?assignment_name={assignment_name}

**GET** `/user/{user_id}/testresults`: Get test results ran by a user

## Class
**POST** `/class`: Create a class

body={Class}

**GET** `/class`: Get classes

?id={class_id}
?code={class_code}
?level={class_level}
?term={class_term}
?year={class_academic_year}

**POST** `/class/{class_id}/attr`: Create a class attribute

body={ClassAttribute}

**GET** `class/{class_id}/attr`: Get class attribute(s)

?id={class_attribute_id}
?name={class_attribute_name}

## Assignments
**POST** `/assignment`: Create an assignment

body={Assignment}

**GET** `/assignment`: Get assignments

?id={assignment_id}
?name={assignment_name}
?due_date={assignment_due_date}
?code={class_code}
?term={class_term}

**POST** `/assignment/{assignment_id}/attr`: Create an assignment attribute

body={AssignmentAttribute}

**GET** `/assignment/{assignment_id}/attr`: Get attributes for an 
assignment

?id={assignment_attribute_id}
?name={assignment_attribute_name}

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

## Engagement

### Vote
**POST** `/source/{source_id}/vote`: Vote on a source

body={Vote}

**GET** `/source/{source_id}/vote`: Get the votes for a source

?id={vote_id}
?action={VoteAction}
?author={id}

**PUT** `/source/{source_id}/vote`: Update vote for a source

body={Vote}

**DELETE** `/source/{source_id}/vote`: Remove a vote from a source

?id={vote_id}

### Comment
**POST** `/source/{source_id}/comment`: Comment on a source

body={Comment}

**GET** `/source/{source_id}/comment`: Get comment for a source

?id={comment_id}

**DELETE** `/source/{source_id}/comment`: Delete a comment on a source

?id={comment_id}