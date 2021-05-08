## @file CompoundT.py
#  @author Benjamin Kostiuk
#  @brief Module defines the CompoundT ADT for chemical compound representation
#  @date 02/01/2020

from MoleculeT import *
from MolecSet import *


## @brief An abstract data type that represents a chemical compound
class CompoundT(ChemEntity, Equality):

    ## @brief CompoundT constructor
    #  @details Initializes a CompoundT object whose state consists of a MolecSet
    #  @param m MolecSet of molecules in the chemical compound
    def __init__(self, m):
        self.C = m

    ## @brief Get the MolecSet of molecules in the chemical compound
    #  @return The MolecSet of molecules in the chemical compound
    def get_molec_set(self):
        return self.C

    ## @brief Get the number of atoms of a given ElementT in the chemical compound
    #  @param e ElementT to check for in chemical compound
    #  @return The number of atoms of the specified ElementT in the chemical compound
    def num_atoms(self, e):
        count = 0
        for m in self.C.to_seq():
            count += m.num_atoms(e)
        return count

    ## @brief Return an ElmSet of the ElementTs in the chemical compound
    #  @return An ElmSet of the ElementTs in the chemical compound
    def constit_elems(self):
        return ElmSet([m.get_elm() for m in self.C.to_seq()])

    ## @brief Determine if the chemical compound is equal to another chemical compound
    #  @details Two chemical compounds are considered equal if they have
    #           all the same molecules in them
    #  @param d CompoundT to compare with
    #  @return True if the chemical compounds are equal, otherwise false
    def equals(self, d):
        return self.C.equals(d.get_molec_set())

    def __eq__(self, value):
        return self.equals(value)
