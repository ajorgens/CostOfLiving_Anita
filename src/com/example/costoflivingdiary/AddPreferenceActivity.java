package com.example.costoflivingdiary;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class AddPreferenceActivity extends Activity{
	
   private Button mSubmit;
   private Button mCancel;
   private Spinner mCountriesList;
   private static final String[] COUNTRIES = new String[] { "Afghanistan", "Aland Islands", "Albania", "Alderney", "Algeria", "Andorra", "Angola",
		"Antigua And Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh",
		"Barbados", "Belarus", "Belgium", "Belize", "Bermuda", "Bhutan", "Bolivia", "Bosnia And Herzegovina", "Botswana", "Brazil",
		"British Virgin Islands", "Brunei", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands",
		"Chad", "Chile", "China", "Colombia", "Congo", "Costa Rica", "Cote Divoire", "Croatia", "Cuba", "Curacao", "Cyprus", "Czech Republic",
		"Denmark", "Djibouti", "Dominica", "Dominican Republic", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia",
		"Ethiopia", "Falkland Islands", "Faroe Islands", "Fiji", "Finland", "France", "French Guiana", "French Polynesia", "Gabon", "Gambia",
		"Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guam", "Guatemala", "Guernsey", "Guinea", "Guinea-bissau",
		"Guyana", "Haiti", "Honduras", "Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Isle Of Man", "Israel",
		"Italy", "Jamaica", "Japan", "Jersey", "Jordan", "Kazakhstan", "Kenya", "Kuwait", "Kyrgyzstan", "Laos", "Latvia", "Lebanon", "Lesotho",
		"Liberia", "Libya", "Lithuania", "Luxembourg", "Macao", "Macedonia", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta",
		"Marshall Islands", "Martinique", "Mauritania", "Mauritius", "Mexico", "Micronesia", "Moldova", "Monaco", "Mongolia", "Montenegro",
		"Morocco", "Mozambique", "Myanmar", "Namibia", "Nepal", "Netherlands", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria",
		"Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palestinian Territory", "Panama", "Papua New Guinea", "Paraguay", "Peru",
		"Philippines", "Poland", "Portugal", "Puerto Rico", "Qatar", "Republic Of Congo", "Reunion", "Romania", "Russia", "Rwanda", "Saint Kitts And Nevis",
		"Saint Lucia", "Samoa", "Saudi Arabia", "Senegal", "Serbia", "Seychelles","Singapore","Slovakia", "Slovenia", "Somalia", "South Africa",
		"South Korea", "South Sudan", "Spain", "Sri Lanka", "Sudan", "Suriname", "Swaziland", "Sweden", "Switzerland", "Syria", "Taiwan",
		"Tajikistan", "Tanzania", "Thailand", "Timor-leste", "Togo", "Tonga", "Trinidad And Tobago", "Tunisia", "Turkey", "Turkmenistan",
		"Turks And Caicos Islands", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "Uruguay", "Us Virgin Islands",
		"Uzbekistan", "Vanuatu", "Venezuela", "Vietnam", "Yemen", "Zambia", "Zimbabwe"
};
   
   public void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.addpref);
     mCountriesList = (Spinner) findViewById(R.id.countryPicker);
     List<String> list = new ArrayList<String>();
     for (String s : COUNTRIES) {
    	 list.add(s);
     }
     ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
       android.R.layout.simple_spinner_item, list);
     dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
     mCountriesList.setAdapter(dataAdapter);
     
     mSubmit = (Button) findViewById(R.id.prefSubmit);
     mSubmit.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
               String country = String.valueOf(mCountriesList.getSelectedItem());
               //Todo need to implement preferences
               PreferenceItem item = new PreferenceItem(country, false);
               MainActivity.PREF_LIST.add(item);
               onBackPressed();
             }
     });
     
     mCancel = (Button) findViewById(R.id.prefCancel);
     
     mCancel.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
               onBackPressed();
             }
     });
   }
  }
