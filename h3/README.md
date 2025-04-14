Ex.1 - MapReduce

Code is in ./ex1/demo/src/main/java/com/example/demo/StudentGradeProcessor.java.

I first wrote the program on Windows and then tested it with Hadoop on Linux. Here are the steps:

1) under ./ex1/demo/ folder, create a distributable JAR file that includes all the project dependencies:
   
    mvn clean compile assembly:single

2) copy the JAR file to the Linux machine (at ./ex1/demo/target/demo-0.0.1-SNAPSHOT-jar-with-dependencies.jar)
   
3) upload the csv file to hdfs
   
    hdfs dfs -put /home/hadoopuser/backup/students.csv /data/

4) run the program
   
    hadoop jar demo-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.example.demo.StudentGradeProcessor /data/ /output1

Here is the table showing the comparison for various file sizes:
| File Size | 4.03MB | 6.36MB | 7.76MB | 2.76GB |
|-----------|--------|--------|--------|--------|
| **MapReduce (s)** | 16.721 | 16.598 | 16.900 | 40.394 |
| **Streaming (s)** | 14.033 | 14.631 | 15.696 | 118.958 |


It can be observed that the MapReduce program is slower than the streaming program for smaller file sizes, but as the file size reaches a unit of GB, the MapReduce program becomes significantly faster. I believe this is because the overhead of setting up the MapReduce job is amortized over a larger dataset, making it more efficient for processing large files.

Ex.2 - Avro

Explain the three ways or API styles into which Avro can be used in MapReduce, and when to apply each of them.

Specific API

The Specific API in Avro uses pre-generated Java classes based on Avro schemas. These classes are generated at compile-time and provide a strongly-typed interface for data access. 

This approach is suitable when the schema is stable and known in advance, as it benefits from compile-time type checking, which enhances performance and reduces runtime errors. Using the Specific API allows developers to work with native Java objects and ensures that any schema changes are caught during compilation.

Generic API

The Generic API does not require pre-generated Java classes and instead utilizes a generic data structure, 'GenericRecord', to handle records dynamically. 

This approach is ideal for scenarios where schemas are not fixed or are expected to evolve over time. It offers flexibility by allowing schema definitions to be specified at runtime. The Generic API is useful in environments where schema changes are frequent, or when integrating with systems that produce or consume data with varying schemas.

Reflect API

The Reflect API leverages Java reflection to serialize and deserialize existing Java classes without requiring pre-generated classes. It is particularly useful when working with legacy codebases where rewriting classes to conform to Avro schemas is impractical. The Reflect API provides a way to serialize and deserialize Java objects with minimal changes to the existing code. '

This approach is beneficial when we want to integrate Avro with existing applications while maintaining the existing object model.

To use MapReduce program from the previous exercise to process the Avro file produced in Homework 2 exercise 4, there are some changes that need to be made. 
Code is in ./ex2/demo/src/main/java/com/example/demo/AvroMaxGradeProcessor.java.

1) under ./ex2/demo/ folder, create a distributable JAR file that includes all the project dependencies:
   
    mvn clean compile assembly:single

2) copy the JAR file to the Linux machine (at ./ex2/demo/target/demo-0.0.1-SNAPSHOT-jar-with-dependencies.jar)
   
3) upload the avro file to hdfs
   
    hdfs dfs -put /home/hadoopuser/backup/students.avro /data2/

4) run the program
   
    hadoop jar demo-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.example.demo.AvroMaxGradeProcessor /data2/ /output2


Ex.3 - Bloom Filters

What is a Bloom Filter?

A Bloom filter is a probabilistic data structure used to test whether an element is a member of a set. It is a highly space-efficient structure that is commonly used in big data applications. The key characteristics of a Bloom filter are its space efficiency, fast query times, and the possibility of false positives. Specifically, if a Bloom filter returns "no," the element is definitely not in the set. However, if it returns "yes," the element might be in the set, but it could also be a false positive.

How Does a Bloom Filter Work?

A Bloom filter works by using an array of bits, initially all set to zero, and k independent hash functions. To add an element, each of the k hash functions hashes the element to a position in the bit array, setting the bit at each position to 1. To query an element, each of the k hash functions hashes the element, and the filter checks if the bits at all resulting positions are 1. If any of the bits are 0, the element is definitely not in the set. If all the bits are 1, the element might be in the set (but could be a false positive).

Code is in ./ex3/demo/src/main/java/com/example/demo/

1) under ./ex3/demo/ folder, create a distributable JAR file that includes all the project dependencies:
   
    mvn clean compile assembly:single

2) copy the JAR file to the Linux machine (at ./ex3/demo/target/demo-0.0.1-SNAPSHOT-jar-with-dependencies.jar)

3) run the program
   
    hadoop jar demo-0.0.1-SNAPSHOT-jar-with-dependencies.jar com.example.demo.BloomFilterProcessor /data/ /output3
