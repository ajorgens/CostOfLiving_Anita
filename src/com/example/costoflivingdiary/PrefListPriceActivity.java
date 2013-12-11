package com.example.costoflivingdiary;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PrefListPriceActivity extends ListActivity {
	public TreeMap<String, TreeMap<String, String>> countryPrices = new TreeMap<String, TreeMap<String, String>>();
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		Bundle bundle = getIntent().getExtras();
		if(bundle != null) {
			int index = bundle.getInt(MainActivity.ITEM_INDEX);
			ArrayList<String> prefStringList = new ArrayList<String>();
			
			for(int i = 0; i < MainActivity.PREF_LIST.size(); i++) {
				//TODO get prices somehow
				String country = MainActivity.PREF_LIST.get(i).getPreference();
				if(countryPrices.get(country) == null){
					String result = queryNumbeo(country.replace(" ", "+")).trim();
					TreeMap<String, String> current = getAveragePrices(result);
					countryPrices.put(country, current);
				}
				ArrayList<CostOfLivingItem> items = MainActivity.LIST;
				CostOfLivingItem item = items.get(index);
				String entry = "Price in " + country + ": " + countryPrices.get(country).get(item.getItem());
				prefStringList.add(entry);
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    R.layout.prefitem, R.id.prefEntry, prefStringList);
			setListAdapter(adapter);
		}
	}
	
	private TreeMap<String, String> getAveragePrices(String result) {

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

	private String queryNumbeo(String country) {
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
