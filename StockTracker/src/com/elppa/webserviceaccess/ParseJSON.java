package com.elppa.webserviceaccess;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * Refactored from FetchYahooData Class
 * 
 * Attempt to simplify the extraction of data from JSON objects.
 * 
 * @author theodorostheodoridis
 *
 */

public class ParseJSON 
{

	private ShareDetailsObject SDO = null;
	private JSONObject test=null;
	public void getJSONObject(JSONObject test)
	{
		this.test=test;
		
	}
	
	/**
	 * Pair Programmed JWO + TT
	 * 
	 * If JSON key has no value, assign appropriate value
	 * 
	 * @param value
	 * @param isString
	 * @throws JSONException
	 */
	
	private void checkValue(String value, boolean isString) throws JSONException
	{
		if(test.get(value).toString().equals("null") && isString){
			test.put(value,"No Data");
		}
		else if(test.get(value).toString().equals("null") && !isString)
			test.put(value,0.0f);
		
	}
	
	/**
	 * Pair Programmed TT + JWO
	 * 
	 * Check for null values and possible errors.
	 * 
	 */
	public void checkNullValues() 
	{
		try {
			checkValue("AskRealTime",false);
			checkValue("Symbol",true);
			checkValue("Name",true);
			checkValue("PreviousClose",false);
			checkValue("Volume",false);
			checkValue("Open",false);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}		
	}
	
	/**
	 * Pair Programmed TT + JWO
	 * 
	 * Create a valid SDO object from JSON object data and return to calling class
	 * 
	 */
	
	public ShareDetailsObject returnSDO() 
	{
			SDO = new ShareDetailsObject();
		
			try
			{
				SDO.setCurrentShareValue(Float.valueOf((String) test.get("AskRealtime")));
				SDO.setSymbol((String)test.get("Symbol"));
				SDO.setCompanyName((String) test.get("Name"));
				SDO.setClosingValue(Float.valueOf((String) test.getString("PreviousClose")));
				SDO.setVolumeTraded(Float.valueOf((String) test.get("Volume")));
				SDO.setOpeningValue(Float.valueOf((String) test.get("Open")));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		return SDO;
	}
}
