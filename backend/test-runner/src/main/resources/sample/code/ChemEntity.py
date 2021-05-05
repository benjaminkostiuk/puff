## @file ChemEntity.py
#  @author Benjamin Kostiuk
#  @brief Module declares the ChemEntity interface
#  @date 02/03/2020

from abc import ABC, abstractmethod

from ChemTypes import ElementT
from ElmSet import ElmSet


## @brief Abstract interface that defines methods related to chemical entities
class ChemEntity(ABC):

    ## @brief Get the number of atoms of the given element in the chemical entity
    #  @param element ElementT to retrieve atom count for
    #  @return Number of atoms of the given element in the chemical entity
    @abstractmethod
    def num_atoms(self, element):
        pass

    ## @brief Return a set of all elements that make up the chemical entity
    #  @return ElmSet of all chemical elements in the chemical entity
    @abstractmethod
    def constit_elems(self):
        pass
