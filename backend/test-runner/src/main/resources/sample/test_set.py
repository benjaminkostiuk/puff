## @file test_All.py
#  @author Benjamin Kostiuk
#  @brief Testing module of Set.py, MoleculeT.py, CompoundT.py, ReactionT.py
#  @date 02/05/2020

from pytest import *
import collections

from Set import *
from MoleculeT import *
from CompoundT import *
from ReactionT import *


## @brief Test class for Set ADT
class TestSet:

	# Check if two lists contain the same elements with the same counts
	def __compare_unorderedlists__(self, x, y):
		return collections.Counter(x) == collections.Counter(y)

	def setup_method(self, method):
		self.emptySet = Set([])
		self.elmSet = Set([ElementT.C, ElementT.H, ElementT.O])
		self.molSet = Set([MoleculeT(3, ElementT.H), MoleculeT(9, ElementT.Li)])

	def teardown_method(self, method):
		pass
