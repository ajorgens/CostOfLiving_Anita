package com.example.costoflivingdiary;

import java.util.ArrayList;

public class CostOfLivingItem {

	private String mItem;
	private float mPrice;

	
	public CostOfLivingItem(String item, float price) {
		mItem = item;
		mPrice = price;
	}
	
	public String getItem() {
		return mItem;
	}
	
	public void setItem(String item) {
		mItem = item;
	}
	
	public String getPriceString() {
		if (mPrice < 0) {
			return "Invalid price";
		}
		return Float.toString(mPrice);
	}
	
	public void setPrice(float price) {
		mPrice = price;
	}
	
}
