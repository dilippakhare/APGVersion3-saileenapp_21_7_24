package com.apgautomation.utility.serverutility.netrequest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class GetServerData {

	public int responseCode=0;
	private String contentAsString;
	public String downloadUrl(String myurl) throws IOException {
		InputStream is = null;
		// Only display the first 500 characters of the retrieved
		// web page content.
		int len = 500;

		try 
		{
			URL url = new URL(myurl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(15000 /* milliseconds */);
			conn.setConnectTimeout(20000 /* milliseconds */);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			// Starts the query
			conn.connect();
			int response = conn.getResponseCode();

			responseCode=response;
			if(responseCode!=200)
				return "";
			Log.d("TAG", "The response is: " + response);
			is = conn.getInputStream();

			// Convert the InputStream into a string
			 contentAsString = getStramSting(is);
			return contentAsString;

			// Makes sure that the InputStream is closed after the app is
			// finished using it.
		} 
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		finally 
		{
			
			if (is != null) 
			{
				is.close();
				
			}
			
			if(contentAsString==null || contentAsString.equalsIgnoreCase(""))
				return null;
			else 
				return contentAsString;
		}

	}

	public String readIt(InputStream stream, int len) throws IOException,
			UnsupportedEncodingException {
		Reader reader = null;
		reader = new InputStreamReader(stream, "UTF-8");
		char[] buffer = new char[len];
		reader.read(buffer);
		return new String(buffer);
	}

	String getStramSting(InputStream stream) throws IllegalStateException,
			IOException 
			{
		

		StringBuilder sb = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		sb.append(reader.readLine() + "\n");
		String line = "0";

		while ((line = reader.readLine()) != null) {

			sb.append(line + "\n");
			// Log.e("log_loginerror1",line.toString());
			// test=line.toString();

		}
		
		String res = sb.toString();
         stream.close();
		return res;
	}
	
	
	
	
	
	private static final char PARAMETER_DELIMITER = '&';
	private static final char PARAMETER_EQUALS_CHAR = '=';
	public static String createQueryStringForParameters(Map<String, String> parameters) {
	    StringBuilder parametersAsQueryString = new StringBuilder();
	    if (parameters != null) {
	        boolean firstParameter = true;
	         
	        for (String parameterName : parameters.keySet()) {
	            if (!firstParameter) {
	                parametersAsQueryString.append(PARAMETER_DELIMITER);
	            } 
	             
	            parametersAsQueryString.append(parameterName)
	                .append(PARAMETER_EQUALS_CHAR)
	                .append(URLEncoder.encode(
	                    parameters.get(parameterName)));
	             
	            firstParameter = false;
	        }
	    }
	    return parametersAsQueryString.toString();
	}

	
	public Bitmap getBitmap(String Url) throws Exception {
		URL url1 = null; 
		url1 = new URL(Url);
		BitmapFactory.Options bfo = new BitmapFactory.Options();
		bfo.outWidth = 620;
		bfo.outHeight = 350;
		return BitmapFactory.decodeStream(url1.openConnection()
				.getInputStream(), null, bfo);
	}
}
