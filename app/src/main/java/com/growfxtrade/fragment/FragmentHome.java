package com.growfxtrade.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.growfxtrade.activity.ChatWithUsActivity;
import com.growfxtrade.activity.MainActivity;
import com.growfxtrade.prefrence.PrefrenceManager;
import com.growfxtrade.R;
import com.growfxtrade.adapter.DeviceDataAdapter;
import com.growfxtrade.model.CurrencyModel;
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

import static com.growfxtrade.activity.ChatWithUsActivity.hideKeyboard;

public class FragmentHome extends BaseFragment {

    private String TAG = "FragmentHome";
    private View rootView;
    LinearLayout lv_nodatafound,lv_main;
    private Dialog dialog;
    RelativeLayout mainrelative;
    private RecyclerView recyclerView_category;
    private ArrayList<CurrencyModel> modelCategoryListList = new ArrayList<>();
    private LinearLayoutManager mLayoutManager;
    private DeviceDataAdapter deviceDataAdapter;
    Handler handler;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.home, container, false);

        initComponent();

        setupUI(lv_main);

        CommonMethods.PrintLog(TAG, PrefrenceManager.getString(getActivity(), PrefrenceManager.allcurrency));

        modelCategoryListList.clear();
        if (!CommonMethods.getInternetStatus(getActivity())) {
            CommonMethods.showInternetDialog(getActivity());
        } else {
            String clist = "";
            Log.e(TAG, "allcurrency  " + PrefrenceManager.getString(getActivity(), PrefrenceManager.allcurrency));
            clist = PrefrenceManager.getString(getActivity(), PrefrenceManager.allcurrency);

            if (clist.length() > 3) {
                clist = clist.substring(1);
                getCurrencyList(clist);
            }
        }

//        handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //Do something after 100ms
//
//                Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
//                modelCategoryListList.clear();
//                if (!CommonMethods.getInternetStatus(getActivity())) {
//                    CommonMethods.showInternetDialog(getActivity());
//                } else {
//                    String clist = "";
//                    clist = PrefrenceManager.getString(getActivity(), PrefrenceManager.allcurrency);
//                    Log.e(TAG, "else  " + clist);
//                    if (clist.length() > 3) {
//                        clist = clist.substring(1);
//                        getCurrencyListTimer(clist);
//                    }
//                }
//                handler.postDelayed(this, 3000);
//            }
//        }, 3000);

        return rootView;
    }

    private void setupUI(View v) {
        if (!(v instanceof EditText)) {
            v.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideKeyboard(getActivity());
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
    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        // check if no view has focus:
        View currentFocusedView = activity.getCurrentFocus();
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    @Override
    public void onPause() {
        handler.removeCallbacksAndMessages(null);
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        CommonMethods.PrintLog(TAG, "onResume");
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms

                modelCategoryListList.clear();
                if (!CommonMethods.getInternetStatus(getActivity())) {
                    CommonMethods.showInternetDialog(getActivity());
                } else {
                    String clist = "";
                    clist = PrefrenceManager.getString(getActivity(), PrefrenceManager.allcurrency);
                    Log.e(TAG, "else  " + clist);
                    if (clist.length() > 3) {
                        clist = clist.substring(1);
                        getCurrencyListTimer(clist);
                    }
                }
                handler.postDelayed(this, 3000);
            }
        }, 3000);

//        modelCategoryListList.clear();
//        if (!CommonMethods.getInternetStatus(getActivity())) {
//            CommonMethods.showInternetDialog(getActivity());
//        } else {
//            String clist = "";
//            Log.e(TAG, "allcurrency  " + PrefrenceManager.getString(getActivity(), PrefrenceManager.allcurrency));
//            clist = PrefrenceManager.getString(getActivity(), PrefrenceManager.allcurrency);
//
//            if (clist.length() > 3) {
//                clist = clist.substring(1);
//                getCurrencyList(clist);
//
//            }
//
//        }

    }


    private void initComponent() {
        recyclerView_category = rootView.findViewById(R.id.rv);
        lv_nodatafound = rootView.findViewById(R.id.lv_nodatafound);
        lv_main = rootView.findViewById(R.id.lv_main);
        mainrelative = rootView.findViewById(R.id.mainrelative);

        MainActivity.img_search_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.edt_serch.setText("");
                MainActivity.edt_serch.setVisibility(View.GONE);
                MainActivity.img_search_back.setVisibility(View.GONE);
                MainActivity.iv_icon.setVisibility(View.VISIBLE);
                deviceDataAdapter.notifyDataSetChanged();
            }
        });

        MainActivity.edt_serch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // filter recycler view when query submitted
                deviceDataAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView_category.setLayoutManager(mLayoutManager);
        deviceDataAdapter = new DeviceDataAdapter(getActivity(), modelCategoryListList);

        recyclerView_category.setAdapter(deviceDataAdapter);
    }
    public void getCurrencyList(String clist) {
        Log.e("list236","=="+clist);
        dialog = CommonMethods.showDialogProgressBarNew(getActivity());
        RequestInterface req = RetrofitClient.getClient1(getActivity()).create(RequestInterface.class);
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
                    if(jsonArray.length()==0){
                        lv_nodatafound.setVisibility(View.VISIBLE);
                    }else{
                        lv_nodatafound.setVisibility(View.GONE);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            CurrencyModel currencyModel = new CurrencyModel();
                            currencyModel.setSymbol(jsonObject.getString("s"));
                            currencyModel.setBid(jsonObject.getString("b"));
                            currencyModel.setAsk(jsonObject.getString("a"));
                            currencyModel.setPrice(jsonObject.getString("p"));
                            currencyModel.setTimestamp(jsonObject.getString("t"));
                            modelCategoryListList.add(currencyModel);
                        }
                    }
                } catch (Exception e) {
                    CommonMethods.PrintLog("Error catch ", e.toString());
                    e.printStackTrace();
                }
                deviceDataAdapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                CommonMethods.simpleSnackbar(getActivity(), AppUtils.SERVER_ERROR);

                CommonMethods.PrintLog(TAG, "onFailure " + t.toString());

            }

        });
    }

    public void getCurrencyListTimer(String clist) {
        RequestInterface req = RetrofitClient.getClient1(getActivity()).create(RequestInterface.class);
        Call<ResponseBody> call = req.getAddedCurrency(clist, AppUtils.API_KEY);

        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String jsonst = response.body().string();
                    CommonMethods.PrintLog(TAG, "url jsonst " + jsonst);
                    JSONArray jsonArray = new JSONArray(jsonst);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        CurrencyModel currencyModel = new CurrencyModel();
                        currencyModel.setSymbol(jsonObject.getString("s"));
                        currencyModel.setBid(jsonObject.getString("b"));
                        currencyModel.setAsk(jsonObject.getString("a"));
                        currencyModel.setPrice(jsonObject.getString("p"));
                        currencyModel.setTimestamp(jsonObject.getString("t"));
                        modelCategoryListList.add(currencyModel);
                    }

                } catch (Exception e) {
                    CommonMethods.PrintLog("Error catch ", e.toString());
                    e.printStackTrace();
                }

                deviceDataAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                CommonMethods.simpleSnackbar(getActivity(), AppUtils.SERVER_ERROR);

                CommonMethods.PrintLog(TAG, "onFailure " + t.toString());

            }

        });
    }

}
