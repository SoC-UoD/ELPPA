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
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String Symbol;
	private String CompanyName;
	private float ShareValue;
	private float HistoricalCloseValue;
	private float ClosingValue;
	private float SharesOutstanding; // Large integer value
	private float ShareVolumeTraded;
	private float ShareQuantity;
	private String ErrorMessage;
	
	/**
	 * Default constructor
	 * 
	 */
	public ShareDetailsObject()
	{
		Symbol = new String("XXX");
		CompanyName = new String("CHOAM");
		ShareValue = 0.0f;
		SharesOutstanding = 0.0f;
	}
	
	/**
	 * Fully qualified constructor
	 * 
	 */
	
	public ShareDetailsObject(String newCompanyName, String newSymbol,  float shareValue, float volumeTraded)
	{
		Symbol = newSymbol;
		CompanyName = newCompanyName;
		ShareValue = shareValue;
		ShareVolumeTraded = volumeTraded;
	}
	
	public void setHistoricalCloseValue(float newHistoricalClosingValue)
	{
		HistoricalCloseValue = newHistoricalClosingValue;
	}
	
	public float getHistoricalCloseValue()
	{
		return HistoricalCloseValue;
	}
	
	public void setClosingValue(float newClosingValue)
	{
		ClosingValue = newClosingValue;
		
	}
	
	public void setShareQuantity(float newQuantity)
	{
		ShareQuantity = newQuantity;
	}
	
	public float getShareQuantity()
	{
		return ShareQuantity;
	}
	
	public float getCalculatedValue()
	{
		if(ShareQuantity == 0 || ShareValue == 0)
			return 0.0f;
		
		return ShareQuantity * (ShareValue/ 100);		
	}
	
	public float getHistoricalCalculatedValue()
	{
		if(HistoricalCloseValue == 0 || ShareQuantity == 0)
			return 0.0f;
		
		return (HistoricalCloseValue/100) * ShareQuantity;
	}
	
	public float getClosingValue()
	{
		return ClosingValue;
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
	
	public void setVolumeTraded(float newVolumeTraded)
	{
		ShareVolumeTraded = newVolumeTraded;
	}
	
	public void setSymbol(String newSymbol)
	{
		if(newSymbol.equals(""))
			Symbol="No Data";
		else
			Symbol = newSymbol;
	}
	
	public void setCompanyName(String newCompanyName)
	{
		if(newCompanyName.equals(""))
			CompanyName = "No Data";
		else		
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
