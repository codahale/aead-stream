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
import com.codahale.aead.BlockWriter;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BlockWriterTest {

  private final AEAD gcm = AEAD.gcm("ayellowsubmarine".getBytes(StandardCharsets.UTF_8));
  private final BlockWriter w = new BlockWriter(gcm, new byte[16], new byte[200]);
  private final byte[][] blocks = new byte[3][10];

  @BeforeEach
  void setUp() {
    for (int i = 0; i < blocks.length - 1; i++) {
      blocks[i] = w.encryptBlock(blocks[i]);
    }
    blocks[blocks.length - 1] = w.encryptFinalBlock(blocks[blocks.length - 1]);
  }

  @Test
  void knownOutputs() {
    assertThat(blocks[0])
        .isEqualTo(
            new byte[] {
              65, 34, -101, -94, -128, 79, 23, -21, -10, -124, -16, 2, 43, -82, 39, 38, 77, 14, 121,
              -88, -117, 61
            });
    assertThat(blocks[1])
        .isEqualTo(
            new byte[] {
              -112, -65, -108, 37, -100, 119, -70, 58, 10, 115, -38, -21, -68, -58, 58, -80, -9,
              125, -60, -24, -5, 42
            });
    assertThat(blocks[2])
        .isEqualTo(
            new byte[] {
              104, 121, -27, -36, 94, 96, 73, -50, -12, 1, 107, 76, -53, 109, 99, -119, -76, -125,
              -16, 106, -123, -104
            });
  }
}
