package com.elppa.stocktracker;


import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.support.v4.app.NavUtils;
import android.content.Intent;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.elppa.webserviceaccess.ShareDetailsObject;
import com.elppa.webserviceaccess.FetchYahooData;

@TargetApi(12)
public class Home extends Activity
{
	private ShareDetailsObject [] ShareDetails = null;
	private Button testButton = null;
	private AccessWebserviceTask AWT = null;
	private TableLayout TL = null;
	
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
    }
    
    /**
     * This method is called before the activity appears on screen.  Initialise event handlers etc.
     * 
     */
    @Override
    protected void onStart() 
    {
        super.onStart();
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
    			checkShareVolumeForRun();
    		return true;
    	}
    	
    	return false;
    }
    
    /**
     * This method looks at the currently traded share volume and ensures it is not more than 10% of the Shares Outstanding
     * 
     * @return
     */
    
    private void checkShareVolumeForRun()
    {
    	if(ShareDetails == null)
    	{
    		Toast.makeText(Home.this, "Cannot Download Data: Please refresh later", Toast.LENGTH_LONG).show();
    		return;
    	}
    	
    	//Clear any views attached to the table view.
    	//TL.removeAllViews();
    	
    	System.out.print(TL.getChildCount());
    	
    	if(TL.getChildCount() > 1)
    		TL.removeViewsInLayout(1, TL.getChildCount() - 1);
    	
    	TableRow row = new TableRow(this);
    	TextView tv = null;
    	int alertCount = 0;
    	
    	for(int i = 0; i < ShareDetails.length; i++)
    	{
    		String Symbol = ShareDetails[i].getSymbol();
    		float outStandingShares = JanetShareDetails.getSharesOutstandingByKey(Symbol);
    		float tradedVolume = ShareDetails[i].getVolumeTraded();
    		
    		if(tradedVolume> (outStandingShares * 0.10f))
    		{
    			alertCount++;
    			float runAmount = tradedVolume/outStandingShares * 100f;
    			tv = new TextView(this);
    			row = new TableRow(this);
    			
    			tv.setTag("There is a run on " + Symbol +". So far today " + String.format("%.0f%n", runAmount) + "% of shares have been traded");
    			tv.setText("ALERT: There is a run on " + Symbol);
    			tv.setTextColor(0xFFFF0000);
    			tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20.0f);
    			
    			//row.setClickable(true);
    			tv.setOnClickListener(new View.OnClickListener() {						
					@Override
					public void onClick(View v) 
					{
						Log.i(Home.class.toString(), v.toString());
						Toast toast = Toast.makeText(Home.this.getApplicationContext(), (String) v.getTag(),Toast.LENGTH_SHORT);
						toast.setGravity(android.view.Gravity.CENTER, 0, 0);
						toast.show();
					}
				});
    			row.setBackgroundColor(0xFFCC0000);
    			row.addView(tv);
    			TL.addView(row, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    		}
    	}
    	
    	if(alertCount == 0)
		{
			tv = new TextView(this);
			row = new TableRow(this);
	    	tv.setText("You have no alerts");
	    	tv.setTextColor(0xFF00FF00);  	
	    	tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20.0f);
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

    		ShareDetails = FYD.DownloadCurrentYahooData(columns, symbols);
    		
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
    	
    	protected void  onProgressUpdate()
    	{
    		//pd.setProgress(0);
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
    			pd.setTitle("Download Error");
    			pd.setMessage("Unable to download finance data, please refresh in a few moments");
    			try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			pd.dismiss();
    		}	
    	}
    }
}
