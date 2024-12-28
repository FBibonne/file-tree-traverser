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

Benchmark is done applying all the versions of the program which compute aggregate size of a file tree over my /home directory which sizes about 43 Gio and is about 362537 files large.
The program is applied in a bash prompt and we measure the `real time`  of execution returned by the `time` command for many
executions of the program in the bash. The reference time is given by the standard implementation which uses the non blocking
parallel computing with the help of the structured concurrency : it takes about 1.8s

### Mono thread

About 3.1s

### Blocking multi-threads

Use parallel computation with a carrier threads pool. Every new computation is submitted as a new task to the thread pool

NB : With the same implementation, using a `newVirtualThreadPerTaskExecutor()` gives better performances than with structured concurrency : cf. next section

### Implementation of InternalTraverser with a `newVirtualThreadPerTaskExecutor()`

The implementation of InternalTraverser with a newVirtualThreadPerTaskExecutor but without structured concurrency takes
roughly the same time as the implementation with structurd concurrency to process.

### Comparison between structured concurrency and virtual thread only with light structure

See bibonne.filestree.onlyvirtualthreads.FileTreeSizeWithVirtualThreadOnly

The lighter implementation (without InternalTraverser) gives better results (about twice quicker) than the case with 
structured concurrency. **Need to do a precise benchmark on this case**

### Parallel Streams

The basic principle of file tree traverser is to use recursive calls to the function which computes size every time we meet
a folder. Recursive calls are enabled with streams using `mapMulti` : the BiConsumer method `mapper` passed to the mapMulti
calls itself every time it meets a folder.

Benchmark give about 4s.

## Native compilation

Compile the program into a native binary and run the binary to compute the size