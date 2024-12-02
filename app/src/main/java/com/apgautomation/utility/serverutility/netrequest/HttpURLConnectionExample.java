package com.apgautomation.utility.serverutility.netrequest;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import static android.content.ContentValues.TAG;

public class HttpURLConnectionExample {

	private final String USER_AGENT = "Mozilla/5.0";

	/*public static void main(String[] args) throws Exception {

		HttpURLConnectionExample http = new HttpURLConnectionExample();

		System.out.println("Testing 1 - Send Http GET request");
		//http.sendGet();
		
		System.out.println("\nTesting 2 - Send Http POST request");
		//http.sendPost();

	}*/

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
		

	
	
	public String  performPostCallJson(String requestURL,
	           String postDataParams) {

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
	            writer.write(postDataParams);

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


	private static void postform_urlencoded(String endpoint, Map<String, String> params)
			throws IOException {

		URL url;
		try
		{
			url = new URL(endpoint);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("invalid url: " + endpoint);
		}
		StringBuilder bodyBuilder = new StringBuilder();
		Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
		// constructs the POST body using the parameters
		while (iterator.hasNext()) {
			Map.Entry<String, String> param = iterator.next();
			bodyBuilder.append(param.getKey()).append('=')
					.append(param.getValue());
			if (iterator.hasNext()) {
				bodyBuilder.append('&');
			}
		}
		String body = bodyBuilder.toString();
		Log.v(TAG, "Posting '" + body + "' to " + url);
		byte[] bytes = body.getBytes();
		HttpURLConnection conn = null;
		try {
			Log.e("URL", "> " + url);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setFixedLengthStreamingMode(bytes.length);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			// post the request
			OutputStream out = conn.getOutputStream();
			out.write(bytes);
			out.close();
			// handle the response
			int status = conn.getResponseCode();
			if (status != 200) {
				throw new IOException("Post failed with error code " + status);
			}
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}
}