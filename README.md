# UnityTest
UnityTest is an open souce platform where students can collectively write and run smoke tests on their assignments or project code for quick and easy sanity testing. Collaborators can either upload test cases or run their source code against the community-created test bank. Individual test cases can be upvoted or downvoted by collaborators to improve the overall quality of the pool. 

UnityTest currently supports projects written in Haskell, Python and Java with plans to support C, C++ and MySQL.

## Motivation
Let's face it. _Everyone writes their test cases last_.

Unless you're someone who lives by TDD, you're like the rest of us lazy developers and write your test cases as the last part of your assignment. But before you start writing you'll run several rounds of sanity checks to make sure your project works as expected.

Now imagine instead of only having the four quick cases you thought up, you also had the ones from your friends also working on the project. Or the ones from the entire class. These quick and dirty smoke tests can help you rat out bugs __before__ you start writing out the fancy test suite you're going to pretend you used to test your assignment.

We all want to make sure our assignment actually performs _according to the requirements_ before we submit it. After all, most of your marks come from the behavior of your code, _not_ the test case writeup.

## Setup


## Development

### Changelog
Regenerate the changelog with the following command:
```
git log --pretty="%ad - %s @%h" --date=format:'%a %b %d %H:%M' > changelog.md
```