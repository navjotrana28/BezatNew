package com.bezat.bezat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bezat.bezat.R;
import com.bezat.bezat.interfaces.ClickCouponBack;
import com.bezat.bezat.models.CouponResult;
import com.squareup.picasso.Picasso;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.MyViewHolder> {

    private Context mcontext;
    private CouponResult responseResult;
    private ClickCouponBack couponBack;

    public CouponAdapter(Context context, CouponResult responseResult, ClickCouponBack couponBack) {
        mcontext = context;
        this.responseResult = responseResult;
        this.couponBack = couponBack;
    }

    public void setDatumList(CouponResult datumList) {
        this.responseResult = datumList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CouponAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.total_coupon_item, parent, false);
        MyViewHolder mHolder = new MyViewHolder(view);
        return mHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CouponAdapter.MyViewHolder holder, int position) {
        try {
            holder.txtBilldate.setText(responseResult.getResult().getRaffles().get(position).getBillDate());
            holder.txtBillno.setText(responseResult.getResult().getRaffles().get(position).getBillNo());
            holder.txtRaffles.setText(responseResult.getResult().getRaffles().get(position).getRaffles());
            holder.txtStoreName.setText(responseResult.getResult().getRaffles().get(position).getStoreName());
            Picasso.get().load(responseResult.getResult().getRaffles().get(position).getStoreLogo()).into(holder.imgCoupon);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (responseResult.getResult() != null) {
            return responseResult.getResult().getRaffles().size();
        } else
            return 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtStoreName, txtRaffles, txtBillno, txtBilldate;
        ImageView imgCoupon;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtBilldate = itemView.findViewById(R.id.txtBilldate);
            txtBillno = itemView.findViewById(R.id.txtBillno);
            txtRaffles = itemView.findViewById(R.id.txtRaffles);
            txtStoreName = itemView.findViewById(R.id.txtStoreName);
            imgCoupon = itemView.findViewById(R.id.imgCoupon);

            itemView.setOnClickListener(view1 -> {
                int pos = getAdapterPosition();
                couponBack.onClickHorizonView(pos);
            });
        }
    }
}