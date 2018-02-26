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
package com.codahale.aead;

import java.util.Arrays;

public class BlockWriter {
  private final AEAD aead;
  private final byte[] nonce;
  private final byte[] data;

  public BlockWriter(AEAD aead, byte[] nonce, byte[] data) {
    this.aead = aead;
    this.nonce = Arrays.copyOf(nonce, nonce.length);
    this.data = data;
    nonce[nonce.length - 1] = 0;
  }

  public byte[] encryptBlock(byte[] block) {
    BlockReader.incrementCounter(nonce);
    return aead.encrypt(nonce, block, data);
  }

  public byte[] encryptFinalBlock(byte[] block) {
    BlockReader.incrementCounter(nonce);
    nonce[nonce.length - 1] = 1;
    return aead.encrypt(nonce, block, data);
  }
}
