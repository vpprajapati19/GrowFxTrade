package com.growfxtrade.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import com.facebook.appevents.AppEventsConstants;
import com.growfxtrade.R;
import com.growfxtrade.prefrence.PrefrenceManager;
import com.growfxtrade.utils.AppUtils;
import com.growfxtrade.utils.CommonMethods;
import com.growfxtrade.utils.RequestInterface;
import com.growfxtrade.utils.RetrofitClient;
import com.kinda.alert.KAlertDialog;
import okhttp3.ResponseBody;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatWithUsActivity extends AppCompatActivity implements View.OnClickListener {
    /* access modifiers changed from: private */
    public String TAG = "AddCurrencyActivity";
    private CardView btn_submit;
    /* access modifiers changed from: private */
    public Dialog dialog;
    private EditText et_contactno;
    private EditText et_email;
    private EditText et_message;
    private EditText et_name;
    private EditText et_reason;
    private ImageView ivicon;
    JSONObject jsonObject;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_chatwithus);
        initComponent();
    }

    private void initComponent() {
        this.ivicon = (ImageView) findViewById(R.id.ivicon);
        this.btn_submit = (CardView) findViewById(R.id.btn_submit);
        this.et_name = (EditText) findViewById(R.id.et_name);
        this.et_email = (EditText) findViewById(R.id.et_email);
        this.et_contactno = (EditText) findViewById(R.id.et_contactno);
        this.et_reason = (EditText) findViewById(R.id.et_reason);
        this.et_message = (EditText) findViewById(R.id.et_message);
        this.et_name.setText(PrefrenceManager.getString(this, PrefrenceManager.USERNAME));
        this.et_email.setText(PrefrenceManager.getString(this, PrefrenceManager.EMAIL));
        this.et_contactno.setText(PrefrenceManager.getString(this, PrefrenceManager.MOBILE));
        this.ivicon.setOnClickListener(this);
        this.btn_submit.setOnClickListener(this);
    }

    public void onClick(View view) {
        if (view == this.ivicon) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        }
        if (view == this.btn_submit) {
            String obj = this.et_name.getText().toString();
            String obj2 = this.et_email.getText().toString();
         //   String obj3 = this.et_contactno.getText().toString();
           // String obj4 = this.et_reason.getText().toString();
            String obj5 = this.et_message.getText().toString();
            if (obj.length() <= 0) {
                this.et_name.setError("Enter Valid Name");
            } else if (obj2.length() <= 0) {
                this.et_email.setError("Enter Valid Email");
            } /*else if (obj3.length() <= 0) {
                this.et_contactno.setError("Enter Valid Contact Number");
            } else if (obj4.length() <= 0) {
                this.et_reason.setError("Enter Valid Reason");
            }*/ else if (obj5.length() <= 0) {
                this.et_message.setError("Enter Valid Message");
            } else if (!CommonMethods.getInternetStatus(this)) {
                CommonMethods.showInternetDialog(this);
            } else {
                addContactInfo(obj, obj2,obj5);
            }
        }
    }

    private void getInfoDialog(String str) {
        new KAlertDialog(this).setTitleText("").setContentText(str).setConfirmClickListener(new KAlertDialog.OnSweetClickListener() {
            public void onClick(KAlertDialog kAlertDialog) {
                kAlertDialog.dismissWithAnimation();
                ChatWithUsActivity.this.startActivity(new Intent(ChatWithUsActivity.this, MainActivity.class));
                ChatWithUsActivity.this.finish();
            }
        }).show();
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

    public void addContactInfo(String str, String str2, String str5) {
        String str6 = this.TAG;
        Log.e(str6, "money1  " + PrefrenceManager.getString(this, PrefrenceManager.USERID));
        this.dialog = CommonMethods.showDialogProgressBarNew(this);
        ((RequestInterface) RetrofitClient.getClient(this).create(RequestInterface.class)).addContact(str, str2, str5).enqueue(new Callback<ResponseBody>() {
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String str;
                ChatWithUsActivity.this.dialog.dismiss();
                String access$100 = ChatWithUsActivity.this.TAG;
                CommonMethods.PrintLog(access$100, "url  " + response.raw().request().url());
                String str2 = "";
                try {
                    String string = response.body().string();
                    String access$1002 = ChatWithUsActivity.this.TAG;
                    CommonMethods.PrintLog(access$1002, "url res " + string);
                    ChatWithUsActivity.this.jsonObject = new JSONObject(string);
                    str2 = ChatWithUsActivity.this.jsonObject.getString("staus");
                    str = ChatWithUsActivity.this.jsonObject.getString(NotificationCompat.CATEGORY_MESSAGE);
                } catch (Exception e) {
                    String access$1003 = ChatWithUsActivity.this.TAG;
                    CommonMethods.PrintLog(access$1003, "url Exception " + e.toString());
                    str = "Error..!!!";
                }
                if (str2.equalsIgnoreCase(AppEventsConstants.EVENT_PARAM_VALUE_NO)) {
                    final Dialog dialog = new Dialog(ChatWithUsActivity.this);
                    dialog.requestWindowFeature(1);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.dialog);
                    ((CardView) dialog.findViewById(R.id.btn_submit)).setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            dialog.dismiss();
                            ChatWithUsActivity.this.startActivity(new Intent(ChatWithUsActivity.this, MainActivity.class));
                            ChatWithUsActivity.this.finish();
                        }
                    });
                    dialog.show();
                    return;
                }
                Toast.makeText(ChatWithUsActivity.this, str, 0).show();
            }

            public void onFailure(Call<ResponseBody> call, Throwable th) {
                ChatWithUsActivity.this.dialog.dismiss();
                CommonMethods.simpleSnackbar(ChatWithUsActivity.this, AppUtils.SERVER_ERROR);
            }
        });
    }
}
