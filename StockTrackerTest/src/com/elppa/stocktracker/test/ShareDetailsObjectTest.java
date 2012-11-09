package com.elppa.stocktracker.test;

import com.elppa.webserviceaccess.ShareDetailsObject;
import android.test.AndroidTestCase;

public class ShareDetailsObjectTest extends AndroidTestCase 
{
	ShareDetailsObject testSDO = null;
	private final float OpeningValue = 10.0f;
	private final float CurrentShareValue = 15.0f;
	private final float ChangeFromOpening = 0.5f;
	private final float ShareQuantity = 1000.0f;
	private final float CalculatedValue = 150.0f;
	private final float HistoricalCalculatedValue = 120.0f;
	private final float HistoricalClosingValue = 12.0f;

	
	
	public void setUp(){
		testSDO = new ShareDetailsObject();
		testSDO.setOpeningValue(OpeningValue);
		testSDO.setCurrentShareValue(CurrentShareValue);
		testSDO.setShareQuantity(ShareQuantity);
		testSDO.setHistoricalCloseValue(HistoricalClosingValue);
		
		
	}
	
	
	public void testCalculatedValue()
	{
		assertEquals(CalculatedValue, testSDO.getCalculatedValue());
	}
	
	public void testChageFromOpeningValue()
	{
		assertEquals(ChangeFromOpening, testSDO.getChangeFromOpening());
	}
	
	public void testHistoricalCalculatedValue()
	{
		assertEquals(HistoricalCalculatedValue, testSDO.getHistoricalCalculatedValue());
	}
}
