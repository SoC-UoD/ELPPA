//textviews ids for output
//txt_total : will take the total value with a proceeding £ symbol
//txt_total_date : will take the reference date used for total value calculation
//txt_change: will take the change between last week and the week before that
//btn_back : button that returns to the home activity
//btn_refresh
package com.elppa.stocktracker;



import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.NavUtils;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.elppa.webserviceaccess.ShareDetailsObject;
import com.elppa.webserviceaccess.FetchYahooData;

public class total_value extends Activity 
{
	private ShareDetailsObject [] ShareDetails = null;
	private Button testButton,backButton = null;
	private AccessWebserviceTask AWT = null;
	private TextView Change = null;
	/**
	 * Initialise GUI Elements
	 * 
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.total_value);
        
        Bundle ShareData = getIntent().getExtras();
        
        Object[] ShareDetails = (Object[])  ShareData.getSerializable("ShareDetails");
        
        Toast.makeText(this, ((ShareDetailsObject) ShareDetails[0]).getSymbol(), Toast.LENGTH_SHORT).show();

        Change = (TextView) findViewById(R.id.txt_change);
        
        ((TextView)Change).setTextColor(0xFF7CFC00);
        Change.setText("Array Size: " + ShareDetails.length);
          
        backButton = (Button) this.findViewById(R.id.btn_back);
        
        backButton.setOnClickListener(new View.OnClickListener() 
        {
			@Override
			public void onClick(View v) 
			{	
				finish();
			}
        });
        
		//Check to see if we already have data, if we do, let's quit.
		//Implement a timer so that refreshes take place on a 5 minute interval
		if(ShareDetails != null)
			return;
		
		//AWT = new AccessWebserviceTask(total_value.this);
		
		Log.i(total_value.class.toString(), "Executing Web Service Task");
		
		//Create arrays with the details required.
		// TODO Create a static object with required share data.
					
		String [] columns = {"*"};
		String [] symbols = {"bp.l"};
	
		//Create AccessWebserviceTask thread - this method calls doInBackground and passes string arrays with required details.
		//
		//AWT.execute(columns, symbols);
    }
    
    /**
     * This method is called before the activity appears on screen.  Initialise event handlers etc.
     * 
     */
    @Override
    protected void onStart() 
    {
        super.onStart();
       
        
    }
    
    @Override
    protected void onPause()
    {
    	super.onPause();
    }
    
    private boolean checkShareVolumeForRun()
    {
    	testButton.setText(ShareDetails[0].getSymbol());
    	
    	return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.activity_home, menu);
        return true;
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
    		//	total_value.this.checkShareVolumeForRun();
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
