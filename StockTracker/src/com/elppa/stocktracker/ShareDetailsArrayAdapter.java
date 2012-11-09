package com.elppa.stocktracker;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import com.elppa.webserviceaccess.ShareDetailsObject;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ShareDetailsArrayAdapter extends ArrayAdapter <ShareDetailsObject>
{
	private final Context cxt;
	private final ShareDetailsObject[] SDO;
	private NumberFormat formatter;
	
	public ShareDetailsArrayAdapter(Context context, ShareDetailsObject[] values) 
	{
	    super(context, R.layout.share_item, values);
	    this.cxt = context;
	    this.SDO = values;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{		
	    formatter = NumberFormat.getCurrencyInstance();
	    formatter.setMaximumFractionDigits(0);
    	formatter.setCurrency(Currency.getInstance(Locale.UK));
		
	    LayoutInflater inflater = (LayoutInflater) cxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.share_item, parent, false);
	    
	    TextView Symbol = (TextView) rowView.findViewById(R.id.Symbol);
	    TextView Price = (TextView) rowView.findViewById(R.id.Price);
	    TextView Quantity = (TextView) rowView.findViewById(R.id.Quantity);
	    TextView Value = (TextView) rowView.findViewById(R.id.Value);
	    TextView CalculatedValue = (TextView) rowView.findViewById(R.id.CalcValue);
	    TextView ChangeValue = (TextView) rowView.findViewById(R.id.ChangeValue);
	    TextView ChangeLabel = (TextView) rowView.findViewById(R.id.Change);
	    
	    Symbol.setText(SDO[position].getCompanyName());
	    
	    Price.setText(String.format("%.0f",SDO[position].getShareValue())+"p");
	    Quantity.setText("Qty: " + String.format("%.0f",SDO[position].getShareQuantity()));   
	    Value.setText("Value");
	    ChangeLabel.setText("Chg");
	    CalculatedValue.setText(formatter.format(SDO[position].getCalculatedValue()));
	    
	    formatter = NumberFormat.getPercentInstance();
	    formatter.setMaximumFractionDigits(2);
	    
	    if(SDO[position].getChangeFromOpening() > 0)
	    {
	    	ChangeValue.setTextColor(Color.parseColor("#5FDB54"));
	    	
	    }else
	    { 	
	    	ChangeValue.setTextColor(Color.parseColor("#AA211C"));
	    }
	    
	    ChangeValue.setText(formatter.format(SDO[position].getChangeFromOpening()));
	   
	    return rowView;
	  }
}	

