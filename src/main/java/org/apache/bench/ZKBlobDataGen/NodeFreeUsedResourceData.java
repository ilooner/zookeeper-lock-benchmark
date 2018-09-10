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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.bench.ZKBlobDataGen.DataDefConstants.DEFAULT_NODE_COUNT;

public class NodeFreeUsedResourceData extends AbstractBlobData {
  private int nodeCount;
  private Map<String, List<ResourceDataDef>> nodeFreeUsedResource;

  public NodeFreeUsedResourceData(int nodeCount) {
    this.nodeCount = nodeCount;
    nodeFreeUsedResource = new HashMap<>(nodeCount);
  }

  public NodeFreeUsedResourceData() {
    this(DEFAULT_NODE_COUNT);
  }

  @Override
  public void generate() {
    super.generate();
    final String baseIp = "10.10.10.";
    final long baseMemory = 1024 * 1024;
    for (int i = 0; i < nodeCount; ++i) {
      long nodeNum = i+1;
      final String nodeIp = String.format("%s%d", baseIp, nodeNum);
      final List<ResourceDataDef> freeInUseList = new ArrayList<>(2);
      long memory = baseMemory * nodeNum;
      freeInUseList.add(new ResourceDataDef(memory, nodeNum));
      freeInUseList.add(new ResourceDataDef(memory, nodeNum));
      nodeFreeUsedResource.put(nodeIp, freeInUseList);
    }
  }

  // only used for testing
  @Override
  public Map<String, List<ResourceDataDef>> getOriginalData() {
    return nodeFreeUsedResource;
  }

  @Override
  public void generate(long sizeInKB) {
    //super.generate(sizeInKB);
    throw new UnsupportedOperationException("Currently this operation is not supported in NodeFreeUsedResourceData");
  }

  public byte[] getDataAsByteArray() throws IOException {
    return super.getDataAsByteArray(nodeFreeUsedResource);
  }
}
