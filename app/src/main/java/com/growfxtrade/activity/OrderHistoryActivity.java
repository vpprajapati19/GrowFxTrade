package com.growfxtrade.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.growfxtrade.model.OrderHIstoryModel;
import com.growfxtrade.prefrence.PrefrenceManager;
import com.growfxtrade.utils.AppUtils;
import com.growfxtrade.utils.CommonMethods;
import com.growfxtrade.utils.RequestInterface;
import com.growfxtrade.utils.RetrofitClient;
import com.growfxtrade.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "AddCurrencyActivity";
    private ImageView ivicon;
    LinearLayout lv_nodatafound;
    private Dialog dialog;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<OrderHIstoryModel> orderHIstoryModelArrayList = new ArrayList<>();
    private OrderHistoryAdapter orderHistoryAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderhistory);
        initComponent();

        if (!CommonMethods.getInternetStatus(this)) {
            CommonMethods.showInternetDialog(this);
        } else {
            orderHIstoryModelArrayList.clear();
            getCurrencyList();

        }
    }

    private void initComponent() {
        ivicon = findViewById(R.id.ivicon);
        ivicon.setOnClickListener(this);
        recyclerView = findViewById(R.id.recyclerView);
        lv_nodatafound = findViewById(R.id.lv_nodatafound);

        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        orderHistoryAdapter = new OrderHistoryAdapter(this, orderHIstoryModelArrayList);
        recyclerView.setAdapter(orderHistoryAdapter);

    }

    public void getCurrencyList() {
        dialog = CommonMethods.showDialogProgressBarNew(this);
        RequestInterface req = RetrofitClient.getClient(this).create(RequestInterface.class);
        Log.e("userid","=="+PrefrenceManager.USERID);
        Call<ResponseBody> call = req.getOrderHistory(PrefrenceManager.getString(this, PrefrenceManager.USERID));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("response--87","--"+response.toString());
                Log.e("response--88","--"+response);
                dialog.dismiss();
                CommonMethods.PrintLog(TAG, "url  " + response.raw().request().url());
                try {
                    String res = response.body().string();
                    CommonMethods.PrintLog(TAG, "url res " + res);
                    JSONObject jsonObject = new JSONObject(res);
                    JSONArray jsonArray = jsonObject.getJSONArray("buy_sell_data");
                    Log.e("jsonarray96","=="+jsonArray);
                    if(jsonArray.length()==0){
                        lv_nodatafound.setVisibility(View.VISIBLE);
                      Log.e("No data found==","98"+jsonArray.length());
                    }else {
                        lv_nodatafound.setVisibility(View.GONE);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            OrderHIstoryModel orderHIstoryModel = new OrderHIstoryModel();
                            orderHIstoryModel.setBuy_sell_id(jsonObject1.getString("buy_sell_id"));
                            orderHIstoryModel.setCurrency_id(jsonObject1.getString("currency_id"));
                            orderHIstoryModel.setUser_id(jsonObject1.getString("user_id"));
                            orderHIstoryModel.setDate(jsonObject1.getString("date"));
                            orderHIstoryModel.setAmount(jsonObject1.getString("amount"));
                         //   orderHIstoryModel.setQty(jsonObject1.getString("qty"));
                            orderHIstoryModel.setTotal(jsonObject1.getString("total"));
                            orderHIstoryModel.setBuy_sell(jsonObject1.getString("buy_sell"));
                            orderHIstoryModel.setSell(jsonObject1.getString("sell"));
                            orderHIstoryModel.setCurrency_name(jsonObject1.getString("currency_name"));
                            orderHIstoryModel.setOrder_id(jsonObject1.getString("order_id"));
                            orderHIstoryModel.setProfit(jsonObject1.getString("profit"));
                           // orderHIstoryModel.setLoss(jsonObject1.getString("loss"));
                            orderHIstoryModelArrayList.add(orderHIstoryModel);
                        }
                    }

                } catch (Exception e) {
                    CommonMethods.PrintLog(TAG, "url Exception " + e.toString());

                }
                orderHistoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                CommonMethods.simpleSnackbar(OrderHistoryActivity.this, AppUtils.SERVER_ERROR);
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

    public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.MainViewHolder> {
        private Context mContext;
        private ArrayList<OrderHIstoryModel> currencyModelArrayList;
        private final int VIEW_ITEM = 1;
        private final int VIEW_PROG = 0;
        private Filter fRecords;


        public OrderHistoryAdapter(Context mContext, ArrayList<OrderHIstoryModel> currencyModelArrayList) {
            this.mContext = mContext;
            this.currencyModelArrayList = currencyModelArrayList;
        }


        @SuppressLint({"WrongConstant", "ResourceAsColor"})
        @Override
        public void onBindViewHolder(MainViewHolder holder, final int position) {

            if (holder.getItemViewType() == VIEW_ITEM) {

                final OrderHIstoryModel orderHIstoryModel = currencyModelArrayList.get(position);
                final ItemViewHeader mholder = (ItemViewHeader) holder;
                mholder.tv_amount.setText(orderHIstoryModel.getTotal());
                mholder.tv_currency_price.setText(orderHIstoryModel.getAmount());
                mholder.tv_currencyname.setText(orderHIstoryModel.getCurrency_name());
                mholder.tv_orderid.setText(orderHIstoryModel.getOrder_id());
                mholder.tv_currencyprice.setText(orderHIstoryModel.getAmount());
                mholder.tv_date.setText(orderHIstoryModel.getDate());
              //  mholder.tv_profit.setText(orderHIstoryModel.getProfit());
                mholder.tv_loss.setText(orderHIstoryModel.getLoss());

                String buy_sell = orderHIstoryModel.getBuy_sell().trim();
              //  String qty = orderHIstoryModel.getQty().trim();
              //  String buy_qty = orderHIstoryModel.getQty().trim();
                String sell_qty = orderHIstoryModel.getSell().trim();


             //   mholder.tv_sellqty.setText(qty);

              /*  if(buy_qty.equalsIgnoreCase(sell_qty)){
                    mholder.tv_status.setText("Closed");
                    mholder.listatus.setBackgroundColor(getResources().getColor(R.color.red));
                }else {
                    mholder.tv_status.setText("Open");
                    mholder.listatus.setBackgroundColor(getResources().getColor(R.color.green));
                }

                if (buy_sell.length() > 0) {
                    mholder.tv_buy.setText("BUY : " + buy_qty);
                } else {
                    mholder.tv_buy.setText("BUY : " + "0");

                }*/
                if (sell_qty.length() > 0) {
                    mholder.tv_sell.setText("SELL : " + sell_qty);
                } else {
                    mholder.tv_sell.setText("SELL : " + "0");
                }
                Log.e(TAG, "sell_qty   " + sell_qty);
              /*  mholder.tv_sellqty.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final OrderHIstoryModel orderHIstoryModel = currencyModelArrayList.get(position);
                    }
                });*/

            }
        }
        @Override
        public int getItemCount() {
            return currencyModelArrayList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return currencyModelArrayList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
        }

        @Override
        public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = null;
            switch (viewType) {
                case VIEW_ITEM:
                    return new ItemViewHeader(LayoutInflater.from(mContext).inflate(R.layout.row_orderhistory, parent, false));

            }
            return null;
        }


        public class ItemViewHeader extends MainViewHolder {
            private TextView tv_loss,tv_profit,tv_amount,tv_currency_price,tv_currencyname, tv_orderid, tv_currencyprice, tv_date, tv_buy, tv_sell, tv_sellqty,tv_status;
            LinearLayout maincard, linemore,listatus;

            public ItemViewHeader(View view) {
                super(view);
                tv_loss = view.findViewById(R.id.tv_loss);
                tv_profit = view.findViewById(R.id.tv_profit);
                tv_amount = view.findViewById(R.id.tv_amount);
                tv_currency_price = view.findViewById(R.id.tv_currency_price);
                tv_currencyname = view.findViewById(R.id.tv_currencyname);
                tv_orderid = view.findViewById(R.id.tv_orderid);
                tv_currencyprice = view.findViewById(R.id.tv_currencyprice);
                tv_date = view.findViewById(R.id.tv_date);
                tv_buy = view.findViewById(R.id.tv_buy);
                tv_sell = view.findViewById(R.id.tv_sell);
             //   tv_sellqty = view.findViewById(R.id.tv_sellqty);
                tv_status = view.findViewById(R.id.tv_status);
                listatus = view.findViewById(R.id.listatus);
            }
        }
        public class MainViewHolder extends RecyclerView.ViewHolder {
            public MainViewHolder(View v) {
                super(v);
            }
        }

    }

    public void getBuySell(String id, String qty) {
        dialog = CommonMethods.showDialogProgressBarNew(this);
        RequestInterface req = RetrofitClient.getClient(this).create(RequestInterface.class);
        Call<ResponseBody> call = req.getsell(id, qty);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dialog.dismiss();
                CommonMethods.PrintLog(TAG, "url  " + response.raw().request().url());
                String staus = "";
                String msg = "";
                JSONObject jsonObject;
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
                    Toast.makeText(OrderHistoryActivity.this, msg, Toast.LENGTH_SHORT).show();
                    if (!CommonMethods.getInternetStatus(OrderHistoryActivity.this)) {
                        CommonMethods.showInternetDialog(OrderHistoryActivity.this);
                    } else {
                        orderHIstoryModelArrayList.clear();
                        getCurrencyList();

                    }

                } else {
                    Toast.makeText(OrderHistoryActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                CommonMethods.simpleSnackbar(OrderHistoryActivity.this, AppUtils.SERVER_ERROR);
//                CommonMethods.PrintLog(TAG, t.toString());

            }
        });
    }


}
