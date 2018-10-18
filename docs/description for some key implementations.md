# Description For Some Key Implementations
ssyram, Wuhan University, Oct-18 2018 7:50

## fpalgorithm.fsetgenerator.util.CombinativelyIterableSet

this class provide a way of iterating all the combination in a specified set.

you can use the for-each loop to access all of its ways of combination.

```
Set<Integer> s_of_int = new HashSet<>();

s_of_int.add(1);
s_of_int.add(2);

CombinativelyIterableSet<Integer> set = new CombinativelyIterableSet(s_of_int);

for (Set<Integer> s: set)
    for (Integer i: s)
        System.out.println(i);

```

> output is: \
1\
2\
1 2

This is implemented by using a binary number as guide of iterating.

```
public Iterator<Set<T>> iterator() {
        return new Iterator<Set<T>>() {
            Object[] va = v.toArray();
            long pattern = -1; // this means it will firstly return an empty set
            long max = (1 << va.length) - 1;
            @Override
            public boolean hasNext() {
                return pattern != max;
            }

            @Override
            public Set<T> next() {
                if (va.length > 31)
                    throw new RuntimeException("out of memory.");
                ++pattern;
                Set<T> s = new HashSet<>();
                for (int j = 0; j < va.length; ++j) {
                    long k = (1 << j);
                    if (j > pattern) break;
                    if ((pattern & k) != 0) s.add((T) va[j]);
                }
                return s;
            }
        };
    }
```

Because a combination in fact means a whether a set is chosen, if it's represented by 0-1. And given then an order. Then you can actually use a binary number to guide it, in which 0 represents absent and 1 represent chosen.


## Division Iteration

This is used when generating Association Rules, for that I use a strategy of top-down dividing to generate association rules.

Originally, according to the definition of association rule, I must firstly find two sets whose intersection combines nothing and find out if their union set is frequent, and then to determine whether it can generate a association rule.

From the description above, we can see that the original floor-ceil way of generating association rules is extremely low efficiency. Why don't we just use a reverse way -- from any frequent sets that contain more than one element and make a division -- divide it into two parts that have no elements in their intersection and a union equals to the original set itself -- because of the property of frequent sets that any not-empty subsets of a frequent set is frequent, there is no such worry of doubting whether any part of the not-empty division set is frequent. And that, 
> if A, B is a division of frequent set C \
> support(C) / support(A) >= confidence_threshold

this means a production of
 
> A => B[support(C), support(C) / support(A)].

So, in this situation, the emphasis turns into the question that -- \
<strong>how can I get all divisions from a single set</strong>?

Here, for the briefness of code, I let 
> class FrequentSet implements Iterable<Pair<Set<Integer, Integer>>

Thinking in this question: \
```
by observing, a two-division of a set is symmetric -- let
the elements in the whole set be an 0-1 array, each position represents a single element
so, a specific choosing of division means which position to be 1 (selected in left set),
or 0 (selected in right set), so, each pattern can be represented by a single binary number
at the same time, the two binary number whose addition (| operation) is all one
is effectively equivalent because left and right is effectively equivalent,
and, in the sense of number, it means when the two numbers' addition is
2^(element_amount) - 1, they are effectively equivalent, which means a specific number's
effectively equivalent number is 2^(element_amount) - 1, so
when the number is less than (1 << (element_amount - 1)), it can get all of the distinct
division pattern without repetition.
```
> From my comment on FrequentSet::iterator

And I use this to guide my code.

## Quick Access to OrderedItemList, FrequentSetContainer, and childNodes-in-FpTreeNode

OrderedItemList is one of the parameters that must be provided by the user to successfully call the calculateFrequentSets in Fp-Growth algorithm.

In order to higher the efficiency, it must be accessed quickly and not traversed every time I want to compare the amount or just access the item on the OrderedItemList.

So, I write a map to record the position on this list.

And that's the same thing I do to the FrequentSetContainer in order to have a quick access to each of the FrequentSet as I provide a specified Set of elements. So that I can quickly get the set I want especially in the process of generating association rules. I use the sum and multiplication result of the character of each set, so I don't need to compare each of the sets in order to access this. In the astonishing efficiency closer to O(1).

It's also the same as what I did to childNodes in FpTreeNode, they can easily be accessed by their item_num which is definitely the key character of each of the node in a specified position. 
