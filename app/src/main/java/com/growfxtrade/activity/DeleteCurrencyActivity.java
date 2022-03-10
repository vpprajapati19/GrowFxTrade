package com.growfxtrade.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.growfxtrade.R;
import com.growfxtrade.prefrence.PrefrenceManager;
import com.growfxtrade.utils.AppUtils;
import com.growfxtrade.utils.CommonMethods;
import com.growfxtrade.utils.RequestInterface;
import com.growfxtrade.utils.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteCurrencyActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "AddCurrencyActivity";
    private ImageView ivicon;
    private Dialog dialog;
    private DeleteAdapter currencyAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<String> currencylist = new ArrayList<>();
    private ArrayList<String> currencylistnew = new ArrayList<>();
    EditText edt_search;
    ImageView img_search,img_search_back;
    TextView tvtitle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deletecurrency);
        initComponent();

        currencylist=new ArrayList<>();
        currencylistnew.clear();
        if (!CommonMethods.getInternetStatus(this)) {
            CommonMethods.showInternetDialog(this);
        } else {

            String clist = "";
            clist = PrefrenceManager.getString(DeleteCurrencyActivity.this, PrefrenceManager.allcurrency);

            if (clist.length() > 3) {
                clist = clist.substring(1);
                getCurrencyList(clist);

            }

        }


        currencyAdapter = new DeleteAdapter(DeleteCurrencyActivity.this, currencylist);
        recyclerView.setAdapter(currencyAdapter);

    }


    public void getCurrencyList(String clist) {

        dialog = CommonMethods.showDialogProgressBarNew(DeleteCurrencyActivity.this);
        RequestInterface req = RetrofitClient.getClient1(DeleteCurrencyActivity.this).create(RequestInterface.class);
        Call<ResponseBody> call = req.getAddedCurrency(clist, AppUtils.API_KEY);

        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dialog.dismiss();
                try {
                    String jsonst = response.body().string();
                    CommonMethods.PrintLog(TAG, "url jsonst " + jsonst);
                    JSONArray jsonArray = new JSONArray(jsonst);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);



                        currencylist.add(jsonObject.getString("s"));
                    }

                } catch (Exception e) {
                    CommonMethods.PrintLog("Error catch ", e.toString());
                    e.printStackTrace();
                }

                currencyAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                CommonMethods.simpleSnackbar(DeleteCurrencyActivity.this, AppUtils.SERVER_ERROR);

                CommonMethods.PrintLog(TAG, "onFailure " + t.toString());

            }

        });
    }

    private void initComponent() {
        ivicon = findViewById(R.id.ivicon);
        recyclerView = findViewById(R.id.recyclerView);
        img_search = findViewById(R.id.img_search);
        edt_search = findViewById(R.id.edt_search);
        img_search_back = findViewById(R.id.img_search_back);
        tvtitle = findViewById(R.id.tvtitle);
        ivicon.setOnClickListener(this);

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvtitle.setVisibility(View.GONE);
                ivicon.setVisibility(View.GONE);
                edt_search.setVisibility(View.VISIBLE);
                img_search_back.setVisibility(View.VISIBLE);
            }
        });

        img_search_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_search.setText("");
                edt_search.setVisibility(View.GONE);
                img_search_back.setVisibility(View.GONE);
                tvtitle.setVisibility(View.VISIBLE);
                ivicon.setVisibility(View.VISIBLE);
                currencyAdapter.notifyDataSetChanged();
            }
        });



      //  recyclerView.setLayoutManager(new LinearLayoutManager(DeleteCurrencyActivity.this, LinearLayoutManager.VERTICAL, false));

        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);


    }

    public class DeleteAdapter extends RecyclerView.Adapter<DeleteAdapter.ItemViewHolder>  {
        Context mContext;
        private ArrayList<String> currencylist;

        public class ItemViewHolder extends RecyclerView.ViewHolder {

            private TextView tv_currencyname;
            private ImageView iv_delete;
            LinearLayout allview;


            public ItemViewHolder(View view) {
                super(view);
                tv_currencyname = view.findViewById(R.id.tv_currencyname);
                iv_delete = view.findViewById(R.id.iv_delete);
                allview = view.findViewById(R.id.allview);
            }
        }
        public DeleteAdapter(Activity activity, ArrayList<String> currencylist) {
            this.mContext = activity;
            this.currencylist = currencylist;

        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.delete_row_currency, viewGroup, false));
        }
        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder mholder, int position) {

            final String cname = currencylist.get(position);
            mholder.tv_currencyname.setText(cname);
            mholder.iv_delete.setVisibility(View.VISIBLE);
        /*String all = PrefrenceManager.getString((Activity) mContext, PrefrenceManager.allcurrency);
        if (all.contains(cname)) {

            mholder.tv_currencyname.setText(cname);
            mholder.iv_delete.setVisibility(View.VISIBLE);

        } else {
            //  mholder.allview.setVisibility(View.GONE);


        }*/


            mholder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PrefrenceManager.removeCurrencyString(mContext, "," + cname);
                    Toast.makeText(mContext, cname + " Currency removed", Toast.LENGTH_SHORT).show();
                    currencylist.clear();
                    if (!CommonMethods.getInternetStatus(mContext)) {
                        CommonMethods.showInternetDialog(mContext);
                    } else {
                        String clist = "";
                        clist = PrefrenceManager.getString(DeleteCurrencyActivity.this, PrefrenceManager.allcurrency);

                        if (clist.length() > 3) {
                            clist = clist.substring(1);
                            getCurrencyList(clist);

                        }


                    }

                }
            });


        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        public long getItemId(int i) {
            return (long) i;
        }

        @Override
        public int getItemCount() {
            return currencylist.size();
        }
    }

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


}
