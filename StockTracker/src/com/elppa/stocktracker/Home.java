package com.elppa.stocktracker;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.NavUtils;

import android.widget.Button;
import android.widget.Toast;

import com.elppa.webserviceaccess.ShareDetailsObject;
import com.elppa.webserviceaccess.FetchYahooData;

public class Home extends Activity 
{
	private ShareDetailsObject [] ShareDetails = null;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        Button testButton = (Button) this.findViewById(R.id.testButton);
        
        testButton.setOnClickListener(new View.OnClickListener() 
        {
			
			@Override
			public void onClick(View v) 
			{
				//Check to see if we already have data, if we do, let's quit.
				//Implement a timer so that refreshes take place on a 5 minute interval
				if(ShareDetails != null)
					return;
				
				// TODO Auto-generated method stub
				AccessWebserviceTask AWT = new AccessWebserviceTask();
				
				Log.i(Home.class.toString(), "Executing Web Service Task");
				
				
				
				
				//Create arrays with the details required.
				// TODO Create a static object with required share data.
							
				String [] columns = {"*"};
	    		String [] symbols = {"bp.l"};
			
	    		//Create AccessWebserviceTask thread - this method calls doInBackground and passes string arrays with required details.
	    		//
				AWT.execute(columns, symbols);
			}
		});
        
    }
    
    private boolean checkShareVolumeForRun()
    {
    	
    	
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
    * 
    * String[] is the input into doInBackgound()
    * ShareDetailsObject[] is returned from onPostExecute()
    * 
    *
    */

    
    protected class AccessWebserviceTask extends AsyncTask<String[], Void, ShareDetailsObject []>
    {
    	public AccessWebserviceTask()
    	{
    		super();
    	
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
    		FetchYahooData FYD = new FetchYahooData();
    		
    		String[] columns = params[0];
    		String[] symbols = params[1];
    		
    		ShareDetailsObject [] SDO = null;
   
    		try
    		{
    			SDO = FYD.DownloadYahooData(columns, symbols);
    		}catch(Exception ex)
    		{
    			ex.printStackTrace();
    		}
    		
    		return SDO;
    	}
    	
    	protected void  onProgressUpdate()
    	{
    		Toast.makeText(getApplicationContext(), "Please Wait", Toast.LENGTH_SHORT).show();
    	}
    	
    	protected void onPostExecute(ShareDetailsObject [] result)
    	{
    		if(result != null)
    			ShareDetails = result;
    	}
    }
}
