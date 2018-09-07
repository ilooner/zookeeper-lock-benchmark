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
package org.apache.bench;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceDataTest {
  //private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ResourceDataTest.class);

  @Rule
  public ExpectedException expected = ExpectedException.none();

  @Test
  public void testFailureToGetDataBeforeGenerate() throws Exception {
    BlobData data = new ResourceData();
    expected.expect(IllegalStateException.class);
    data.getDataAsByteArray();
  }

  @Test
  public void testFailureOnDoubleGenerate() {
    BlobData data = new ResourceData();
    expected.expect(IllegalStateException.class);
    data.generate();
    data.generate();
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testSerDeResourceData() throws Exception {
    BlobData data = new ResourceData(10);
    data.generate();
    Map<String, List<ResourceData.ResourceDataDef>> originalData = ((ResourceData) data).getOriginalData();

    byte[] payload = data.getDataAsByteArray();

    try (final ByteArrayInputStream byteIn = new ByteArrayInputStream(payload);
         final ObjectInputStream objIn = new ObjectInputStream(byteIn)) {
      final Map<String, List<ResourceData.ResourceDataDef>> deserializedMap =
        (HashMap<String, List<ResourceData.ResourceDataDef>>) objIn.readObject();
      Assert.assertEquals(deserializedMap.size(), 10);
      Assert.assertEquals(deserializedMap, originalData);
    }
  }
}
