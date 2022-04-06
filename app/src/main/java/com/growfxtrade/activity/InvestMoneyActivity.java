package com.growfxtrade.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Selection;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.growfxtrade.adapter.CustomAdapter1;
import com.growfxtrade.adapter.CustomAdapterPayment;
import com.growfxtrade.prefrence.PrefrenceManager;
import com.growfxtrade.utils.AppUtils;
import com.growfxtrade.utils.CommonMethods;
import com.growfxtrade.utils.RequestInterface;
import com.growfxtrade.utils.RetrofitClient;
import com.kinda.alert.KAlertDialog;
import com.growfxtrade.R;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvestMoneyActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "AddCurrencyActivity";
    private ImageView ivicon;
    private Dialog dialog;
    String screentype="";
    private EditText et_name, et_email, et_contactno, et_coundtrycode, et_investmoney;
    private Spinner spinner_country, spinner_payment;
    private LinearLayout btn_submit;
    public static String selectPaymentValue ="";
    JSONObject jsonObject;
    private final Integer[] countryflag = {
            R.drawable.afghanistan, R.drawable.australia, R.drawable.bangladesh, R.drawable.england,
            R.drawable.india,
            R.drawable.newzealand, R.drawable.pakistan, R.drawable.southafrica, R.drawable.srilanka, R.drawable.westindies

    };
    private final String[] country = {"Afghanistan",
            "Australia", "Bangladesh", "England", "India",
            "New Zealand",
            "Pakistan", "South Africa", "Sri Lanka", "West Indies"};

    private final Integer[] paymentFlg = {R.drawable.ic_deposit_new};
    private final String[] paymentValue = {"CRYPTO", "UPI", "NET BANKING"};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investmoney);
        initComponent();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            screentype = bundle.getString("screntype");
        }


    }

    private void initComponent() {
        ivicon = findViewById(R.id.ivicon);
        btn_submit = findViewById(R.id.btn_submit);
        et_name = findViewById(R.id.et_name);
        spinner_country = findViewById(R.id.spinner_country);
        spinner_payment = findViewById(R.id.spinner_payment);

        spinner_country.setAdapter(new CustomAdapter1(this,country,countryflag));
        spinner_country.setSelection(4);

        spinner_payment.setAdapter(new CustomAdapterPayment(this,paymentValue));
        spinner_payment.setSelection(0);
        spinner_payment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                Log.e("logitrm999",""+selectedItem);
                selectPaymentValue = selectedItem;
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            { }
        });


        et_email = findViewById(R.id.et_email);
        et_contactno = findViewById(R.id.et_contactno);
        et_coundtrycode = findViewById(R.id.et_coundtrycode);
        et_investmoney = findViewById(R.id.et_investmoney);

        et_name.setText(PrefrenceManager.getString(this, PrefrenceManager.USERNAME));
        et_email.setText(PrefrenceManager.getString(this, PrefrenceManager.EMAIL));
        et_contactno.setText(PrefrenceManager.getString(this, PrefrenceManager.MOBILE));

        final String prefix = "$ ";
        et_investmoney.setText(prefix);

       /* et_investmoney.setFilters(new InputFilter[] {
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
        });*/


        ivicon.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
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


            if (money.length() <= 0) {
                et_investmoney.setError("Enter Valid Amount");
                return;
            }
            if (!CommonMethods.getInternetStatus(this)) {
                CommonMethods.showInternetDialog(this);
            } else {
                if(selectPaymentValue.equals("UPI")) {
                    Bundle b=new Bundle();
                    Intent intent = new Intent(getApplicationContext(), InvestMoneyUpiActivity.class);
                    Log.e("price170","=="+money);
                    b.putString("price",money);
                    intent.putExtras(b);
                    startActivity(intent);
                } else if(selectPaymentValue.equals("CRYPTO")) {
                    Bundle b=new Bundle();
                    Intent intent = new Intent(getApplicationContext(), InvestMoneyQrCodeActivity.class);
                    Log.e("price176","=="+money);
                    b.putString("price",money);
                    intent.putExtras(b);
                    startActivity(intent);
                }else if(selectPaymentValue.equals("NET BANKING")) {
                    Bundle b=new Bundle();
                    Intent intent = new Intent(getApplicationContext(), InvestMoneyNetbankingActivity.class);
                    Log.e("price176","=="+money);
                    b.putString("price",money);
                    intent.putExtras(b);
                    startActivity(intent);
                }

              //  addMoney(money);
            }

        }
    }

    private void getInfoDialog(final String message) {
        new KAlertDialog(this)
                .setTitleText("")
                .setContentText(message)
                .setConfirmClickListener(new KAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(KAlertDialog kAlertDialog) {
                        kAlertDialog.dismissWithAnimation();
                        startActivity(new Intent(InvestMoneyActivity.this, MainActivity.class));
                        finish();
                    }
                })
                .show();


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

    public void addMoney(String money) {
        Log.e(TAG, "money  " + money);
        Log.e(TAG, "money1  " + PrefrenceManager.getString(InvestMoneyActivity.this, PrefrenceManager.USERID));
        dialog = CommonMethods.showDialogProgressBarNew(this);
        RequestInterface req = RetrofitClient.getClient(this).create(RequestInterface.class);
        Call<ResponseBody> call = req.addMoney(PrefrenceManager.getString(InvestMoneyActivity.this, PrefrenceManager.USERID), "add", money);

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

                    final Dialog dialog = new Dialog(InvestMoneyActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.dialog);

                    CardView dialogButton = dialog.findViewById(R.id.btn_submit);
                    TextView txtt = dialog.findViewById(R.id.txtt);
                    txtt.setText("Request has been Updated. You will get a mail within few time");
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            startActivity(new Intent(InvestMoneyActivity.this,MainActivity.class));
                            finish();
                        }
                    });

                    dialog.show();
                    //getInfoDialog("Request has been Updated. You will get a mail within few time");
//                    Toast.makeText(InvestMoneyActivity.this, msg, Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(InvestMoneyActivity.this, msg, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                CommonMethods.simpleSnackbar(InvestMoneyActivity.this, AppUtils.SERVER_ERROR);
//                CommonMethods.PrintLog(TAG, t.toString());

            }
        });
    }

}
