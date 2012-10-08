package com.elppa.stocktracker;

/**
 * 
 * @author JWO
 *
 *This class holds the permanent values for Coffee Janet's share holdings.
 *
 */


public class JanetShareDetails 
{
	private static String [] AllShareSymbols = {"bl.l"};
	private static String [] HistoricalColumns = {"Volume", "Adj_Close"};
	private static String AllColumns = "*";
	private static String ColVolume = "volume";
	private static String ColAskingprice = "AskPrice";
	private static int numBPShares = 127; //Check this value
	private static int numMSShares = 100; //Check this value
	private static float BPShares = 180000000f;

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
	
	public static String getAllColumns()
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

}
