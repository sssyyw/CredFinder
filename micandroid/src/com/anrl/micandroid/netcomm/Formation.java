package com.anrl.micandroid.netcomm;
//Author Wesley Ellis; wesleyjellis@gmail.com


import java.io.FileOutputStream;

import java.io.IOException;

//import java.util.HashSet;
//import java.util.Set;
//import com.anrl.micandroid.MICAdevice;
import com.anrl.micandroid.Messages;
//import com.anrl.micandroid.bluetooth.RegReq;
//import com.anrl.micandroid.crypto.AES;
//import com.anrl.micandroid.crypto.Base64;
//import com.anrl.micandroid.crypto.Random;
//import com.anrl.micandroid.crypto.Registration;
//import com.anrl.micandroid.crypto.Util;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.Handler;
import android.util.Log;



public class Formation extends NetComm {

    
	private String micapassword;
	private String micalogin;
//	private String uplogin;
//	private String uppassword;
//	private Set<MICAdevice> devicelist = new HashSet<MICAdevice>();
	private String selfAddress;
//	private String upkey;
//	private String clientkey;



	private final static String TAG = "Formation";
	private static final String FORMATION_LOGIN = "FORMATIONLOGIN";
	private static final String FORMATION_LOGIN_SUCCESS = "FORMATIONLOGINVERYFIED";
	private static final String FORMATION_LOGIN_FAILED = "FORMATIONLOGINFAILED";
	private static final String FORMATION_DEVICES = "FORMATIONDEVICES";
	//private static final String FORMATION_CLIENT_KEY = "FORMATIONCLIENTCLOUDKEY";
	private static final String FORMATION_SERVER_KEY = "FORMATIONSERVERCLOUDKEY";
	//private static final String FORMATION_Y_VALUES = "FORMATIONYVALUES";
	//private static final String FORMATION_UP = "FORMATIONUP";
	private static final String FORMATION_FINISHED = "FORMATIONFINISHED";
    
	//Store token
	private String tokenReceived = null;
	private String FILENAME = "token";

	//TODO deal with incorrect login attempts

	public Formation(Handler tmpHandler, Context context) {
		super(tmpHandler, context);
		//Used by netcomm
		mode_string = "FORMATION";
		//Also used in netcomm
		first_message = "FORMATIONREQUEST";
		//this is what our address is
		selfAddress = ANDROID + AT + BluetoothAdapter.getDefaultAdapter().getAddress().replaceAll(COLON, "");

	}


	public void doStep2(String tmpUser, String tmpPass){
		Log.d(TAG, "Begin Stage 2 of formation");
		mHandler.obtainMessage(Messages.NETCOMM, LOADING, 0).sendToTarget();

		//Set the username and password
		micalogin = tmpUser;
		micapassword = tmpPass;
		loginThread current = new loginThread();
		current.start();

	}



	private class loginThread extends Thread{
		public void run(){
			writeOut(FORMATION_LOGIN + TILDE + micalogin + COMMA + micapassword, ENCRYPT);

			String parsedmessage = readIn(DECRYPT);
			String[] inputs = parsedmessage.split(COMMA);

			if(inputs[0].equals(FORMATION_LOGIN_SUCCESS)){
				//Tell the object that called us that we've logged in properly
				mHandler.obtainMessage(Messages.NETCOMM, SUCCESS, ARGS.LOGIN).sendToTarget(); // Do not do the following step
			}
			else if(inputs[0].equals(FORMATION_LOGIN_FAILED)){
				//Wrong username or password
				//Check to see how many login attempts we have left
				Log.d(TAG, inputs[1] + " login attempts left");
				mHandler.obtainMessage(Messages.NETCOMM, ERROR, ARGS.LOGIN, inputs[1]).sendToTarget();
				return;
			}
			else{
				endConnectThread(THROW_ERROR);
			}
		}
	}

	public void doStep3(){
		Log.d(TAG, "commencing Stage 3 of formation");
		mHandler.obtainMessage(Messages.NETCOMM, LOADING, 0).sendToTarget();

		//These are the devices we discovered
		//devicelist = tempdevicelist;
        
		cloudKeyThread current = new cloudKeyThread();
		current.start();
	}


	private class cloudKeyThread extends Thread{
		public void run(){
/*
			//This is going to be our part of the client key
			byte[] temp = Random.generateRandom(16);
			clientkey = Util.makeStringfromBytes(temp);

			//This is the UPKey
			temp = Random.generateRandom(16);
			upkey = Util.makeStringfromBytes(temp);
*/
			writeOut(FORMATION_DEVICES + TILDE + formatDeviceList(), ENCRYPT);

			String inputLine = readIn(DECRYPT);

			String[] inputs = inputLine.split(TILDE);
			if(!inputs[0].equals(FORMATION_SERVER_KEY))
				endConnectThread(THROW_ERROR);
			tokenReceived = inputs[1].substring(0,inputs[1].length() - 5);
			
			mHandler.obtainMessage(Messages.NETCOMM, SUCCESS, ARGS.GENERAL).sendToTarget();
			Log.d(TAG, "Got the tokens, needs to store");
			Log.d(TAG, tokenReceived.substring(0, 100));
	
			/*
			 * Store tokens in a file for future use. 
			 */
			
			
			try {
			    FileOutputStream fos = mContext.openFileOutput(FILENAME, Context.MODE_PRIVATE);
		    		
			    fos.write(tokenReceived.getBytes());
			    Log.d(TAG, "**************The length of token Received is :" + tokenReceived.getBytes().length);
			    
			    	
			    fos.close();
			    
			    } catch (IOException e){
			    	System.out.println("cannot write to token");
			    	e.printStackTrace();
			    }  
	/*		
			String hashItem = "";
			String[] hashStore = tokenReceived.split(",");
			
			try {
			    FileOutputStream fos = mContext.openFileOutput(FILENAME, Context.MODE_PRIVATE);
			
			    	for(int i = 0; i < hashStore.length; i++){
				
			    		hashItem = hashStore[i];
			    		fos.write((hashItem + "\n").getBytes());
			    		Log.d(TAG, "" + (hashItem + "\n").getBytes().length);
			    		Log.d(TAG, "is " + "n".getBytes().length);
			    	}
			    	fos.close();
			    } catch (IOException e){
			    	System.out.println("cannot write to token");
			    	e.printStackTrace();
			    }    
	*/		    
			    
			//I know, I know, this isn't a very efficient search, but it's simple
			//And I doubt we will be dealing with more than 10 devices at any one time
			//Hate me later when I'm wrong

			//input are in the format DEVICENAME1:YVALUE1,DEVICENAME2:YVALUE
	/*		String[] yvalues = inputs[1].split(COMMA);
			for(int i = 0; i < yvalues.length; i++){
				String[] tempp = yvalues[i].split(COLON);
				String address = tempp[0];
				String yvalue = tempp[1];
				if(address.equals(selfAddress)){
					Registration regState = Registration.getRegistration(mContext);
					boolean test = regState.register(clientkey, yvalue, upkey);
					Log.d(TAG, "we are registered successfully: " + Boolean.toString(test));
				}
				else{
					for(MICAdevice device : devicelist){
						if(device.legacyGetAddress().equals(address)){
							//I suspect this is probably going to mess up on a large set of devices...
							RegReq.talkTo(mHandler, device, yvalue, clientkey, upkey);
						}
					}
				}
			}
	*/
			/*
			 * Formulation(Cloud setup) ends here.
			 */
			
		}
	}

	public void doStep4(String UPuser, String UPpass){
		Log.d(TAG, "commencing final step");
		mHandler.obtainMessage(Messages.NETCOMM, LOADING, 0).sendToTarget();
//		uplogin = UPuser;
//		uppassword = UPpass;
		UPthread current = new UPthread();
		current.start();
	}



	private class UPthread extends Thread{
		public void run(){
			//Get the UPkey
	//		byte[] key = Util.makeBytesfromString(upkey);
			//Encrypt the username and password the user gave us
	//		String enclogin = Base64.encodeBytes(AES.encryptByte(uplogin.getBytes(), key));
	//		String encpass = Base64.encodeBytes(AES.encryptByte(uppassword.getBytes(), key));

	//		String toWrite = FORMATION_UP + TILDE + enclogin + COMMA + encpass;
	//		writeOut(toWrite, ENCRYPT);

			String inputline = readIn(DECRYPT);

			if(inputline.equals(FORMATION_FINISHED)){
				mHandler.obtainMessage(Messages.NETCOMM, SUCCESS, ARGS.GENERAL).sendToTarget();
			}
			else{
				endConnectThread(THROW_ERROR);
			}
		}
	}

	//This makes a pretty list of all the devices we have found
	private String formatDeviceList() {
		
		//String result = "";
		/*
		for (MICAdevice device : devicelist) {
			String temp = device.legacyGetAddress();
			result = temp + COMMA + result;
		}
		result = result + selfAddress;
		*/
		// better replace ANDROID by actual name 
		
		return selfAddress;
		
		
	}
}
