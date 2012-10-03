package com.elppa.stocktracker;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.NavUtils;

import android.widget.Button;

import com.elppa.webserviceaccess.*;

public class Home extends Activity 
{

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
				// TODO Auto-generated method stub
				AccessWebserviceTask AWT = new AccessWebserviceTask();
				
				Log.i(this.toString(), "Executing Web Service Task");
				
				AWT.execute("1");
				
			}
		});
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.activity_home, menu);
        return true;
    }

    
}
