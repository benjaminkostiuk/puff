## @file Set.py
#  @author Benjamin Kostiuk
#  @brief Module that defines the Set ADT
#  @details Assumes that the Set constructor is called for each object instance
#           before any other access methods are called.
#  @date 02/01/2020

from Equality import Equality


## @brief An abstract data type that represents a set
class Set(Equality):

    ## @brief Set constructor
    #  @details Initializes a Set object whose state consists of a set of elements
    #  @param s Sequence of elements with which to initialize the Set
    def __init__(self, s):
        self.S = set(s)

    ## @brief Add an element to the set
    #  @param Element to be added to the set
    def add(self, e):
        self.S = self.S.union({e})

    ## @brief Remove an element from the set
    #  @param e Element to be removed from the set
    #  @throws ValueError if element to be removed cannot be found in the set
    def rm(self, e):
        if not self.member(e):
            raise ValueError("Cannot remove element not found in set.")
        self.S = self.S.difference({e})

    ## @brief Determine whether an element is in the set
    #  @param e Element to check whether in the set
    #  @return True if the element is in the set, otherwise false
    def member(self, e):
        return e in self.S

    ## @brief Get the size of the set
    #  @return The size of the set
    def size(self):
        return len(self.S)

    ## @brief Determine if the Set is equal to another set
    #  @details A Set is considered equal if all elements in one set are in another
    #  @param r Set to compare with
    #  @return True if the two Sets are equal, otherwise false
    def equals(self, r):
        if self.size() != r.size():
            return False

        for element in self.S:
            if not r.member(element):
                return False
        return True

    ## @brief Returns a sequence of all elements in the set
    #  @return A sequence of all elements in the set
    def to_seq(self):
        return list(self.S)

    def __eq__(self, value):
        return self.equals(value)
