package com.growfxtrade.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.growfxtrade.model.ViewServiceModel;
import com.growfxtrade.R;

import java.util.ArrayList;

public class ViewServiceAdapter extends RecyclerView.Adapter<ViewServiceAdapter.ItemViewHolder>  {
    Context context;
    private ArrayList<ViewServiceModel> list;

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView txtdate,txttype,txtamount;


        public ItemViewHolder(View view) {
            super(view);
            this.txtdate =  view.findViewById(R.id.txtdate);
            this.txttype = (TextView) view.findViewById(R.id.txttype);
            this.txtamount = view.findViewById(R.id.txtamount);
        }
    }
    public ViewServiceAdapter(Activity activity, ArrayList<ViewServiceModel> list) {
        this.context = activity;
        this.list = list;

    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_find_match, viewGroup, false));
    }
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {
        final int pos=i;
        /*Picasso.with(this.context)
                .load((list.get(pos).getUrl(resize(200, 200))))
                .
                .centerCrop()
                .into(itemViewHolder.grid_item_image);*/
        itemViewHolder.txtdate.setText(list.get(pos).getDate());
        itemViewHolder.txttype.setText(list.get(pos).getName());
        itemViewHolder.txtamount.setText(list.get(pos).getAmount());

      //  Picasso.with(this.context).load((FilterdList.get(pos).getImgurl())).placeholder(R.drawable.default_image).into(itemViewHolder.grid_item_image);




    }

    public long getItemId(int i) {
        return (long) i;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



}

