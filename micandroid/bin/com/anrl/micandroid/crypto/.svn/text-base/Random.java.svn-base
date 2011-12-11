package com.anrl.micandroid.crypto;
//Author Wesley Ellis; wesleyjellis@gmail.com

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import android.util.Log;

public class Random {
	private static final String TAG = "micandroid";

	public static byte[] generateRandom(int len){
		byte[] result = new byte[len];
		SecureRandom random = null;
		try {
			random = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e) {
			Log.d(TAG, "generateRandom Error: " + e.toString());
		}
		random.nextBytes(result);
		return result;
	}
}
