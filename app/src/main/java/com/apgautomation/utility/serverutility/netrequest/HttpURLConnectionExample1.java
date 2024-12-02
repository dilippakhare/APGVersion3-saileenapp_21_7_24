package com.apgautomation.utility.serverutility.netrequest;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class HttpURLConnectionExample1 {

	private final String USER_AGENT = "Mozilla/5.0";


	// HTTP GET request
	public String sendGet(String url) throws Exception {

		//String url = "http://www.google.com/search?q=mkyong";
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		Log.d(url,responseCode+"");
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		return response.toString();

	}
	
	// HTTP POST request
	/*public String sendPost(String url,ArrayList<BasicNameValuePair> param) throws Exception {

		//String url = "https://selfsolve.apple.com/wcResults.do";
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		String urlParameters = getQuery(param);//"sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
		
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		//print result
	  return	response.toString();

	}*/
	
	
	
	public String  performPostCall(String requestURL,
            HashMap<String, String> postDataParams) {

        URL url;
        String response = "";
       // postDataParams.put("", "");
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(20000);
            conn.setConnectTimeout(20000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="";

                throw new Exception(responseCode+"");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
	
	
	
	
	

	
	
	private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
	
	
	
	
	
	
	public String  performPostJson(String requestURL,
	           String postJsonString) {

	        URL url;
	        String response = "";
	       // postDataParams.put("", "");
	        try {
	            url = new URL(requestURL);

	            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	            conn.setReadTimeout(20000);
	            conn.setConnectTimeout(20000);
	            conn.setRequestMethod("POST");
	        	conn.setRequestProperty("Content-Type", 
	                    "application/json");
	            conn.setDoInput(true);
	            conn.setDoOutput(true);
	            

	            OutputStream os = conn.getOutputStream();
	            BufferedWriter writer = new BufferedWriter(
	                    new OutputStreamWriter(os, "UTF-8"));
	            //writer.
	            writer.write(postJsonString);
	            writer.flush();
	            writer.close();
	            os.close();
	            int responseCode=conn.getResponseCode();

	            if (responseCode == HttpsURLConnection.HTTP_OK) {
	                String line;
	                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
	                while ((line=br.readLine()) != null) {
	                    response+=line;
	                }
	            }
	            else {
	                response="";

	                throw new Exception(responseCode+"");
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return response;
	    }



	HttpURLConnection conn;
	public String  performPostCallJson(String requestURL,
	           String postDataParams) {
		Log.d(requestURL,postDataParams);
	        URL url;
	        String response = "";
	       // postDataParams.put("", "");
	        try {
	            url = new URL(requestURL);

				conn = (HttpURLConnection) url.openConnection();
	            conn.setReadTimeout(20000);
	            conn.setConnectTimeout(20000);
	            conn.setRequestMethod("POST");
	        	conn.setRequestProperty("Content-Type", 
	                    "application/json");
	            conn.setDoInput(true);
	            conn.setDoOutput(true);
	            

	            OutputStream os = conn.getOutputStream();
	            BufferedWriter writer = new BufferedWriter(
	                    new OutputStreamWriter(os, "UTF-8"));
	            //writer.
	            writer.write(postDataParams);

	            writer.flush();
	            writer.close();
	            os.close();
	            int responseCode=conn.getResponseCode();
				Log.d(requestURL,responseCode+"");
	            if (responseCode == HttpsURLConnection.HTTP_OK) {
	                String line;
	                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
	                while ((line=br.readLine()) != null) {
	                    response+=line;
	                }
	            }
	            else 
	            {
	                response="";

	                throw new Exception(responseCode+"");
	            }
	        } catch (Exception e) 
	        {
	            e.printStackTrace();
	        }

	        return response;
	    }

	public InputStream performPostCallJsonAndGetStream(String requestURL,
									   String postDataParams) {

		URL url;
		String response = "";
		// postDataParams.put("", "");
		try {
			url = new URL(requestURL);

			conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(20000);
			conn.setConnectTimeout(20000);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/json");
			conn.setDoInput(true);
			conn.setDoOutput(true);


			OutputStream os = conn.getOutputStream();
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(os, "UTF-8"));
			//writer.
			writer.write(postDataParams);

			writer.flush();
			writer.close();
			os.close();
			int responseCode=conn.getResponseCode();


			if (responseCode == HttpsURLConnection.HTTP_OK)
			{
				return conn.getInputStream();
			}
			else
			{
                return  null;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}


	public void closeConnection()
	{
		if(conn!=null)
		{
			try {
				conn.disconnect();
			}
			catch (Exception e){}
		}
	}
}