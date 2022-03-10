package com.growfxtrade.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.growfxtrade.R;


public class CustomAdapter extends ArrayAdapter<String> {

	private Context activity;
	private String[] data;
	public Integer[] res;
	LayoutInflater inflater;
	private Typeface tf;
	/************* CustomAdapter Constructor *****************/
	public CustomAdapter(Context context,
                         int textViewResourceId, String[] values,
                         Integer[] mobileproviderarrayimages2) {
		super(context, textViewResourceId, values);

		/********** Take passed values **********/
		activity = context;
		data = values;
		res = mobileproviderarrayimages2;
		/*********** Layout inflator to call external xml layout () **********************/
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = inflater.inflate(R.layout.spinnerimage,parent, false);
		}
		String tempValues = data[position];
		Integer tempImages = res[position];
		TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
		txtTitle.setText(tempValues);
		ImageView imageView = (ImageView) convertView.findViewById(R.id.icon);
		imageView.setImageResource(tempImages);
		return convertView;
	}




	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView v = (TextView) super.getView(position, convertView, parent);
		v.setTypeface(tf);
		v.setMaxWidth(110);
		//v.setTextSize(14);
		v.setPadding(0, 5, 0, 5);
		v.setTextColor(Color.parseColor("#000000"));
		return v;
	}

	// This funtion called for each row ( Called data.size() times )
	public View getCustomView(int position, View convertView, ViewGroup parent) {

		/********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
		View row = inflater.inflate(R.layout.spinnerimage, parent, false);

		/***** Get each Model object from Arraylist ********/
		String tempValues = data[position];
		Integer tempImages = res[position];
		
		TextView label = (TextView) row.findViewById(R.id.spinnerTarget1);
		ImageView companyLogo = (ImageView) row.findViewById(R.id.imageView11);


//		if(position == data.) {
//			label.setTextColor(ContextCompat.getColor(getContext(), R.color.white));//change color
//		}

		label.setText(tempValues);
		if(tempImages!=0){
			companyLogo.setImageResource(tempImages);	
		}
		else{
			companyLogo.setVisibility(View.INVISIBLE);
		}
		

		return row;
	}
}