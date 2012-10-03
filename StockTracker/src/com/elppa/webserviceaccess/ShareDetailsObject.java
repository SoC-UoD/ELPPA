/**
 * 
 */
package com.elppa.webserviceaccess;

/**
 * @author jamesoliver
 *
 * Container object for share information fetched from webservice api.
 * 
 * TODO Add validation to set and get
 *
 */
public class ShareDetailsObject
{
	String DateTime; // May or may not be good as a string - final date for presentation.
	String Symbol;
	String CompanyName;
	float ShareValue;
	float SharesOutstanding; // Large integer value
	
	/**
	 * Default constructor
	 * 
	 */
	
	public ShareDetailsObject()
	{
		DateTime = new String("1st Jan 2012"); // See above, may change from String
		Symbol = new String("XXX");
		CompanyName = new String("CHOAM");
		ShareValue = 0.0f;
		SharesOutstanding = 0.0f;
	}
	
	
	/**
	 * Fully qualified constructor
	 * 
	 */
	
	public ShareDetailsObject(String newCompanyName, String newSymbol, String newDateTime, long shareValue, long sharesOutstanding)
	{
		DateTime = newDateTime;
		Symbol = newSymbol;
		CompanyName = newCompanyName;
		
		ShareValue = shareValue;
		SharesOutstanding = sharesOutstanding;
	}
	
	public String getDateTime()
	{
		return DateTime;
	}
	
	public String getSymbol()
	{
		
		return Symbol;
	}
	
	
	public String getCompanyName()
	{
		
		return CompanyName;
	}
	
	public float getShareValue()
	{
		
		return ShareValue;
	}
	
	public float getSharesOutstanding()
	{
		
		return SharesOutstanding;
	}
	
	
	public void setDateTime(String newDateTime)
	{
		DateTime = newDateTime;
	}
	
	public void setSymbol(String newSymbol)
	{
		Symbol = newSymbol;
	}
	
	public void setCompanyName(String newCompanyName)
	{
		
		CompanyName = newCompanyName;
	}
	
	public void setShareValue(float newShareValue)
	{
		
		ShareValue = newShareValue;
	}
	
	public void setSharesOutstanding(float newSharesOutstanding)
	{
		
		SharesOutstanding = newSharesOutstanding;
	}
}
