/**
 * 
 */
package com.elppa.webserviceaccess;

import java.io.Serializable;

/**
 * @author jamesoliver
 *
 * Container object for share information fetched from webservice api.
 * 
 * TODO Add validation to set and get
 *
 */
public class ShareDetailsObject implements Serializable
{
	private String DateTime; // May or may not be good as a string - final date for presentation.
	private String Symbol;
	private String CompanyName;
	private float ShareValue;
	private float TotalValue;
	private float SharesOutstanding; // Large integer value
	private float ShareVolumeTraded;
	
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
	
	public float getVolumeTraded()
	{
		return ShareVolumeTraded;
	}
	
	public void updateShareValue(float numberOfShares)
	{
		if(numberOfShares > 0)
			TotalValue = ShareValue * numberOfShares;
	}
	
	public void setVolumeTraded(float newVolumeTraded)
	{
		ShareVolumeTraded = newVolumeTraded;
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
		if(newSharesOutstanding > 0)
			SharesOutstanding = newSharesOutstanding;
	}
}
