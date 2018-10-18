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

#### database setting

Append a file /src/database/database_info, and write information about the database to which you would use as a place storing transactions info, by specifying <strong> your database address </strong>, <strong> your user name</strong>, <strong> your password</strong> in the beginning 3 lines, and please do not contain any redundant information. Note that the database you specify <strong> must contain a table as following</strong>: 
```sql

create table transactions
(
  transaction_num int not null,
  item_num        int not null,
  primary key (transaction_num, item_num)
);

```

#### support and confidence threshold setting

Specify them in the
``` java 
DatabaseOperator.loadGlobalInfo(<support threshold>, <confidence threshold>); 
```
method in util.GlobalInfo

#### run by calling Runner.run<algorithm_name>

If you want to get the results, run this method.


### ** 3. Run by TestRunner -- a way to analyze your own data

Save your own data in /samples/_\<number>.txt, \<number> means that you can specify this which must corresponding to TestRunner.run<algorithm_name>(\<number>).

Pay attention that this method <strong>is not originally prepared for being used by users</strong>, so it's not so convenient to use, the data must be written in the following way: 
> [\<char> <space_divider>]*\<char> \
> that means: \
> a b c \
> is a good input \
> but not:  
> ab ac ad

for it will only take the <storng>numeric value</strong> in ascii for the first character in the first place and after each <space_divider>


## support function -- Samples Generator

You can use util.samplegenerator.SamplesGenerator to generate samples distributed in a Gaussian Function pattern.

You can also specify the detail parameters of these samples.
