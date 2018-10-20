# business_intelligence_homework

to synchronize between my desktop and macbook.

details description of this project is in the directory docs.

## the mix_type_similarity_calculation is not associated with the algorithm, but it's a part of the home work.
This part is implemented by C++, to be specific, C++17, and it uses std::variant, std::optional to implement.
<strong>There are two implementations</strong>: inheritance and traits. For that the inheritance one is too ugly to use -- you must use pointers to keep the meta_data in vector, traits implementation, on the other hand, can use meta_data itself as element type of the vector.
### Note that because at the time I write traits implementation, I've already handed in the homework, so I didn't write the runner for the traits implementation, but in principle it should not be far different from the runner of the inheritance implementation.
