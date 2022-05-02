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
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import com.growfxtrade.utils.ImageResizer;
import com.growfxtrade.utils.RequestInterface;
import com.growfxtrade.utils.RetrofitClient;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    String price,path;
    File pathh;
    private static final int REQUEST_EXTERNAL_STORAGe = 1;
    private static String[] permissionstorage = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};


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
        if (CheckingPermissionIsEnabledOrNot()) {
        } else {
            //Calling method to enable permission.
            Log.e("first_click", "first_click");
            RequestMultiplePermission();
        }
        takeScreenshot();
       // screenshot(getWindow().getDecorView().getRootView(), "result");

    }
    public boolean CheckingPermissionIsEnabledOrNot() {

        int CAMERA_PermissionResult = ContextCompat.checkSelfPermission(InvestMoneyNetbankingActivity.this, CAMERA);
        int READ_EXTERNAL_STORAGE_PermissionResult = ContextCompat.checkSelfPermission(InvestMoneyNetbankingActivity.this, READ_EXTERNAL_STORAGE);
        int WRITE_EXTERNAL_STORAGE_PermissionResult = ContextCompat.checkSelfPermission(InvestMoneyNetbankingActivity.this, WRITE_EXTERNAL_STORAGE);

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
                        Toast.makeText(InvestMoneyNetbankingActivity.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(InvestMoneyNetbankingActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }
    private void RequestMultiplePermission() {
        Log.e("requested", "requested");
        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(InvestMoneyNetbankingActivity.this, new String[]
                {
                        CAMERA,
                        READ_EXTERNAL_STORAGE,
                        WRITE_EXTERNAL_STORAGE,

                }, RequestPermissionCode);

    }
    public static File saveBitmapToFile(File file) {
        try {

// BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 1;
// factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

// The new size we want to scale to
            final int REQUIRED_SIZE = 75;

            /*int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE
                    && o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }*/

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            //  o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream,
                    null, o2);
            inputStream.close();

            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.PNG, 100,
                    outputStream);

            return file;
        } catch (Exception e) {
            return null;
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        Log.e("PathURLLLLLLLLLLLL", "" + Uri.parse(path));
        return Uri.parse(path);
    }private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }
    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
           // Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);
            Log.e("image199","---"+v1);
            pathh = new File(mPath);
            Log.e("image146","---"+pathh);
            path = String.valueOf(pathh);
            FileOutputStream outputStream = new FileOutputStream(pathh);
            int quality = 100;
            //bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            openScreenshot(pathh);
        } catch (Throwable e) {
            Log.e("erro210","===="+e);
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }


    protected static File screenshot(View view, String filename) {
        Date date = new Date();

        // Here we are initialising the format of our image name
        CharSequence format = android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", date);
        try {
            // Initialising the directory of storage
            String dirpath = Environment.getExternalStorageDirectory() + "";
            File file = new File(dirpath);
            if (!file.exists()) {
                boolean mkdir = file.mkdir();
            }
            // File name
            String path = dirpath + "/" + filename + "-" + format + ".jpeg";
            view.setDrawingCacheEnabled(true);
           // Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
            view.setDrawingCacheEnabled(false);
            File imageurl = new File(path);
            Log.e("image88",""+imageurl);
            FileOutputStream outputStream = new FileOutputStream(imageurl);
          //  bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
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
        tv_bank_accountno = findViewById(R.id.tv_bank_accountno);
        tv_ifsccode= findViewById(R.id.tv_ifsccode);
        tv_bankname= findViewById(R.id.tv_bankname);
        btn_sent_money_netbank = findViewById(R.id.btn_sent_money_netbank);
        ivicon.setOnClickListener(this);
        btn_sent_money_netbank.setOnClickListener(this);
        Log.e("Account_No","==="+PrefrenceManager.getString(InvestMoneyNetbankingActivity.this, PrefrenceManager.Accountno));
        Log.e("IFSC","==="+PrefrenceManager.getString(InvestMoneyNetbankingActivity.this, PrefrenceManager.IFCI));
        Log.e("Account_name","==="+PrefrenceManager.getString(InvestMoneyNetbankingActivity.this, PrefrenceManager.Bankname));
        tv_bank_accountno.setText(PrefrenceManager.getString(InvestMoneyNetbankingActivity.this, PrefrenceManager.Accountno));
        tv_ifsccode.setText(PrefrenceManager.getString(InvestMoneyNetbankingActivity.this, PrefrenceManager.IFCI));
        tv_bankname.setText(PrefrenceManager.getString(InvestMoneyNetbankingActivity.this, PrefrenceManager.Bankname));

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

    private File getbitmapfile(Bitmap reduse) {
        File file =new File(Environment.getExternalStorageDirectory() +File.separator+"document_img");

        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        reduse.compress(Bitmap.CompressFormat.JPEG,0,bos);
        byte[] bitmapdata=bos.toByteArray();
        try {
            file.createNewFile();
            FileOutputStream fos=new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }
    public void addMoney(String money) {
       /*
        Bitmap fullsize=BitmapFactory.decodeFile(path);
        Bitmap reduse= ImageResizer.reduceBitmapSize(fullsize,240000);
        File fileredues=getbitmapfile(reduse);*/
        MultipartBody.Part document_image;
        document_image =MultipartBody.Part.createFormData("image",pathh.getName());

        Log.e(TAG, "money  " + money);
        Log.e(TAG, "money1  " + PrefrenceManager.getString(InvestMoneyNetbankingActivity.this, PrefrenceManager.USERID));
        dialog = CommonMethods.showDialogProgressBarNew(this);
        RequestInterface req = RetrofitClient.getClient(this).create(RequestInterface.class);
        Call<ResponseBody> call = req.addMoney(document_image,PrefrenceManager.getString(InvestMoneyNetbankingActivity.this, PrefrenceManager.USERID), "Net-Banking", money);

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