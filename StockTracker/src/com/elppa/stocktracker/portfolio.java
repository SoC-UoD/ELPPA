// symbol*_name : share symbol
// symbol*_price : share price 
// symbol*_qnt : shares quantity
// symbol*_total: share price * quantity
// * = 1,2,3,4,5

package com.elppa.stocktracker;

import java.text.NumberFormat;

import com.elppa.webserviceaccess.ShareDetailsObject;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

public class Portfolio extends ListActivity
{
	ShareDetailsObject[] ShareDetails = null;
	ShareDetailsArrayAdapter SharesList = null;
	
	ImageView BackButton = null;
	
	public Portfolio()
	{
		
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.portfolio, menu);
        return true;
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

		setShareList(ShareDetails);
	}
	
	public void setShareList(ShareDetailsObject[] sdo)
	{
		SharesList = new ShareDetailsArrayAdapter(this.getBaseContext(), sdo);
		setListAdapter(SharesList);
	}
	
	 @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
		 ShareDetailsObject [] ShareDetails = null;
    	switch(item.getItemId())
    	{
    		case R.id.rocket1:
	    		// Test Object defined to create a run on shares.    			
    			ShareDetails = new ShareDetailsObject[1];
	    		ShareDetails[0] = new ShareDetailsObject("BP Rocket", "BP.L", 120, JanetShareDetails.getSharesOutstandingByKey("BP.L") * 0.3f);	
	    		ShareDetails[0].setShareQuantity(100f);
	    		ShareDetails[0].setCurrentShareValue(90);
	    		ShareDetails[0].setOpeningValue(50);
	    		setShareList(ShareDetails);
	    		return true;
    		case R.id.plummet1:
    			ShareDetails = new ShareDetailsObject[1];
	    		ShareDetails[0] = new ShareDetailsObject("BP Plummet", "BP.L", 120, JanetShareDetails.getSharesOutstandingByKey("BP.L") * 0.3f);	
	    		ShareDetails[0].setShareQuantity(100f);
	    		ShareDetails[0].setCurrentShareValue(70);
	    		ShareDetails[0].setOpeningValue(100);
	    		setShareList(ShareDetails);
	    		return true;	
    	}  	
    	return false;
    }
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) 
	{
	    super.onListItemClick(l, v, position, id);
	    NumberFormat percentFormatter = null;
	    String message;
	    AlertDialog.Builder InformationDialogue = null;
	    ShareDetailsObject SDO = SharesList.getItem(position);
	    
	    percentFormatter = NumberFormat.getPercentInstance();
	    percentFormatter.setMaximumFractionDigits(2);
	    
	    message = "Company ";
	    
	    if(SDO.getIsPlummeting())
	    {
	    	message += SDO.getCompanyName();
	    	message += " has plummeted by ";
	    	message += percentFormatter.format(SDO.getChangeFromOpening());
	    	message += " current value :" + SDO.getShareValue() + " opening value: " + SDO.getOpeningValue();
	    }else if(SDO.getIsRocketing())
	    {
	    	message += SDO.getCompanyName();
	    	message += " has rocketed by ";
	    	message += percentFormatter.format(SDO.getChangeFromOpening());
	    	message += " current value :" + SDO.getShareValue() + " opening value: " + SDO.getOpeningValue();
	    }else
	    {
	    	message += SDO.getCompanyName();
	    	message += " has not changed significantly ";
	    	message += percentFormatter.format(SDO.getChangeFromOpening());
	    }
	   
	    InformationDialogue = new AlertDialog.Builder(Portfolio.this);
		InformationDialogue.setTitle("Share Details");
		InformationDialogue.setMessage(message);
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
