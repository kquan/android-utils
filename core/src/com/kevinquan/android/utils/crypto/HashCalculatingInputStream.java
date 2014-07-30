/*
 * Copyright 2013 Kevin Quan (kevin.quan@gmail.com)
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
package com.kevinquan.android.utils.crypto;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.util.Log;

import com.kevinquan.android.utils.ByteUtils;
import com.kevinquan.android.utils.crypto.CryptoUtils.HashAlgorithm;

/**
 * Compute a hash as input content becomes available.
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
public class HashCalculatingInputStream extends FilterInputStream {

    private static final String TAG = HashCalculatingInputStream.class.getSimpleName();
    
    public static int BUFFER_SIZE = 1024;
    
    protected HashAlgorithm mAlgorithm;
    protected MessageDigest mDigest;
    protected byte[] mBuffer;
    protected int mPointer;
    
    public HashCalculatingInputStream(InputStream in, HashAlgorithm algorithm) {
        super(in);
        mAlgorithm = algorithm;
        
        try {
            if (algorithm == HashAlgorithm.MD5) {
                mDigest = MessageDigest.getInstance("MD5");
            } else if (algorithm == HashAlgorithm.SHA1) {
                mDigest = MessageDigest.getInstance("SHA-1");
            } else if (algorithm == HashAlgorithm.SHA256) {
                mDigest = MessageDigest.getInstance("SHA-256");
            } else if (algorithm == HashAlgorithm.SHA512) {
                mDigest = MessageDigest.getInstance("SHA-512");
            } else {
                Log.w(TAG, "Unknown hash algorithm specified.");
            }
        } catch (NoSuchAlgorithmException nsae) {
            Log.e(TAG, "The requested algorithm is not supported by the (default) security provider.", nsae);
        }
        mPointer= 0;
    }
    
    @Override
    public int read() throws IOException {
        if (mBuffer == null || mPointer >= mBuffer.length) {
            mBuffer = new byte[BUFFER_SIZE];
            // Populate buffer
            int readBytes = super.read(mBuffer);
            if (readBytes == -1) {
                return -1;
            } else if (readBytes < BUFFER_SIZE) {
                // Resize buffer so we don't have to take care of boundary conditions later
                byte[] smallerBuffer = new byte[readBytes];
                System.arraycopy(mBuffer, 0, smallerBuffer, 0, readBytes);
                mBuffer = smallerBuffer;
            }
            // Perform intermediate hash
            mDigest.update(mBuffer);
            mPointer = 0;
        }
        return mBuffer[mPointer++];       
    }
    

    @Override
    public int read(byte[] buffer, int offset, int count) throws IOException {
        int bytesRead = super.read(buffer, offset, count);
        if (bytesRead != -1) {
            try {
                mDigest.update(buffer, offset, bytesRead);
            } catch (Exception e) {
                Log.e(TAG, "Could not perform intermediate digest",e);
            }
        }
        return bytesRead;
    }
    
    public String getHash() {
        if (mDigest == null) return null;
        // This .toLowerCase() is ok - we are only using ASCII a-f
        return ByteUtils.convertToHexString(mDigest.digest());
    }
}
