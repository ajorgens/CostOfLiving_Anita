package com.example.costoflivingdiary;

import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PreferenceListActivity extends ListActivity {

	private boolean mFooterAdded = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		if(!mFooterAdded) {
			mFooterAdded = true;
			LayoutInflater inflater = getActivity().getLayoutInflater();		
			View view = inflater.inflate(R.layout.preffooter, null);
			view.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(), AddPreferenceActivity.class);
					startActivity(intent);
				}
			});
			getListView().addFooterView(view);
		}
		addPreferences();
	}
	
	public Activity getActivity() {
		return this;
	}
	
	public void addPreferences() {
		setListAdapter(new ItemAdapter(getActivity(),
				R.layout.prefitem, MainActivity.PREF_LIST));
	}
	
    public void onResume(){
    	super.onResume();
        addPreferences();
    }
    
    private class ItemAdapter extends ArrayAdapter<PreferenceItem> {
    	private ArrayList<PreferenceItem> items;
    	
        public ItemAdapter(Context context, int textViewResourceId,
                ArrayList<PreferenceItem> objects) {
            super(context, textViewResourceId, objects);
            this.items = objects;
        }
        
        public View getView(int position, View convertView, ViewGroup parent){
            View v = convertView;

            if(v == null){
                LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
                v = vi.inflate(R.layout.prefitem, null);
            }

            final PreferenceItem item = items.get(position);
            final int pos = position;
            
            v.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                	builder.setMessage("Set " + item.getPreference() + " as the default?");
                	builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                	           public void onClick(DialogInterface dialog, int id) {
                	               for(PreferenceItem i: items) {
                	            	   i.setDefault(false);
                	               }
                	               item.setDefault(true);
                	           }
                	       });
                	builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                	           public void onClick(DialogInterface dialog, int id) {
                	           }
                	       });
                	AlertDialog dialog = builder.create();
                	dialog.show();
                }
            });

            if (item != null){
                TextView pref = (TextView) v.findViewById(R.id.prefEntry); 
                
                pref.setText(item.getPreference());

            }       
            return v;           
        }
    }
}
