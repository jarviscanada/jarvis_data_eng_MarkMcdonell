package ca.jrvs.apps.grep;

import java.util.*;
import java.io.*;

public interface JavaGrep {
  /** Top level search workflow
   * @throws java.io.IOException
   */
  void process() throws IOException;
  /**
   * Traverse a given directory and return all files
   * @param rootDir input directory
   * @return files under the rootDir
   */
  List<File> listFiles(String rootDir);
  /**
   * Read a file and return all the lines
   * FileReader 
   * @param inputFile file to be read
   * @return lines
   * @throws IllegalArgumentException if a given inputFile is not a file
   */
  List<String> readLines(File inputFile) throws IOException;
  /**
   * check if a line contains the regex pattern (passed by user)
   * @param line input string
   * @return true if there is a match
   */
  boolean containsPattern(String line);
  /**
   * Write lines to a file
   * FileOutputStream, OutputStreamWriter, and BufferedWriter
   * @param lines matched line
   * @throws IOException if write failed
   */
  void writeToFile(List<String> lines) throws IOException;

  String getRootPath();

  File setRootPath(String rootPath);

  String getRegex();

  void setRegex(String regex);

  String getOutFile();

  void setOutFile(String outFile);
}
