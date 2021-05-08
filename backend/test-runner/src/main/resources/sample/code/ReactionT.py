## @file ReactionT.py
#  @author Benjamin Kostiuk
#  @brief Module defines the ReactionT ADT for representing checmical reactions
#  @date 02/05/2020

from CompoundT import *

import numpy as np
from sympy import Matrix, lcm


## @brief An abstract data type that represents a chemical reaction
class ReactionT:

    ## @brief ReactionT constructor
    #  @details Initializes a ReactionT object whose state consists of a sequence of
    #           reactants a sequence of its coefficients, a sequence of products
    #           and a sequence of its coefficients. The sequences of coefficients
    #           are computed as to balance the chemical reaction with an equal
    #           number of elements on both sides.
    #  @param reactants Sequence of CompoundT in the left-hand side of the chemical
    #                   reaction, known as reactants
    #  @param products Sequence of CompoundT in the right-hand side of the chemical
    #                  reaction, known as products
    #  @throws ValueError if the elements in the reactants do not match the elements
    #                     in the products, the two sides of the reaction cannot be
    #                     balanced, any of coefficients are non-positive or if the
    #                     the sequences of coefficients do not match their
    #                     respective side of the chemical reaction
    def __init__(self, reactants, products):
        # Get ElmSet of ElementTs in L and R
        lhs_elems = self.__elements_in_equation__(reactants)
        rhs_elems = self.__elements_in_equation__(products)

        # Check that elements in the reactants and the products are the same
        if not lhs_elems.equals(rhs_elems):
            raise ValueError("Elements in reactants must match elements in products.")

        # Get coefficient matrix to solve linear equation
        lhs_coeffs, rhs_coeffs = self.__solve_matrix__(reactants, products, lhs_elems)

        # Check if length of lists match
        if len(lhs_coeffs) != len(reactants) or len(rhs_coeffs) != len(products):
            raise ValueError("Cannot match coefficients to reactants and products.")

        # Check if coefficients are balanced
        for element in lhs_elems.to_seq():
            if not self.__is_balanced__(reactants, products, lhs_coeffs, rhs_coeffs, element):
                raise ValueError("Invalid ReactionT. Reaction cannot be balanced.")

        self.lhs = reactants
        self.rhs = products
        self.coeff_L = lhs_coeffs
        self.coeff_R = rhs_coeffs

    ## @brief Get the sequence of reactants of the chemical reaction
    #  @return The sequence of CompoundT in the left-hand side of the chemical reaction
    def get_lhs(self):
        return self.lhs

    ## @brief Get the sequence of products of the chemical reaction
    #  @return The sequence of CompoundT in the right-hand side of the chemical reaction
    def get_rhs(self):
        return self.rhs

    ## @brief Get the sequence of coefficients in the left-hand side of the chemical reaction
    #  @return The sequence of coefficients in the left-hand side of the chemical reaction
    def get_lhs_coeff(self):
        return self.coeff_L

    ## @brief Get the sequence of coefficients in the right-hand side of the chemical reaction
    #  @return The sequence of coefficients in the right-hand side of the chemical reaction
    def get_rhs_coeff(self):
        return self.coeff_R

    # Returns an ElmSet of ElementT in a list of CompoundTs
    def __elements_in_equation__(self, equation):
        elems = []
        for compound in equation:
            elems += compound.constit_elems().to_seq()
        return ElmSet(elems)

    # Check if a ReactionTs coefficients for reactants and products are balanced
    def __is_balanced__(self, reactants, products, left_coeffs, right_coeffs, element):
        lhs_count, rhs_count = 0, 0
        # Count element for left hand side
        for i in range(len(reactants)):
            if left_coeffs[i] <= 0:
                raise ValueError("Invalid ReactionT. Coefficients must be positive.")
            lhs_count += left_coeffs[i] * reactants[i].num_atoms(element)

        # Count element for right hand side
        for i in range(len(products)):
            if right_coeffs[i] <= 0:
                raise ValueError("Invalid reaction. Coefficients must be positive.")
            rhs_count += right_coeffs[i] * products[i].num_atoms(element)

        return lhs_count == rhs_count

    # Return right and left coefficients solved from a list of reactants and products
    def __solve_matrix__(self, reactants, products, elems):
        # Create a coefficient matrix to solve
        coeff_matrix = []
        for e in elems.to_seq():
            row = [compnd.num_atoms(e) for compnd in reactants]
            row += [-compnd.num_atoms(e) for compnd in products]
            coeff_matrix.append(row)

        # Check if reaction is null
        if(reactants == [] and products == []):
            return [], []
        else:
            # Solve for lhs and rhs coefficients
            # Uses algorithm proposed here:
            # https://stackoverflow.com/questions/42637872/solve-system-of-linear-integer-equations-in-python
            matrix = Matrix(coeff_matrix)
            null_vectors = matrix.nullspace()

            if null_vectors == []:
                raise ValueError("Invalid ReactionT. Reaction cannot be balanced.")

            null_vectors = null_vectors[0]
            multiple = lcm([val.q for val in null_vectors])
            x = multiple * null_vectors
            solution = np.array([int(val) for val in x]).tolist()

            lhs_coeffs = solution[:len(reactants)]
            rhs_coeffs = solution[len(reactants):]

        return lhs_coeffs, rhs_coeffs
