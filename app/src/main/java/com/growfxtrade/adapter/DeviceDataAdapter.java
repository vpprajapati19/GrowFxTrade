package com.growfxtrade.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.growfxtrade.model.CurrencyModel;
import com.growfxtrade.R;

import java.util.ArrayList;


public class DeviceDataAdapter extends RecyclerView.Adapter<DeviceDataAdapter.MainViewHolder> implements Filterable {


    private Context mContext;
    private ArrayList<CurrencyModel> currencyModelArrayList;
    ArrayList<CurrencyModel> currencyFilterList = new ArrayList<>();
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
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
