package com.kevinquan.android.utils.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.kevinquan.android.utils.ByteUtils;

import android.util.Log;

/**
 * Collection of utilities related to cryptography
 * 
 * @author Kevin Quan (kevin.quan@gmail.com)
 *
 */
public class CryptoUtils {
	
	private static final String TAG = CryptoUtils.class.getSimpleName();

	public static enum HashAlgorithm { MD5, SHA1, SHA256, SHA512 };
	
	/**
	 * Computes a hash for the provided input, using the requested algorithm
	 * @param input The input to hash
	 * @param algorithm The algorithm to use
	 * @return The hash, or an empty string if no hash was computed.
	 */
	public static String computeHash(byte[] input, HashAlgorithm algorithm) {
	    if (input == null || input.length == 0) {
	        return new String();
	    }
		MessageDigest digest = null;
		
		try {
			if (algorithm == HashAlgorithm.MD5) {
				digest = MessageDigest.getInstance("MD5");
			} else if (algorithm == HashAlgorithm.SHA1) {
				digest = MessageDigest.getInstance("SHA-1");
			} else if (algorithm == HashAlgorithm.SHA256) {
				digest = MessageDigest.getInstance("SHA-256");
			} else if (algorithm == HashAlgorithm.SHA512) {
				digest = MessageDigest.getInstance("SHA-512");
			} else {
				Log.w(TAG, "Unknown hash algorithm specified: "+algorithm);
				return new String();
			}
		} catch (NoSuchAlgorithmException nsae) {
			Log.e(TAG, "The requested algorithm is not supported by the (default) security provider.",nsae);
		}
		if (digest == null) return new String();
		// Read input stream and update digest
		return ByteUtils.convertToHexString(digest.digest(input));
	}
}
