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
 *
 */
public class ShareDetailsObject implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String Symbol;
	private String CompanyName;
	private float ShareValue;
	private float HistoricalCloseValue;
	private float ClosingValue;
	private float OpeningValue;
	private float SharesOutstanding;
	private float ShareVolumeTraded;
	private float ShareQuantity;
	
	private String ErrorMessage;
	private boolean isErrorMessage;
	
	/*
	 *Error Conditions
	 * 
	 */
	private static final String ErrorMess_1 = "value is 0 - may be error in feed";
	private static final String ErrorMess_2 = "value is not valid - may be error in feed";
	
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
	
	public void setOpeningValue(float newOpeningValue)
	{
		if(newOpeningValue == 0)
		{
			setErrorDetails("Opening Value", 1);	
		}
		
		OpeningValue = newOpeningValue;
	}

	public float getChangeFromOpening()
	{
		float change = (ShareValue / OpeningValue) - 1.0f;
		
		return change;
	}
	
	/**
	 * Refactored - JWO + TT 09/11/12
	 * 
	 * Extract method so that error setting is more generic.
	 * 
	 * @param detail
	 * @param error
	 */
	
	private void setErrorDetails(String detail, int error) 
	{
		isErrorMessage = true;
		String message = null;
		
		switch(error)
		{
			case 1:
				message = ShareDetailsObject.ErrorMess_1;
				break;
			case 2:
				message = ShareDetailsObject.ErrorMess_2;
				break;
		}
		
		ErrorMessage = detail + message;
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
	
	/*
	 * Refactor - Change Method Name for clarity - JO + TT
	 * 
	 * 
	 */
	public void setCurrentShareValue(float newShareValue)
	{
		ShareValue = newShareValue;
	}
	
	public void setSharesOutstanding(float newSharesOutstanding)
	{
		if(newSharesOutstanding > 0)
			SharesOutstanding = newSharesOutstanding;
	}
}
