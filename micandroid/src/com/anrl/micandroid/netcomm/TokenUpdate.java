package com.anrl.micandroid.netcomm;


import com.anrl.micandroid.UI.BluetoothChat;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


public class TokenUpdate extends NetComm { 
	
	private final static String TAG = "TokenUpdate";
	private static final String UPDATETOKEN = "UPDATETOKEN";
	private static final String UPDATESUCCESS = "UPDATESUCCESS";
	private String token; 

    public TokenUpdate(Handler tmpHandler, Context context) {
    	super(tmpHandler, context);
    	mode_string = "FORMATION";
		first_message = "FORMATIONREQUEST";
    }
    
    public void update(String token){
    	Log.d(TAG, "update the received token to the server");
    	//////////////
				
		this.token = token;
		
		updateThread current = new updateThread();
		current.start();
    	
    }
    
    private class updateThread extends Thread{ 
		public void run(){
			writeOut(UPDATETOKEN + TILDE + token, ENCRYPT);
			Log.d(TAG, "***What happened 1");
			String parsedmessage = readIn(DECRYPT);
			Log.d(TAG, "***What happened 2");
			if(!parsedmessage.equals(UPDATESUCCESS)){	
				Log.d(TAG, "***What happened 3");
				endConnectThread(THROW_ERROR);
			} else {
		    //Tell the object that called us that we've logged in properly
			// Send back to activity
			    //mHandler.obtainMessage(BluetoothChat.UP_SERVER).sendToTarget();
				Log.d(TAG, "***What happened 4");
			    endConnectThread(false);
		    /*    Bundle bundle = new Bundle();
		        bundle.putString(BluetoothChat.UP_FINISH, UPDATESUCCESS);
		        msg.setData(bundle);
		        mHandler.sendMessage(msg);*/
			}
			
		}
	}
}