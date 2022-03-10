package com.growfxtrade.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;      
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.growfxtrade.R;
import com.growfxtrade.prefrence.PrefrenceManager;
import com.growfxtrade.utils.AppUtils;
import com.growfxtrade.utils.CommonMethods;
import com.growfxtrade.utils.RequestInterface;
import com.growfxtrade.utils.RetrofitClient;


import org.json.JSONArray;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCurrencyActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "AddCurrencyActivity";
    private ImageView ivicon;
    private Dialog dialog;
    private CurrencyAdapter currencyAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<String> currencylist = new ArrayList<>();
    EditText edt_search;
    ImageView img_search,img_search_back;
    TextView tvtitle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcurrency);
        initComponent();

        currencylist.clear();
        if (!CommonMethods.getInternetStatus(this)) {
            CommonMethods.showInternetDialog(this);
        } else {
            getCurrencyList();

        }

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

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // filter recycler view when query submitted
                currencyAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        currencyAdapter = new CurrencyAdapter(this, currencylist);
        recyclerView.setAdapter(currencyAdapter);

    }

    public void getCurrencyList() {
        dialog = CommonMethods.showDialogProgressBarNew(this);
        RequestInterface req = RetrofitClient.getClient1(this).create(RequestInterface.class);
        Call<ResponseBody> call = req.getCurrency(AppUtils.API_KEY);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dialog.dismiss();
                CommonMethods.PrintLog(TAG, "url  " + response.raw().request().url());
                try {
                    String res = response.body().string();
                    CommonMethods.PrintLog(TAG, "url res " + res);
                    JSONArray jsonArray = new JSONArray(res);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        currencylist.add(jsonArray.getString(i));
                    }
                } catch (Exception e) {
                    CommonMethods.PrintLog(TAG, "url Exception " + e.toString());

                }
                currencyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                CommonMethods.simpleSnackbar(AddCurrencyActivity.this, AppUtils.SERVER_ERROR);
//                CommonMethods.PrintLog(TAG, t.toString());

            }
        });
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

    public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.MainViewHolder> implements Filterable {
        private Context mContext;
        private ArrayList<String> currencylist;
        ArrayList<String> currencyFilterList = new ArrayList<>();
        private final int VIEW_ITEM = 1;
        private final int VIEW_PROG = 0;


        public CurrencyAdapter(Context mContext, ArrayList<String> currencylist) {
            this.mContext = mContext;
            this.currencylist = currencylist;
            this.currencyFilterList = currencylist;
        }


        @Override
        public void onBindViewHolder(MainViewHolder holder, final int position) {

            if (holder.getItemViewType() == VIEW_ITEM) {
                final String cname = currencyFilterList.get(position);
                final ItemViewHeader mholder = (ItemViewHeader) holder;
                mholder.tv_currencyname.setText(cname);
                String all = PrefrenceManager.getString((Activity) mContext, PrefrenceManager.allcurrency);
                if (all.contains(cname)) {
                    /*mholder.cardView.setCardElevation(5);
                    mholder.cardView.setCardBackgroundColor(0x29292a);
                    mholder.cardView.setRadius(10);
                    mholder.cardView.setPreventCornerOverlap(true);
                    mholder.cardView.setUseCompatPadding(true);*/
                    mholder.iv_delete.setVisibility(View.VISIBLE);
                    mholder.iv_add.setVisibility(View.GONE);
                } else {
                    mholder.iv_delete.setVisibility(View.GONE);
                    mholder.iv_add.setVisibility(View.VISIBLE);
                }
                mholder.iv_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PrefrenceManager.setCurrencyString(mContext, cname);
                        Toast.makeText(mContext, cname + " Currency Added", Toast.LENGTH_SHORT).show();
                        currencyFilterList.clear();
                        if (!CommonMethods.getInternetStatus(mContext)) {
                            CommonMethods.showInternetDialog(mContext);
                        } else {
                            edt_search.setText("");
                            getCurrencyList();

                        }

                    }
                });
                mholder.iv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PrefrenceManager.removeCurrencyString(mContext, "," + cname);
                        Toast.makeText(mContext, cname + " Currency removed", Toast.LENGTH_SHORT).show();
                        currencyFilterList.clear();
                        if (!CommonMethods.getInternetStatus(mContext)) {
                            CommonMethods.showInternetDialog(mContext);
                        } else {
                            edt_search.setText("");
                            getCurrencyList();

                        }

                    }
                });
            }
        }


        @Override
        public int getItemCount() {
            return currencyFilterList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return currencyFilterList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
        }

        @Override
        public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = null;
            switch (viewType) {
                case VIEW_ITEM:
                    return new ItemViewHeader(LayoutInflater.from(mContext).inflate(R.layout.row_currency, parent, false));

            }
            return null;
        }


        public class ItemViewHeader extends MainViewHolder {

            private TextView tv_currencyname;
            private ImageView iv_delete, iv_add;
            private CardView cardView;

            public ItemViewHeader(View view) {
                super(view);
                cardView = view.findViewById(R.id.cardview_add_currency);
                tv_currencyname = view.findViewById(R.id.tv_currencyname);
                iv_delete = view.findViewById(R.id.iv_delete);
                iv_add = view.findViewById(R.id.iv_add);
            }
        }

        //
        public class MainViewHolder extends RecyclerView.ViewHolder {

            public MainViewHolder(View v) {
                super(v);
            }

        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        currencyFilterList = currencylist;
                    } else {
                        ArrayList<String> filteredList = new ArrayList<>();
                        for (String row : currencylist) {
                            // name match condition. this might differ depending on your requirement
                            // here we are looking for name or phone number match
                            if (row.toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }
                        }

                        currencyFilterList = filteredList;
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = currencyFilterList;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    currencyFilterList = (ArrayList<String>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }

    }
}
