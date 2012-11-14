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
	private boolean isErrorMessage=false;
	
	/**
	 * Error Conditions
	 * 
	 * Error conditions are added to the object if there is suspicion that an input value is
	 * not correct.  For example, prices or values of 0 would make it likely that the feed is not working
	 * correctly
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
		Symbol = new String("Undefined Symbol");
		CompanyName = new String("Undefined Company");
		ShareValue = 0.0f;
		SharesOutstanding = 0.0f;
	}
	
	
	
	/**
	 * Fully qualified constructor
	 *
	 * Refactored JO + TT 12/11/12 - Update parameters
	 */
	public ShareDetailsObject(String newCompanyName, String newSymbol,  float shareValue, float volumeTraded)
	{
		Symbol = newSymbol;
		CompanyName = newCompanyName;
		ShareValue = shareValue;
		ShareVolumeTraded = volumeTraded;
	}
	
	/**
	 * 
	 * Refactored 12/11/12 - Added error trigger if value input is 0 - may be error in feed.
	 * 
	 * @param newOpeningValue
	 */
	
	public void setOpeningValue(float newOpeningValue)
	{
		if(newOpeningValue == 0)
		{
			setErrorDetails("Opening Value", 1);	
		}
		
		OpeningValue = newOpeningValue;
	}
	
	
	public float getOpeningValue()
	{
		return OpeningValue;
	}

	/**
	 * Pair programmed 09/11/12
	 * 
	 * JWO + TT + DCW
	 * 
	 * Returns the percentage change from the supplied opening value
	 * If either value is 0, set an error.
	 * 
	 * @return
	 */
	public float getChangeFromOpening()
	{
		if(ShareValue == 0 || OpeningValue == 0)
		{
			setErrorDetails("Percentage Change", 1);
		}
		
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
		setErrorMessage(true);
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
		
		setErrorMessage(detail + message);
	}
	
	/**
	 * Set the error message on this SDO
	 * @param message
	 */
	
	private void setErrorMessage(String message) 
	{
		ErrorMessage = message;
		
	}

	/**
	 * Set error on this SDO
	 * 
	 * @param b
	 */
	private void setErrorMessage(boolean b) 
	{
		isErrorMessage = true;
		
	}



	/**
	 * Return whether or not this SDO may have suspicious data as indicated by the error state
	 *  
	 * @return
	 */
	public boolean isErrorMessage() 
	{
		return isErrorMessage;
	}
	
	/**
	 * Return the string statement of the error on this SDO
	 *  
	 * @return
	 */
	
	public String getErrorMessage()
	{
		return ErrorMessage;
	}
	
	
	/**
	 * Pair programmed 09/11/12
	 * 
	 * JWO + TT + DCW
	 * 
	 * Check the change from opening for rocketing value (>= 10%)
	 */
	
	public boolean getIsRocketing()
	{
		if(getChangeFromOpening() >= 0.1)
			return true;
		else
			return false;
	}
	
	/**
	 * Pair programmed 09/11/12
	 * 
	 * JWO + TT + DCW
	 * 
	 * Check the change from opening for plummeting value (<= 20%)
	 */	
	public boolean getIsPlummeting()
	{
		if(getChangeFromOpening() <= -0.2)
			return true;
		else
			return false;
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
	
	/**
	 * JWO + TT + DCW
	 * 
	 * Calculate and return the value of the shares
	 * 
	 * Refactored 14/11/12 - added error condition if values are 0
	 */
	
	public float getCalculatedValue()
	{
		if(ShareQuantity == 0 || ShareValue == 0)
			setErrorDetails("Calculated Share Value", 1);
		
		return ShareQuantity * (ShareValue/ 100);		
	}
	
	/**
	 * JWO + TT + DCW
	 * 
	 * Calculate and return the value of the shares
	 * 
	 * Refactored 14/11/12 - added error condition if values are 0
	 */
	
	public float getHistoricalCalculatedValue()
	{
		if(HistoricalCloseValue == 0 || ShareQuantity == 0)
			setErrorDetails("Calculated Historical Value", 1);
		
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
	
	
	/**
	 * JWO + TT + DCW
	 * 
	 * Calculate and return the value of the shares
	 * 
	 * Refactored 14/11/12 - added error condition if values are 0
	 */
	
	public void setSymbol(String newSymbol)
	{
		if(newSymbol.equals(""))
		{
			Symbol="No Data";
			setErrorDetails("Share Symbol", 2);
		}
		else
			Symbol = newSymbol;
	}
	
	/**
	 * JWO + TT + DCW
	 * 
	 * Calculate and return the value of the shares
	 * 
	 * Refactored 14/11/12 - added error condition if values are 0
	 */
	
	public void setCompanyName(String newCompanyName)
	{
		if(newCompanyName.equals(""))
		{
			CompanyName = "No Data";
			setErrorDetails("Company Name", 2);
		}
		else		
			CompanyName = newCompanyName;
	}
	
	/**
	 * Refactored - 09/11/12 Change Method Name for clarity - JO + TT
	 * 
	 * 
	 */
	public void setCurrentShareValue(float newShareValue)
	{
		if(newShareValue==0)
			setErrorDetails(ErrorMess_1,1);
		ShareValue = newShareValue;
	}
	
	public void setSharesOutstanding(float newSharesOutstanding)
	{
		if(newSharesOutstanding > 0)
			SharesOutstanding = newSharesOutstanding;
	}

}
