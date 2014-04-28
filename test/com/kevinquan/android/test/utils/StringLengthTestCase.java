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
package com.kevinquan.android.test.utils;

import java.io.UnsupportedEncodingException;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.kevinquan.utils.StringUtils;

/**
 * This class houses BVT-level tests for string length.
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
public class StringLengthTestCase extends TestCase {
	
	public void test_getNullStringLength() {
		Assert.assertEquals("The string length of a null string is expected to be 0.", 0, StringUtils.getLength(null));
	}
	
	public void test_getEmptyStringLength() {
		Assert.assertEquals("The string length of an empty string is expected to be 0.", 0, StringUtils.getLength(new String()));
	}
	
	public void test_getASCIIStringLength() {
		String testString = "This is a typical string.";
		Assert.assertEquals("The string length was not correct.", testString.length(), StringUtils.getLength(testString));
	}
	
	public void test_getDBCSStringLength() {
		// This is a byte representation of 我說中國的語言 in order to get around lack of DBCS support in javac
		byte[] stringInBytes = new byte[] {-26, -120, -111, -24, -86, -86, -28, -72, -83, -27, -100, -117, -25, -102, -124, -24, -86, -98, -24, -88, -128};
		try {
			String testString = new String(stringInBytes, StringUtils.UTF8_LITERAL);
			Assert.assertEquals("The string length was not correct.", 7, StringUtils.getLength(testString));
		} catch (UnsupportedEncodingException uee) {
			Assert.fail("Could not convert byte array to DBCS string.");
		}
	}

}
