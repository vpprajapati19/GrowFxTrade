package com.growfxtrade.adapter;

//public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.MainViewHolder> {
//    private Context mContext;
//    private ArrayList<OrderHIstoryModel> currencyModelArrayList;
//    private final int VIEW_ITEM = 1;
//    private final int VIEW_PROG = 0;
//    private Filter fRecords;
//
//
//    public OrderHistoryAdapter(Context mContext, ArrayList<OrderHIstoryModel> currencyModelArrayList) {
//        this.mContext = mContext;
//        this.currencyModelArrayList = currencyModelArrayList;
//    }
//
//
//    @SuppressLint({"WrongConstant", "ResourceAsColor"})
//    @Override
//    public void onBindViewHolder(MainViewHolder holder, final int position) {
//
//        if (holder.getItemViewType() == VIEW_ITEM) {
//            final OrderHIstoryModel orderHIstoryModel = currencyModelArrayList.get(position);
//            final ItemViewHeader mholder = (ItemViewHeader) holder;
//            mholder.tv_currencyname.setText(orderHIstoryModel.getCurrency_name());
//            mholder.tv_orderid.setText(orderHIstoryModel.getBuy_sell_id());
//            mholder.tv_currencyprice.setText(orderHIstoryModel.getAmount());
//            mholder.tv_date.setText(orderHIstoryModel.getDate());
//
//            String buy_sell = orderHIstoryModel.getBuy_sell().trim();
//            String qty = orderHIstoryModel.getQty().trim();
//            String buy_qty = orderHIstoryModel.getQty().trim();
//            String sell_qty = orderHIstoryModel.getSell().trim();
//
//            if (buy_sell.length() > 0) {
//                mholder.tv_buy.setText("BUY : " + buy_qty);
//            } else {
//                mholder.tv_buy.setText("BUY : " + "0");
//
//            }
//            if (sell_qty.length() > 0) {
//                mholder.tv_sell.setText("SELL : " + sell_qty);
//            } else {
//                mholder.tv_sell.setText("SELL : " + "0");
//            }
//
//            if (sell_qty.equalsIgnoreCase("null") || sell_qty.length() <= 0) {
//                mholder.tv_sellqty.setVisibility(View.VISIBLE);
//            } else {
//                mholder.tv_sellqty.setVisibility(View.GONE);
//            }
//
//            mholder.tv_sellqty.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//
//
//        }
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return currencyModelArrayList.size();
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return currencyModelArrayList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
//    }
//
//    @Override
//    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View v = null;
//        switch (viewType) {
//            case VIEW_ITEM:
//                return new ItemViewHeader(LayoutInflater.from(mContext).inflate(R.layout.row_orderhistory, parent, false));
//
//        }
//        return null;
//    }
//
//
//    public class ItemViewHeader extends MainViewHolder {
//
//        private TextView tv_currencyname, tv_orderid, tv_currencyprice, tv_date, tv_buy, tv_sell, tv_sellqty;
//        LinearLayout maincard, linemore;
//
//        public ItemViewHeader(View view) {
//            super(view);
//            tv_currencyname = view.findViewById(R.id.tv_currencyname);
//            tv_orderid = view.findViewById(R.id.tv_orderid);
//            tv_currencyprice = view.findViewById(R.id.tv_currencyprice);
//            tv_date = view.findViewById(R.id.tv_date);
//            tv_buy = view.findViewById(R.id.tv_buy);
//            tv_sell = view.findViewById(R.id.tv_sell);
//            tv_sellqty = view.findViewById(R.id.tv_sellqty);
////
////
//        }
//    }
//
//    //
//    public class MainViewHolder extends RecyclerView.ViewHolder {
//
//        public MainViewHolder(View v) {
//            super(v);
//        }
//
//    }
//
//
//}
