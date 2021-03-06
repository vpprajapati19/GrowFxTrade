package com.growfxtrade.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.growfxtrade.R;
import com.growfxtrade.adapter.CustomAdapterPayment;
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

public class WithdrawMoneyActivity extends AppCompatActivity implements View.OnClickListener {
    private Dialog dialog;
    private String TAG = "WithdrawMoneyActivity";
    String screentype="";
    JSONObject jsonObject;
    TextView tv_amount;
    private ImageView ivicon;
    private Spinner spinner_payment;
    String selectPaymentValue ="";

    LinearLayout btn_submit;
    TextInputEditText et_investmoney;
    private final Integer[] paymentFlg = {R.drawable.ic_deposit_new};
    private final String[] paymentValue = {"Crypto", "Upi", "Net-Banking"};


   /* private String TAG = "WithdrawMoneyActivity";
    private ImageView ivicon;
    private Dialog dialog;
    private EditText et_name, et_email, et_contactno, et_wallet, et_investmoney;
    private CardView btn_submit;
    JSONObject jsonObject;
    private Spinner spinner_country;
    private final Integer[] countryflag = {
            R.drawable.afghanistan, R.drawable.australia, R.drawable.bangladesh, R.drawable.england,
            R.drawable.india,
            R.drawable.newzealand, R.drawable.pakistan, R.drawable.southafrica, R.drawable.srilanka, R.drawable.westindies

    };
    private final String[] country = {"Afghanistan",
            "Australia", "Bangladesh", "England", "India",
            "New Zealand",
            "Pakistan", "South Africa", "Sri Lanka", "West Indies"};*/


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawal);
        initComponent();

        Bundle bundle= getIntent().getExtras();
        if(bundle != null){
            screentype = bundle.getString("screntype");
        }
        if (!CommonMethods.getInternetStatus(this)) {
            CommonMethods.showInternetDialog(this);
        } else {
            getProfileInfo();

        }


    }

    private void initComponent() {
        ivicon = findViewById(R.id.ivicon);
        btn_submit = findViewById(R.id.btn_submit);
        et_investmoney = findViewById(R.id.et_investmoney);
        spinner_payment = findViewById(R.id.spinner_payment_withdraw);
        tv_amount = findViewById(R.id.tv_amount);

        spinner_payment.setAdapter(new CustomAdapterPayment(this,paymentValue));
        spinner_payment.setSelection(0);
        spinner_payment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                //selectPaymentValue = selectedItem;
                selectPaymentValue = selectedItem;
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            { }
        });

        ivicon.setOnClickListener(this);
        btn_submit.setOnClickListener(this);


        /*ivicon = findViewById(R.id.ivicon);
        btn_submit = findViewById(R.id.btn_submit);
        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_contactno = findViewById(R.id.et_contactno);
        et_wallet = findViewById(R.id.et_wallet);
        et_investmoney = findViewById(R.id.et_investmoney);

        et_name.setText(PrefrenceManager.getString(this, PrefrenceManager.USERNAME));
        et_email.setText(PrefrenceManager.getString(this, PrefrenceManager.EMAIL));
        et_contactno.setText(PrefrenceManager.getString(this, PrefrenceManager.MOBILE));
//        et_coundtry.setText(PrefrenceManager.getString(this, PrefrenceManager.COUNTRY));
//        et_coundtrycode.setText(PrefrenceManager.getString(this,PrefrenceManager.MOBILE));
        spinner_country = findViewById(R.id.spinner_country);
        spinner_country.setAdapter(new CustomAdapter1(this, country, countryflag));
        spinner_country.setSelection(4);

        final String prefix = "$ ";
        et_investmoney.setText(prefix);

        et_investmoney.setFilters(new InputFilter[] {
                new InputFilter() {
                    @Override
                    public CharSequence filter(final CharSequence source, final int start,
                                               final int end, final Spanned dest, final int dstart, final int dend) {
                        final int newStart = Math.max(prefix.length(), dstart);
                        final int newEnd = Math.max(prefix.length(), dend);
                        if (newStart != dstart || newEnd != dend) {
                            final SpannableStringBuilder builder = new SpannableStringBuilder(dest);
                            builder.replace(newStart, newEnd, source);
                            if (source instanceof Spanned) {
                                TextUtils.copySpansFrom(
                                        (Spanned) source, 0, source.length(), null, builder, newStart);
                            }
                            Selection.setSelection(builder, newStart + source.length());
                            return builder;
                        } else {
                            return null;
                        }
                    }
                }
        });

        ivicon.setOnClickListener(this);
        btn_submit.setOnClickListener(this);*/
    }
//
    @Override
    public void onClick(View view) {
        if (view == ivicon) {

            if(screentype.equalsIgnoreCase("portfolio")){
                finish();
                Intent intent = new Intent(this, MyPortfolioActivity.class);
                startActivity(intent);
            }else{
                finish();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_from_left,
                        R.anim.exit_to_right);
            }

        }

        if (view == btn_submit) {
            String money = "";
            money = et_investmoney.getText().toString().replace("$","").trim();
            Log.e(TAG, "money  " + money);
            if (money.length() <= 0) {
                et_investmoney.setError("Enter Valid Amount");
                return;
            }
            if (!CommonMethods.getInternetStatus(this)) {
                CommonMethods.showInternetDialog(this);
            } else {
                addMoney(money);

            }

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

    public void getProfileInfo() {
        dialog = CommonMethods.showDialogProgressBarNew(this);
        RequestInterface req = RetrofitClient.getClient(this).create(RequestInterface.class);
        Call<ResponseBody> call = req.getPrfile(PrefrenceManager.getString(WithdrawMoneyActivity.this, PrefrenceManager.USERID));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("res_197", "=" + response.body());
                Log.e("res__198", "=" + response);
                dialog.dismiss();
                CommonMethods.PrintLog(TAG, "url  " + response.raw().request().url());
                String usernm = "";
                String email = "";
                String mono = "";
                String amount = "";
                String profit = "";
                try {
                    String res = response.body().string();

                    JSONObject jsonObject = new JSONObject(res);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("wallet_data");
//                    usernm=jsonObject1.getString("user_id");
//                    email=jsonObject1.getString("email");
//                    mono=jsonObject1.getString("mono");
                    amount = jsonObject1.getString("amountt");
                    tv_amount.setText("$ "+amount);
//                    profit=jsonObject1.getString("profit");
                    Log.e("ammount217","=="+amount);

                } catch (Exception e) {
                    CommonMethods.PrintLog(TAG, "url Exception " + e.toString());
                }
                //et_wallet.setText("$ " + amount);
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                CommonMethods.simpleSnackbar(WithdrawMoneyActivity.this, AppUtils.SERVER_ERROR);
//                CommonMethods.PrintLog(TAG, t.toString());

            }
        });
    }
    public void addMoney(String money) {
        Log.e(TAG, "money  " + money);
        Log.e(TAG, "money1  " + PrefrenceManager.getString(WithdrawMoneyActivity.this, PrefrenceManager.USERID));
        dialog = CommonMethods.showDialogProgressBarNew(this);
        RequestInterface req = RetrofitClient.getClient(this).create(RequestInterface.class);
        Call<ResponseBody> call = req.getWithdraw(PrefrenceManager.getString(WithdrawMoneyActivity.this, PrefrenceManager.USERID),selectPaymentValue, money);

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
                    Toast.makeText(WithdrawMoneyActivity.this, msg, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(WithdrawMoneyActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(WithdrawMoneyActivity.this, msg, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                CommonMethods.simpleSnackbar(WithdrawMoneyActivity.this, AppUtils.SERVER_ERROR);
//                CommonMethods.PrintLog(TAG, t.toString());

            }
        });
    }


}
