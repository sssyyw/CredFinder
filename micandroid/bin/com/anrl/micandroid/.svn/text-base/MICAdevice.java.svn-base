package com.anrl.micandroid;
//Author Wesley Ellis; wesleyjellis@gmail.com

import android.bluetooth.BluetoothDevice;

//Custom object for keeping track of a mica device
//Contains it's name, it's host and the bluetooth device (though potentially it
//just be the address string of a bluetooth device because I can obtain the actual
//object...)

public class MICAdevice {
	private String mName;
	private String mHostname;
	private BluetoothDevice mHost;
	private String mHashedVal;
	
	private static final String seperator = "@";
	
	public MICAdevice(String name, String hostname, BluetoothDevice host){
		mName = name;
		mHostname = hostname;
		mHost = host;
	}

	public String getName(){
		return mName;
	}
	
	public String getHashVal(){
		return mHashedVal;
	}
	public void setHashVal(String HashVal){
		mHashedVal = HashVal;
	}
	
	public String getHostName(){
		return mHostname;
	}
	public String getTextAddress(){
		return mHost.getAddress().replaceAll(":", "");
	}
	public BluetoothDevice getHost(){
		return mHost;
	}	
	
	@Override
	public String toString(){
		return mName + seperator + mHostname + seperator + getTextAddress();
	}
	//This is for the older mica protocol when the format was hostname@address instead of name@host@address
	public String legacyGetAddress(){
		return mHostname + seperator + getTextAddress();
	}
	
	
}

