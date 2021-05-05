## @file Equality.py
#  @author Benjamin Kostiuk
#  @brief Module declares Equality inteface
#  @date 02/01/2020

from abc import ABC, abstractmethod


## @brief Abstract interface for equality between two objects
class Equality(ABC):

    ## @brief Determine if two objects are equal
    #  @param other Object to compare with
    #  @return True if objects are equal, otherwise false
    @abstractmethod
    def equals(self, other):
        pass
