//textviews ids for output
//txt_total : will take the total value with a proceeding £ symbol
//txt_total_date : will take the reference date used for total value calculation
//txt_change: will take the change between last week and the week before that
//btn_back : button that returns to the home activity
//btn_refresh
package com.elppa.stocktracker;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.NavUtils;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.elppa.stocktracker.Home.AccessWebserviceTask;
import com.elppa.webserviceaccess.ShareDetailsObject;
import com.elppa.webserviceaccess.FetchYahooData;

public class TotalValue extends Activity 
{
	private ShareDetailsObject [] ShareDetails = null;
	private String DateLastFriday = null;
	private AccessWebserviceTask AWT = null;
	private TextView Total = null;
	private TextView LastFriday = null;
	private ImageView BackButton = null;
	private TextView SymbolsUsed = null;
	
	/**
	 * Initialise GUI Elements
	 * 
	 */
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.total_value);
		
		Total = (TextView) findViewById(R.id.total);
		LastFriday = (TextView) findViewById(R.id.txt_total_date);
		SymbolsUsed = (TextView) findViewById(R.id.symbolsUsed);
		
		SymbolsUsed.setText("");
		
		setTitle("Total Portfolio Value");
		
		BackButton = (ImageView) findViewById(R.id.portfolio_back);
		
		
		// Go back to the home screen
		BackButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				TotalValue.this.finish();	
			}
		});	
    }
    
    private void calculateTotalValue()
    {
    	LastFriday.setText(DateLastFriday);
    	NumberFormat formatter = NumberFormat.getCurrencyInstance();
    	
    	float runningTotal = 0;
    	StringBuilder SB = new StringBuilder();
    	
    	SB.append("Share symbols included: ");
    	
    	if(ShareDetails == null)
    	{
    		Total.setText("Error: Data not available");
    		return;
    	}
    	
    	for(int i = 0; i < ShareDetails.length; i++)
    	{
    		if(ShareDetails[i].getHistoricalCalculatedValue() != 0)
    		{
    			SB.append(" "+ ShareDetails[i].getSymbol()+" ");
    			runningTotal += ShareDetails[i].getHistoricalCalculatedValue();
    		}
    	}
    	
    	formatter.setMaximumFractionDigits(0);
    	formatter.setCurrency(Currency.getInstance(Locale.UK));
    	
    	Total.setText(formatter.format(runningTotal));
    	SymbolsUsed.setText(SB.toString());
    }
    
    private String getDateLastFriday()
    {
    	Calendar cal = new GregorianCalendar();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    	int currentDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
    	int currentDate = cal.get(Calendar.DAY_OF_MONTH);
    	int lastFridayDate = 0;
    	
    	switch(currentDayOfWeek)
    	{
    		case Calendar.MONDAY:
    			lastFridayDate = currentDate - 3;
    		break;
    		case Calendar.TUESDAY:
    			lastFridayDate = currentDate - 4;
    		break;
    		case Calendar.WEDNESDAY:
    			lastFridayDate = currentDate - 5;
    		break;
    		case Calendar.THURSDAY:
    			lastFridayDate = currentDate - 6;
    		break;
    		case Calendar.FRIDAY:
    			lastFridayDate = currentDate - 7;
    		break;
    		case Calendar.SATURDAY:
    			lastFridayDate = currentDate - 1;
    		break;
    		case Calendar.SUNDAY:
    			lastFridayDate = currentDate - 2;
    	}
    	
    	cal.setTime(new Date());
    	cal.set(Calendar.DAY_OF_MONTH, lastFridayDate);
    	
    	
    	return sdf.format(cal.getTime());
    }
    
    /**
     * This method is called before the activity appears on screen.  Initialise event handlers etc.
     * 
     */
    @Override
    protected void onStart() 
    {
        super.onStart();
        DateLastFriday = getDateLastFriday();
    	refreshData();
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
    
    private void refreshData()
    {
    	//Create AccessWebserviceTask thread - this method calls doInBackground and passes string arrays with required details.
		//
		if(ShareDetails == null)
		{
			Log.i(Home.class.toString(), "Executing Web Service Task");
			String [] columns = JanetShareDetails.getHistoricalColumns();
			String [] symbols = JanetShareDetails.getAllShareSymbols();
			
			AWT = new AccessWebserviceTask(TotalValue.this);
			AWT.execute(columns, symbols);
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
     	private AlertDialog.Builder ad = null;
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

     		ShareDetails = FYD.DownloadHistoricalYahooData(columns, symbols, DateLastFriday);
     		
     		return ShareDetails;
     	}
     	
     	protected void onPreExecute()
     	{
     		pd = new ProgressDialog(cxt);
     		pd.setTitle("Refreshing Data");
     		pd.setMessage("Downloading New Data, please wait...");
     		pd.setCancelable(true);
     		pd.setIndeterminate(false); 
     		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER); 
     		pd.setMax(5);
     		pd.show();
     	}
     	
     	protected void  onProgressUpdate()
     	{
     		
     	}
     	
     	protected void onPostExecute(ShareDetailsObject [] result)
     	{
     		if(result != null)
     		{
     			pd.dismiss();
     			ShareDetails = result;
     			TotalValue.this.calculateTotalValue();
     		}else
     		{
     			pd.dismiss();
     			ad = new AlertDialog.Builder(TotalValue.this);
				ad.setTitle("Cannot Access Data");
				ad.setMessage("Yahoo finance is not providing data. Please refresh later");
				ad.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() 
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						dialog.dismiss();						
					}
				});
				ad.create();
				ad.show();
     			
     			TotalValue.this.calculateTotalValue();
     		}	
     	}
     }
}
