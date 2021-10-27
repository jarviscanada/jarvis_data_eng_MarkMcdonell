package ca.jrvs.apps.grep;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
public class JavaGrepImp implements JavaGrep {

  private final Logger logger = LoggerFactory.getLogger(JavaGrep.class);
  private String regex;
  private String rootPath;
  private String outFile;

  @Override
  public String getRegex() {
    return regex;
  }

  @Override
  public void setRegex(String regex) {
    this.regex = regex;
  }

  @Override
  public void process() throws IOException {
    List<String> matchedLines = new ArrayList<>();
    for (File inputFile : listFiles(this.getRootPath())) {
      for (String line : readLines(inputFile)) {
        if (containsPattern(line)) {
          matchedLines.add(line);
        }
      }
    }
    writeToFile(matchedLines);
  }

  @Override
  public List<File> listFiles(String rootDir) {
    //create filepath for file
    File filesInDir = new File(rootDir);
    File[] pathList = filesInDir.listFiles();
    List<File> fileList = new ArrayList<>();
    assert pathList != null;
    for (File files : pathList) {
      if (files.isDirectory()) {
        listFiles(files.getPath());
      } else {
        fileList.add(files);
      }
    }
    return fileList;
  }

  @Override
  public List<String> readLines(File inputFile)
      throws IOException {
    if (!inputFile.isFile()){
      throw new IllegalArgumentException("There's a non-file inputFile");
    }
    BufferedReader br = new BufferedReader(new FileReader(inputFile));
    List<String> lines = new ArrayList<>();
    while ((br.readLine()) != null){
      lines.add(br.readLine());
    }
    return lines;
  }

  @Override
  public boolean containsPattern(String line) {
    if (line.matches(regex)) {
      System.out.println(line);
      return true;
    } else {
    return false;}
  }

  @Override
  public void writeToFile(List<String> lines) throws IOException {
    File matchedLinesFile = new File("/home/centos/dev/jarvis_data_eng_MarkMcdonell/core_java/grep/out/matchedLines.txt");
    matchedLinesFile.createNewFile();
    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
        new FileOutputStream("matchedLines.txt"), StandardCharsets.UTF_8));
    writer.write(String.valueOf(lines));
    writer.close();
    }


  @Override
  public String getRootPath() {
    return rootPath;
  }

  @Override
  public File setRootPath(String rootPath) {
    this.rootPath = rootPath;
    return null;
  }

  @Override
  public String getOutFile() {
    return outFile;
  }

  @Override
  public void setOutFile(String outFile) {
    this.outFile = outFile;
  }

  public static void main(String[] args) {
    if (args.length != 3) {
      throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
    }

    JavaGrepImp javaGrepImp = new JavaGrepImp();
    javaGrepImp.setRegex(args[0]);
    javaGrepImp.setRootPath(args[1]);
    javaGrepImp.setOutFile(args[2]);
    try {
      javaGrepImp.process();
    } catch (IOException e) {
      javaGrepImp.logger.error("Error: Unable to process", e);
    }
  }
}
