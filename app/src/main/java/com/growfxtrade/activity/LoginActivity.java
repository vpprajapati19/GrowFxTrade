package com.growfxtrade.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.growfxtrade.prefrence.PrefrenceManager;
import com.growfxtrade.utils.AppUtils;
import com.growfxtrade.utils.CommonMethods;
import com.growfxtrade.utils.RequestInterface;
import com.growfxtrade.utils.RetrofitClient;
import com.growfxtrade.R;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_usernmae, et_password;
    private LinearLayout btnsignup;
    private TextView tv_forgot;
    private LinearLayout iv_login;
    private Dialog dialog;
    private String TAG = "LoginActivity";
    JSONObject jsonObject;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initComponent();
    }

    private void initComponent() {
        btnsignup = findViewById(R.id.btnsignup);
        tv_forgot = findViewById(R.id.tv_forgot);
        iv_login = findViewById(R.id.ivlogin);
        et_usernmae = findViewById(R.id.et_usernmae);
        et_password = findViewById(R.id.et_password);
        iv_login.setOnClickListener(this);
        btnsignup.setOnClickListener(this);
        tv_forgot.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == btnsignup) {
//            Intent intent = new Intent(LoginActivity.this, SignupOtpActivity.class);
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right,
                    R.anim.slide_out_left);
        }
        if (view == tv_forgot) {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right,
                    R.anim.slide_out_left);
        }
        if (view == iv_login) {
            String username = "";
            String password = "";

            username = et_usernmae.getText().toString().trim();
            password = et_password.getText().toString().trim();

            if (username.length() <= 0) {
                et_usernmae.setError("Valid Username");
                return;
            }
            if (password.length() <= 0) {
                et_password.setError("Valid Password");
                return;
            }
            if (!CommonMethods.getInternetStatus(LoginActivity.this)) {
                CommonMethods.showInternetDialog(LoginActivity.this);
            } else {
                getLogin(username, password);
            }

//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(intent);
//            overridePendingTransition(R.anim.item_animation_from_bottom,
//                    R.anim.item_animation_from_bottom);
        }
        if (view == tv_forgot) {
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(intent);
//            overridePendingTransition(R.anim.slide_in_right,
//                    R.anim.slide_out_left);
        }
    }

    public void getLogin(String uname, String pwd) {
        dialog = CommonMethods.showDialogProgressBarNew(this);
        RequestInterface req = RetrofitClient.getClient(this).create(RequestInterface.class);
        Call<ResponseBody> call = req.getLoginDetails(uname, pwd);

        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dialog.dismiss();
                String staus = "";
                String msg = "";
                String user_id = "";
                String email = "";
                String mono = "";
                String status = "";
                String country = "";
                String user_name = "";
                try {

                    String jsonst = response.body().string();
                    CommonMethods.PrintLog(TAG, "url jsonst " + jsonst);

                    jsonObject = new JSONObject(jsonst);
                    staus = jsonObject.getString("staus");
                    msg = jsonObject.getString("msg");

                } catch (Exception e) {
                    msg = "Error..!!!";
                    CommonMethods.PrintLog("Error catch ", e.toString());
                    e.printStackTrace();
                }

                if (staus.equalsIgnoreCase("0")) {
                    try {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("user");
                        Log.e("jsonloginapp",""+jsonObject1);
                        user_id = jsonObject1.getString("user_id");
                        Log.e("user_id--147","=="+user_id);
                        email = jsonObject1.getString("email");
                        mono = jsonObject1.getString("mono");
                        status = jsonObject1.getString("status");
                        country = jsonObject1.getString("country");
                        country = jsonObject1.getString("country");
                        user_name = jsonObject1.getString("user_name");

                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                        PrefrenceManager.setString(LoginActivity.this, PrefrenceManager.USERID, user_id);
                        PrefrenceManager.setString(LoginActivity.this, PrefrenceManager.EMAIL, email);
                        PrefrenceManager.setString(LoginActivity.this, PrefrenceManager.MOBILE, mono);
                        PrefrenceManager.setString(LoginActivity.this, PrefrenceManager.STATUS, status);
                        PrefrenceManager.setString(LoginActivity.this, PrefrenceManager.COUNTRY, country);
                        PrefrenceManager.setString(LoginActivity.this, PrefrenceManager.USERNAME, user_name);
                        PrefrenceManager.setString(LoginActivity.this, PrefrenceManager.LOGIN_STATUS, "true");
                        getwirthdraw();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.item_animation_from_bottom,
                            R.anim.item_animation_from_bottom);
                    finish();

                } else {
                    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                CommonMethods.simpleSnackbar(LoginActivity.this, AppUtils.SERVER_ERROR);
                CommonMethods.PrintLog(TAG, "onFailure " + t.toString());
            }
        });
    }

    public void getwirthdraw() {

        RequestInterface req = RetrofitClient.getClient(this).create(RequestInterface.class);
        Call<ResponseBody> call = req.get_pending_withdraw(PrefrenceManager.getString(LoginActivity.this, PrefrenceManager.USERID));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                CommonMethods.PrintLog(TAG, "url  " + response.raw().request().url());
                String withdrawamount = "";
                String Accountno="";
                String IFCI="";
                String UPI="";

                try {
                    String res = response.body().string();
                    CommonMethods.PrintLog(TAG, "url res " + res);
                    JSONObject jsonObject = new JSONObject(res);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("withdraw_data");
                    Accountno = jsonObject1.getString("bank_account_no");
                    IFCI = jsonObject1.getString("ifsc");
                    UPI = jsonObject1.getString("upi_id");
                    Log.e("bank_account_no--204","--"+Accountno);
                    Log.e("ifsc--204","--"+IFCI);
                    Log.e("upi_id--204","--"+UPI);
                    PrefrenceManager.setString(LoginActivity.this, PrefrenceManager.Accountno, Accountno);
                    PrefrenceManager.setString(LoginActivity.this, PrefrenceManager.IFCI, IFCI);
                    PrefrenceManager.setString(LoginActivity.this, PrefrenceManager.UPI, UPI);

                   // withdrawamount = jsonObject1.getString("req_amount");

                } catch (Exception e) {
                   // tv_show_wirthdawmoney.setVisibility(View.GONE);
                    CommonMethods.PrintLog(TAG, "url Exception " + e.toString());
                }

              //  tv_show_wirthdawmoney.setText("Withdrawal : " + withdrawamount+"$ Pending");


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                CommonMethods.simpleSnackbar(LoginActivity.this, AppUtils.SERVER_ERROR);
//                CommonMethods.PrintLog(TAG, t.toString());

            }
        });
    }

}
