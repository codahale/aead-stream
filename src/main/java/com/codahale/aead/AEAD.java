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

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public interface AEAD {
  byte[] encrypt(byte[] nonce, byte[] plaintext, byte[] data);

  byte[] decrypt(byte[] nonce, byte[] ciphertext, byte[] data);

  static AEAD gcm(byte[] key) {
    return new AEAD() {
      @Override
      public byte[] encrypt(byte[] nonce, byte[] plaintext, byte[] data) {
        try {
          return process(Cipher.ENCRYPT_MODE, plaintext, data, nonce);
        } catch (BadPaddingException e) {
          throw new IllegalStateException(e);
        }
      }

      @Override
      public byte[] decrypt(byte[] nonce, byte[] ciphertext, byte[] data) {
        try {
          return process(Cipher.DECRYPT_MODE, ciphertext, data, nonce);
        } catch (BadPaddingException e) {
          return null;
        }
      }

      private byte[] process(int mode, byte[] input, byte[] data, byte[] nonce)
          throws BadPaddingException {
        try {
          final Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
          final GCMParameterSpec params = new GCMParameterSpec(96, nonce);
          final SecretKeySpec spec = new SecretKeySpec(key, "AES");
          cipher.init(mode, spec, params);
          cipher.updateAAD(data);
          return cipher.doFinal(input);
        } catch (InvalidKeyException
            | InvalidAlgorithmParameterException
            | NoSuchPaddingException
            | NoSuchAlgorithmException
            | IllegalBlockSizeException e) {
          throw new IllegalStateException(e);
        }
      }
    };
  }
}
