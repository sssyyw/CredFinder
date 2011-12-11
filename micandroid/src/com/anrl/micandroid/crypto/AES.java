package com.anrl.micandroid.crypto;
//Author Wesley Ellis; wesleyjellis@gmail.com

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Log;

public class AES {

	private static final String TAG = "micandroid";
	private static final String AES = "AES";
	private static final String AES_MODE = "AES/CBC/PKCS5Padding";
	private static final String AES_NO_PADDING = "AES/CBC/NoPadding";

	public static byte[] encryptByte(byte[] plaintext, byte[] key) {
		byte[] result = null;

		SecretKeySpec keySpec = null;
		keySpec = new SecretKeySpec(key, AES);

		byte[] IVseed = new byte[16];
		IvParameterSpec IV = new IvParameterSpec(IVseed);
		// Create an IV that is all zeros
		// AES operating in CBC mode requires an IV
		// If one is not specified, a random one is chosen
		// The problem is that we can't transfer that easily when we decrypt
		//java's byte arrays are intialized empty

		Cipher cipher = null;
		try {
			//IMPORTANT you must use the same instance
			cipher = Cipher.getInstance(AES_MODE);
		} catch (NoSuchAlgorithmException e) {
			Log.d(TAG, "No Such Algorithm Exception");
		} catch (NoSuchPaddingException e) {
			Log.d(TAG, "No Such Padding Exception");
		}
		if (cipher != null) {
			try {
				cipher.init(Cipher.ENCRYPT_MODE, keySpec, IV);
			} catch (InvalidKeyException e) {
				Log.d(TAG, "Invalid Key Exception");
			} catch (InvalidAlgorithmParameterException e) {
				Log.d(TAG, "Invaliad Algorithm Parameter Exception");
			}
			try {
				result = cipher.doFinal(plaintext);
			} catch (IllegalBlockSizeException e) {
				Log.d(TAG, "Illegal Block Size Exception");
			} catch (BadPaddingException e) {
				Log.d(TAG, "Bad Padding Exception in encrypt");
			}
		}
		return result;
	}

	public static byte[] decryptByte(byte[] ciphertext, byte[] key) {
		byte[] result = new byte[16];
		SecretKeySpec keySpec = null;
		byte[] IVseed = new byte[16];

		keySpec = new SecretKeySpec(key, AES);
		IvParameterSpec IV = new IvParameterSpec(IVseed);
		// Using an IV that's initilazed from an all zero byte array
		// This is because if I don't pass one to the cipher engine it chooses a
		// random one
		// and I have no way to get that to anyone

		Cipher cipher = null;

		try {
			cipher = Cipher.getInstance(AES_MODE);
		} catch (NoSuchAlgorithmException e) {
			Log.d(TAG, "No Such Algorithm Exception");
		} catch (NoSuchPaddingException e) {
			Log.d(TAG, "No Such Padding Exception");
		}
		if (cipher != null) {
			try {
				cipher.init(Cipher.DECRYPT_MODE, keySpec, IV);
			} catch (InvalidKeyException e) {
				Log.d(TAG, "Invalid Key Exception");
			} catch (InvalidAlgorithmParameterException e) {
				Log.d(TAG, "Invalid Algorithm Parameter Exception");
			}
			try {
				result = cipher.doFinal(ciphertext);
			} catch (IllegalBlockSizeException e) {
				Log.d(TAG, "Illegal Block Size Exception");
			} catch (BadPaddingException e) {
				Log.d(TAG, e.toString());
				Log.d(TAG, "Bad Padding Exception in decrypt");
			}
		}
		return result;
	}
	
	//We need this method because the Y string is too short for padding
	public static byte[] decryptByteNoPadding(byte[] ciphertext, byte[] key) {
		byte[] result = new byte[16];
		SecretKeySpec keySpec = null;
		byte[] IVseed = new byte[16];

		keySpec = new SecretKeySpec(key, AES);
		IvParameterSpec IV = new IvParameterSpec(IVseed);
		// Using an IV that's initilazed from an all zero byte array
		// This is because if I don't pass one to the cipher engine it chooses a
		// random one
		// and I have no way to get that to anyone

		Cipher cipher = null;

		try {
			cipher = Cipher.getInstance(AES_NO_PADDING);
		} catch (NoSuchAlgorithmException e) {
			Log.d(TAG, "No Such Algorithm Exception");
		} catch (NoSuchPaddingException e) {
			Log.d(TAG, "No Such Padding Exception");
		}
		if (cipher != null) {
			try {

				cipher.init(Cipher.DECRYPT_MODE, keySpec, IV);
			} catch (InvalidKeyException e) {
				Log.d(TAG, "Invalid Key Exception");
			} catch (InvalidAlgorithmParameterException e) {
				Log.d(TAG, "Invalid Algorithm Parameter Exception");
			}
			try {
				result = cipher.doFinal(ciphertext);
			} catch (IllegalBlockSizeException e) {
				Log.d(TAG, "Illegal Block Size Exception");
			} catch (BadPaddingException e) {
				Log.d(TAG, e.toString());
				Log.d(TAG, "Bad Padding Exception in decrypt");
			}
		}
		return result;
	}
}
