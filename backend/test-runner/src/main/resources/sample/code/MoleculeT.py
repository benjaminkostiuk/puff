## @file MoleculeT.py
#  @author Benjamin Kostiuk
#  @brief Module defines the MoleculeT ADT for molecule representation
#  @date 02/01/2020

from Equality import Equality
from ChemEntity import *


## @brief An abstract data type that represents a molecule
class MoleculeT(ChemEntity, Equality):

    ## @brief MoleculeT constructor
    #  @details Initializes a MoleculeT object whose state consists of
    #           an ElementT and the number of that element in the molecule
    #  @param m Number of the ElementT in molecule
    #  @param e ElementT in the molecule
    def __init__(self, n, e):
        self.num = n
        self.elm = e

    ## @brief Get the number of ElementT in the molecule
    #  @return The number of ElementT in the molecule
    def get_num(self):
        return self.num

    ## @brief Get the ElementT in the molecule
    #  @return The ElementT in the molecule
    def get_elm(self):
        return self.elm

    ## @brief Get the number of atoms of a given ElementT in the molecule
    #  @param e ElementT to check for in molecule
    #  @return The number of atoms of the specified ElementT in the molecule
    def num_atoms(self, e):
        if self.elm == e:
            return self.num
        return 0

    ## @brief Return an ElmSet of the ElementT in the molecule
    #  @return An ElmSet of the ElementT in the molecule
    def constit_elems(self):
        return ElmSet([self.elm])

    ## @brief Determine if the molecule is equal to another molecule
    #  @details Two molecules are considered equal if they are composed of the same
    #           ElementT and have the same number of atoms.
    #  @param m MoleculeT to compare with
    #  @return True if the molecules are equal, otherwise false
    def equals(self, m):
        return self.elm == m.get_elm() and self.num == m.get_num()

    def __eq__(self, value):
        return self.equals(value)

    def __hash__(self):
        return hash(str(self.num) + str(self.elm))
