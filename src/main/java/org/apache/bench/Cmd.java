package org.apache.bench;

import com.beust.jcommander.JCommander;

public class Cmd {
  public static void main(String[] args) {
    final CmdArgs cmdArgs = new CmdArgs();

    //Duration
    JCommander.newBuilder()
      .addObject(cmdArgs)
      .build()
      .parse(args);
  }
}
