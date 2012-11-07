// symbol*_name : share symbol
// symbol*_price : share price 
// symbol*_qnt : shares quantity
// symbol*_total: share price * quantity
// * = 1,2,3,4,5

package com.elppa.stocktracker;

import com.elppa.webserviceaccess.ShareDetailsObject;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Portfolio extends ListActivity
{
	ShareDetailsObject[] ShareDetails = null;
	ImageView BackButton = null;
	
	public Portfolio()
	{
		
	}
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.portfolio);
		
		getWindow().getAttributes().format = android.graphics.PixelFormat.RGBA_8888;
		
		setTitle("Your Portfolio");
		
		BackButton = (ImageView) findViewById(R.id.portfolio_back);
		
		
		// Go back to the home screen
		BackButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				Portfolio.this.finish();	
			}
		});
		
		Object[] incoming = null;
		
		Bundle ShareData = getIntent().getExtras();
		
		
		// If there is no share data available.
		
		incoming = (Object[])  ShareData.getSerializable("ShareDetails");
		
		if(incoming == null)	
			return;
		
		ShareDetails = new ShareDetailsObject[incoming.length];
        
		for (int i = 0; i< incoming.length; i++)
		{
			ShareDetails[i] = (ShareDetailsObject) incoming[i];
		}
		
		ShareDetailsArrayAdapter SharesList = new ShareDetailsArrayAdapter(this.getBaseContext(), ShareDetails);
		
		setListAdapter(SharesList);	
	}
	
}
