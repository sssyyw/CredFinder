package com.anrl.micandroid.crypto;
//Author Wesley Ellis; wesleyjellis@gmail.com

import android.util.Log;

public class Secrets {

	private static final String TAG = "micandroid";
	private static final String ERROR = "HASHFAIL";

	

	public static String getHashMessage(String nonce, String serverCloudKey, Registration regState) {
		if (!regState.isRegistered()){
			Log.d(TAG, "Cannot generate hash message, we are not registered");
			return ERROR;
		}
		String output = "";
		byte[] cloudKey = getCloudKey(serverCloudKey, regState);
		byte[] yString = getY(cloudKey, regState);
		//Hash the nonce and the Y value together
		//Must be hased in this order
		byte[] byteoutput = MD5hash.hash(yString, Util.makeBytesfromString(nonce));
		output = Util.makeStringfromBytes(byteoutput);
//Useful for checking to see if you have the correct values
//		Log.d(TAG, "LOOK HERE==================================");
//		Log.d(TAG, "Client key: " + regState.getClientKey());
//		Log.d(TAG, "Server key: " + serverCloudKey);
//		Log.d(TAG, "CloudKey: " + Util.makeStringfromBytes(cloudKey));
//		Log.d(TAG, "EncryptedY: " + regState.getEncryptedY());
//		Log.d(TAG, "DecryptedY: " + Util.makeStringfromBytes(yString));
//		Log.d(TAG, "Hash: " + output);
//		Log.d(TAG , "LOOK UP====================================");
		
		return output;
	}

	public static byte[] getCloudKey(String serverCloudKey, Registration regState) {
		//Must be hased in this order
		return MD5hash.hash(
				Util.makeBytesfromString(regState.getClientKey()),
				Util.makeBytesfromString(serverCloudKey));
	}
	
	public static byte[] getY(byte[] cloudkey, Registration regState){
		Log.d(TAG, regState.getEncryptedY());
		return AES.decryptByteNoPadding(
				Util.makeBytesfromString(regState.getEncryptedY()), 
				cloudkey);
	}
	
}
