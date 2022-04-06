package com.growfxtrade.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.growfxtrade.adapter.ViewServiceAdapter;
import com.growfxtrade.model.ViewServiceModel;
import com.growfxtrade.prefrence.PrefrenceManager;
import com.growfxtrade.utils.AppUtils;
import com.growfxtrade.utils.CommonMethods;
import com.growfxtrade.utils.RequestInterface;
import com.growfxtrade.utils.RetrofitClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.growfxtrade.R;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionActivity extends AppCompatActivity {
    private String TAG = "AddCurrencyActivity";
    private ImageView ivicon;
    private Dialog dialog;
    NestedScrollView ne_abc;
    private TextView tv_invest, tv_withdrawal, tv_profit, tv_loss;
    RecyclerView rec_home;
    ViewServiceAdapter viewServiceAdapter;
    ProgressDialog progressDialog;
    private ArrayList<ViewServiceModel> viewServiceModelArrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        rec_home=findViewById(R.id.rec_home);
        ne_abc=findViewById(R.id.ne_abc);
        rec_home.setLayoutManager(new LinearLayoutManager(TransactionActivity.this));
        initComponent();

        if (!CommonMethods.getInternetStatus(this)) {
            CommonMethods.showInternetDialog(this);
        } else {
            getPortfolio();
        }

      //  ItemOffsetDecoration itemDecoration1 = new ItemOffsetDecoration(SelectServiceActivity.this, R.dimen.item_padding);
        //rec_home.addItemDecoration(itemDecoration1);
    }

    private void initComponent() {
        ivicon = findViewById(R.id.ivicon_tran);

        tv_invest = findViewById(R.id.tv_invest);
        tv_withdrawal = findViewById(R.id.tv_withdrawal);
        tv_profit = findViewById(R.id.tv_profit);
        tv_loss = findViewById(R.id.tv_loss);

        ivicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              onBackPressed();
            }
        });

    }

    public void getPortfolio() {
      //  Toast.makeText(TransactionActivity.this,PrefrenceManager.getString(TransactionActivity.this, PrefrenceManager.USERID),Toast.LENGTH_LONG).show();
        dialog = CommonMethods.showDialogProgressBarNew(this);
        RequestInterface req = RetrofitClient.getClient(this).create(RequestInterface.class);
        Call<ResponseBody> call = req.get_traction(PrefrenceManager.getString(TransactionActivity.this, PrefrenceManager.USERID));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dialog.dismiss();
                CommonMethods.PrintLog(TAG, "url  " + response.raw().request().url());
                String invest = "";
                String withdrawal = "";
                String profit = "";
                String loss = "";
                String withdraw = "";
                try {
                    String res = response.body().string();
                    CommonMethods.PrintLog(TAG, "url res " + res);
                    JSONObject jsonObject = new JSONObject(res);
                    invest = jsonObject.getString("invest");
                    withdrawal = jsonObject.getString("withdrawal");
                    profit = jsonObject.getString("profit");
                    loss = jsonObject.getString("loss");
                    Log.e("invest170","=="+invest);
                    Log.e("withdrawal170","=="+withdrawal);
                    Log.e("profit170","=="+profit);
                    Log.e("loss170","=="+loss);


                    tv_invest.setText(invest);
                    tv_withdrawal.setText(withdrawal);
                    tv_profit.setText(profit);
                    tv_loss.setText(loss);
                  /*  JSONObject jsonObject1= jsonObject.getJSONObject("data");

                    for(int i=0;i<jsonObject1.length();i++){


                        String date= jsonObject1.getString("date");
                        String type_id= jsonObject1.getString("type_id");
                        String amount= jsonObject1.getString("amount");
                    }*/

                    viewServiceModelArrayList = new ArrayList<>();
                    Type getpetDTO = new TypeToken<List<ViewServiceModel>>() {
                    }.getType();

                    viewServiceModelArrayList = (ArrayList<ViewServiceModel>) new Gson().fromJson(jsonObject.getJSONArray("data").toString(), getpetDTO);
                    viewServiceAdapter=new ViewServiceAdapter(TransactionActivity.this,viewServiceModelArrayList);
                    rec_home.setAdapter(viewServiceAdapter);
                } catch (Exception e) {
                    CommonMethods.PrintLog(TAG, "url Exception " + e.toString());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                CommonMethods.simpleSnackbar(TransactionActivity.this, AppUtils.SERVER_ERROR);
//                CommonMethods.PrintLog(TAG, t.toString());
            }
        });
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
