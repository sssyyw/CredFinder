package com.anrl.micandroid;
//Author Wesley Ellis; wesleyjellis@gmail.com

import java.util.List;

import com.anrl.micandroid.checkboxlist.CheckBoxItem;
import com.anrl.micandroid.netcomm.Formation;

public class ReturnObject {
	//This is a bad way to do it but... OH WELL
	//When the SetupWizard activity recieves an onPause
	//it creates this object that is returned when the activity is resumed
	//There are ways to pass primatives but this aren't even close to primatives
	
	public Formation mThread = null;
	public List<CheckBoxItem> mItems = null;
	
	public ReturnObject(Formation serverThread, List<CheckBoxItem> items){
		mThread = serverThread;
		mItems = items;
	}
}
