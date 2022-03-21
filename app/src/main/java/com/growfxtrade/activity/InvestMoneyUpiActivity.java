package com.growfxtrade.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.growfxtrade.R;

public class InvestMoneyUpiActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivicon,iv_copybutton;
    TextView tv_price,tv_copy;
    String price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invest_money_upi);
        initComponent();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            price = bundle.getString("price");
            Log.e("price255555","=="+price);
            tv_price.setText(price);
        }
        iv_copybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) InvestMoneyUpiActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(tv_copy.getText().toString());
                } else {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) InvestMoneyUpiActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", tv_copy.getText().toString());
                    Toast.makeText(InvestMoneyUpiActivity.this, "Copied Text", Toast.LENGTH_SHORT).show();
                    clipboard.setPrimaryClip(clip);
                }
            }
        });
    }

    private void initComponent() {
        ivicon = findViewById(R.id.ivicon);
        tv_price = findViewById(R.id.tv_price);
        iv_copybutton = findViewById(R.id.iv_copybutton);
        tv_copy = findViewById(R.id.tv_copy);

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