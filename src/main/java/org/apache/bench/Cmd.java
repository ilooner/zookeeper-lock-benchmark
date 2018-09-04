package org.apache.bench;

import com.beust.jcommander.JCommander;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;

import java.time.Duration;

public class Cmd {
  public static void main(String[] args) {
    final CmdArgs cmdArgs = new CmdArgs();

    //Duration
    JCommander.newBuilder()
      .addObject(cmdArgs)
      .build()
      .parse(args);

    // "localhost:2181"
    final RetryPolicy policy = new RetryNTimes(3, 10000);
    final CuratorFramework curator = CuratorFrameworkFactory.newClient(null, policy);

    curator.start();
  }
}
