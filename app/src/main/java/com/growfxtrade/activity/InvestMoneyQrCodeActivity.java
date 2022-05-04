package com.growfxtrade.activity;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.CAMERA;
import static com.growfxtrade.activity.RegisterActivity.RequestPermissionCode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvestMoneyQrCodeActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivicon;
    TextView tv_price;
    private String TAG = "InvestMoneyQrCodeActivity";
    LinearLayout btn_sent_money_QrCode;
    private Dialog dialog;
    JSONObject jsonObject;
    String price;
    static String path;
    static File pathh;
    private static final int REQUEST_EXTERNAL_STORAGe = 1;
    private static String[] permissionstorage = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    String randomString(int len){
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invest_money_qr_code);
        initComponent();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            price = bundle.getString("price");
            Log.e("price25","=="+price);
            tv_price.setText(price);
        }
        if (CheckingPermissionIsEnabledOrNot()) {
        } else {
            //Calling method to enable permission.
            Log.e("first_click", "first_click");
            verifystoragepermissions(this);
            //  RequestMultiplePermission();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                screenshot(getWindow().getDecorView().getRootView(),"result");
            }
        }, 2000);
    }

    public boolean CheckingPermissionIsEnabledOrNot() {

        int CAMERA_PermissionResult = ContextCompat.checkSelfPermission(InvestMoneyQrCodeActivity.this, CAMERA);
        int READ_EXTERNAL_STORAGE_PermissionResult = ContextCompat.checkSelfPermission(InvestMoneyQrCodeActivity.this, READ_EXTERNAL_STORAGE);
        int WRITE_EXTERNAL_STORAGE_PermissionResult = ContextCompat.checkSelfPermission(InvestMoneyQrCodeActivity.this, WRITE_EXTERNAL_STORAGE);

        return CAMERA_PermissionResult == PackageManager.PERMISSION_GRANTED &&
                READ_EXTERNAL_STORAGE_PermissionResult == PackageManager.PERMISSION_GRANTED &&
                WRITE_EXTERNAL_STORAGE_PermissionResult == PackageManager.PERMISSION_GRANTED;

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(InvestMoneyQrCodeActivity.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(InvestMoneyQrCodeActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }
    protected File screenshot(View view, String filename) {
        Date date = new Date();

        // Here we are initialising the format of our image name
        CharSequence format = android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", date);
        try {
            // Initialising the directory of storage
          /*  String dirpath;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
                dirpath= InvestMoneyNetbankingActivity.this.getExternalFilesDir(Environment.DIRECTORY_DCIM) + "/" + format + ".jpeg";
            }
            else
            {
                dirpath= Environment.getExternalStorageDirectory().toString() + "/" + format + ".jpeg";
            }*/

            String dirpath = Environment.getExternalStorageDirectory() + "";
            File file = new File(dirpath);
            if (!file.exists()) {
                boolean mkdir = file.mkdir();
            }
            // File name
            String paths = Environment.getExternalStorageDirectory().toString() + "/" + randomString(5) + ".jpg";
            // String paths = dirpath + "/" + filename + "-" + format + ".jpeg";
            view.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
            view.setDrawingCacheEnabled(false);
            File imageurl = new File(paths);
            Log.e("image88",""+imageurl);
            pathh = new File(paths);
            Log.e("image146","---"+pathh);
            path = String.valueOf(pathh);
            FileOutputStream outputStream = new FileOutputStream(imageurl);
            int quality=73;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            return imageurl;

        } catch (FileNotFoundException io) {
            io.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // verifying if storage permission is given or not
    public static void verifystoragepermissions(Activity activity) {

        int permissions = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        // If storage permission is not given then request for External Storage Permission
        if (permissions != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, permissionstorage, REQUEST_EXTERNAL_STORAGe);
        }
    }
    private void initComponent() {
        ivicon = findViewById(R.id.ivicon);
        tv_price = findViewById(R.id.tv_price);
        btn_sent_money_QrCode = findViewById(R.id.btn_sent_money_QrCode);
        ivicon.setOnClickListener(this);
        btn_sent_money_QrCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == ivicon) {
            finish();
            onBackPressed();
        }else if (v== btn_sent_money_QrCode){
            addMoney(price);
        }
    }
    public void addMoney(String money) {

        Log.e("debug_800",""+path);
        File file = new File(path);
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("image", file.getName(), fileReqBody);

        RequestBody useridpart = RequestBody.create(MediaType.parse("text/plain"), PrefrenceManager.getString(InvestMoneyQrCodeActivity.this, PrefrenceManager.USERID));
        RequestBody typee = RequestBody.create(MediaType.parse("text/plain"), "Crypto");
        RequestBody moneyy = RequestBody.create(MediaType.parse("text/plain"), money);
        Log.e(TAG, "money  " + money);
        Log.e(TAG, "money1  " + PrefrenceManager.getString(InvestMoneyQrCodeActivity.this, PrefrenceManager.USERID));
        dialog = CommonMethods.showDialogProgressBarNew(this);
        RequestInterface req = RetrofitClient.getClient(this).create(RequestInterface.class);
        Call<ResponseBody> call = req.addMoney(part,useridpart,typee , moneyy);

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

                    final Dialog dialog1 = new Dialog(InvestMoneyQrCodeActivity.this, R.style.df_dialog);
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
                            startActivity(new Intent(InvestMoneyQrCodeActivity.this,MainActivity.class));
                            finish();
                        }
                    });

                    dialog1.show();
                    //getInfoDialog("Request has been Updated. You will get a mail within few time");
//                    Toast.makeText(InvestMoneyActivity.this, msg, Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(InvestMoneyQrCodeActivity.this, msg, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                CommonMethods.simpleSnackbar(InvestMoneyQrCodeActivity.this, AppUtils.SERVER_ERROR);
//                CommonMethods.PrintLog(TAG, t.toString());

            }
        });
    }
}