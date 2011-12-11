package com.anrl.micandroid.checkboxlist;
//Author Wesley Ellis; wesleyjellis@gmail.com

import com.anrl.micandroid.MICAdevice;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CheckBoxTextView extends LinearLayout{

	private TextView mText;
    private CheckBox mCheckBox;
    private CheckBoxItem mCheckBoxText;
	
	public CheckBoxTextView(Context context, CheckBoxItem cbt) {
		super(context);
		//Set the oritentaiton
		this.setOrientation(HORIZONTAL);
		
		
		mCheckBoxText = cbt;
		//Create a checkbox
		mCheckBox = new CheckBox(context);
		//Set Alignment Left
		mCheckBox.setPadding(0, 0, 20, 0);
		//Make sure it's in the proper state when first drawn
		mCheckBox.setChecked(cbt.getChecked());
		//Add the View (checkbox extends view) to the parent View
		//addView is fromLinearLayout
		addView(mCheckBox, new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, 
				LayoutParams.WRAP_CONTENT));
		//Create a TextView label
		mText = new TextView(context);

		mText.setText(cbt.getText());
		//Add the View to the parent View
		addView(mText, new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, 
				LayoutParams.WRAP_CONTENT));
		//Create an even listener to toggle the state of the checkbox
		//when it is clicked
		mCheckBox.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setCheckBoxState(mCheckBox.isChecked());
			}
		});
		
	}
	public void setDevice(MICAdevice device){
		mCheckBoxText.setDevice(device);
		mText.setText(device.toString());
	}
	public void setCheckBoxState(boolean bol){
		mCheckBox.setChecked(bol);
		mCheckBoxText.setChecked(bol);
	}
}
