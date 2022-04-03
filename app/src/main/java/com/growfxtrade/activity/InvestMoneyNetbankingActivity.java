package com.growfxtrade.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.growfxtrade.R;
import com.growfxtrade.prefrence.PrefrenceManager;
import com.growfxtrade.utils.AppUtils;
import com.growfxtrade.utils.CommonMethods;
import com.growfxtrade.utils.RequestInterface;
import com.growfxtrade.utils.RetrofitClient;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvestMoneyNetbankingActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivicon;
    TextView tv_price,tv_bankname,tv_bank_accountno,tv_ifsccode;
    private String TAG = "InvestMoneyQrCodeActivity";
    LinearLayout btn_sent_money_netbank;
    private Dialog dialog;
    JSONObject jsonObject;
    String price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invest_money_netbanking);
        initComponent();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            price = bundle.getString("price");
            Log.e("price25","=="+price);
            tv_price.setText(price);
        }
    }
    private void initComponent() {
        ivicon = findViewById(R.id.ivicon);
        tv_price = findViewById(R.id.tv_price);
        tv_bank_accountno = findViewById(R.id.tv_bank_accountno);
        tv_ifsccode= findViewById(R.id.tv_ifsccode);
        tv_bankname= findViewById(R.id.tv_bankname);
        btn_sent_money_netbank = findViewById(R.id.btn_sent_money_netbank);
        ivicon.setOnClickListener(this);
        btn_sent_money_netbank.setOnClickListener(this);

        tv_bank_accountno.setText(PrefrenceManager.Accountno);
        tv_ifsccode.setText(PrefrenceManager.IFCI);
        tv_bankname.setText(PrefrenceManager.Bankname);
    }

    @Override
    public void onClick(View v) {
        if (v == ivicon) {
            finish();
            onBackPressed();
        }else if (v== btn_sent_money_netbank){
            addMoney(price);
        }
    }
    public void addMoney(String money) {
        Log.e(TAG, "money  " + money);
        Log.e(TAG, "money1  " + PrefrenceManager.getString(InvestMoneyNetbankingActivity.this, PrefrenceManager.USERID));
        dialog = CommonMethods.showDialogProgressBarNew(this);
        RequestInterface req = RetrofitClient.getClient(this).create(RequestInterface.class);
        Call<ResponseBody> call = req.addMoney(PrefrenceManager.getString(InvestMoneyNetbankingActivity.this, PrefrenceManager.USERID), "add", money);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dialog.dismiss();
                CommonMethods.PrintLog(TAG, "url  " + response.raw().request().url());
                String staus = "";
                String msg = "";
                try {
                    String res = response.body().string();
                    CommonMethods.PrintLog(TAG, "url res " + res);

                    jsonObject = new JSONObject(res);
                    staus = jsonObject.getString("staus");
                    msg = jsonObject.getString("msg");
                } catch (Exception e) {
                    msg = "Error..!!!";
                    CommonMethods.PrintLog(TAG, "url Exception " + e.toString());
                }
                if (staus.equalsIgnoreCase("0")) {
                    final Dialog dialog1 = new Dialog(InvestMoneyNetbankingActivity.this, R.style.df_dialog);
                    dialog1.setContentView(R.layout.dialog);
                    dialog1.setCancelable(true);
                    dialog1.setCanceledOnTouchOutside(true);

                    LinearLayout dialogButton = dialog1.findViewById(R.id.btn_submit);
                    TextView txtt = dialog1.findViewById(R.id.txtt);
                    txtt.setText("Request has been Updated. You will get a mail within few time");
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            startActivity(new Intent(InvestMoneyNetbankingActivity.this,MainActivity.class));
                            finish();
                        }
                    });

                    dialog1.show();
                    //getInfoDialog("Request has been Updated. You will get a mail within few time");
//                    Toast.makeText(InvestMoneyActivity.this, msg, Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(InvestMoneyNetbankingActivity.this, msg, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                CommonMethods.simpleSnackbar(InvestMoneyNetbankingActivity.this, AppUtils.SERVER_ERROR);
//                CommonMethods.PrintLog(TAG, t.toString());

            }
        });
    }
}