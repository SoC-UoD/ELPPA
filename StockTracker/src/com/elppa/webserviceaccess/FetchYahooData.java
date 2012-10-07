package com.elppa.webserviceaccess;

/**
 * 
 * @author James Oliver
 * @version 0.5
 * @since 05/10/2012
 * This class is used by the GUI to retrieve share details from the Yahoo finance webservice.  We subsitiute the required columns and symbols into a YQL query
 * (YQL is an SQL like language we can use to retrieve data from Yahoo Opentables.
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

import android.util.Log;

/**
 * 
 * @author JWO
 * 
 * This class fetches data from the Yahoo finance YQL api.
 *
 */

public class FetchYahooData
{
	/**
	 * URL - %1 is the YQL query
	 * YQL - %1 is the stock symbol for which to search
	 * 
	 */

	String URL = "http://query.yahooapis.com/v1/public/yql?q=%1&format=json&env=store://datatables.org/alltableswithkeys";
	String YQL = "SELECT CC FROM yahoo.finance.quotes WHERE symbol IN (SS)";
	String HSQ = "SELECT CC FROM yahoo.finance.historicaldata WHERE symbol = SS AND startDate = DD AND endDate = DD";
	
	String TimePeriod = "";
	String JSONResponse;
	
	ShareDetailsObject[] SDO = null;
	
	/**
	 * TODO Complete constructor (if necessary)
	 */
	public FetchYahooData()
	{
		
		
	}

	
	/**
	 * 
	 * @param Columns
	 * @param Symbols
	 * @param Dates
	 * @return
	 */
	
	public ShareDetailsObject[] DownloadHistoricalYahooData(String[] Columns, String [] Symbols, String[] Dates)
	{
		ShareDetailsObject[] SDO = null;
		
		
		
		return SDO;
	}
	
	
	/**
	 * 
	 * Takes an array of columns required from JSON data and concatenates them together in a comma separated list
	 * 
	 * @param Columns
	 * @return
	 */
	
	private String subColumns(String[] Columns)
	{
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
		
		return SB.toString();
	}
	
	/**
	 * 
	 * Takes an array of company symbols and concatenates them together in a comma separated list
	 * 
	 * @param Symbols
	 * @return String of symbols concatenated together
	 */
	
	private String subSymbols(String[] Symbols)
	{
		StringBuilder SB = new StringBuilder();
		
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
		
		return SB.toString();
	}
	
	
	private void getJSONData(String JSONObject)
	{
		int numberOfResults = 0;
    	
		try
		{
			JSONArray allDetails = new JSONArray(JSONObject);
	    	
	    	JSONObject outer = allDetails.getJSONObject(0);
	    	
	    	JSONObject query = outer.getJSONObject("query");
	    	
	    	/**
	    	 * TODO Modify code to extract date and time from JSON at "results" level object - may have to change depending on the requirements for the date and time...
	    	 * 
	    	 */
	    	
	    	JSONObject results = query.getJSONObject("results");
	    	numberOfResults = Integer.valueOf(query.getString("count"));
	    	
	    	SDO = new ShareDetailsObject[numberOfResults];
	    	
	    	if(numberOfResults > 1)
	    	{
	    		Log.i(FetchYahooData.class.toString(), "Fetching multiple items.");
	    		
	        	JSONArray quote = results.getJSONArray("quote");
	        	        	
	        	for(int i = 0; i < quote.length(); i++)
	        	{
	        		JSONObject currDetails = quote.getJSONObject(i);
	        		SDO[i] = new ShareDetailsObject();
	        		SDO[i].setShareValue(Float.valueOf((String) currDetails.get("AskRealtime")));
	        		SDO[i].setSymbol((String)currDetails.get("Symbol"));
	        		SDO[i].setDateTime((String) currDetails.get("Name"));
	        		SDO[i].setVolumeTraded(Float.valueOf((String) currDetails.get("Volume")));
	        	}
	    	}else
	    	{
	    		Log.i(FetchYahooData.class.toString(), "Fetching single item");
	    		
	    		JSONObject quoteDetails = results.getJSONObject("quote");
	    		SDO[0] = new ShareDetailsObject();
	    		SDO[0].setShareValue(Float.valueOf((String) quoteDetails.get("AskRealtime")));
	    		SDO[0].setSymbol((String)quoteDetails.get("symbol"));
	    		SDO[0].setDateTime((String)quoteDetails.get("Name"));
	    		SDO[0].setVolumeTraded(Float.valueOf((String) quoteDetails.getString("Volume")));
	    	}
		}catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/**
	 *  
	 * GUI Task Thread must provide all company symbols and the columns required.
	 * 
	 * @param String[] Columns
	 * @param String[] Symbols
	 * @throws java.lang.Exception
	 * @returns ShareDetailsObject array containing retrieved details.
	 */
	
	public ShareDetailsObject[] DownloadCurrentYahooData(String[] Columns, String[] Symbols)
	{
		String finalColumns = null;
		String finalSymbols = null;
		
		finalColumns = subColumns(Columns);

		/**
		 *  Replace CC in the YQL statement with the columns we want
		 */
		
		YQL = YQL.replace("CC", finalColumns);
			
		/**
		 * Replace SS in the YQL statement with the symbols we want
		 */
		
		finalSymbols = subSymbols(Symbols);
		
		YQL = YQL.replace("SS", finalSymbols);

		/**
		 *  Fetch JSON feed from Yahoo API
		 */
		Log.i(FetchYahooData.class.toString(), "Fetch JSON from Yahoo Server");
		
		JSONResponse = fetchJSONHTTP();
	
		getJSONData(JSONResponse);
		
		return SDO;	
	}
	
	/**
	 * 
	 * fetchJSON accesses Yahoo finance API over HTTP.
	 * @return String JSON object or null if no data is returned.
	 */
	
	private String fetchJSONHTTP()
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


