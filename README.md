# Non-blocking and Parallel computing within a file tree in java language

This project illustrates how to use of virtual threads and structured concurrency with java version 23 in order 
to execute paralleled tasks on each file of a file tree.

## How does it work ?

The project provides a walker method (`Traverser.browseFor`) which is responsible for executing the method 
`BrowseResult.processFile` for each file of a file tree, given an initial instance of BrowseResult.

An instance of `BrowseResult` is created for every directory which is browsed. So, walking through the content 
of a root directory, if a file is found, the method `processFile` is called, **if a directory is found a new instance 
of `BrowseResult` is created to process the directory in a new structured task scope nested in the current task**. 
StructuredTaskScopes use virtual threads

As The Traverser is designed to produce an aggregated result so, after all the files and all the subdirectories
of a directory are processed, results can be aggregated with a call to `aggregate()`.

The previous requirements are implemented in the method [`bibonne.filestree.traverser.InternalTraverser#call`](src/main/java/bibonne/filestree/traverser/InternalTraverser.java)

## Two illustrations

To operate the project, one should supply an implementation of BrowseResult. This implementation is at least responsible for :
- processing a file to mutate its state and get a result from the given file
- supplying a new instance for a given directory
- returning the directory to which an instance is linked
- eventually, aggregating results for the linked directory and its subdirectories

The two following sections demonstrate :
- a BrowseResult to compute aggregated sizes of a file tree
- a BrowseResult to convert audio files from a format to another (external call to ffmpeg). This example does not use aggregation

### Compute aggregated sizes in a file tree

### Call `ffmpeg` to convert music files
BrowseResult
## Comparing with

### Mono thread

### Blocking multi-threads

### Parallel Streams