package com.growfxtrade.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.growfxtrade.R;
import com.growfxtrade.activity.InvestMoneyActivity;

public class CustomAdapterPayment extends BaseAdapter {

    Context c;
    private String[] data;

    public CustomAdapterPayment(Context c, String[] values) {
        this.c = c;
        data = values;
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
            view = LayoutInflater.from(c).inflate(R.layout.spinner_payment, viewGroup, false);
        }
        String tempValues = data[i];

        TextView label = (TextView) view.findViewById(R.id.spinnerTarget1);
        LinearLayout linlay = (LinearLayout) view.findViewById(R.id.linlay);
        ImageView imageView11 = (ImageView) view.findViewById(R.id.imageView11);
        View viewe = (View) view.findViewById(R.id.viewe);
        /*if(InvestMoneyActivity.selectPaymentValue.equalsIgnoreCase(tempValues)){
            linlay.setVisibility(View.GONE);
        }else{
            linlay.setVisibility(View.VISIBLE);
        }*/
        if (tempValues.equalsIgnoreCase("CRYPTO")){
            imageView11.setVisibility(View.INVISIBLE);

        }else {
            imageView11.setVisibility(View.INVISIBLE);
        }
        if (tempValues.equalsIgnoreCase("NET BANKING")){
            viewe.setVisibility(View.INVISIBLE);

        }else {
            viewe.setVisibility(View.VISIBLE);
        }

        label.setText(tempValues);
        return view;
    }
}
