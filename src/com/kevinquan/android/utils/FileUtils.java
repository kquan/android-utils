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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.util.Log;

/**
 * Utilities for working with files
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
public class FileUtils {
    
    private static final String TAG = FileUtils.class.getSimpleName();
    
    /**
     * Copies a file from the source to the destination
     * @param source The path to the source file
     * @param destination The path to the destination file
     * @return true if the file was successfully copied.
     */
    public static boolean copyFile(File source, File destination) {
        if (source == null || destination == null) return false;
        if (!source.exists()) {
            Log.w(TAG, "The source file doesn't exist so we can't copy it!");
            return false;
        }
        boolean result = false;
        try {
            InputStream in = new FileInputStream(source);
            try {
                result = copyToFile(in, destination);
            } finally  {
                in.close();
            }
        } catch (IOException e) {
            Log.e(TAG, "Could not copy file: "+e.getMessage(), e);
            return false;
        }
        return result;
    }
    
    /**
     * Write out data from a source stream to a destination file.  The source stream will not be closed.
     * @param inputStream The input stream to write out
     * @param destination The destination file to write to
     * @return True if the stream was written out to the destination file successfully.
     */
    public static boolean copyToFile(InputStream inputStream, File destination) {
        if (inputStream == null || destination == null) return false;
        try {
            OutputStream out = new FileOutputStream(destination);
            try {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) >= 0) {
                    out.write(buffer, 0, bytesRead);
                }
                out.flush();
            } finally {
                out.close();
            }
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Could not write stream to file: "+e.getMessage(), e);
            return false;
        }
    }

    /**
     * Delete all files within a directory
     * @param directory The directory whose files need to be deleted
     * @return True if the files are deleted 
     */
    public static boolean deleteAllFilesInDirectory(File directory) {
        if (directory == null || !directory.isDirectory()) {
            Log.w(TAG, "Could not delete directory: "+directory);
            return false;
        }
        boolean result = true;
        String[] files = directory.list();
        if (files == null) {
            // Successful in deleting 0 files
            return true;
        }
        for (String aFile : files) {
            File childFile = new File(directory, aFile);
            if (childFile.isDirectory()) {
                result &= deleteAllFilesInDirectory(childFile);
            }
            result &= childFile.delete();
        }
        return result;
    }
    
    /**
     * Ensures that the path to the file has all of its parent directories created.
     * @param aFile A path to a file or folder whose parent directories we want to be created  
     * @param leaf true if the path is to a file, false if the path is to a folder
     */
    public static void ensureParentFoldersCreated(File aFile, boolean leaf) {
        if (aFile == null) return;
        if (leaf) {
            ensureParentFoldersCreated(aFile.getParentFile(), false);
        } else if (aFile != null && !aFile.exists()) {
            ensureParentFoldersCreated(aFile.getParentFile(), false);
            boolean result = aFile.mkdir();
            if (!result) {
                Log.e(TAG, "Could not create directory: "+aFile.getAbsolutePath());
            }
        }
    }

}