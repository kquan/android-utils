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
package com.kevinquan.android.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.annotation.SuppressLint;
import android.util.Log;

/**
 * Utilities to work with byte arrays.
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
public class ByteUtils {
    
    private static final String TAG = ByteUtils.class.getSimpleName();
    
    /**
     * Writes the input to the output stream and closes both streams.  No additional buffering is performed on the input stream.
     * Both streams will be closed. 
     * @param input The input stream to read
     * @param output The stream to output too
     */
    public static void convertStream(InputStream input, OutputStream output) {
        convertStream(input, output, true);
    }
    
    /**
     * Writes the input to the output stream and closes both streams if it is desired
     * @param input The input stream to read
     * @param output The stream to output too
     * @param close whether both streams should be closed after completion
     */
    public static void convertStream(InputStream input, OutputStream output, boolean close) {
        byte[] buffer = new byte[1024];
        int readBytes = 0;
        try {
            while ((readBytes = input.read(buffer)) != -1) {
                output.write(buffer,0,readBytes);
            }
            output.flush();
        } catch (IOException ioe) {
            Log.e(TAG, "Could not write input to output stream.", ioe);
        }
        try {
            if (close) {
                input.close();
            }
            output.flush();
            if (close) {
                output.close();
            }
        } catch (IOException ioe) {}
    }
    
    /**
     * Converts the input stream to a byte array.  The reading of the input stream is not buffered.  The input stream will not be closed.
     * @param stream The stream to convert
     * @return A byte array with the contents of the stream
     */
    public static byte[] convertStreamToByteArray(InputStream stream) {
        if (stream == null) {
            return new byte[] {};
        }
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        byte[] data = new byte[Short.MAX_VALUE-1];
        int read = -1;
        
        try {
            while ((read = stream.read(data, 0, data.length)) != -1) {
                buf.write(data,0,read);
            }
            buf.flush();
        } catch (IOException ioe) {
            Log.e(TAG, "Could not read from stream.", ioe);
        }
        try {
            return buf.toByteArray();
        } finally {
            try {
                buf.close();
            } catch (IOException ioe) {}
        }
    }
    
    /**
     * Converts a byte array into a hexadecimal string.  The string will always be lowercase.
     * @param input The byte array to convert
     * @return The hexadecimal version of the byte array
     */
    @SuppressLint("DefaultLocale")
    public static String convertToHexString(byte[] input) {
        if (input == null || input.length == 0) {
            return new String();
        }
        // From http://stackoverflow.com/a/13006907/1339200
        StringBuilder sb = new StringBuilder();
        for (byte b : input) {
           sb.append(String.format("%02x", b&0xff));
        }
        return sb.toString().toLowerCase();
     }
}
