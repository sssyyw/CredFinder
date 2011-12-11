package com.anrl.micandroid.checkboxlist;
//Author Wesley Ellis; wesleyjellis@gmail.com
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import com.anrl.micandroid.MICAdevice;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


//Based off of code from bluefloyd8
// @ http://www.anddev.org/checkbox_text_list___extension_of_iconified_text_tutorial-t771.html

//Class extends BaseAdapter	which lets you interface with a ListView
//Read the android docs on BaseAdapter and ListViews if this is confusing

public class CheckBoxAdapter extends BaseAdapter{
	
	private Context mContext;
	//Internal list of items that have been added to the Adapter
	private List<CheckBoxItem> mItems = new ArrayList<CheckBoxItem>();	
	
	public CheckBoxAdapter(Context context){
		mContext = context;
	}
	
	public void addItem(CheckBoxItem it){
		mItems.add(it);
	}
	//Overload addItem so that it's easier to interface with
	public void addItem(MICAdevice device, boolean state){
		mItems.add(new CheckBoxItem(device, state));
	}
	//Used when we want to set the adapter to a restored state
	public void setListItems(List<CheckBoxItem> list){
		mItems = list;
	}
	
	public List<CheckBoxItem> getListItems(){
		return mItems;
	}
	
	//Useful method
	public HashSet<MICAdevice> getCheckedItems(){
		HashSet<MICAdevice> checked = new HashSet<MICAdevice>();
		for(CheckBoxItem item : mItems){
			if(item.getChecked()){
				checked.add(item.getDevice());
			}
		}
		return checked;
	}
	
	public int getCount() {
		return mItems.size();
	}

	public CheckBoxItem getItem(int arg0) {
		return mItems.get(arg0);
	}
	
	public void setText(CheckBoxItem text, int posistion){
		text = mItems.get(posistion);
	}
	
	public long getItemId(int position) {
		return position;
	}

	//This is what gets called when the items need to be drawn
	public View getView(int position, View convertView, ViewGroup parent) {
		CheckBoxTextView cbtv;
		cbtv = new CheckBoxTextView(mContext, mItems.get(position));
		return cbtv;
	}

	public void clear() {
		mItems.removeAll(mItems);	
	}
	
	public boolean isEmpty(){
		return mItems.isEmpty();
	}
}
