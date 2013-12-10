package com.example.costoflivingdiary;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.os.AsyncTask;

public class QueryNumbeo extends AsyncTask <Void, Void, String> {
	
	String country;
	String results;
	
	protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
       InputStream in = entity.getContent();
         StringBuffer out = new StringBuffer();
         int n = 1;
         while (n>0) {
             byte[] b = new byte[4096];
             n =  in.read(b);
             if (n>0) out.append(new String(b, 0, n));
         }
         return out.toString();
    }
	
	@Override
	protected String doInBackground(Void... params) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
        HttpGet httpGet = new HttpGet("http://www.numbeo.com/api/country_prices?api_key=umd_edu_640&country=" + country);
        String text = null;
        try {
              HttpResponse response = httpClient.execute(httpGet, localContext);
              HttpEntity entity = response.getEntity();
              text = getASCIIContentFromEntity(entity);
        } catch (Exception e) {
        	return e.getLocalizedMessage();
        }
        this.results = text;
        return text;
	}	
	
	protected void onPostExecute(String results) {
		super.onPostExecute(results);
	}
}

