package com.growfxtrade.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.growfxtrade.prefrence.PrefrenceManager;
import com.growfxtrade.utils.AppUtils;
import com.growfxtrade.utils.CommonMethods;
import com.growfxtrade.utils.RequestInterface;
import com.growfxtrade.utils.RetrofitClient;
import com.growfxtrade.R;

import org.json.JSONObject;

import java.text.DecimalFormat;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPortfolioActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "AddCurrencyActivity";
    private ImageView ivicon;
    private Dialog dialog;
    private TextView tv_proitloss, tv_available,
            tv_totalorex, tv_totalportfolio,
            tv_withdrawl,tv_investmoney,
            tv_withdraemoney,tv_show_investmoney,tv_show_wirthdawmoney;

    private LinearLayout btnInvestMoney, btnWithdrowMoney;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myportfolio);
        initComponent();


        if (!CommonMethods.getInternetStatus(this)) {
            CommonMethods.showInternetDialog(this);
        } else {
            getPortfolio();
            getinvest();
            getwirthdraw();

        }
    }

    private void initComponent() {
        ivicon = findViewById(R.id.ivicon);

        tv_proitloss = findViewById(R.id.tv_proitloss);
        tv_available = findViewById(R.id.tv_available);
        tv_totalorex = findViewById(R.id.tv_totalorex);
        tv_totalportfolio = findViewById(R.id.tv_totalportfolio);
        tv_withdrawl = findViewById(R.id.tv_withdrawl);
       // tv_investmoney = findViewById(R.id.tv_investmoney);
       // tv_withdraemoney = findViewById(R.id.tv_withdraemoney);
        btnInvestMoney = findViewById(R.id.btn_InvestMoneyMore);
        btnWithdrowMoney = findViewById(R.id.btn_WithdrawMoney);
        tv_show_investmoney = findViewById(R.id.tv_show_investmoney);
        tv_show_wirthdawmoney = findViewById(R.id.tv_show_wirthdawmoney);

        ivicon.setOnClickListener(this);
        btnWithdrowMoney.setOnClickListener(this);
        btnInvestMoney.setOnClickListener(this);
    }

    public void getPortfolio() {
        dialog = CommonMethods.showDialogProgressBarNew(this);
        RequestInterface req = RetrofitClient.getClient(this).create(RequestInterface.class);
        Call<ResponseBody> call = req.getPortfolio(PrefrenceManager.getString(MyPortfolioActivity.this, PrefrenceManager.USERID));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("res_87", "=" + response.body());
                Log.e("res__88", "=" + response);
                dialog.dismiss();
                CommonMethods.PrintLog(TAG, "url  " + response.raw().request().url());
                String profit_loss = "";
                String available = "";
                String total_buy = "";
                String total_portfolio = "";
                String withdraw = "";
                try {
                    String res = response.body().string();
                    CommonMethods.PrintLog(TAG, "url res " + res);
                    JSONObject jsonObject = new JSONObject(res);
                    profit_loss = jsonObject.getString("profit_loss");
                    available = jsonObject.getString("available");
                    total_buy = jsonObject.getString("total_buy");
                    total_portfolio = jsonObject.getString("total_portfolio");
                    withdraw = jsonObject.getString("withdraw");
                } catch (Exception e) {
                    CommonMethods.PrintLog(TAG, "url Exception " + e.toString());
                }

              //  Log.e(TAG,"profit_loss  "+profit_loss);
                Log.e(TAG,"available  "+available);
                Log.e(TAG,"total_buy  "+total_buy);
                Log.e(TAG,"total_portfolio  "+total_portfolio);
                Log.e(TAG,"withdraw  "+withdraw);


                if(profit_loss.equalsIgnoreCase("null") || profit_loss.equalsIgnoreCase("0.00")){
                    tv_proitloss.setText("$ " + "0");
                }else {
                    if(Double.parseDouble(profit_loss)<0){
                        tv_proitloss.setTextColor(getResources().getColor(R.color.red));
                        Double value= Double.valueOf(profit_loss);
                        tv_proitloss.setText("$ " + String.format("%.2f", value));
                    }else {
                        tv_proitloss.setTextColor(getResources().getColor(R.color.green));
                        Double value= Double.valueOf(profit_loss);
                        tv_proitloss.setText("$ " + String.format("%.2f", value));
                    }
                }

                if(profit_loss.equalsIgnoreCase("null")|| profit_loss.equalsIgnoreCase("0.00")){
                  // tv_available.setText("$ " + "0");
                    tv_totalportfolio.setText("$ " + "0");
                }else {
                    Double value= Double.valueOf(total_portfolio);
                    tv_totalportfolio.setText("$ " + String.format("%.2f", value));
                   // tv_available.setText("$ " + available);
                }
                Double value_available= Double.valueOf(available);
                Double value_withdraw= Double.valueOf(withdraw);
                Double value_total_buy= Double.valueOf(total_buy);
                tv_available.setText("$ " + String.format("%.2f", value_available));
                tv_withdrawl.setText("$ " + String.format("%.2f", value_withdraw));
                tv_totalorex.setText("$ " + String.format("%.2f", value_total_buy));
                //tv_totalorex.setText("$ " + new DecimalFormat("##.###").format(Double.parseDouble(total_buy)));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                CommonMethods.simpleSnackbar(MyPortfolioActivity.this, AppUtils.SERVER_ERROR);
//                CommonMethods.PrintLog(TAG, t.toString());

            }
        });
    }

    public void getinvest() {

        RequestInterface req = RetrofitClient.getClient(this).create(RequestInterface.class);
        Call<ResponseBody> call = req.get_pending_wallet(PrefrenceManager.getString(MyPortfolioActivity.this, PrefrenceManager.USERID));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                CommonMethods.PrintLog(TAG, "url  " + response.raw().request().url());
                String invest = "";

                try {
                    String res = response.body().string();
                    CommonMethods.PrintLog(TAG, "url res " + res);
                    JSONObject jsonObject = new JSONObject(res);

                    JSONObject jsonObject1=jsonObject.getJSONObject("wallet_data");

                    invest = jsonObject1.getString("wamount");

                } catch (Exception e) {
                    tv_show_investmoney.setVisibility(View.GONE);
                    CommonMethods.PrintLog(TAG, "url Exception " + e.toString());
                }
                tv_show_investmoney.setText("Invest : " + invest+"$ Pending");

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                CommonMethods.simpleSnackbar(MyPortfolioActivity.this, AppUtils.SERVER_ERROR);
//                CommonMethods.PrintLog(TAG, t.toString());

            }
        });
    }



    public void getwirthdraw() {

        RequestInterface req = RetrofitClient.getClient(this).create(RequestInterface.class);
        Call<ResponseBody> call = req.get_pending_withdraw(PrefrenceManager.getString(MyPortfolioActivity.this, PrefrenceManager.USERID));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                CommonMethods.PrintLog(TAG, "url  " + response.raw().request().url());
                String withdrawamount = "";

                try {
                    String res = response.body().string();
                    CommonMethods.PrintLog(TAG, "url res " + res);
                    JSONObject jsonObject = new JSONObject(res);

                    JSONObject jsonObject1=jsonObject.getJSONObject("withdraw_data");

                    withdrawamount = jsonObject1.getString("req_amount");

                } catch (Exception e) {
                    tv_show_wirthdawmoney.setVisibility(View.GONE);
                    CommonMethods.PrintLog(TAG, "url Exception " + e.toString());
                }

                tv_show_wirthdawmoney.setText("Withdrawal : " + withdrawamount+"$ Pending");


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                CommonMethods.simpleSnackbar(MyPortfolioActivity.this, AppUtils.SERVER_ERROR);
//                CommonMethods.PrintLog(TAG, t.toString());

            }
        });
    }
    @Override
    public void onClick(View view) {
        if (view == ivicon) {
            finish();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.enter_from_left,
                    R.anim.exit_to_right);


        }
        if (view == btnInvestMoney) {
            String screen="portfolio";
            Bundle b =new Bundle();
            b.putString("screntype", "" +screen);
            Intent intent = new Intent(this, InvestMoneyActivity.class);
            intent.putExtras(b);
            startActivity(intent);
            overridePendingTransition(R.anim.enter_from_left,
                    R.anim.exit_to_right);


        } if (view == btnWithdrowMoney) {
            String screen="portfolio";
            Bundle b =new Bundle();
            b.putString("screntype", "" +screen);
            Intent intent = new Intent(this, WithdrawMoneyActivity.class);
            intent.putExtras(b);
            startActivity(intent);
            overridePendingTransition(R.anim.enter_from_left,
                    R.anim.exit_to_right);


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_left,
                R.anim.exit_to_right);
    }

}
