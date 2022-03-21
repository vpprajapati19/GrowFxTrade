package com.growfxtrade.activity;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.CAMERA;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.growfxtrade.adapter.CustomAdapter11;
import com.growfxtrade.R;
import com.growfxtrade.utils.CommonMethods;
import com.growfxtrade.utils.RequestInterface;
import com.growfxtrade.utils.RetrofitClient;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private String TAG = "RegisterActivity";
    private ImageView iv_back;
    LinearLayout iv_reg;
    private EditText et_usernmae, et_useremail,et_state, et_password, et_usermobile, et_usercity,et_accno,et_ifsccode,et_doc_no,et_pancard;
    private Dialog dialog;
    private CheckBox cb_privacy;
    private Spinner spinner_country;
    TextView tv_adharcard_front,tv_adharcard_back;
    LinearLayout moveLogin;
    String docposition="";
    public static final int RequestPermissionCode = 7;
    String flag="0";
    Bitmap bitmap = null;
    String path, filename;
    String screentype;

    String[] proff = { "Select Document","Aadhar Card",  "Voter Id", "Other"};
    Spinner spiner;
    private final Integer[] countryflag = {
            R.drawable.afghanistan, R.drawable.australia, R.drawable.bangladesh, R.drawable.england,
            R.drawable.india,
            R.drawable.newzealand, R.drawable.pakistan, R.drawable.southafrica, R.drawable.srilanka, R.drawable.westindies

    };
    private final String[] country = {"Afghanistan",
            "Australia", "Bangladesh", "England", "India",
            "New Zealand",
            "Pakistan", "South Africa", "Sri Lanka", "West Indies"};


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initComponent();

        tv_adharcard_front.setOnClickListener(this);
        tv_adharcard_back.setOnClickListener(this);
    }

    private void initComponent() {
        cb_privacy = findViewById(R.id.cb_privacy);
               iv_reg = findViewById(R.id.iv_reg);
        et_usernmae = findViewById(R.id.et_usernmae);
        et_useremail = findViewById(R.id.et_useremail);
        et_state = findViewById(R.id.et_state);
        et_password = findViewById(R.id.et_password);
        et_usermobile = findViewById(R.id.et_usermobile);
        et_usercity = findViewById(R.id.et_usercity);
        et_accno = findViewById(R.id.et_accno);
        et_ifsccode = findViewById(R.id.et_ifsccode);
        spiner = findViewById(R.id.spiner);
        et_doc_no = findViewById(R.id.et_doc_no);
        et_pancard = findViewById(R.id.et_pancard);
        moveLogin = findViewById(R.id.iv_login_move);
        tv_adharcard_front = findViewById(R.id.tv_adharcard_front);
        tv_adharcard_back = findViewById(R.id.tv_adharcard_back);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, proff);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spiner.setAdapter(dataAdapter);

        spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(i==0){

                }else {

                    String item = adapterView.getItemAtPosition(i).toString();
                    docposition= String.valueOf(i);
                }

                // Showing selected spinner item
              //  Toast.makeText(adapterView.getContext(), docposition, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_country = findViewById(R.id.spinner_country);

        spinner_country.setAdapter(new CustomAdapter11(this, country, countryflag));
        spinner_country.setSelection(4);


        moveLogin.setOnClickListener(this);
        iv_reg.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        if (view == moveLogin) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        }
        if(view == tv_adharcard_front){
            if (CheckingPermissionIsEnabledOrNot()) {
            } else {
                //Calling method to enable permission.
                Log.e("first_click", "first_click");
                RequestMultiplePermission();
            }

            screentype="first";
            selectImage();
        }if(view ==tv_adharcard_back){
            if (CheckingPermissionIsEnabledOrNot()) {
            } else {
                //Calling method to enable permission.
                Log.e("first_click", "first_click");
                RequestMultiplePermission();
            }

            screentype="second";
            selectImage();
        }
        if (view == iv_reg) {
            final String pan_pattern = "(([A-Za-z]{5})([0-9]{4})([a-zA-Z]))";
            Pattern r = Pattern.compile(pan_pattern);
            if (et_usernmae.getText().length() <= 0) {
                et_usernmae.setError("Enter Valid User Name");
                return;
            }
            else if (et_useremail.getText().length() <= 0) {
                et_useremail.setError("Enter Valid Email");
                return;
            }else if ((isValidEmailAddress(et_useremail.getText().toString()) == false)) {
                et_useremail.setError("Please enter a valid Email address");
                et_useremail.requestFocus();
            }else if (et_usermobile.getText().length() <= 0) {
                et_usermobile.setError("Enter Valid Number");
                return;
            } else if (et_usermobile.length() <10 || et_usermobile.length() > 13) {

                et_usermobile.setError("Phone number length should be 10 digits");
                et_usermobile.requestFocus();
            }
            else if (et_usercity.getText().length() <= 0) {
                et_usercity.setError("Enter Valid City");
                return;
            }else if (et_state.getText().length() <= 0) {
                et_usercity.setError("Enter Valid state");
                return;
            }
            if (et_pancard.getText().length() <= 0) {
                Toast.makeText(this, "Enter Valid PanCard Number", Toast.LENGTH_SHORT).show();
                return;
            }else  if (!regex_matcher(r, et_pancard.getText().toString())) {
                //error = "Invalid PAN number";
                Toast.makeText(this, "Invalid PAN number", Toast.LENGTH_SHORT).show();
            }
            if (et_accno.getText().length() <= 0) {
                et_accno.setError("Enter Valid Account Number");
                return;
            }
            if (et_ifsccode.getText().length() <= 0) {
                et_ifsccode.setError("Enter Valid IFSC CODE");
                return;
            }else if (et_password.getText().length() <= 0) {
                et_password.setError("Enter Valid Password");
                return;
            }
            if (et_doc_no.getText().length() <= 0) {
                Toast.makeText(this, "Enter Valid Documnet Number", Toast.LENGTH_SHORT).show();
                return;
            }
            if (docposition.equalsIgnoreCase("")) {
                Toast.makeText(this, "Please Select Document", Toast.LENGTH_SHORT).show();
                return;
            }

           /* if (txtimgetwo.getText().length() <= 0) {
                Toast.makeText(this, "Please Upload Image Two", Toast.LENGTH_SHORT).show();
                return;
            }*/
            String country = spinner_country.getSelectedItem().toString();
            Log.e(TAG, "country   " + country);
            if (!cb_privacy.isChecked()) {
                Toast.makeText(this, "Please accept privacy and policy..!!", Toast.LENGTH_SHORT).show();
                return;
            }

            getRegister(et_usernmae.getText().toString().trim(),et_useremail.getText().toString().trim(),et_password.getText().toString().trim(),et_usermobile.getText().toString().trim()
                    ,et_usercity.getText().toString().trim(),country,et_accno.getText().toString().trim(), et_ifsccode.getText().toString().trim()
            ,et_doc_no.getText().toString().trim(),et_pancard.getText().toString().trim());


           /* Intent intent = new Intent(this, SignupOtpActivity.class);
            intent.putExtra("username", et_usernmae.getText().toString().trim());
            intent.putExtra("email", et_useremail.getText().toString().trim());
            intent.putExtra("password", et_password.getText().toString().trim());
            intent.putExtra("mobile", et_usermobile.getText().toString().trim());
            intent.putExtra("city", et_usercity.getText().toString().trim());
            intent.putExtra("accno", et_accno.getText().toString().trim());
            intent.putExtra("ifsccode", et_ifsccode.getText().toString().trim());
            intent.putExtra("country", country);
            intent.putExtra("docposition", docposition);
            intent.putExtra("docno", et_doc_no.getText().toString().trim());
            intent.putExtra("docpancard", et_pancard.getText().toString().trim());
            startActivity(intent);
            overridePendingTransition(R.anim.enter_from_left,
                    R.anim.exit_to_right);
*/

        }
    }
    private boolean regex_matcher(Pattern pattern, String string) {
        Matcher m = pattern.matcher(string);
        return m.find() && (m.group(0) != null);
    }
    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
    private void selectImage() {
        final CharSequence[] items = {RegisterActivity.this.getResources().getString(R.string.take_photo)
                  ,RegisterActivity.this.getResources().getString(R.string.choose_gallery),
                RegisterActivity.this.getResources().getString(R.string.Cancel)};
        final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle( RegisterActivity.this.getResources().getString(R.string.add_photo));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                //  boolean result = Utility.checkPermission(getActivity());
                if (items[item].equals(RegisterActivity.this.getResources().getString(R.string.take_photo))) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);
                } else if (items[item].equals(RegisterActivity.this.getResources().getString(R.string.choose_gallery))) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else if (items[item].equals( RegisterActivity.this.getResources().getString(R.string.Cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void setImage(Bitmap bitmap, Uri tempUri,String pathh) {
        Log.e("debug_336",""+tempUri);
       // Damagedapi(OrderReference,pathh);
    }
    @SuppressLint("LongLogTag")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

                Log.e("DATATATATAT===========", "==" + data.getExtras().get("data"));
                bitmap = (Bitmap) data.getExtras().get("data");

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = getImageUri(RegisterActivity.this, bitmap);
                // CALL THIS METHOD TO GET THE ACTUAL PATH
                File finalFile = new File(getRealPathFromURI(tempUri));
                String  path_pass = String.valueOf(finalFile);

                Log.e("length_567",""+saveBitmapToFile(new File(path_pass)).length());
                long length = saveBitmapToFile(new File(path_pass)).length();
                //   length = length / (1024 * 1024);
                Log.e("length_570",""+length);

                path = String.valueOf(finalFile);
                Log.e("length_580",""+path);
                filename = path.substring(path.lastIndexOf("/") + 1);
                if(screentype.equalsIgnoreCase("second")){
                    tv_adharcard_back.setText(filename);
                }else{
                    tv_adharcard_front.setText(filename);
                }
                //iv_upload_image.setImageBitmap(bitmap);
                setImage(bitmap,tempUri,path);


            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = RegisterActivity.this.getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String imagePath = c.getString(columnIndex);
                c.close();
                bitmap = (BitmapFactory.decodeFile(imagePath));
                Log.e("path of image from gallery..*************...", imagePath);
                File imagefile = new File(imagePath);

                path = String.valueOf(imagefile);
                Log.e("pathhhhhhh_profilepic", "" + path);
                filename = path.substring(path.lastIndexOf("/") + 1);
                if(screentype.equalsIgnoreCase("second")){
                   tv_adharcard_back.setText(filename);
                }else{
                    tv_adharcard_front.setText(filename);
                }
                Log.e("pat_gallery_filenm", "" + filename);


                BitmapDrawable d = new BitmapDrawable(getResources(), imagefile.getAbsolutePath());
                Log.e("selectedImage_671", "" + selectedImage);
                setImage(bitmap, selectedImage,path);

            }
        }
    }
    public File saveBitmapToFile(File file) {
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
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = RegisterActivity.this.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public boolean CheckingPermissionIsEnabledOrNot() {

        int CAMERA_PermissionResult = ContextCompat.checkSelfPermission(RegisterActivity.this, CAMERA);
        int READ_EXTERNAL_STORAGE_PermissionResult = ContextCompat.checkSelfPermission(RegisterActivity.this, READ_EXTERNAL_STORAGE);
        int WRITE_EXTERNAL_STORAGE_PermissionResult = ContextCompat.checkSelfPermission(RegisterActivity.this, WRITE_EXTERNAL_STORAGE);

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
                        Toast.makeText(RegisterActivity.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }
    private void RequestMultiplePermission() {
        Log.e("requested", "requested");
        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(RegisterActivity.this, new String[]
                {
                        CAMERA,
                        READ_EXTERNAL_STORAGE,
                        WRITE_EXTERNAL_STORAGE,

                }, RequestPermissionCode);

    }
    public void getRegister(String uname, String email, String pwd, String mobile, String ci, String co,String accno,String ifsccode,String docno,String docpancard) {
        dialog = CommonMethods.showDialogProgressBarNew(this);
        //  RequestInterface req = RetrofitClient.getClient(this).create(RequestInterface.class);
        RequestInterface req = RetrofitClient.getClientone().create(RequestInterface.class);
        Call<ResponseBody> call = req.getREgDetails(mobile, email, "male", pwd, uname, ci, co,accno,ifsccode,docposition,docno,docpancard);

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

                    JSONObject jsonObject = new JSONObject(jsonst);
                    staus = jsonObject.getString("staus");
                    msg = jsonObject.getString("msg");

                } catch (Exception e) {
                    msg = "Error..!!!";
                    CommonMethods.PrintLog("Error catch ", e.toString());
                    e.printStackTrace();
                }

                if (staus.equalsIgnoreCase("1")) {
                    Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();


            }

        });

    }


}
