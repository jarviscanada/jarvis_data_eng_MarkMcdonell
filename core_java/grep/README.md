# Introduction
The Java grep app mimics the Linux in-built grep -Er function, recursively searching through text to find lines that match the regex input given by the user, and outputting to a designated file.
The project is built with Maven, and written in IntelliJ IDE. In the project, a Java interface is implemented to collect, parse text and write out using BufferedReader, BufferedWriter and logging with slf4j Logger.
# Quick Start

The app is used by calling from terminal:
>java grep [regex] [root directory Path] [outFile path]

This will output the lines in any file under the root directory that contain a pattern matching the user-entered regex, and output the matched lines to a specified by outFile path.
This app is also dockerized and can be run in a container built from the image found in the repository docker.io/markpmcd/grep

# Implemenation
## Pseudocode
> process (regex, rootPath, outFile)  
> matchedLines = new ArrayList<>  
> for file in rootPath:  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; return listFiles[files]  
for line in files:  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; return readLine(lines)  
> if lines.matches(regex):  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; matchedLines.add(lines);
> writeToOutfile(matchedLines)


## Performance Issue
If input file is very large (larger than default heap size), you will get OutOfMemoryException. The first step is to increase the minimum heap size using -Xmx(Megabyte requirement).
# Test
How did you test your application manually? (e.g. prepare sample data, run some test cases manually, compare result)
Testing was done using the practice project folder and running my methods individually on test files containing a few lines and a few words on each line. When an error occurred, debugger was used to pinpoint root cause.

# Deployment
The app was dockerized by creating a dockerfile that copies the .jar file to local repository.
Then mvn clean package was implemented, followed by docker build -t [docker-user]/grep
With this image created, a container was run with sample arguments. The
# Improvement
1. Add all Streams instead of lists.
2. Create a sharded database to take huge files (larger than machine capacity)
3. Add exceptions for other errors.
