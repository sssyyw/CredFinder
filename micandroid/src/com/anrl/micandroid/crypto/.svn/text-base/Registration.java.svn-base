package com.anrl.micandroid.crypto;
//Author Wesley Ellis; wesleyjellis@gmail.com

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Registration {
	// This is my registration class. It will be a singleton,
	//BUT you need to pass it context...

	private static Registration regState = null;
	private String clientKey = null;
	private String encryptedY = null;
	private static SharedPreferences settings = null;
	private static Context mContext;
	
	private boolean registered = false;

	private String UPkey = null;
	private static final String prefClient = "ClientKey";
	private static final String prefY = "YValue";
	private static final String prefUP = "UPkey";
	private static final String prefReg = "Registered";
	private static final String fail = "fail";
	private static final String TAG = "micandroid";
	private static final String PrefName = "micandroid";


	public static Registration getRegistration(Context context) {
		if (regState == null) {
			mContext = context;
			settings = mContext.getSharedPreferences(PrefName, Context.MODE_PRIVATE);
			regState = new Registration();
		}
		return regState;
	}

	private Registration() {
		registered = settings.getBoolean(prefReg, false);
		if (registered) {
			//getString requires two arguments
			//1) the key name
			//2) the value to return if there is no such key/value pair
			String tempClientKey = settings.getString(prefClient, fail);
			String tempYValue = settings.getString(prefY, fail);
			String tempUP = settings.getString(prefUP, fail);
			if (!tempClientKey.equals(fail) && !tempYValue.equals(fail) && !tempUP.equals(fail)) {
				clientKey = tempClientKey;
				encryptedY = tempYValue;
				UPkey = tempUP;
			}
			else{
				registered = false;
			}
		}
	}

	public String getClientKey() {
		return clientKey;
	}

	public String getEncryptedY() {
		return encryptedY;
	}

	public String getUPKey() {
		return UPkey;
	}
	
	//Try to register us
	public boolean register(String tempClientKey, String tempYString,
			String tempUPKey) {
		if (registered)
			return false;
		boolean result = false;
		try {
			SharedPreferences.Editor editor = settings.edit();
			editor.putString(prefClient, tempClientKey);
			editor.putString(prefY, tempYString);
			editor.putString(prefUP, tempUPKey);
			editor.putBoolean(prefReg, true);
			editor.commit();
			result = true;
			registered = true;
			UPkey = tempUPKey;
			encryptedY = tempYString;
			clientKey = tempClientKey;
		} catch (Exception e) {
			Log.d(TAG, "Error registering");
		}
		return result;
	}

	public boolean isRegistered() {
		return registered;
	}

	public boolean unregister() {
		if (!registered)
			return false;
		boolean result = false;
		try {
			SharedPreferences.Editor editor = settings.edit();
			editor.putString(prefClient, fail);
			editor.putString(prefY, fail);
			editor.putString(prefUP, fail);
			editor.putBoolean(prefReg, false);
			editor.commit();
			result = true;
			registered = false;
		} catch (Exception e) {
			Log.d(TAG, "Error unregistering");
		}
		return result;
	}
}
