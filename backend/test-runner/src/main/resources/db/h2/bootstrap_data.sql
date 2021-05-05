INSERT INTO TEST_SUITE(ID, ASSIGNMENT_ID, NAME, LANG, UPVOTE_COUNT, AUTHOR_ID)
VALUES
(1, 1000, 'TEST 1', 'JAVA', (-2), 'X01'),
(2, 1000, 'TEST 2', 'PYTHON3', 5, 'X02'),
(3, 1001, 'TEST 3', 'HASKELL', 7, 'X03');

INSERT INTO TEST_CASE(ID, SUITE_ID, AUTHOR_ID, FUNCTION_NAME, DESCRIPTION, UPVOTE_COUNT, RUN_COUNT, PASS_COUNT, LANG, CODE)
VALUES
-- PART OF TEST SUITE 1 (JAVA)
(1, 1, 'X01', 'test_func_a', 'tests function a', 5, 10, 8, 'JAVA',
    CONCAT('def test_func_a(self):', CHAR(10), CHAR(9), '', CHAR(10))),
(2, 1, 'X02', 'test_func_b', 'tests function b', 20, 25, 25, 'JAVA',
    CONCAT('def test_func_b(self):', CHAR(10), CHAR(9), 'System.out.println("Testing");', CHAR(10))),
(3, 1, 'X05', 'test_func_c', 'tests function c', -6, 40, 0, 'JAVA',
    CONCAT('def test_func_b(self):', CHAR(10), CHAR(9), 'assertEquals(2, 2);', CHAR(10))),

-- PART OF TEST SUITE 3
(4, 3, 'X01', 'test_func_a', 'tests function a', 5, 10, 8, 'HASKELL',
    CONCAT('def test_func_a(self):', CHAR(10), CHAR(9), 'error "Unimplemented test"', CHAR(10)));


-- UNCOMMENT TO RUN TESTS FOR TEST SUITE 2

--SET @USER_ID = 'USER ID HERE';
--
---- CREATE SUBMISSION FOR FILES
--INSERT INTO SUBMISSION(ID, ASSIGNMENT_ID, AUTHOR_ID)
--VALUES
--(1, 1000, @USER_ID);
--
---- CREATE SUBMISSION FILES
--INSERT INTO SUBMISSION_FILE(SUBMISSION_ID, FILE_NAME, FILE_SIZE, AUTHOR_ID, CONTENT)
--VALUES
--(1, 'ChemEntity.py', 813, @USER_ID, FILE_READ('classpath:/sample/code/ChemEntity.py')),
--(1, 'ChemTypes.py', 2108, @USER_ID, FILE_READ('classpath:/sample/code/ChemTypes.py')),
--(1, 'CompoundT.py', 1780, @USER_ID, FILE_READ('classpath:/sample/code/CompoundT.py')),
--(1, 'ElmSet.py', 230, @USER_ID, FILE_READ('classpath:/sample/code/ElmSet.py')),
--(1, 'Equality.py', 448, @USER_ID, FILE_READ('classpath:/sample/code/Equality.py')),
--(1, 'MolecSet.py', 236, @USER_ID, FILE_READ('classpath:/sample/code/MolecSet.py')),
--(1, 'MoleculeT.py', 1979, @USER_ID, FILE_READ('classpath:/sample/code/MoleculeT.py')),
--(1, 'ReactionT.py', 5817, @USER_ID, FILE_READ('classpath:/sample/code/ReactionT.py')),
--(1, 'Set.py', 2092, @USER_ID, FILE_READ('classpath:/sample/code/Set.py'));
--
---- CREATE SUITE FILE
--INSERT INTO SUITE_FILE(SUITE_ID, FILE_NAME, FILE_SIZE, AUTHOR_ID, CONTENT)
--VALUES
--(2, 'test_set.py', 738, @USER_ID, FILE_READ('classpath:/sample/test_set.py'));
--
---- CREATE TEST CASES TO RUN
---- CHAR(10) is \n, CHAR(9) is \t
--INSERT INTO TEST_CASE(ID, SUITE_ID, AUTHOR_ID, FUNCTION_NAME, DESCRIPTION, UPVOTE_COUNT, RUN_COUNT, PASS_COUNT, LANG, CODE)
--VALUES
--(100, 2, 'X03', 'member_empty', 'tests if member is empty', 100, 10, 7, 'PYTHON3',
--    CONCAT('def test_member_empty(self):',CHAR(10), CHAR(9), 'assert not self.emptySet.member(ElementT.F)', CHAR(10))),
--
--(101, 2, 'X03', 'member_contains', 'tests if member contains an element', 200, 200, 42, 'PYTHON3',
--    CONCAT('def test_member_contains(self):', CHAR(10), CHAR(9), 'assert self.elmSet.member(ElementT.H)', CHAR(10)));