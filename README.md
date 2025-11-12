# Non-blocking and Parallel computing within a file tree in java language : illustrate structrured concurrency

This project illustrates how to use of virtual threads and structured concurrency with java version 25 in order 
to execute paralleled tasks on each file of a file tree. It uses java virtual threads (JEP 444) combined with 
structured concurency (JEP 505)

## How does it work ?

The project provides a walker method (`Traverser.browseFor`) which is responsible for executing the method 
`BrowseResult.processFile` for each file of a file tree, given an initial instance of BrowseResult.

An instance of `BrowseResult` is created for every directory which is browsed. So, walking through the content 
of a root directory, if a file is found, the method `processFile` is called, **if a directory is found a new instance 
of `BrowseResult` is created to process the directory in a new structured task scope nested in the current task**. 
StructuredTaskScopes use virtual threads

As The Traverser is designed to produce an aggregated result so, after all the files and all the subdirectories
of a directory are processed, results can be aggregated with a call to `aggregate()`.

The previous requirements are implemented in the method [`traverser.poc.java.filestree.InternalTraverser#call`](src/main/java/poc/java/filestree/withstructuredconcurrency/traverser/InternalTraverser.java)
