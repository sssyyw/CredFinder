package com.anrl.micandroid.UI;

/*
 * Author Yiwei Shi; yiweishi22@gmail.com
 * Date: 2011.10
 * Name: CF
 * This is the first activity a user will see Consider it the main{}
 * Prototype from Wesley Ellis; wesleyjellis@gmail.com
 */


import com.anrl.micandroid.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.util.Log;


public class MainUI extends Activity {

	// VARS

	private static final String TAG = "CredFinder";	
	
	//This is essentially our main{} 
	//We need to setup the UI and associated event listeners
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "++BEGIN MICANDROID++");
		//Let the super do all the really hard stuff
		super.onCreate(savedInstanceState);
		//Get rid of the ugly title that is usually there
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		//Set the layout from res/layout/main.xml
		//Note R.layout.main maps to res/layout/main.xml
		setContentView(R.layout.main);
		

		final Button makeCloudButton = (Button) findViewById(R.id.SetupCloud);

		final Button btExchange = (Button) findViewById(R.id.bluetoothex);
		
		btExchange.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BTService();
			}
		});
		
		
	
		makeCloudButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				wizard();
			}
		});

	}

	private void BTService() {
			//This is our test activity. Doesn't do anything ATM
		    Intent i = new Intent(this, BluetoothChat.class);
		    Log.d(TAG, "btexchange button ran");
			
			startActivity(i);
		}
	
	private void wizard() {
		//Start the cloud formation wizard
		//If we are already registered, don't let the user form another cloud
		//Registration temp = Registration.getRegistration(getApplicationContext());
		//if(temp.isRegistered()){
		//	makeToast("Errm... You're already registered, please unregister before setting up a cloud");
		//}
		//else{
			Intent i = new Intent(this, SetupWizardUI.class);
			startActivity(i);
		//}
	}

/*
	//Doesn't actually make toast, but creates a message popup
	private void makeToast(String message){
		Toast.makeText(getApplicationContext(), message,
				Toast.LENGTH_LONG).show();
	}


	//This gets called when we call showDialog
	//It let's us configure the nature of the dialog
	//In this case we just want a simple indeterminate length loading box
	@Override
	protected Dialog onCreateDialog(int id) {
		ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage("Loading");
		//No progress bar, just a spinner
		dialog.setIndeterminate(true);
		//Can't be cancelled by pressing back
		dialog.setCancelable(false);
		return dialog;
	}
	
	//This gets called when the menu button is pressed
	//Tell the system what xml file we want to use for the menu layout
	//In this case res/menu/mainmenu.xml
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		return true;
	}
	
	//This gets called when one of the menu items is pressed
	//Figure out what we should true
	//Needs to return true for some reason
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case R.id.BeginWizard:
//			wizard();
			break;
		case R.id.Dissolve:
			dissolve();
			break;
		case R.id.BTMenu:
	//		MICAUtil();
			break;
		case R.id.UnReg:
//			unregister();
			break;
		case R.id.TEST:
			test();
			break;
		}

		return true;
	}
*/
}