"""
# Copyright Nick Cheng, Brian Harrington, Danny Heap, Nixon Alvarado
2013, 2014, 2015
# Distributed under the terms of the GNU General Public License.
#
# This file is part of Assignment 2, CSCA48, Winter 2015
#
# This is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This file is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this file.  If not, see <http://www.gnu.org/licenses/>.
"""

# Do not change this import statement, or add any of your own!
from regextree import RegexTree, StarTree, DotTree, BarTree, Leaf

# Do not change anything above this comment except for the copyright
# statement

# Student code below this comment.
# 1001304545 Student Number


def star_string(input_str):
    '''
    (str) -> bool
    Helper function to is regex. Checks if the whole string is just made up
    of star ("*")
    REQ: The input string must be at least of length 1
    >>>star_string("****")
    True
    >>>star_string("bs**")
    False
    >>>star_string("**.**")
    False
    '''
    # Base Case if the length of the string is one compare and return
    if len(input_str) == 1:
        return (input_str[0] == "*")
    else:
        # do the recursive call and start by spliting the list until the
        # last element is reached
        result = star_string(input_str[1:])
        return (result and star_string(input_str[0]))


def is_regex_helper(input_str):
    '''
    (str) -> bool
    Helper function for is_regex where it deals with the cases where
    dot, bars and stars can be joined together.
    REQ: The expression must have the same number of opening brackets
    as closing brackets
    >>>is_regex_helper("(21.0)")
    False
    >>>is_regex_helper("((2.0)*.(2|1))")
    True
    '''
    # Base Case We know we have reached the smaller case if there is no
    # . or | that will join two regexes
    if not("." in input_str) and not("|" in input_str):
        # use our helper method for smaller regexes
        return smallest_regex(input_str)
    else:
        # if the last character of string is a star recurse with
        # all to the left but the first character
        if input_str[-1] == "*":
            result = is_regex_helper(input_str[:-1])
            return result and True
        else:
            # other wise find the index of the root and recurse with left and
            # right
            index = find_root(input_str)
            # recurse with the left expression without including open bracket
            if input_str[0] == "(" and input_str[-1] == ")":
                left_result = is_regex_helper(input_str[1:index])
                right_result = is_regex_helper(input_str[index + 1:-1])
                return left_result and right_result
            else:
                return False


def find_root(input_str, counter=0):
    '''
    (str[,int]) -> int
    Returns the index of where the root of the given regex is located
    in order to be able to use recursion and get the right and left
    for this given regex
    REQ: The number of opening and closing brackets must be the same in the
    overall string
    >>>find_root("((e|0).(1.0))")
    6
    >>>find_root("((1.(0|2)*).0)")
    11
    '''
    # loop through every character of the string
    for i in range(len(input_str) - 1):
        # every time we hit an opening bracket add one to count
        if input_str[i] == "(":
            counter += 1
        # every time we hit a closing bracket subtract one to count
        elif input_str[i] == ")":
            counter -= 1
        elif input_str[i] == "." or input_str[i] == "|":
            # the root is any . or | that is not inside any pair of brackets
            # that just has one opening bracket in front
            if counter == 1:
                return i


def smallest_regex(input_str):
    '''
    (str) -> bool
    Returns True iff the input_str is a valid regex. Therefore after the
    first index there should only be stars
    REQ: The string cannot contain brackets
    >>>smallest_regex("e**")
    True
    >>>smallest_regex("0*.*")
    False
    '''
    possible = ["0", "1", "2", "e"]
    # if the string has length one then that element must be one of the
    if len(input_str) == 1:
        return input_str in possible
    # possible 4 single regex
    # else: check if first index is a valid one and pass the rest of the
    # string to the helper function star_string
    else:
        return (input_str[0] in possible) and (star_string(input_str[1:]))


def is_regex(string):
    '''
    (str) -> bool
    Evaluate the given expression and return True iff the input string
    is a valid regex expression. According to the given rules such that
    >>>is_regex("((2.(0|1))|(1**.(e*|1)*))")
    True
    >>>is_regex("**(2.(0|1)*)")
    False
    '''
    # The first case would be if there is no brackets in th regex then
    # Call our helper function to deal with this case
    try:
        if not("(" in string) or not(")" in string):
            result = smallest_regex(string)
            return result
        # the other case will require a recursive definition when brackets are
        # being added
        else:
            # then we know that if the number of opening brackets and closing
            # brackets are not the same then it is not a valid regex
            if string.count("(") != string.count(")"):
                return False
            else:
                # other wise use our helperfunction to
                return is_regex_helper(string)
    except IndexError:
        return False
    except TypeError:
        return False


def permutations(input_str):
    '''
    (str) -> list of str
    Returns all the possible permutations for a given string.
    Takes every character and pairs it up with every other chracter in the
    given string
    The number of permutations will be len(input_str)!
    >>>permutations("abc")
    ['abc', 'acb', 'bac', 'bca', 'cab', 'cba']
    >>>permutations("1*")
    ['1*', '*1']
    '''
    # Base Case: if the length of the  input_str is one then return that str
    if len(input_str) == 1:
        return input_str
    # initialize the list to return
    final_list = []
    # loop through every character of the input_str to find the permutations
    for next_char in input_str:
        # do the recursive call with rest of the input_str
        for following in permutations(input_str.replace(next_char, "", 1)):
            # check if the given str is a valid regex expression
            # if it is then add to the set
            final_list.append(next_char + following)
    return final_list


def all_regex_permutations(input_str):
    '''
    (str) -> set of str
    Given any string return all the possible valid regexes that can be formed
    by different permutations of the characters that are given in input_str
    '''
    return_set = set()
    final_list = permutations(input_str)
    for next_str in final_list:
        if(is_regex(next_str)):
            return_set.add(next_str)
    return return_set


def base_tree_regex(regex):
    '''
    (str) -> StarTree or Leaf
    Given a regex build the appropriate tree to represente the smallest case
    of a valid regex
    REQ: input regex cannot contain brackets or . , |
    >>>base_tree_regex("e*")
    StarTree(Leaf(e))
    >>>base_tree_regex("1**")
    StarTree(StarTree(Leaf(1)))
    >>>base_tree_regex("e")
    Leaf("e")
    '''
    # Base Case: if the regex is one of the length one just create the Leaf
    # corresponding to that value
    possible = ["0", "1", "2", "e"]
    if regex in possible:
        leaf_node = Leaf(regex)
        return leaf_node
    else:
        # then this means we have at least one star to the right
        child = base_tree_regex(regex[:-1])
        # then make this one the child of a star tree
        star_tree = StarTree(child)
        return star_tree


def helper_build_tree(regex):
    '''
    (str) -> RegexTree
    Given a fully parenthesized regex and valid return the root of the corres
    ponding tree representation. Helper function when the regex has at
    least one joint (. or |)
    REQ: The input regex string must be a valid one
    >>>helper_build_tree("(1.0*)*")
    StarTree(DotTree(Leaf('1'), StarTree(Leaf('0'))))
    >>>helper_build_tree("((2.0)|1)")
    BarTree(DotTree(Leaf('2'), Leaf('0')), Leaf('1'))
    '''
    # Base Case: If there is no dot or bar in the given regex.
    # Then just call ourbase_tree_regex function
    if not("|" in regex) and not("." in regex):
        return base_tree_regex(regex)
    else:
        # otherwise we will have to find the root
        # recurse with the right and left subtrees
        # find the index of the root in the string
        index = find_root(regex)
        # if the star is the last of the string we must recurse with
        # all the left
        if regex[-1] == "*":
            child = helper_build_tree(regex[:-1])
            star_tree = StarTree(child)
            return star_tree
        elif regex[index] == "|":
            # then we have to use a bar tree. Find left child and right
            # child then assign them
            left_child = helper_build_tree(regex[1:index])
            right_child = helper_build_tree(regex[index + 1:-1])
            bar_tree = BarTree(left_child, right_child)
            return bar_tree
        elif regex[index] == ".":
            # Find left child and right child and then assign them
            left_child = helper_build_tree(regex[1:index])
            right_child = helper_build_tree(regex[index + 1:-1])
            dot_tree = DotTree(left_child, right_child)
            return dot_tree


def build_regex_tree(regex):
    '''
    (str) -> RegexTree
    Given a valid regular expression, builds a Tree containing the
    corresponding
    subtrees and returns the root of the whole expression.
    REQ: The given regex must be a valid regex
    >>>build_regex_tree("(2.0)")
    DotTree(Leaf(2), Leaf(0))
    >>>build_regex_tree("(1.(2|0))")
    DotTree(Leaf('1'), BarTree(Leaf('2'), Leaf('0')))
    '''
    # First Case if no . and | are given we just have to handle star and basis
    if not("|" in regex) and not("." in regex):
        # build the tree from right to left
        return base_tree_regex(regex)
    # The rest of the cases will be handled when there are joints
    else:
        # we are guaranteed to have at least a dot or a bar tree
        return helper_build_tree(regex)


def regex_match(root, string):
    '''
    (RegexTree, str) -> bool
    '''
    # Base Case: The leafs are the base cases therefore
    # the match to 0,1,2,e are the base cases for this regex_match
    if isinstance(root, Leaf):
        value = root.get_symbol()
        if value == "e":
            return string == ""
        else:
            return value == string
    else:
        # we will have to recurse with the different possibilities of
        # subtree
        if isinstance(root, BarTree):
            left_tree = regex_match(root.get_left_child(), string)
            right_tree = regex_match(root.get_right_child(), string)
            # check if it either matches the left or the right
            return left_tree or right_tree
        elif isinstance(root, DotTree):
            # call our helper function  to return the value for this dot tree
            return helper_dot_tree(root, string)
        elif isinstance(root, StarTree):
            # call our helper function
            return helper_star_tree(root, string)


def helper_dot_tree(root, string):
    '''
    (DotTree, string) -> bool
    Helper function for the regex match. Returns True iff we are able to find
    s1 and s2 such that s1 matches the left subtree and s2 matches the right
    subtree.
    >>>r = DotTree(StarTree(Leaf("1")), Leaf("0"))
    >>>helper_dot_tree(r, "11110")
    True
    >>>helper_dot_tree(r, "11101")
    False
    '''
    # we have to find s1 and s2 such that s1 matches left subtree
    # and s2 matches right subtree
    s1 = ""
    s2 = string
    # do a first check if with this partition both match
    left_tree = regex_match(root.get_left_child(), s1)
    right_tree = regex_match(root.get_right_child(), s2)
    # start an index at 0 for the index of the string
    # the loop will stop until we find the partition s1 and s2 that match the
    # left and right subtrees or we are at the end of the string when s2 is ""
    index = 0
    while not(left_tree and right_tree) and s2 != "":
        s1 = string[:index + 1]
        s2 = string[index + 1:]
        left_tree = regex_match(root.get_left_child(), s1)
        right_tree = regex_match(root.get_right_child(), s2)
        index += 1
    # at the end of the loop if the left and right subtree are true for
    # s1 and s2 then return True
    if (left_tree and right_tree):
        return True
    else:
        # otherwise, we have to check with s2 being the empty string and
        # s1 being the whole string
        s1 = string
        s2 = ""
        return ((regex_match(root.get_left_child(), s1) and
                 regex_match(root.get_right_child(), s2)))


def helper_star_tree(root, string):
    '''
    (StarTree, str) -> bool
    Return True iff every partition of the string matches the child of root
    The function finds this partitions if the last partition cannot
    be found then it returns False.
    >>>r = StarTree(DotTree(Leaf("1"),Leaf("0")))
    >>>helper_star_tree(r, "1010")
    True
    >>>helper_star_tree(r, "101002")
    False
    '''
    # in this case every Si has to match the r in the form r*
    # if it is the empty string it matches
    if string == "":
        return True
    else:
        # check which partition will make the child to match
        # that string
        length = len(string)
        index = 0
        while index <= length and (length != 0):
            # check the result for the child with a substring until index
            child_result = regex_match(root.get_child(), string[:index])
            # if that partition works redefine string and
            # restart the index count
            if child_result:
                if index == 0:
                    # if the index is 0 we take the substring from index + 1
                    # to avoid an infinite loop with the empty string
                    string = string[index + 1:]
                else:
                    string = string[index:]
                length = len(string)
                # we set index to 0 again because we want to do the same
                # algorithm  for the new substring
                index = 0
            # if that partition does not work then just add one to the
            # index and loop again
            else:
                index += 1
        return child_result
