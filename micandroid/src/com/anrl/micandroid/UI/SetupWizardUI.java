package com.anrl.micandroid.UI;
//Author Wesley Ellis; wesleyjellis@gmail.com

//import java.util.Set;

//import com.anrl.micandroid.bluetooth.BluetoothMessage;
//import com.anrl.micandroid.bluetooth.ListReq;
import com.anrl.micandroid.checkboxlist.CheckBoxAdapter;
import com.anrl.micandroid.netcomm.Formation;
import com.anrl.micandroid.netcomm.NetComm;
//import com.anrl.micandroid.MICAdevice;
import com.anrl.micandroid.Messages;
import com.anrl.micandroid.R;
import com.anrl.micandroid.ReturnObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
//import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SetupWizardUI extends Activity{

	// VARS
	private CheckBoxAdapter cba;
	//private ReturnObject returned = null;
	private Formation serverThread = null;
	//private ListView pairedListView = null;
	private int screen = begin;
	
	private static final String TAG = "CredFinder";
	private static final int begin = 1;
	private static final int login = 2;
	private static final int bluetooth = 3;
	private static final int upscreen = 4;
	private static final int errorscreen = 5;
	private static final int successscreen = 6;
	private static final int DIALOG = 2;
	private static final String SCREEN_STATE = "screenstate";
	private static final String CRYPT_ERROR = "Cryptographic Failure";
	private static final String SERVER_UNAVAILABLE = "Server Unreachable";
	private static final String NO_MORE_ATTEMPTS = "You have run out of " +
			"login attempts. Please wait some unspecified length of time";
	private static final String WARN_MESSAGE = "Are you sure you want to exit?";
	private static final String IP_BLOCKED = "Server isn't talking to use for a while";
	private static final String YES = "Yes";
	private static final String NO = "NO";
	private static final String ZERO = "0";

	// END VARS

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "Wizard-ing commence");

		super.onCreate(savedInstanceState);

		//getLastNonConfiguration instance returns an Object of our choosing
		//We set that object when we are paused, and can retrieve it here
		ReturnObject temp = (ReturnObject) getLastNonConfigurationInstance();
		//If this is the first time we've been run temp will be null
		if(temp != null){
			//returned = temp;
			serverThread = temp.mThread;
			//point serverThread at the correct handler
			serverThread.setHandler(mHandler);
		}
		else{
			//If this is the first time we've been run, create a new serverThread
			serverThread = new Formation(mHandler, getApplicationContext());
		}
		//Get rid of the damn title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//If we've been run before attempt to load screenstate, which is how we keep track
		//of where we are in the wizard
		if(savedInstanceState != null){
			screen = savedInstanceState.getInt(SCREEN_STATE);
		}
		//Set screen chooses which screen to load
		//If we are running for the first time thehn screen is set to begin
		setScreen(screen);

	}


	//I'm sorry for the nested switch statements, but it's more sane than
	//keeping track of unique ints
	//This handler is passed messages for hashreqs, netcomm, formation and listreqs
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			//What kind of message?
			switch (msg.what) {
/*			case Messages.BLUETOOTH:
				//What kind of bluetooth message?
				switch (msg.arg1){
				case BluetoothMessage.LIST:
					//What happened with List?
					switch(msg.arg2){
					case BluetoothMessage.ARGS.LOADING:
						showDialog(DIALOG);
						break;
					case BluetoothMessage.ARGS.DONE:
						Log.d(TAG, "Finished ListReqs!");
						dismissDialog(DIALOG);
						break;
					case BluetoothMessage.ARGS.SUCCESS:
						//Get the MICAdevice we are passed
						MICAdevice temp = (MICAdevice) msg.obj;
						//Add it to the CheckboxAdapter 
						cba.addItem(temp, false);
						//Tell CBA it needs updating
						//This makes sure the visable CBA and the actual CBA are in sync
						cba.notifyDataSetChanged();
						break;
					}
				}
				break;
*/			case Messages.NETCOMM:
				//What kind of Netcomm message?
				switch(msg.arg1){
				case NetComm.LOADING:
					showDialog(DIALOG);
					break;
				case NetComm.SUCCESS:
					//What kind of Success?
					switch(msg.arg2){
					case NetComm.ARGS.GENERAL:
						//This gets called when we recieve a formation success message
						dismissDialog(DIALOG);
						//advance wizard to the success screen
						setScreen(successscreen);
						break;
					case NetComm.ARGS.CRYPTO:
						//Crypto has been setup!
						dismissDialog(DIALOG);
						//Advance to the MICA login screen
						setScreen(login);
						break;
					case NetComm.ARGS.LOGIN:
						//We logged in ok!
						dismissDialog(DIALOG);
						//Move to the bluetooth device screen
						setScreen(bluetooth);
						break;
					case NetComm.ARGS.BLUETOOTH:
						//We've successfully sent reg reqs to all the devices!
						dismissDialog(DIALOG);
						setScreen(upscreen);
						break;
					}
					break;
				case NetComm.DONE:
					//Generally finished
					dismissDialog(DIALOG);
					break;
				case NetComm.ERROR:
					//What kind of error?
					switch(msg.arg2){
					case NetComm.ARGS.CRYPTO:
						dismissDialog(DIALOG);
						//Go to error screen
						error(CRYPT_ERROR);
						break;
					case NetComm.ARGS.LOGIN:
						//We failed to login
						//See how many login tries we have left
						String tries_left = (String) msg.obj;
						dismissDialog(2);
						//If we have no more tries left
						if(tries_left.equals(ZERO)){
							error(NO_MORE_ATTEMPTS);
						}
						else{
							Toast.makeText(getApplicationContext(), 
									"Invalid Username or Password. Please Try again",
									Toast.LENGTH_LONG).show();
						}
						break;
					case NetComm.ARGS.GENERAL:
						dismissDialog(2);
						error(null);
						break;
					case NetComm.ARGS.SERVER_UNREACHABLE:
						dismissDialog(2);
						error(SERVER_UNAVAILABLE);
						break;
					case NetComm.ARGS.IP_BLOCKED:
						dismissDialog(2);
						error(IP_BLOCKED);
						break;
					}
				}
			}
		}
	};
	//Called when we press the discover button
	private void discover() {
		//Clear the checkboxadapter of entries
		cba.clear();
		Log.d(TAG, "Commence listreq");
		//Get devices we have paired with
//		Set<BluetoothDevice> pairedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
		//ListReq.talkTo(mHandler, pairedDevices);
	}
	
	//Screen 1
	private void Begin1(){
		//Set the current screen to begin so that if we are interupted 
		screen = begin;
		//Set the layout to res/layout/wizard_start
		setContentView(R.layout.wizard_start1);

		final Button cancelButton1 = (Button) findViewById(R.id.cancel1);
		cancelButton1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				cancel();
			}
		});
		
		final Button nextButton1 = (Button) findViewById(R.id.begin1);
		nextButton1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//Start talking to the server
				serverThread.startConnectThread();
			}
		});


	}
	
	//Screen 2
	private void Login2() {
		// Setup step2
		//Set the screen to login, so we will get restored to here
		screen = login;
		//Set the screen to res/layout/wizard_mica
		setContentView(R.layout.wizard_mica2);
		final Button cancelButton2 = (Button) findViewById(R.id.cancel2);
		
		cancelButton2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				cancel();
			}
		});
		
		final Button nextButton2 = (Button) findViewById(R.id.next2);
		
		//Our Username and Password Fields
		final EditText userfield = (EditText) findViewById(R.id.username1);
		final EditText passfield = (EditText) findViewById(R.id.password1);
		
		//Set the next button to false until both fields are full
		nextButton2.setEnabled(false);
		
		//This textwatcher will enable the next button if both fields have at least 1 char
		TextWatcher textwatcher = new TextWatcher(){
			public void afterTextChanged(Editable s) {
				if(userfield.getText().length() > 0 && passfield.getText().length() > 0){
					nextButton2.setEnabled(true);
				}
				else{
					nextButton2.setEnabled(false);
				}
			}
			//These methods have to be implemented, but I don't use them
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
			public void onTextChanged(CharSequence s, int start, int before,int count) {}			
		};
		
		//Add the textwatcher to both fields
		userfield.addTextChangedListener(textwatcher);
		passfield.addTextChangedListener(textwatcher);

		nextButton2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String micausername = userfield.getText().toString();
				String micapassword = passfield.getText().toString();
				//server thread doStep2(username, password)
				//Starts a thread that contacts the server with our username and password
				serverThread.doStep2(micausername, micapassword);
			}
		});

		Button backButton2 = (Button) findViewById(R.id.back2);
		backButton2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//Go back a step
				Begin1();
			}
		});
		
		//Set the cursor/focus to the username field
		userfield.requestFocus();

	}
	
	//Screen 3
	private void Bluetooth3() {
		// Setup step 3
		//Set this so if we get restored it will be to here
		screen = bluetooth;
		//Screen layout from res/layout/wizard_discover.xml
		setContentView(R.layout.wizard_discover3);
		
		/*		//Intialize our CheckboxAdapter
		//See com.anrl.micandroid.checkboxlist
		cba = new CheckBoxAdapter(this);
		//Find out ListView by it's android:id
		pairedListView = (ListView) findViewById(R.id.ListView01);
		//Set the listview to be paired with our CBA
		pairedListView.setAdapter(cba);
		//Set the view to be seen when the listview is empty
		//In this case, the text that says "Time to discover some devices"
		
		pairedListView.setEmptyView(findViewById(R.id.EmptyListView));
		
		//If we have been restored at some point check to see if we have
		//contents for the CBA from ReturnObject
		if(returned != null){
			if(returned.mItems != null){
				//Set the content
				cba.setListItems(returned.mItems);
				//Tell the CBA it needs to be updated
				cba.notifyDataSetChanged();
			}
		}
*/
		final Button cancelButton3 = (Button) findViewById(R.id.cancel3);
		cancelButton3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				cancel();
			}
		});

		final Button nextButton3 = (Button) findViewById(R.id.next3);		
		nextButton3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//Talk to the server about the MICA devices we have
				//Also send RegReqs
				serverThread.doStep3();
			}
		});

		//Disable the next button until we have tried to discover devices at least once
//		nextButton3.setEnabled(false);
/*
		Button discoverButton = (Button) findViewById(R.id.discoverwizard);
		discoverButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				discover();
//				nextButton3.setEnabled(true);
			}
		});
*/
		final Button backButton0 = (Button) findViewById(R.id.back3);
		backButton0.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//So we can go back a step..
				Login2();
			}
		});
	}

	
	//Screen 4
	private void Final4() {
		//Set this so that if we get restored, we are in the right place
		screen = upscreen;
		//Set the layout to res/layout/wizard_up4.xml
		setContentView(R.layout.wizard_up4);
		
		Button cancelButton4 = (Button) findViewById(R.id.cancel4);
		cancelButton4.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				cancel();
			}
		});
		
		final Button nextButton4 = (Button) findViewById(R.id.next4);
		
		//Disable the next button until the username/password fields are filled
		nextButton4.setEnabled(false);
		final EditText userfield = (EditText) findViewById(R.id.username2);
		final EditText passfield = (EditText) findViewById(R.id.password2);
		//This is will react to changes in the contents of the textview
		//Mainly whether they are filled or not
		TextWatcher textwatcher = new TextWatcher(){
			public void afterTextChanged(Editable s) {
				if(userfield.getText().length() > 0 && passfield.getText().length() > 0){
					//If not empty, enable it
					nextButton4.setEnabled(true);
				}
				else{
					nextButton4.setEnabled(false);
				}
			}
			//Methods that need to be implemented but I don't ever use
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
			public void onTextChanged(CharSequence s, int start, int before,int count) {}			
		};
		userfield.addTextChangedListener(textwatcher);
		passfield.addTextChangedListener(textwatcher);
		
		nextButton4.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//Start the final thread
				String upusername = userfield.getText().toString();
				String uppassword = passfield.getText().toString();
				serverThread.doStep4(upusername, uppassword);
			}
		});

		Button backButton4 = (Button) findViewById(R.id.back4);
		backButton4.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				//Go back a step
				Bluetooth3();
			}
		});
		//Set the cursor to the user name field 
		userfield.requestFocus();
	}


	//Success Screen
	private void success() {
		//We're done now, so close the thread
		serverThread.endConnectThread(false);
		screen = successscreen;
		//Set the layout from res/layout/success.xml
		setContentView(R.layout.success);
		Button doneButton = (Button) findViewById(R.id.successdone);
		doneButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//finish is a method from activity
				finish();
			}
		});
	}
	
	//Error Screen
	private void error(String message) {
		//We've had an error, so close the socket to the server
		serverThread.endConnectThread(false);
		screen = errorscreen;
		//Set layout from res/layout/failure.xml
		setContentView(R.layout.failure);
		TextView errorDetails = (TextView) findViewById(R.id.ErrorDetails);
		if(message != null){
			//If we've got an error message to use
			errorDetails.setText(message);
		}


		Button cancelButton6 = (Button) findViewById(R.id.cancel6);
		cancelButton6.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				cancel();
			}
		});
		
		Button retryButton = (Button) findViewById(R.id.retry);
		retryButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				serverThread = new Formation(mHandler, getApplicationContext());
				Begin1();
			}
		});
	}
	
	//This gets called when the back button is pressed
	//We don't want to got straight back 
	private void warn() {
		//Get android's dialog builder
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		//Set the message
		builder.setMessage(WARN_MESSAGE)
				.setCancelable(false)
				.setPositiveButton(
						//this is the code for the positive button
						YES,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								serverThread.endConnectThread(false);
								SetupWizardUI.this.finish();
							}})
				.setNegativeButton(
						//this is the code for the negative button
						NO,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
					}});
		//Actually create the dialog
		AlertDialog alert = builder.create();
		//Show the dialog
		alert.show();
	}
	
	private void cancel(){
		serverThread.endConnectThread(true);
		finish();
	}
	
	//We can set the screen to wherever we need it to go from here
	private void setScreen(int tmpscreenstate) {
		screen = tmpscreenstate;
		switch (screen) {
		case begin:
			Begin1();
			break;
		case login:
			Login2();
			break;
		case bluetooth:
			Bluetooth3();
			break;
		case upscreen:
			Final4();
			break;
		case successscreen:
			success();
			break;
		case errorscreen:
			error(null);
			break;
		}
	}
	
	//This the the loading indicator
	@Override
	protected Dialog onCreateDialog(int id) {
		ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage("Please Hold, your call is important to us");
		dialog.setIndeterminate(true);
		dialog.setCancelable(false);
		return dialog;
	}
	
	//This is where we override the behaviour of any key
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//We just want to do something on the back button
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			warn();
		}
		return super.onKeyDown(keyCode, event);
	}


	//This method is called when our application is being paused for whatever reason
	//This can happen because we are changing orientation (eg: opening the keyboard)
	//Or because the user is recieving a phone call or something
	//Save the screen state by placing the int in a bundle
	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putInt(SCREEN_STATE, screen);
		super.onSaveInstanceState(savedInstanceState);
	}

	//This is also called when we are getting paused
	//We want to create a new ReturnObject to be retrieved onRestoreInstanceState
	@Override
	public Object onRetainNonConfigurationInstance(){
		if(screen == bluetooth){
			return new ReturnObject(serverThread, cba.getListItems());
		}
		else{
			return new ReturnObject(serverThread, null);
		}
	}

}
