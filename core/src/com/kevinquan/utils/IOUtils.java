/*
 * Copyright 2014 Kevin Quan (kevin.quan@gmail.com)
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
package com.kevinquan.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.zip.ZipFile;

/**
 * Helpers for I/O
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
public class IOUtils {

	@SuppressWarnings("unused")
	private static final String TAG = IOUtils.class.getSimpleName();

	/**
	 * Close a zip file if necessary. Exceptions are squelched
	 * @param file The file to close
	 */
	public static void safeClose(ZipFile file) {
		if (file == null) {
			return;
		}
		try {
			file.close();
		} catch (IOException ioe) {
			// Squelched.
		}
	}
	
	/**
	 * Close an inputstream if necessary.  Exceptions are squelched
	 * @param stream The stream to close
	 */
	public static void safeClose(InputStream stream) {
		if (stream == null) {
			return;
		}
		try {
			stream.close();
		} catch (IOException ioe) {
			// Squelched.
		}
	}
	
	/**
     * Close a writer if necessary.  Exceptions are squelched
     * @param writer The writer to close
     */
    public static void safeClose(Writer writer) {
        if (writer == null) {
            return;
        }
        try {
            writer.close();
        } catch (IOException ioe) {
            // Squelched.
        }
    }
    
    /**
     * Close a reader if necessary. Exceptions are squelched
     * @param reader The reader to close
     */
    public static void safeClose(Reader reader) {
        if (reader == null) {
            return;
        }
        try {
            reader.close();
        } catch (IOException ioe) {
            // Squelched.
        }
    }
}
