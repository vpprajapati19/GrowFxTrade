package com.growfxtrade.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.growfxtrade.R;
import com.growfxtrade.utils.AppUtils;
import com.growfxtrade.utils.CommonMethods;
import com.growfxtrade.utils.RequestInterface;
import com.growfxtrade.utils.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GraphActivity extends AppCompatActivity {

    String symbol;
    Handler handler;
    List<Entry> lineEntries = new ArrayList<Entry>();
    LineChart lineChart;
    LineDataSet lineDataSet;
    LineData lineData;
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        symbol=  getIntent().getStringExtra("symbol");
        lineEntries=new ArrayList<>();

        lineChart = findViewById(R.id.lineChart);

        //lineDataSet = new LineDataSet(lineEntries, symbol);

       /* lineDataSet = new LineDataSet(lineEntries, symbol);
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setHighlightEnabled(true);
        lineDataSet.setLineWidth(2);
        lineDataSet.setColor(Color.RED);
        lineDataSet.setCircleColor(Color.YELLOW);
        lineDataSet.setCircleRadius(6);
        lineDataSet.setCircleHoleRadius(3);
        lineDataSet.setDrawHighlightIndicators(true);
        lineDataSet.setHighLightColor(Color.RED);
        lineDataSet.setValueTextSize(12);
        lineDataSet.setValueTextColor(Color.DKGRAY);

        lineData = new LineData(lineDataSet);
       // lineChart.getDescription().setText("12 days");
        //lineChart.getDescription().setTextSize(12);
        lineChart.setDrawMarkers(true);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        lineChart.animateY(1000);
        lineChart.getXAxis().setGranularityEnabled(true);
        lineChart.getXAxis().setGranularity(1.0f);
        lineChart.getXAxis().setLabelCount(lineDataSet.getEntryCount());
        lineChart.setData(lineData);*/


        if (!CommonMethods.getInternetStatus(GraphActivity.this)) {
            CommonMethods.showInternetDialog(GraphActivity.this);
        } else {
           /* String clist = "";
           //Log.e(TAG, "allcurrency  " + PrefrenceManager.getString(getActivity(), PrefrenceManager.allcurrency));
            clist = PrefrenceManager.getString(GraphActivity.this, PrefrenceManager.allcurrency);

            if (clist.length() > 1) {
                clist = clist.substring(1);
               getCurrencyList(clist);

            }*/
            getCurrencyList();

        }




    }


    @Override
    protected void onResume() {
        super.onResume();

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms


                if (!CommonMethods.getInternetStatus(GraphActivity.this)) {
                    CommonMethods.showInternetDialog(GraphActivity.this);
                } else {
                   /* String clist = "";
                    clist = PrefrenceManager.getString(GraphActivity.this, PrefrenceManager.allcurrency);

                    if(clist.equalsIgnoreCase("")){

                    }else {
                        if (clist.length() > 1) {
                            clist = clist.substring(1);
                            getCurrencyList(clist);

                        }

                    }*/
                    getCurrencyList();

                }
                handler.postDelayed(this, 5000);
            }
        }, 5000);

    }
    public void getCurrencyList() {
      //  dialog = CommonMethods.showDialogProgressBarNew(GraphActivity.this);
        RequestInterface req = RetrofitClient.getClient1(GraphActivity.this).create(RequestInterface.class);
        Call<ResponseBody> call = req.getAddedCurrency(symbol, AppUtils.API_KEY);

        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
              //  dialog.dismiss();
                try {
                    String jsonst = response.body().string();
                   // CommonMethods.PrintLog(TAG, "url jsonst " + jsonst);
                    JSONArray jsonArray = new JSONArray(jsonst);

                    String fsymbol="";
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        fsymbol=  jsonObject.getString("s");

                         if(symbol.equalsIgnoreCase(fsymbol)){
                        float pirce= Float.parseFloat(jsonObject.getString("p"));


                             float bid= Float.parseFloat(jsonObject.getString("b"));
                             float ask= Float.parseFloat(jsonObject.getString("a"));
                             String timestamp= jsonObject.getString("t");


                             Calendar calendar = Calendar.getInstance();
                             TimeZone tz = TimeZone.getDefault();
                             calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
                             SimpleDateFormat sdf = new SimpleDateFormat("ss", Locale.getDefault());

                             long time= Long.parseLong(timestamp);
                             java.util.Date currenTimeZone=new java.util.Date((long)time*1000);
                          //   Toast.makeText(mContext, sdf.format(currenTimeZone), Toast.LENGTH_SHORT).show();


                             float timestampflaot= Float.parseFloat(sdf.format(currenTimeZone));
                               lineEntries.add(new Entry(timestampflaot, bid));
                             lineDataSet = new LineDataSet(lineEntries, symbol);
                             lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
                             lineDataSet.setHighlightEnabled(true);
                             lineDataSet.setLineWidth(2);
                             lineDataSet.setColor(Color.RED);
                             lineDataSet.setCircleColor(Color.YELLOW);
                             lineDataSet.setCircleRadius(6);
                             lineDataSet.setCircleHoleRadius(3);
                             lineDataSet.setDrawHighlightIndicators(true);
                             lineDataSet.setHighLightColor(Color.RED);
                             lineDataSet.setValueTextSize(12);
                             lineDataSet.setValueTextColor(Color.DKGRAY);

                             lineData = new LineData(lineDataSet);
                             // lineChart.getDescription().setText("");
                             // lineChart.getDescription().setTextSize(12);
                             lineChart.setDrawMarkers(true);
                             lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                             lineChart.animateY(1000);
                             lineChart.getXAxis().setGranularityEnabled(true);
                             lineChart.getXAxis().setGranularity(1.0f);
                             lineChart.getXAxis().setLabelCount(lineDataSet.getEntryCount());
                             lineChart.setData(lineData);

                    }
                    }



                } catch (Exception e) {
                    CommonMethods.PrintLog("Error catch ", e.toString());
                    e.printStackTrace();
                }




            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
              //  dialog.dismiss();

               // CommonMethods.simpleSnackbar(getActivity(), AppUtils.SERVER_ERROR);

               // CommonMethods.PrintLog(TAG, "onFailure " + t.toString());

            }


        });
    }


}
