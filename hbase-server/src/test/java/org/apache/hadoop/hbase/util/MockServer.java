/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hadoop.hbase.util;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.hbase.ChoreService;
import org.apache.hadoop.hbase.CoordinatedStateManager;
import org.apache.hadoop.hbase.HBaseTestingUtility;
import org.apache.hadoop.hbase.Server;
import org.apache.hadoop.hbase.ServerName;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.ClusterConnection;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.keymeta.PBEClusterKeyCache;
import org.apache.hadoop.hbase.keymeta.PBEKeyAccessor;
import org.apache.hadoop.hbase.keymeta.PBEKeymetaAdmin;
import org.apache.hadoop.hbase.log.HBaseMarkers;
import org.apache.hadoop.hbase.zookeeper.ZKWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Basic mock Server for handler tests.
 */
public class MockServer implements Server {
  private static final Logger LOG = LoggerFactory.getLogger(MockServer.class);

  public boolean stopped;
  public boolean aborted;
  public final ZKWatcher zk;
  public final HBaseTestingUtility htu;
  public final ServerName name;
  public final boolean trackStatus;

  public MockServer() throws ZooKeeperConnectionException, IOException {
    // Shutdown default constructor by making it private.
    this(null);
  }

  public MockServer(final HBaseTestingUtility htu)
    throws ZooKeeperConnectionException, IOException {
    this(htu, true);
  }

  /**
   * @param htu Testing utility to use
   * @param zkw If true, create a zkw.
   */
  public MockServer(final HBaseTestingUtility htu, final boolean zkw)
    throws ZooKeeperConnectionException, IOException {
    this("MockServer,60020,000000", htu, zkw, true, true);
  }

  public MockServer(final String name, final HBaseTestingUtility htu, final boolean createZkWatcher,
      final boolean canCreateBaseZNode, final boolean trackStatus) throws IOException {
    this.name = ServerName.valueOf(name);
    this.htu = htu;
    this.zk = createZkWatcher ? new ZKWatcher(htu.getConfiguration(), this.name.toShortString(), this,
      canCreateBaseZNode) : null;
    this.trackStatus = trackStatus;
  }

  @Override
  public void abort(String why, Throwable e) {
    if (! trackStatus) return;
    LOG.error(HBaseMarkers.FATAL, "Abort why=" + why, e);
    stop(why);
    this.aborted = true;
  }

  @Override
  public void stop(String why) {
    if (! trackStatus) return;
    LOG.debug("Stop why=" + why);
    this.stopped = true;
  }

  @Override
  public boolean isStopped() {
    return this.stopped;
  }

  @Override
  public Configuration getConfiguration() {
    return this.htu.getConfiguration();
  }

  @Override
  public ZKWatcher getZooKeeper() {
    return this.zk;
  }

  @Override
  public CoordinatedStateManager getCoordinatedStateManager() {
    return null;
  }

  @Override
  public ClusterConnection getConnection() {
    return null;
  }

  @Override
  public ServerName getServerName() {
    return name;
  }

  @Override
  public boolean isAborted() {
    // TODO Auto-generated method stub
    return this.aborted;
  }

  @Override
  public ChoreService getChoreService() {
    return null;
  }

  @Override public PBEClusterKeyCache getPBEClusterKeyCache() {
    return null;
  }

  @Override public PBEKeyAccessor getPBEKeyAccessor() {
    return null;
  }

  @Override public PBEKeymetaAdmin getPBEKeymetaAdmin() {
    return null;
  }

  @Override
  public ClusterConnection getClusterConnection() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public FileSystem getFileSystem() {
    return null;
  }

  @Override
  public boolean isStopping() {
    return false;
  }

  @Override
  public Connection createConnection(Configuration conf) throws IOException {
    return null;
  }
}
