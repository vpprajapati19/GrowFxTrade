package com.growfxtrade.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.growfxtrade.R;

public class InvestMoneyUpiActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivicon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invest_money_upi);
        initComponent();
    }

    private void initComponent() {
        ivicon = findViewById(R.id.ivicon);

        ivicon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == ivicon) {
            finish();
            onBackPressed();
        }
    }
}