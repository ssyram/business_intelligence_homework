# Description For the Source Code Structure
ssyram, Wuhan University, Oct-18 2018 7:10

## fpalgorithm -- core

The package fpalgorithm is the core of this project, which contains, fsetgenerator standing for "frequent set generator" inside which is the algorithm of Apriori and Fp-Growth for generating frequent sets, and also the AssociationRuleBuilder, which is used to generate a set of association rules.

Then, these classes are in principle, separated from the other parts of the project. And, the other parts, can, in some extend, be considered as a sample project of using these algorithms.


## util -- key parts of the implementation of the project

### adaptor -- the connector between project and fpalgorithm

The two adaptors in util.adaptor implements the how to use the key algorithms, they provide a convenient way of automatically querying the database and then get Frequent Sets by calling the specified algorithm.

### samplegenerator -- a supplement of the project

Generate samples distributed in a Gaussian Function manner.
Also provided ways of detail-controlled generation of samples.

### GlobalInfo -- some key static information

It contains some information like the threshold of support and confidence and other supportive information that are used in everywhere except for fpalgorithm (you must pass them to the core methods) package.

### Runner -- highly encapsulated running methods

It contains some static methods to easily access and output the result needed. 



## database -- how the database work with the project

Almost all other parts except for package fpalgorithm use this to access the database.
Key is Class DatabaseOperator, the other two classes are just supplement and util of this class.

## test -- some process static methods to test

Except for TestRunner, these are not important classes.

If you want to use algorithms to analyze your own data, you can probably use this class. 
