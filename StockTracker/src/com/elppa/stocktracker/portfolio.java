// symbol*_name : share symbol
// symbol*_price : share price 
// symbol*_qnt : shares quantity
// symbol*_total: share price * quantity
// * = 1,2,3,4,5

package com.elppa.stocktracker;

import com.elppa.webserviceaccess.ShareDetailsObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.DialogInterface;
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
		Object[] incoming = null;
		Bundle ShareData = getIntent().getExtras();
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.portfolio);

		/*
		 *  This line of code enables PNG transparency
		*/
		getWindow().getAttributes().format = android.graphics.PixelFormat.RGBA_8888;
		
		setTitle("Your Portfolio");
		
		BackButton = (ImageView) findViewById(R.id.portfolio_back);
		
		
		/*
		 *  Go back to the home screen
		 */
		BackButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				Portfolio.this.finish();	
			}
		});
			
		/*
		 * Check if share data is available, if not then return from the function.
		 * 
		 */
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
		
		//checkForFluctuation();
	}
	
	
	/*
	public void checkForFluctuation()
	{	
		Builder InformationDialogue; 
		InformationDialogue = new AlertDialog.Builder(Portfolio.this);
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
	}*/
	
}
