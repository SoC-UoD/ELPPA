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

import com.elppa.stocktracker.JanetShareDetails;

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

	String URL = null;
	String YQL = null;
	String HSQ = null;
	
	String TimePeriod = "";
	String JSONResponse;
	
	ShareDetailsObject[] SDO = null;
	
	/**
	 * TODO Complete constructor (if necessary)
	 */
	public FetchYahooData()
	{
		URL = "http://query.yahooapis.com/v1/public/yql?q=%1&format=json&env=store://datatables.org/alltableswithkeys";
		YQL = "SELECT CC FROM yahoo.finance.quotes WHERE symbol IN (SS)";
		HSQ = "SELECT CC FROM yahoo.finance.historicaldata WHERE symbol = SS AND startDate = DD AND endDate = DD";
		
	}
	
	/**
	 * Constructor with alternative YQL statement and HSQ statement
	 * 
	 * @param YQL
	 * @param HSQ
	 */
	
	public FetchYahooData(String newYQL, String newHSQ)
	{
		if(!newYQL.equals(""))
			YQL = newYQL;
		
		if(!newHSQ.equals(""))
			HSQ = newHSQ;
	}

	
	public ShareDetailsObject[] DownloadHistoricalYahooData(String[] Columns, String [] Symbols, String Date)
	{
		ShareDetailsObject[] SDO = null;
		String finalColumns = null;
		
		SDO = new ShareDetailsObject[Symbols.length];
		
		finalColumns = subColumns(Columns);

		/**
		 *  Replace CC in the YQL statement with the columns we want
		 */
		
		HSQ = HSQ.replace("CC", finalColumns);
		HSQ = HSQ.replace("DD",  "\""+Date+"\"");
		
		String newHSQ = new String();
		
		newHSQ = HSQ;
		/**
		 * Replace SS in the YQL statement with the symbols we want
		 */
		
		for(int i = 0; i < Symbols.length; i++)
		{	
			newHSQ = newHSQ.replace("SS", "\""+Symbols[i]+"\"");
			
			JSONResponse = fetchJSONHTTP(newHSQ);
			
			SDO[i] = new ShareDetailsObject();
			SDO[i] = getHistoricalJSONData(JSONResponse);
			SDO[i].setSymbol(Symbols[i]);
			SDO[i].setShareQuantity(JanetShareDetails.getSharesByKey(Symbols[i]));
			newHSQ = HSQ;
		}
		
		/**
		 *  Fetch JSON feed from Yahoo API
		 */
		Log.i(FetchYahooData.class.toString(), "Fetch JSON from Yahoo Server");		
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
	
	private ShareDetailsObject getHistoricalJSONData(String JSONObject)
	{
		ShareDetailsObject NewShareDetails = new ShareDetailsObject();
		
		if(JSONObject == null)
		{
			return null;
		}
		
		try
		{
			JSONArray allDetails = new JSONArray(JSONObject);
	    	
	    	JSONObject outer = allDetails.getJSONObject(0);
	    	
	    	JSONObject query = outer.getJSONObject("query");
	    	
	    	JSONObject results = query.getJSONObject("results");
    		Log.i(FetchYahooData.class.toString(), "Fetching single item");
    		JSONObject quoteDetails = results.getJSONObject("quote");
    		NewShareDetails.setHistoricalCloseValue(Float.valueOf((String) quoteDetails.get("Adj_Close")));
		}catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
		return NewShareDetails;
	}
	
	private void getCurrentJSONData(String JSONObject)
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
	        		
	        		if(currDetails.get("AskRealtime").toString().equals("null"))
	        			SDO[i].setCurrentShareValue(0.0f);
	        		else
	        			SDO[i].setCurrentShareValue(Float.valueOf((String) currDetails.get("AskRealtime")));
	        		
	        		if(currDetails.get("Symbol").toString().equals("null"))
	        			SDO[i].setSymbol("No Data");
	        		else
	        			SDO[i].setSymbol((String)currDetails.get("Symbol"));
	        		
	        		if(currDetails.get("Name").toString().equals("null"))
	        			SDO[i].setCompanyName("No Data");
	        		else
	        			SDO[i].setCompanyName((String) currDetails.get("Name"));
	        		
	        		if(currDetails.getString("PreviousClose").toString().equals("null"))
	        			SDO[i].setClosingValue(0.0f);
	        		else
	        			SDO[i].setClosingValue(Float.valueOf((String) currDetails.getString("PreviousClose")));
	        		
	        		if(currDetails.get("Volume").toString().equals("null"))
	        			SDO[i].setVolumeTraded(0.0f);
	        		else
	        			SDO[i].setVolumeTraded(Float.valueOf((String) currDetails.get("Volume")));
	        		
	        		if(currDetails.get("Open").toString().equals("null"))
	        			SDO[i].setOpeningValue(0.0f);
	        		else
	        			SDO[i].setOpeningValue(Float.valueOf((String) currDetails.get("Open")));
	        		
	        		if(SDO[i].getSymbol() == null)
	        			SDO[i].setShareQuantity(0.0f);
	        		else
	        			SDO[i].setShareQuantity(JanetShareDetails.getSharesByKey(SDO[i].getSymbol()));
	        	}
	    	}else
	    	{
	    		Log.i(FetchYahooData.class.toString(), "Fetching single item");
	    		
	    		JSONObject quoteDetails = results.getJSONObject("quote");
	    		SDO[0] = new ShareDetailsObject();
	    		SDO[0].setCurrentShareValue(Float.valueOf((String) quoteDetails.get("LastTradePriceOnly")));
	    		SDO[0].setSymbol((String)quoteDetails.get("symbol"));
	    		SDO[0].setCompanyName((String) quoteDetails.get("Name"));
	    		SDO[0].setClosingValue(Float.valueOf((String) quoteDetails.getString("PreviousClose")));
	    		SDO[0].setVolumeTraded(Float.valueOf((String) quoteDetails.getString("Volume")));
	    		SDO[0].setShareQuantity(JanetShareDetails.getSharesByKey(SDO[0].getSymbol()));
	    		
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
		
		JSONResponse = fetchJSONHTTP(YQL);
	
		getCurrentJSONData(JSONResponse);
		
		return SDO;	
	}
	
	/**
	 * 
	 * fetchJSON accesses Yahoo finance API over HTTP.
	 * @return String JSON object or null if no data is returned.
	 */
	
	private String fetchJSONHTTP(String YQLQuery)
	{
		HttpClient httpClient = new DefaultHttpClient();
		StringBuilder builder = new StringBuilder();
    	String newURL = URL;
    	try
    	{
    		YQLQuery = URLEncoder.encode(YQLQuery,"UTF-8");
    	}catch(UnsupportedEncodingException ex)
    	{
    		ex.printStackTrace();
    	}
    	
    	newURL = newURL.replace("%1", YQLQuery);
    	
    	HttpGet httpget = new HttpGet(newURL);
    	
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


