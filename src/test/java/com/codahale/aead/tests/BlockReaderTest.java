/*
 * Copyright © 2018 Coda Hale (coda.hale@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codahale.aead.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.codahale.aead.AEAD;
import com.codahale.aead.BlockReader;
import com.codahale.aead.BlockWriter;
import java.nio.charset.StandardCharsets;
import org.junit.Before;
import org.junit.Test;

public class BlockReaderTest {

  private static final byte[] A = "hello".getBytes(StandardCharsets.UTF_8);
  private static final byte[] B = "it is me".getBytes(StandardCharsets.UTF_8);
  private static final byte[] C = "you're looking for".getBytes(StandardCharsets.UTF_8);
  private final AEAD gcm = AEAD.gcm("ayellowsubmarine".getBytes(StandardCharsets.UTF_8));
  private final BlockReader r = new BlockReader(gcm, new byte[16], new byte[200]);
  private final byte[][] blocks = new byte[3][];

  @Before
  public void setUp() {
    final BlockWriter w = new BlockWriter(gcm, new byte[16], new byte[200]);
    blocks[0] = w.encryptBlock(A);
    blocks[1] = w.encryptBlock(B);
    blocks[2] = w.encryptFinalBlock(C);
  }

  @Test
  public void reading() {
    assertThat(r.decryptBlock(blocks[0])).contains(A);
    assertThat(r.decryptBlock(blocks[1])).contains(B);
    assertThat(r.decryptFinalBlock(blocks[2])).contains(C);
  }
}
