package com.anrl.micandroid.crypto;
//Author Wesley Ellis; wesleyjellis@gmail.com

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.util.Log;

public class MD5hash {
	private static final String TAG = "micandroid";

	public static byte[] hash(byte[] input1, byte[] input2) {
		byte[] result = new byte[16];
		try {
			MessageDigest digest = java.security.MessageDigest
					.getInstance("MD5");
			digest.update(input1);
			digest.update(input2);
			result = digest.digest();
		} catch (NoSuchAlgorithmException e) {
			Log.d(TAG, "Error: no such hash algorithm");
			e.printStackTrace();
		}
		return result;
	}

	public static byte[] hash(byte[] input) {
		byte[] result = new byte[16];
		try {
			MessageDigest digest = java.security.MessageDigest
					.getInstance("MD5");
			result = digest.digest(input);
			
		} catch (NoSuchAlgorithmException e) {
			Log.d(TAG, "Error: no such hash algorithm");
			e.printStackTrace();
		}
		return result;
	}
}
