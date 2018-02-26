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
import java.util.Optional;

public class BlockReader {
  private final AEAD aead;
  private final byte[] nonce;
  private final byte[] data;

  public BlockReader(AEAD aead, byte[] nonce, byte[] data) {
    this.aead = aead;
    this.nonce = Arrays.copyOf(nonce, nonce.length);
    this.data = data;
    nonce[nonce.length - 1] = 0;
  }

  public Optional<byte[]> decryptBlock(byte[] block) {
    incrementCounter(nonce);
    return aead.decrypt(nonce, block, data);
  }

  public Optional<byte[]> decryptFinalBlock(byte[] block) {
    incrementCounter(nonce);
    nonce[nonce.length - 1] = 1;
    return aead.decrypt(nonce, block, data);
  }

  static void incrementCounter(byte[] nonce) {
    int n = nonce.length - 2;
    while (n >= 0 && ++nonce[n] == 0) {
      --n;
    }
  }
}
