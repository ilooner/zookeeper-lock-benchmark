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

import com.google.common.base.Preconditions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public abstract class AbstractBlobData implements BlobData {
  //private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(AbstractBlobData.class);

  private boolean isDataGenerated;

  @Override
  public void generate() {
    Preconditions.checkState(!isDataGenerated, "Data can be generated only once");
    isDataGenerated = true;
  }

  @Override
  public void generate(long sizeInKB) {
    Preconditions.checkState(!isDataGenerated, "Data can be generated only once");
    isDataGenerated = true;
  }

  public abstract byte[] getDataAsByteArray() throws IOException;


  protected byte[] getDataAsByteArray(Object inputData) throws IOException {
    Preconditions.checkState(isDataGenerated, "Please generate the data before getting it");
    try(final ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        final ObjectOutputStream objOut = new ObjectOutputStream(byteOut)) {
      objOut.writeObject(inputData);
      return byteOut.toByteArray();
    } catch (Exception ex) {
      throw new IOException("Failed to convert generated data to byteArray", ex);
    }
  }
}
