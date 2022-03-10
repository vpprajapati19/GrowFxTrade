package com.growfxtrade.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.growfxtrade.R;

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

        label.setText(tempValues);
        return view;
    }
}
