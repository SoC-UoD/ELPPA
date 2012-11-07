package com.elppa.stocktracker;


import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.support.v4.app.NavUtils;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.elppa.webserviceaccess.ShareDetailsObject;
import com.elppa.webserviceaccess.FetchYahooData;

public class Home extends Activity
{
	private ShareDetailsObject [] ShareDetails = null;
	private AccessWebserviceTask AWT = null;
	private TableLayout TL = null;
	private ImageView PortfolioDetailsButton = null;
	private ImageView TotalValueButton = null;
	private TextView NotificationHeader = null;

	
	/**
	 * Initialise GUI Elements
	 * 
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    
		//Check to see if we already have data, if we do, let's quit.
		//Implement a timer so that refreshes take place on a 5 minute interval
		TL = (TableLayout) findViewById(R.id.AlertTable);
		
		PortfolioDetailsButton = (ImageView) findViewById(R.id.stockDetailsImage);
		TotalValueButton = (ImageView) findViewById(R.id.portfolioDetailsButton);
		NotificationHeader = (TextView) findViewById(R.id.notificationHeader);
		
		PortfolioDetailsButton.setOnClickListener(new View.OnClickListener() 
		{			
			@Override
			public void onClick(View v) 
			{
				Intent I = new Intent(v.getContext(), Portfolio.class);
				I.putExtra("ShareDetails", ShareDetails);
				startActivityForResult(I, 0);			
			}
		});
		
		TotalValueButton.setOnClickListener(new View.OnClickListener() 
		{			
			@Override
			public void onClick(View v) 
			{	
				Intent I = new Intent(v.getContext(), TotalValue.class);
				startActivityForResult(I, 0);				
			}
		});	
    }
    
    /**
     * This method is called before the activity appears on screen.  Initialise event handlers etc.
     * 
     */
    @Override
    protected void onStart() 
    {
        super.onStart();
        refreshData();	  
    }
    
    private void refreshData()
    {
    	//Create AccessWebserviceTask thread - this method calls doInBackground and passes string arrays with required details.
		//
		if(ShareDetails == null)
		{
			Log.i(Home.class.toString(), "Executing Web Service Task");
			String [] columns = JanetShareDetails.getAllColumns();
			String [] symbols = JanetShareDetails.getAllShareSymbols();
			
			AWT = new AccessWebserviceTask(Home.this);
			AWT.execute(columns, symbols);
		}   
    }
    
    @Override
    protected void onPause()
    {
    	super.onPause();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.activity_home, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	switch(item.getItemId())
    	{
    		case R.id.menu_run1:
	    		// Test Object defined to create a run on shares.
	    		ShareDetails = new ShareDetailsObject[1];
	    		ShareDetails[0] = new ShareDetailsObject("BP Test", "BP.L", 120, JanetShareDetails.getSharesOutstandingByKey("BP.L") * 0.3f);
	    		checkShareVolumeForRun();
	    	return true;
    		case R.id.menu_run2:
    			ShareDetails = new ShareDetailsObject[2];
        		ShareDetails[0] = new ShareDetailsObject("BP Test", "BP.L", 120, JanetShareDetails.getSharesOutstandingByKey("BP.L") * 0.3f);
        		ShareDetails[1] = new ShareDetailsObject("Experian Test", "EXPN.L", 120, JanetShareDetails.getSharesOutstandingByKey("EXPN.L") * 0.4f);
        		checkShareVolumeForRun();
    		return true;	
    		
    		case R.id.menu_run3:
    			ShareDetails = new ShareDetailsObject[3];
        		ShareDetails[0] = new ShareDetailsObject("BP Test", "BP.L", 120, JanetShareDetails.getSharesOutstandingByKey("BP.L") * 0.3f);
        		ShareDetails[1] = new ShareDetailsObject("Experian Test", "EXPN.L", 120, JanetShareDetails.getSharesOutstandingByKey("EXPN.L") * 0.4f);
        		ShareDetails[2] = new ShareDetailsObject("M&S Test", "MKS-GBX.L", 120, JanetShareDetails.getSharesOutstandingByKey("MKS-GBX.L") * 0.4f);
        		checkShareVolumeForRun();
        	return true;
    		case R.id.menu_clear:
    			ShareDetails = new ShareDetailsObject[1];
    			ShareDetails[0] = new ShareDetailsObject("M&S Test", "MKS-GBX.L", 120, JanetShareDetails.getSharesOutstandingByKey("MKS-GBX.L") * 0.05f);
    			ShareDetails = null;
    			refreshData();
    		return true;
    	}  	
    	return false;
    }
    
    private boolean checkNetworkConnectivity()
    {
    	ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    	
    	NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
    	
    	return activeNetwork != null && activeNetwork.isConnectedOrConnecting();	
    }
    
    /**
     * This method looks at the currently traded share volume and ensures it is not more than 10% of the Shares Outstanding
     * 
     * @return
     */
   
    private void checkShareVolumeForRun()
    {	 	
    	if(TL.getChildCount() > 1)
    		TL.removeViewsInLayout(1, TL.getChildCount() - 1);
    	
    	TableRow row = new TableRow(this);
    	TextView tv = null;
    	int alertCount = 0;
    	
    	for(int i = 0; i < ShareDetails.length; i++)
    	{
    		String Symbol = ShareDetails[i].getSymbol();
    		String Name = ShareDetails[i].getCompanyName();
    		float outStandingShares = JanetShareDetails.getSharesOutstandingByKey(Symbol);
    		float tradedVolume = ShareDetails[i].getVolumeTraded();
    		
    		if(tradedVolume> (outStandingShares * 0.10f))
    		{
    			alertCount++;
    			float runAmount = tradedVolume/outStandingShares * 100f;
    			tv = new TextView(this);
    			row = new TableRow(this);
    			
    			tv.setHeight(50);
    			tv.setTag("There is a run on " + Name +". " + String.format("%.0f%% of shares have been traded so far today.", runAmount));
    			tv.setText("ALERT: There is a run on " + Symbol);
    			tv.setTextColor(0xFFFF2200);
    			tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 21.0f);
    			tv.setHeight(90);
    			
    			tv.setOnClickListener(new View.OnClickListener() 
    			{						
					@Override
					public void onClick(View v) 
					{
						AlertDialog.Builder AD = new AlertDialog.Builder(Home.this);
						AD.setTitle("Run on Your Shares");
						AD.setMessage((String) v.getTag());
						AD.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() 
						{
							
							@Override
							public void onClick(DialogInterface dialog, int which) 
							{
								dialog.dismiss();						
							}
						});
						
						AD.create();
						AD.show();
						Log.i(Home.class.toString(), v.toString());
					}
				});
    			row.addView(tv);
    			
    			TL.addView(row, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    			NotificationHeader.setText("Notifications (tap for more detail)");
    		}
    	}
    	
    	if(alertCount == 0)
		{
    		NotificationHeader.setText("Notifications");
			tv = new TextView(this);
			row = new TableRow(this);
			tv.setHeight(50);
	    	tv.setText("You have no alerts");
	    	tv.setTextColor(0xFF00FF00);  	
	    	tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20.0f);
	    	tv.setHeight(50);
	    	row.addView(tv);
	    	row.setBackgroundColor(0xFF00AA00);
	    	TL.addView(row, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		}
    }   
   
   /**
    * 
    * @author James Oliver
    * 
    * This class create a new thread separate from the UI.  Input into the class is: String[] (columns and symbols required from Yahoo) and returns a ShareDetailsObject[] array.
    * Constructor requires the current activity context to display a refreshing dialogue.
    *
    */
    protected class AccessWebserviceTask extends AsyncTask<String[], Void, ShareDetailsObject []>
    {
    	private ProgressDialog pd = null;
    	private AlertDialog.Builder InformationDialogue = null;
    	private Context cxt = null;
    	private FetchYahooData FYD = null;
    	
    	/**
    	 * 
    	 * Default constructor, requires the current activity to display refresh dialogue (Activity.this)
    	 * 
    	 * @param context
    	 */
    	public AccessWebserviceTask(Context context)
    	{
    		super();
    		
    		cxt = context;
    	}
    	
    	@Override
    	/**
    	 * This method creates a background thread.  Required parameters are an array of columns and an array of symbols.  Uses FetchYahooData class to retrieve data from the Yahoo webservice.
    	 * 
    	 * 
    	 */
    	protected ShareDetailsObject[] doInBackground(String[]... params) 
    	{
    		// TODO Auto-generated method stub
    		FYD = new FetchYahooData();
    		
    		String[] columns = params[0];
    		String[] symbols = params[1];
    		    		
    		ShareDetailsObject [] ShareDetails = null;

    		if(Home.this.checkNetworkConnectivity())
    			ShareDetails = FYD.DownloadCurrentYahooData(columns, symbols);
    		else
    			ShareDetails = null;
    		
    		return ShareDetails;
    	}
    	
    	protected void onPreExecute()
    	{
    		pd = new ProgressDialog(cxt);
    		pd.setTitle("Refreshing Data");
    		pd.setMessage("Downloading New Data, please wait...");
    		pd.setCancelable(true);
    		pd.setIndeterminate(false); pd.setProgressStyle(ProgressDialog.STYLE_SPINNER); pd.setMax(5);
    		pd.show();
    	}
    	
    	protected void onPostExecute(ShareDetailsObject [] result)
    	{
    		if(result != null)
    		{
    			ShareDetails = result;
    			Home.this.checkShareVolumeForRun();
    			pd.dismiss();
    		}else
    		{
    			pd.dismiss();
    			InformationDialogue = new AlertDialog.Builder(Home.this);
    			InformationDialogue.setTitle("Error Downloading Data");
    			InformationDialogue.setMessage("No network connection - cannot download data");
    			InformationDialogue.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() 
    			{
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						dialog.dismiss();
					}
				});
    			InformationDialogue.show();
    		}	
    	}
    }
}
