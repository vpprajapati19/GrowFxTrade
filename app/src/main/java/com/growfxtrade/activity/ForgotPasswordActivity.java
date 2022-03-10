package com.growfxtrade.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_usernmae;
    private LinearLayout btnSend;
    private Dialog dialog;
    JSONObject jsonObject;
    //private ImageView iv_back;
    private String TAG = "ForgotPasswordActivity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        initComponent();
    }

    private void initComponent() {
      //  iv_back = findViewById(R.id.iv_back);

        btnSend = findViewById(R.id.btnSend);
        et_usernmae = findViewById(R.id.et_usernmae);
        btnSend.setOnClickListener(this);
      //  iv_back.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
       /* if (view == iv_back) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.enter_from_left,
                    R.anim.exit_to_right);
        }*/
        if (view == btnSend) {
            String username = "";
            String password = "";

            username = et_usernmae.getText().toString().trim();

            if (username.length() <= 0) {
                et_usernmae.setError("Valid Email");
                return;
            }
            if (!CommonMethods.getInternetStatus(ForgotPasswordActivity.this)) {
                CommonMethods.showInternetDialog(ForgotPasswordActivity.this);
            } else {
                getLogin(username, password);
            }

        }
    }

    public void getLogin(String uname, String pwd) {
        dialog = CommonMethods.showDialogProgressBarNew(this);
        RequestInterface req = RetrofitClient.getClient(this).create(RequestInterface.class);
        Call<ResponseBody> call = req.getforgot(uname);

        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dialog.dismiss();
                String staus = "";
                String msg = "";
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
                    Toast.makeText(ForgotPasswordActivity.this, msg, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter_from_left,
                            R.anim.exit_to_right);
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, msg, Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                CommonMethods.simpleSnackbar(ForgotPasswordActivity.this, AppUtils.SERVER_ERROR);
                CommonMethods.PrintLog(TAG, "onFailure " + t.toString());

            }


        });
    }

}
