package com.elppa.stocktracker;

import com.elppa.webserviceaccess.ShareDetailsObject;

/**
 * 
 * @author JWO
 *
 *This class holds the permanent values for Coffee Janet's share holdings.
 *
 */
public class JanetShareDetails 
{
	private static String [] AllShareSymbols = {"BP.L","HSBA.L", "EXPN.L", "MKS.L", "SN.L"};
	private static String [] HistoricalColumns = {"Adj_Close"};
	private static String [] AllColumns = {"*"};
	private static String [] CurrentColumns = {"LastTradePriceOnly", "Symbol", "Name", "PreviousClose", "Volume", "Open"};
	private static String ColVolume = "volume";
	private static String ColAskingprice = "AskPrice";
	
	private static int [] SharesArray = {192, 258, 343,485, 1219};
	private static float [] OutstandingSharesArray = {19030000000f, 161100000f,  989000000f, 1590000000f, 894000000f};
	
	private static int numBPShares = 127; //Check this value
	private static int numMSShares = 485; //Check this value
	private static int numHSBCShares = 343;
	private static int numEXPShares = 258;
	private static int numSmthNphShares = 1219;
	
	private static float BPShares = 1903000000f;
	private static float HSBCShares = 161100000f;
	private static float EXPShares = 989000000f;
	private static float MSShares = 1590000000f;
	private static float SmthNph = 894000000f;
	
	
	public static ShareDetailsObject [] getErrorData()
	{
		ShareDetailsObject [] SDO = new ShareDetailsObject[6];
		
		return SDO;
	}

	public static String[] getCurrentShareDetails()
	{
		return CurrentColumns;
	}

	public static int getSharesByKey(String key)
	{
		int numberOfShares = 0;
		
		for (int i = 0; i < AllShareSymbols.length; i++)
		{
			if (AllShareSymbols[i].equals(key))
			{
				numberOfShares = SharesArray[i];
			}	
		}
		return numberOfShares;
	}
	
	public static float getSharesOutstandingByKey(String key)
	{
		float outstandingShares = 0;
		
		for (int i = 0; i < AllShareSymbols.length; i++)
		{
			if(AllShareSymbols[i].equals(key))
			{
				outstandingShares = OutstandingSharesArray[i];
			}
		}
		
		return outstandingShares;
	}
	
	public static float getBPSharesOutstanding()
	{
		return BPShares;
	}
	
	public static String[] getAllShareSymbols()
	{
		return AllShareSymbols;
	}
	
	public static String[] getHistoricalColumns()
	{
		return HistoricalColumns;
	}
	
	public static String[] getAllColumns()
	{
		return AllColumns;
	}
	
	public static String getColVolume()
	{
		return ColVolume;
	}
	
	public static String getColAskingPrice()
	{
		return ColAskingprice;
	}

	public static float getSmthNph() 
	{
		return SmthNph;
	}

	public static float getMSShares() 
	{
		return MSShares;
	}

	public static void setMSShares(float mSShares) 
	{
		MSShares = mSShares;
	}

	public static float getEXPShares() 
	{
		return EXPShares;
	}

	public static void setEXPShares(float eXPShares) 
	{
		EXPShares = eXPShares;
	}

	public static float getHSBCShares() 
	{
		return HSBCShares;
	}

	public static int getNumSmthNphShares() 
	{
		return numSmthNphShares;
	}

	public static int getNumEXPShares() 
	{
		return numEXPShares;
	}

	public static int getNumHSBCShares() 
	{
		return numHSBCShares;
	}

	public static int getNumMSShares() 
	{
		return numMSShares;
	}

	public static int getNumBPShares() 
	{
		return numBPShares;
	}

}
