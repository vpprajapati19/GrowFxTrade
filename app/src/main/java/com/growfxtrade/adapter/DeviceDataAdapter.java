package com.growfxtrade.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.growfxtrade.activity.ChatWithUsActivity;
import com.growfxtrade.activity.demo_popup;
import com.growfxtrade.model.CurrencyModel;
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


public class DeviceDataAdapter extends RecyclerView.Adapter<DeviceDataAdapter.MainViewHolder> implements Filterable {


    private static Context mContext;
    private ArrayList<CurrencyModel> currencyModelArrayList;
    ArrayList<CurrencyModel> currencyFilterList = new ArrayList<>();
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private Dialog dialog;
    AlertDialog alertDialog;
    String qty,amount;
    private Filter fRecords;



    public DeviceDataAdapter(Context mContext, ArrayList<CurrencyModel> currencyModelArrayList) {
        this.mContext = mContext;

        this.currencyModelArrayList = currencyModelArrayList;
        this.currencyFilterList = currencyModelArrayList;
    }


    @SuppressLint({"WrongConstant", "ResourceAsColor"})
    @Override
    public void onBindViewHolder(MainViewHolder holder, final int position) {

        if (holder.getItemViewType() == VIEW_ITEM) {
            final CurrencyModel analogDashboardModel = currencyFilterList.get(position);
            final ItemViewHeader mholder = (ItemViewHeader) holder;
            mholder.symbol.setText(analogDashboardModel.getSymbol());
            mholder.timestamp.setText(analogDashboardModel.getTimestamp());
            mholder.bid.setText(analogDashboardModel.getBid());
            mholder.ask.setText(analogDashboardModel.getAsk());
            mholder.price.setText(analogDashboardModel.getPrice());
            mholder.main_row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater li = LayoutInflater.from(mContext);
                    View promptsView = li.inflate(R.layout.dialog_watchlist, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            mContext,R.style.CustomAlertDialog);

                    // set prompts.xml to alertdialog builder
                    alertDialogBuilder.setView(promptsView);

                    LinearLayout dialogParent=(LinearLayout) promptsView.findViewById(R.id.dialogParent);
                    TextView currencyvalue=(TextView)promptsView.findViewById(R.id.tv_currencyvalue);
                    TextView tv_balance_value=(TextView)promptsView.findViewById(R.id.tv_balance_value);
                    TextView askvalue=(TextView)promptsView.findViewById(R.id.tv_askvalue);
                    TextView bidvalue=(TextView)promptsView.findViewById(R.id.tv_bidvalue);
                    TextView balance=(TextView)promptsView.findViewById(R.id.tv_balance_value);
                    Button btn_buy=(Button)promptsView.findViewById(R.id.btn_buy);
                    final EditText et_amount=(EditText) promptsView.findViewById(R.id.et_amount);
                  //  final EditText et_qty=(EditText) promptsView.findViewById(R.id.et_qty);
                    currencyvalue.setText(analogDashboardModel.getSymbol());
                    askvalue.setText(analogDashboardModel.getAsk());
                    bidvalue.setText(analogDashboardModel.getBid());
                    balance.setText(analogDashboardModel.getPrice());
                    tv_balance_value.setText(PrefrenceManager.getString((Activity) mContext, PrefrenceManager.user_balence));
                 /*   et_qty.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void afterTextChanged(Editable s) {
                            Log.e("text_111",""+s);
                        }
                        @Override
                        public void beforeTextChanged(CharSequence s, int start,
                                                      int count, int after) {
                            Log.e("text_115",""+s);
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start,
                                                  int before, int count) {
                            if(s.length() != 0){
                                qty=s.toString();
                                Log.e("text_122",""+s);
                                String t2 = String.valueOf(Float.valueOf(analogDashboardModel.getBid()) * Float.valueOf(s.toString()));
                                Log.e("text_result",""+t2);
                                et_amount.setText(t2);

                            }else {
                                et_amount.setText("");
                            }

                               // field2.setText("");
                        }
                    });*/
                    btn_buy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String total=et_amount.getText().toString();
                            String cur_name=analogDashboardModel.getSymbol();
                            String cur_id=analogDashboardModel.getTimestamp();
                            if(et_amount.getText().length()==0){
                                Toast.makeText(mContext, "Please Enter Amount", Toast.LENGTH_SHORT).show();
                            }else{
                                Buyapi(total,analogDashboardModel.getAsk(),cur_name,cur_id,alertDialog,qty);
                            }
                        }
                    });
                    dialogParent.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                         //What is relAppointment1, variable is undefined
                            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context
                                    .INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(et_amount.getWindowToken(), 0);
                                return true;
                        }
                    });
                    // create alert dialog
                    alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();

              /*  final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);*/

                    // set dialog message
               /*     alertDialogBuilder
                            .setCancelable(true)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            // get user input and set it to result
                                            // edit text
                                            //  result.setText(userInput.getText());
                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            dialog.cancel();
                                        }
                                    });*/


/*

                        final Dialog dialog1 = new Dialog(mContext, R.style.df_dialog);
                        dialog1.setContentView(R.layout.dialog_watchlist);
                        dialog1.setCancelable(true);
                        dialog1.setCanceledOnTouchOutside(true);
                        LinearLayout dialogParent=(LinearLayout) dialog1.findViewById(R.id.dialogParent);
                        TextView currencyvalue=(TextView)dialog1.findViewById(R.id.tv_currencyvalue);
                        TextView askvalue=(TextView)dialog1.findViewById(R.id.tv_askvalue);
                        TextView bidvalue=(TextView)dialog1.findViewById(R.id.tv_bidvalue);
                        TextView balance=(TextView)dialog1.findViewById(R.id.tv_balance_value);
                        EditText et_amount=(EditText) dialog1.findViewById(R.id.et_amount);
                        currencyvalue.setText(analogDashboardModel.getSymbol());
                        askvalue.setText(analogDashboardModel.getAsk());
                        bidvalue.setText(analogDashboardModel.getBid());
                        balance.setText(analogDashboardModel.getPrice());
                      //  setupUI(dialogParent);
                       *//* dialog1.findViewById(R.id.btnSpinAndWinRedeem).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog1.dismiss();
                            }
                        });*//*
                        dialog1.show();*/

                }
            });
        }
    }
    public void Buyapi(String total,String amoun, String cur_name, String cur_id, final AlertDialog alertDialog,String qty) {
     //   Log.e("list236","=="+clist);
        Log.e("userid187","=="+PrefrenceManager.getString((Activity) mContext, PrefrenceManager.USERID));
        dialog = CommonMethods.showDialogProgressBarNew(mContext);
        RequestInterface req = RetrofitClient.getClient(mContext).create(RequestInterface.class);
        Call<ResponseBody> call = req.getBuySell("buy",cur_name,amoun,total, PrefrenceManager.getString((Activity) mContext, PrefrenceManager.USERID),cur_name);

        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String msg;
                dialog.dismiss();
                try {
                    alertDialog.dismiss();
                    String jsonst = response.body().string();
                    Log.e( "url jsonst ","194"+ jsonst);

                    JSONObject jsonObject = new JSONObject(jsonst);
                   String staus = jsonObject.getString("staus");
                   msg = jsonObject.getString("msg");
                   Log.e("message","200"+msg);
                    Toast.makeText(mContext, ""+msg, Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    alertDialog.dismiss();
                    msg = "Error..!!!";
                    CommonMethods.PrintLog("Error catch ", e.toString());
                    e.printStackTrace();
                }
               // deviceDataAdapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                alertDialog.dismiss();
                CommonMethods.simpleSnackbar(mContext, AppUtils.SERVER_ERROR);
                Log.e("onFailure",""+t.toString());
              //  CommonMethods.PrintLog(TAG, "onFailure " + t.toString());

            }

        });
    }
    private void setupUI(View v) {
        if (!(v instanceof EditText)) {
            v.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                  //  hideKeyboard(mContext);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (v instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) v).getChildCount(); i++) {
                View innerView = ((ViewGroup) v).getChildAt(i);
                setupUI(innerView);
            }
        }
    }
   /* public static void hideKeyboard(Context activity) {
        InputMethodManager inputManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        // check if no view has focus:
        View currentFocusedView = mContext.getCurrentFocus();
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }*/

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
                return new ItemViewHeader(LayoutInflater.from(mContext).inflate(R.layout.row_devicedata, parent, false));

        }
        return null;
    }

    public class ItemViewHeader extends MainViewHolder {

        private TextView symbol, timestamp, price, bid, ask, typevalue;
        LinearLayout main_row, linemore;

        public ItemViewHeader(View view) {
            super(view);
            symbol = view.findViewById(R.id.symbol);
            timestamp = view.findViewById(R.id.timestamp);
            price = view.findViewById(R.id.price);
            bid = view.findViewById(R.id.bid);
            ask = view.findViewById(R.id.ask);
            main_row = view.findViewById(R.id.main_row);

        }
    }

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
                    currencyFilterList = currencyModelArrayList;
                } else {
                    ArrayList<CurrencyModel> filteredList = new ArrayList<>();
                    for (CurrencyModel row : currencyModelArrayList) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getSymbol().toLowerCase().contains(charString.toLowerCase())) {
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
                currencyFilterList = (ArrayList<CurrencyModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
