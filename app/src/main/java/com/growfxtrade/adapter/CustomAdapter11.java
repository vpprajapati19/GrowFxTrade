package com.growfxtrade.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.growfxtrade.R;

/**
 * Created by Oclemy on 8/4/2016 for ProgrammingWizards Channel and http://www.camposha.com.
 */
public class CustomAdapter11 extends BaseAdapter {

    Context c;
    private String[] data;
    public Integer[] res;

    public CustomAdapter11(Context c, String[] values,
                           Integer[] mobileproviderarrayimages2) {
        this.c = c;
        data = values;
        res = mobileproviderarrayimages2;
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int i) {
        return data[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(c).inflate(R.layout.spinnerimage2, viewGroup, false);
        }
        String tempValues = data[i];
        Integer tempImages = res[i];

        TextView label = (TextView) view.findViewById(R.id.spinnerTarget1);
        ImageView companyLogo = (ImageView) view.findViewById(R.id.imageView11);

        label.setText(tempValues);
        if(tempImages!=0){
            companyLogo.setImageResource(tempImages);
        }
        else{
            companyLogo.setVisibility(View.INVISIBLE);
        }
        return view;
    }
}
