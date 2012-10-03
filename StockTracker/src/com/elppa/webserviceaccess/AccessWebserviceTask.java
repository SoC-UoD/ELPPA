package com.elppa.webserviceaccess;

/**
 * @Description
 * 
 * 
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class AccessWebserviceTask extends AsyncTask<String, Integer, ShareDetailsObject>
{
	String RequestDetails;
	
	public AccessWebserviceTask()
	{
		super();
	
	}
	
	@Override
	protected ShareDetailsObject doInBackground(String... params) 
	{
		// TODO Auto-generated method stub
		FetchYahooData FYD = new FetchYahooData();
		ShareDetailsObject SDO = new ShareDetailsObject();
		
		String [] columns = {"AskRealtime"};
		String [] symbols = {"yhoo", "goog", "aapl"};
		int[] numberOfShares = { 100, 120, 140};
		
		FYD.TotalValueOfPortfolio(columns, symbols, numberOfShares);		
		
		return null;
	}
}

/**
 * 
 * @author JWO
 *
 */

class FetchYahooData
{
	/**
	 * URL - %1 is the YQL query
	 * YQL - %1 is the stock symbol for which to search
	 * 
	 */
	
	float exchangeRate = 1.80f;
	
	String JSONResponse = new String();
	
	String URL = "http://query.yahooapis.com/v1/public/yql?q=%1&format=json&env=store://datatables.org/alltableswithkeys";
	
	String StringSelect;
	
	String YQL = "SELECT CC FROM yahoo.finance.quotes WHERE symbol IN (SS)";
		
	/**
	 * TODO Complete constructor
	 */
	public FetchYahooData()
	{
		
		
	}

	/**
	 * Calculate the total value of the shares portfolio
	 * 
	 * GUI Must provide all company symbols
	 * 
	 * @param Columns, Symbols
	 */
	
	public float TotalValueOfPortfolio(String[] Columns, String[] Symbols, int [] numberOfShares)
	{
		float totalValue = 0;
		
		StringBuilder SB = new StringBuilder();

		if(Columns.length == 1)
			SB.append(Columns[0]);
		else
		{
			for(int i = 0; i < Columns.length; i++)
			{
					SB.append(Columns[i]+ ", ");
			}
		}
		
		// Replace %1 with the columns we want
		YQL = YQL.replace("CC", SB.toString());
		
		SB = new StringBuilder();
		
		if(Symbols.length == 1)
			SB.append("'"+Symbols[0]+"'");
		else
		{
			int counter = 0;
			while(counter < Symbols.length)
			{
				if(counter == (Symbols.length-1 ))
					SB.append("'"+Symbols[counter]+"'");
				else
					SB.append("'"+Symbols[counter]+"', ");
				
				counter++;
			}
		}
		
		YQL = YQL.replace("SS", SB.toString());

		/**
		 *  Fetch JSON feed from Yahoo API
		 */
		
		System.out.println("YQL query: " + YQL);
		System.out.println("URL: " + URL);
		
		JSONResponse = fetchJSON();
		
		try
        {
        	int numberOfResults = 0;
        	
			JSONArray allDetails = new JSONArray(JSONResponse);
        	
        	JSONObject outer = allDetails.getJSONObject(0);
        	
        	JSONObject query = outer.getJSONObject("query");
        	
        	JSONObject results = query.getJSONObject("results");
        	numberOfResults = Integer.valueOf(query.getString("count"));
        	
        	if(numberOfResults > 1)
        	{
	        	JSONArray quote = results.getJSONArray("quote");
	        	        	
	        	for(int i = 0; i < quote.length(); i++)
	        	{
	        		JSONObject currDetails = quote.getJSONObject(i);
	        		
	        		totalValue += Float.valueOf(currDetails.getString("AskRealtime")) * exchangeRate;
	        	}
        	}else
        	{
        		JSONObject quote = results.getJSONObject("quote");
        		totalValue = Float.valueOf(quote.getString("AskRealtime")) * exchangeRate;
        	}
        } catch (Exception ex)
        {
        	ex.printStackTrace();
        }

		return totalValue;
	}
	
	public String [] FetchWeeklyTrendForSymbol(String Symbol)
	{
		String [] QuoteDetails = new String[5];
		
		// Work out the date of last Friday
		//
		
		return QuoteDetails;
	}
	
	public String [] CheckForRunOnShare(String Symbol)
	{
		String [] RunDetails = new String[5];
		
		return RunDetails;
	}
	
	private String fetchJSON()
	{
		HttpClient httpClient = new DefaultHttpClient();
		StringBuilder builder = new StringBuilder();
    	
    	try
    	{
    		YQL = URLEncoder.encode(YQL,"UTF-8");
    	}catch(UnsupportedEncodingException ex)
    	{
    		ex.printStackTrace();
    	}
    	
    	URL = URL.replace("%1", YQL);
    	
    	HttpGet httpget = new HttpGet(URL);
    	
    	try
    	{
    		HttpResponse response = httpClient.execute(httpget);
    		StatusLine sl = response.getStatusLine();
    		
    		int statusCode = sl.getStatusCode();
    		
    		if(statusCode == 200)
    		{
    			HttpEntity entity = response.getEntity();
    			
    			InputStream content = entity.getContent();
    			
    			BufferedReader reader = new BufferedReader(new InputStreamReader(content));
    			
    			String line;
    			
    			while ((line = reader.readLine()) != null)
    			{
    				builder.append(line);
    			}
    			
    		}else
        	{
        		Log.e(FetchYahooData.class.toString(), "Failed to download file");
        	}    		
    	}catch(ClientProtocolException ex)
    	{
    		ex.printStackTrace();
    	}catch(IOException ex)
    	{
    		ex.printStackTrace();
    	}catch(IllegalArgumentException ex)
    	{
    		ex.printStackTrace();
    	}catch (Exception ex)
    	{
    		ex.printStackTrace();
    	}	
    	 	
    	if(builder.length() > 0)
    		 return "[" + builder.toString()+"]";
    	else
    		return null;
    }
}


