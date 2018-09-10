/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.bench.ZKBlobDataGen;

import java.io.Serializable;

public class NodeResourceDataDef extends ResourceDataDef implements Serializable {
  private static final long serialVersionUID = -2234662262516818310L;

  private String ipAddress;

  NodeResourceDataDef(long memory, long cpu, String ip) {
    super(memory, cpu);
    this.ipAddress = ip;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  @Override
  public String toString() {
    return String.format("Node_IP: %s, %s", ipAddress, super.toString());
  }

  @Override
  public int hashCode() {
    int assignedPrime = 19;
    assignedPrime = 31 * assignedPrime + ipAddress.hashCode();
    assignedPrime = 31 * assignedPrime + super.hashCode();
    return assignedPrime;
  }

  @Override
  public boolean equals(Object obj) {
    return (obj == this) || ((obj instanceof NodeResourceDataDef) && equals((NodeResourceDataDef) obj));
  }

  private boolean equals(NodeResourceDataDef obj) {
    return (this.ipAddress.equals(obj.getIpAddress()) && super.equals(obj));
  }
}
