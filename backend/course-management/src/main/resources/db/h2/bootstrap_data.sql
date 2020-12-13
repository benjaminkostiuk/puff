INSERT INTO COURSE (ID, CODE, LEVEL, TERM, ACADEMIC_YEAR)
VALUES
(1, 'COMPSCI 1JC3', 1, 'FALL', 2019),
(2, 'COMPSCI 1MD3', 1, 'WINTER', 2019),
(3, 'COMPSCI 1XA3', 1, 'WINTER', 2019),

(4, 'COMPSCI 1JC3', 1, 'FALL', 2020),
(5, 'COMPSCI 1MD3', 1, 'FALL', 2020),
(6, 'COMPSCI 1DM3', 1, 'WINTER', 2020),
(7, 'COMPSCI 1XC3', 1, 'WINTER', 2020);

INSERT INTO COURSE_ATTR (ID, COURSE_ID, ATTR_NAME, ATTR_VALUE)
VALUES
(1, 1, 'PROFESSOR_NAME', 'William M. Farmer'),
(2, 1, 'TITLE', 'Introduction to Computational Thinking'),

(3, 2, 'PROFESSOR_NAME', 'George Fizherbert'),
(4, 2, 'TITLE', 'Introduction to Programming'),

(5, 3, 'PROFESSOR_NAME', 'Curtis D''Alves'),
(6, 3, 'TITLE', 'Practice and Expertise');

