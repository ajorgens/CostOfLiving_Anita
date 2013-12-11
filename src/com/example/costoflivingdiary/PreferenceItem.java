package com.example.costoflivingdiary;

public class PreferenceItem {

	private String mPreference;
	private boolean mIsDefault;
	
	public PreferenceItem(String preference) {
		mPreference = preference;
	}
	
	public PreferenceItem(String preference, boolean isDefault) {
		mPreference = preference;
		mIsDefault = isDefault;
	}
	
	public String getPreference() {
		return mPreference;
	}
	
	public boolean isDefault() {
		return mIsDefault;
	}
	
	public void setDefault(boolean bool) {
		mIsDefault = bool;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof PreferenceItem){
			PreferenceItem p = (PreferenceItem) o;
			if(p.mPreference.equals(((PreferenceItem) o).mPreference)){
				return true;
			}
		}
		return false;
		
	}
}
