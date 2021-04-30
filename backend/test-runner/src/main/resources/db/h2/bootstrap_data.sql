INSERT INTO TEST_SUITE(ID, ASSIGNMENT_ID, NAME, LANG, UPVOTE_COUNT, AUTHOR_ID)
VALUES
(1, 1000, 'TEST 1', 'JAVA', (-2), 'X01'),
(2, 1000, 'TEST 2', 'PYTHON3', 5, 'X02'),
(3, 1001, 'TEST 3', 'HASKELL', 7, 'X03');

INSERT INTO TEST_CASE(ID, SUITE_ID, AUTHOR_ID, FUNCTION_NAME, DESCRIPTION, UPVOTE_COUNT, RUN_COUNT, PASS_COUNT, LANG, CODE)
VALUES
-- PART OF TEST SUITE 3
(1, 2, 'X01', 'test_func_a', 'tests function a', 5, 10, 8, 'PYTHON3', 'def test_func_a(self):\n\tpass\n'),
(2, 2, 'X02', 'test_func_b', 'tests function b', 20, 25, 25, 'PYTHON3', 'def test_func_b(self):\n\tassert 4 == 4\n'),
(3, 2, 'X05', 'test_func_c', 'tests function c', -6, 40, 0, 'PYTHON3', 'def test_func_b(self):\n\tassert 2 == 3\n');