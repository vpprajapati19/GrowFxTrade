package com.growfxtrade.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "AddCurrencyActivity";
    private ImageView ivicon;
    LinearLayout lv_pf;
    private Dialog dialog;
    private EditText et_name, et_email,et_adharcard, et_contactno,et_accno,et_ifscno,et_pancardno,et_docno;
    JSONObject jsonObject;
    private TextView tv_profit, tv_available;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initComponent();

        if (!CommonMethods.getInternetStatus(this)) {
            CommonMethods.showInternetDialog(this);
        } else {
            getProfileInfo();
            getPortfolio();
        }

        lv_pf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProfileActivity.this,demo_popup.class);
                startActivity(intent);
            }
        });
    }

    private void initComponent() {
        ivicon = findViewById(R.id.ivicon);
        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_contactno = findViewById(R.id.et_contactno);
        et_accno = findViewById(R.id.et_accno);
        tv_available = findViewById(R.id.tv_available);
        tv_profit = findViewById(R.id.tv_profit);
        et_ifscno = findViewById(R.id.et_ifscno);
        et_pancardno = findViewById(R.id.et_pancardno);
        et_docno = findViewById(R.id.et_docno);
        et_adharcard = findViewById(R.id.et_adharcard);
        lv_pf = findViewById(R.id.lv_pf);

//        et_name.setText(PrefrenceManager.getString(this, PrefrenceManager.USERID));
//        et_email.setText(PrefrenceManager.getString(this, PrefrenceManager.EMAIL));
//        et_contactno.setText(PrefrenceManager.getString(this, PrefrenceManager.MOBILE));

        ivicon.setOnClickListener(this);
    }

//


    @Override
    public void onClick(View view) {
        if (view == ivicon) {
            finish();
            Intent intent = new Intent(this, MainActivity.class);
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

    public void getProfileInfo() {
        dialog = CommonMethods.showDialogProgressBarNew(this);
        RequestInterface req = RetrofitClient.getClient(this).create(RequestInterface.class);
        Call<ResponseBody> call = req.getPrfile(PrefrenceManager.getString(ProfileActivity.this, PrefrenceManager.USERID));
        Log.e("url_100","=="+call);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("res_104", "=" + response.body());
                Log.e("res_105", "=" + response);
                dialog.dismiss();
                CommonMethods.PrintLog(TAG, "url  " + response.raw().request().url());
                String usernm = "";
                String email = "";
                String mono = "";
                String amount = "";
                String profit = "";
                String bank_account_no = "";
                String ifsc = "";
                String doc_id = "";
                String doc_no = "";
                String pan_no = "";
                String upi = "";
                String bank_name = "";
                try {
                    String res = response.body().string();

                    JSONObject jsonObject = new JSONObject(res);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("wallet_data");
                    usernm=jsonObject1.getString("user_name");
                    email=jsonObject1.getString("email");
                    mono=jsonObject1.getString("mono");
                    amount=jsonObject1.getString("amountt");
                    profit=jsonObject1.getString("profit");
                    Log.e("profit128=","=="+profit);
                    bank_account_no=jsonObject1.getString("bank_account_no");
                    ifsc=jsonObject1.getString("ifsc");
                    doc_no=jsonObject1.getString("doc_no");
                    pan_no=jsonObject1.getString("pan_no");
                    doc_id=jsonObject1.getString("doc_id");
                    doc_id=jsonObject1.getString("doc_id");
                    upi=jsonObject1.getString("upi_id");
                    bank_name = jsonObject1.getString("bank_name");

                    PrefrenceManager.setString(ProfileActivity.this, PrefrenceManager.Bankname, bank_name);
                    PrefrenceManager.setString(ProfileActivity.this, PrefrenceManager.Accountno, bank_account_no);
                    PrefrenceManager.setString(ProfileActivity.this, PrefrenceManager.IFCI, ifsc);
                    PrefrenceManager.setString(ProfileActivity.this, PrefrenceManager.UPI, upi);


                } catch (Exception e) {
                    CommonMethods.PrintLog(TAG, "url Exception " + e.toString());
                }
                et_name.setText(usernm);
                et_email.setText(email);
                et_contactno.setText(mono);
                et_accno.setText(bank_account_no);
                et_ifscno.setText(ifsc);
                et_pancardno.setText(pan_no);
                et_docno.setText(doc_no);
                et_adharcard.setText(doc_no);

                if(doc_id.equalsIgnoreCase("1")){
                    et_docno.setHint("Aadhar Card Number");
                }else if(doc_id.equalsIgnoreCase("2")){
                    et_docno.setHint("Voter Id Number");
                }else if(doc_id.equalsIgnoreCase("3")){
                    et_docno.setHint("Other");
                }else {
                    et_docno.setHint("Other");
                }
                Double value= Double.valueOf(profit);
                tv_profit.setText("$ "+String.format("%.2f", value));

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                CommonMethods.simpleSnackbar(ProfileActivity.this, AppUtils.SERVER_ERROR);
//                CommonMethods.PrintLog(TAG, t.toString());

            }
        });
    }

    public void getPortfolio() {

        RequestInterface req = RetrofitClient.getClient(this).create(RequestInterface.class);
        Call<ResponseBody> call = req.getPortfolio(PrefrenceManager.getString(ProfileActivity.this, PrefrenceManager.USERID));
        Log.e("url_174","=="+call);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                Log.e("res_179", "=" + response.body());
                Log.e("res_180", "=" + response);
                String profit_loss = "";
                String available = "";
                String total_buy = "";
                String total_portfolio = "";
                String withdraw = "";
                try {
                    String res = response.body().string();
                    JSONObject jsonObject = new JSONObject(res);
                    profit_loss = jsonObject.getString("profit_loss");
                    available = jsonObject.getString("available");
                    total_buy = jsonObject.getString("total_buy");
                    total_portfolio = jsonObject.getString("total_portfolio");
                    Log.e("portfolio",""+total_portfolio);
                    withdraw = jsonObject.getString("withdraw");
                } catch (Exception e) {
                    CommonMethods.PrintLog(TAG, "url Exception " + e.toString());
                }
                Double value= Double.valueOf(total_portfolio);
                tv_available.setText("$ "+String.format("%.2f", value));
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                CommonMethods.simpleSnackbar(ProfileActivity.this, AppUtils.SERVER_ERROR);
//                CommonMethods.PrintLog(TAG, t.toString());

            }
        });
    }

}
