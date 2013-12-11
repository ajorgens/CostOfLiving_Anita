package com.example.costoflivingdiary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ListActivity {
	final static String SAVE_ITEM_FILE = "com.example.costoflivingdiary.itemlist";
	final static String SAVE_PREF_FILE = "com.example.costoflivingdiary.preflist";
	final static String TAG = "COST OF LIVING";
	final static String ITEM_INDEX = "ITEM_INDEX";
	final static ArrayList<CostOfLivingItem> LIST = new ArrayList<CostOfLivingItem>();
	final static ArrayList<PreferenceItem> PREF_LIST = new ArrayList<PreferenceItem>();
	private boolean mFooterAdded = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//PREF_LIST.add(new PreferenceItem("United States", true));
		loadPref();
		loadItems();
		addItems();
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		if (!mFooterAdded) {
			mFooterAdded = true;
			LayoutInflater inflater = getActivity().getLayoutInflater();
			View view = inflater.inflate(R.layout.itemfooter, null);
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(),
							AddItemActivity.class);
					startActivity(intent);
				}
			});
			getListView().addFooterView(view);
		}
	}

	public void addItems() {
		setListAdapter(new ItemAdapter(getActivity(), R.layout.colitem, LIST));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.preferenceSettings:
			Intent intent = new Intent(getActivity(),
					PreferenceListActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void onResume() {
		super.onResume();
		addItems();
	}

	public static PreferenceItem getDefaultPreference() {
		for (PreferenceItem item : PREF_LIST) {
			if (item.isDefault()) {
				return item;
			}
		}
		return null;
	}

	private Activity getActivity() {
		return this;
	}

	private class ItemAdapter extends ArrayAdapter<CostOfLivingItem> {
		private ArrayList<CostOfLivingItem> items;
		private ItemAdapter adapter;
		public ItemAdapter(Context context, int textViewResourceId,
				ArrayList<CostOfLivingItem> objects) {
			super(context, textViewResourceId, objects);
			this.items = objects;
			adapter = this;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;

			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getActivity()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.colitem, null);
			}

			final CostOfLivingItem item = items.get(position);
			final int pos = position;

			v.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Bundle bundle = new Bundle();
					bundle.putInt(ITEM_INDEX, pos);
					Intent intent = new Intent(getActivity(),
							PrefListPriceActivity.class);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			});
			 v.setOnLongClickListener( new OnLongClickListener() {
	            	public boolean onLongClick(View v) {
	                	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	                	builder.setMessage("Delete " + item.getItem() + "?");
	                	builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	                	           public void onClick(DialogInterface dialog, int id) {
	                	              MainActivity.LIST.remove(pos);
	                	              adapter.notifyDataSetChanged();
	                	           }
	                	       });
	                	builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	                	           public void onClick(DialogInterface dialog, int id) {
	                	           }
	                	       });
	                	AlertDialog dialog = builder.create();
	                	dialog.show();
	                	return true;
	                }
	            });
			 

			if (item != null) {
				TextView itemView = (TextView) v.findViewById(R.id.item);
				TextView price = (TextView) v.findViewById(R.id.price);
				TextView pref = (TextView) v.findViewById(R.id.pref);

				itemView.setText("Item: " + item.getItem());
				price.setText("Price: " + item.getPriceString());
				// TODO need to add in price from database
				PreferenceItem prefItem = getDefaultPreference();
				if (prefItem != null) {
					String country = prefItem.getPreference();
					if(country.contains(" ")){
						country = country.replace(" ", "+");
					}
					String result = queryNumbeo(country);
					TreeMap<String, String> averagePrices = getAveragePrices(result);
					if(country.contains("+")){
						country = country.replace("+", " ");
					}
					pref.setText(country + ": " + averagePrices.get(item.getItem()));
					
				} else {
					pref.setText("No Preference");
				}

			}
			return v;
		}
		public TreeMap<String, String> getAveragePrices(String result) {

			String[] array = result.split(",\"item_name\":\"");
			String[] averagePrice = result.split("\"average_price\":");
			ArrayList<String> prices = new ArrayList<String>();
			ArrayList<String> items = new ArrayList<String>();
			TreeMap<String, String> averagePricePerItem = new TreeMap<String, String>();
			for (String a : averagePrice) {
				if (!a.startsWith("{")) {
					String[] arr = a.split(",\"");
					prices.add(arr[0]);
				}
			}
			for (String s : array) {
				if (!s.startsWith("{")) {
					String[] item = s.split("\",\"");
					items.add(item[0]);
				}
			}
			for (int i = 0; i < items.size(); i++) {
				averagePricePerItem.put(items.get(i), prices.get(i));
			}
			return averagePricePerItem;
		}

		public String queryNumbeo(String country) {
			QueryNumbeo q = new QueryNumbeo();
			q.country = country;
			q.execute();
			// have to wait for doInBackground to finish so q.results is no longer
			// null
			if(q.results == null) {
				try {
					q.get(1000, TimeUnit.MILLISECONDS);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return q.results;
		}
	}
	private void loadItems() {
		BufferedReader reader = null;
		
		try {
			FileInputStream fis = openFileInput(SAVE_ITEM_FILE);
			reader = new BufferedReader(new InputStreamReader(fis));

			String line, name = null;
			String[] params;
			float price;// = -1;
			while (null != (line = reader.readLine())) {
				params = line.split(",");
				name = params[0];
				try{
					price = Float.parseFloat(params[1]);
				}catch(Exception e){
					price = -1;
				}
				LIST.add(new CostOfLivingItem(name, price));
			}

		} catch (FileNotFoundException e) {
			Log.i(TAG, "File not found");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
	private void loadPref() {
		BufferedReader reader = null;
		
		try {
			FileInputStream fis = openFileInput(SAVE_PREF_FILE);
			reader = new BufferedReader(new InputStreamReader(fis));

			String line, pref = null;
			String[] params;
			boolean isDefault;// = -1;
			while (null != (line = reader.readLine())) {
				params = line.split(",");
				pref = params[0];
				try{
					isDefault = Boolean.parseBoolean(params[1]);
				}catch(Exception e){
					isDefault = false;
				}
				PreferenceItem itemPref = new PreferenceItem(pref, isDefault);
				if(!PREF_LIST.contains(itemPref)){
					PREF_LIST.add(itemPref);
				}
			}
//			if(PREF_LIST.size() < 1)
//				PREF_LIST.add(new PreferenceItem("United States", true));

		} catch (FileNotFoundException e) {
			Log.i(TAG, "File not found");
			PREF_LIST.add(new PreferenceItem("United States", true));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}








	protected void onDestroy(){
		super.onDestroy();
		//Save files
		PrintWriter writer = null;
		try {
			FileOutputStream fos = openFileOutput(SAVE_ITEM_FILE, MODE_PRIVATE);
			writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					fos)));
			for(CostOfLivingItem curr : LIST){
				
				writer.println(curr.getItem()+","+curr.getPriceString());
			}
			
			fos = openFileOutput(SAVE_PREF_FILE, MODE_PRIVATE);
			writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					fos)));
			for(PreferenceItem curr : PREF_LIST){
				writer.println(curr.getPreference()+","+curr.isDefault());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != writer) {
				writer.close();
			}
		}
		
	}

}
