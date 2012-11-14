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
	private final float PlummetingShareValue = 5.0f;
	private final float RocketingShareValue = 25.0f;

	public void setUp()
	{
		testSDO = new ShareDetailsObject();
		testSDO.setOpeningValue(OpeningValue);
		testSDO.setCurrentShareValue(CurrentShareValue);
		testSDO.setShareQuantity(ShareQuantity);
		testSDO.setHistoricalCloseValue(HistoricalClosingValue);
	}
	
	/**
	 * PAir Programmed - JWO + TT + DCW
	 * 
	 * Test that the SDO class produces the correct share value, for given inputs.
	 * 
	 */
	public void testCalculatedValue()
	{
		assertEquals(CalculatedValue, testSDO.getCalculatedValue());
	}
	
	/**
	 * Pair Programmed - JWO + TT + DCW
	 * 
	 * Check that the SDO class produces the correct percentage change from opening value.
	 * 
	 */
	
	public void testChageFromOpeningValue()
	{
		assertEquals(ChangeFromOpening, testSDO.getChangeFromOpening());
	}
	
	
	/**
	 * Pair Programmed - JWO + TT + DCW
	 * 
	 * Check that the SDO class produces the correct historical calculated value.
	 * 
	 */
	public void testHistoricalCalculatedValue()
	{
		assertEquals(HistoricalCalculatedValue, testSDO.getHistoricalCalculatedValue());
	}
	
	/**
	 * Pair Programmed - JWO + TT + DCW
	 * 
	 * Check that the SDO recognises when a share price is plummeting.
	 * 
	 */
	
	public void testIsPlummeting()
	{
		testSDO.setCurrentShareValue(PlummetingShareValue);
		
		assertTrue(testSDO.getIsPlummeting());
	}
	
	/**
	 * Pair Programmed - JWO + TT + DCW
	 * 
	 * Check that the SDO recognises when a share price is rocketing.
	 * 
	 */
	
	public void testIsRocketing()
	{
		testSDO.setCurrentShareValue(RocketingShareValue);
		
		assertTrue(testSDO.getIsRocketing());
		
	}
	
	
	/**
	 * 
	 * Clean up tests
	 */
	
	public void tearDown()
	{
		/**
		 * When all tests are complete set the test object to null
		 * 
		 */
		testSDO = null;
		
	}
}
