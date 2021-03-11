#!/usr/bin/env python
# -*- coding: utf-8 -*- 

import sys, string
import numpy as np



# Leet array

leet = ['4','8','<','D','3','ƒ','6','#','1','9','K','l','M','N','0','P','Q','Я','5','7','U','V','W','%','¥','2']


characters = np.array([' ']+list(open(sys.argv[1]).read())+[' '])
# Result: array([' ', 'H', 'e', 'l', 'l', 'o', ' ', ' ', 
#           'W', 'o', 'r', 'l', 'd', '!', ' '], dtype='<U1')

# Normalize
characters[~np.char.isalpha(characters)] = ' '
characters = np.char.upper(characters)
# Result: array([' ', 'H', 'E', 'L', 'L', 'O', ' ', ' ', 
#           'W', 'O', 'R', 'L', 'D', ' ', ' '], dtype='<U1')

### Split the words by finding the indices of spaces
sp = np.where(characters == ' ')
# Result: (array([ 0, 6, 7, 13, 14], dtype=int64),)
# A little trick: let's double each index, and then take pairs
sp2 = np.repeat(sp, 2)
# Result: array([ 0, 0, 6, 6, 7, 7, 13, 13, 14, 14], dtype=int64)
# Get the pairs as a 2D matrix, skip the first and the last
w_ranges = np.reshape(sp2[1:-1], (-1, 2))
# Result: array([[ 0,  6],
#                [ 6,  7],
#                [ 7, 13],
#                [13, 14]], dtype=int64)
# Remove the indexing to the spaces themselves
w_ranges = w_ranges[np.where(w_ranges[:, 1] - w_ranges[:, 0] > 2)]
# Result: array([[ 0,  6],
#                [ 7, 13]], dtype=int64)

# Voila! Words are in between spaces, given as pairs of indices
words = list(map(lambda r: characters[r[0]:r[1]], w_ranges))
# Result: [array([' ', 'h', 'e', 'l', 'l', 'o'], dtype='<U1'), 
#          array([' ', 'w', 'o', 'r', 'l', 'd'], dtype='<U1')]
# Let's recode the characters as strings
swords = np.array(list(map(lambda w: ''.join(w).strip(), words)))
# Result: array(['hello', 'world'], dtype='<U5')

# Next, let's remove stop words
stop_words = np.array(list(set(open('../stop_words.txt').read().split(','))))
stop_words = np.char.upper(stop_words)
ns_words = swords[~np.isin(swords, stop_words)]


# for x in range(0, ns_words.size-1):
for x in range(0, ns_words.size-1):
  for y in ns_words[x]:
    ns_words[x] = ns_words[x].replace(y, leet[ord(y) - ord('A')])
    
        

ns_words2 = np.repeat(ns_words,2)
two_grams = np.reshape(ns_words2[1:-1], (-1, 2))



### Finally, count the word occurrences
uniq, counts = np.unique(two_grams, axis=0, return_counts=True)
wf_sorted = sorted(zip(uniq, counts), key=lambda t: t[1], reverse=True)

for w, c in wf_sorted[:5]:
    print(w, '-', c)

