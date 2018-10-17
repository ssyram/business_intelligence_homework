# Description For the Whole Project Structure

ssyram, Wuhan University, Oct-17 2018 17:46

## target -- what is this project about

to implement the two typical and most basic algorithm in getting association rules -- Apriori and Fp-Growth Algorithm.

## whole structure -- what does each directory means

docs: document\
log: the file output\
samples: test samples\
    when using test mode, you can use this to load your samples
    
> but pay attention that the format should be chars divided by space, like: \
 a b c 1 2 \
> and they will be automatically converted to numbers. For this is initially only intended to be used as a form of debug, not for user use.
    
src: the source code of this project 

## result -- how to use these algorithms

### 1. Use the core algorithm

Use Class Algorithm in either package fpalgorithm.fsetgenerator.apriori / fpalgorithm.fsetgenerator.fpgrowth, and you'll get the Frequent Set as you provide correct parameters for their calculateFrequentSet() static method.

Then, use Class AssociationRuleBuilder for the FrequentSetContainer result you get from the method above and you'll get all of the association rules.

> You must specify what the support and confidence threshold as you use these methods. 

### 2. Use the whole project

Append a 

The package fpalgorithm is the core of this project, which contains, fsetgenerator standing for "frequent set generator" inside which is the algorithm of Apriori and Fp-Growth for generating frequent sets, and also the AssociationRuleBuilder, which is used to generate a set of association rules.

Then, these classes are in principle, separated from the other parts of the project. And, the other parts, can, in some extend, be considered as a sample project of using these algorithms.

Just using the algorithms:
    
> Import the Algorithm class from fpgrowth or apriori as you want, and then, call them, you can get the frequent sets. But, you must provide information needed by their calculateFrequentSets() static method.
    
> After that, you can call AssociationRuleBuilder to analyze the FrequentSetContainer to get the target association rules needed.
    
## quick start -- how to quickly use this project to calculate

Add a "password" file in directory src/database, and type the password of your database.
> you must have a MySQL database and also a schema named "business_intelligence_database" and a "transactions" table in it.

Import util.Runner, and run the things you want, and you'll finally get the target association rules being output at log/... .
> before that, you can either use util.samplegenerator.SamplesGenerator to generate some test samples distributed in a controllable Gaussian Function way



