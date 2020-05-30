package com.bezatnew.bezat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bezatnew.bezat.R;
import com.bezatnew.bezat.interfaces.CategoryId;
import com.bezatnew.bezat.models.vip_list_responses.VipShopListDetails;
import com.bezatnew.bezat.utils.SharedPrefs;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VipShopListAdapter extends RecyclerView.Adapter<VipShopListAdapter.MyViewHolder> {

    private Context mcontext;
    private List<VipShopListDetails> responseResult;
    private static CategoryId categoryId;
    private String lang;

    public VipShopListAdapter(Context context,List<VipShopListDetails> datumList, CategoryId categoryId ) {
        mcontext = context;
        this.responseResult = datumList;
        this.categoryId = categoryId;
    }

    public void setSearchList(List<VipShopListDetails> datumList) {
        this.responseResult = datumList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vertical_adapter_search_retailer, parent, false);
        MyViewHolder mHolder = new MyViewHolder(view);
        if (SharedPrefs.getKey(view.getContext(), "selectedlanguage").contains("ar")) {
            lang = "_ar";
        } else {
            lang = "";
        }
        return mHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.imnageHorizontalName.setText(responseResult.get(position).getStoreName() + lang);
        Picasso.get()
                .load(responseResult.get(position).getStoreImage())
                .resize(500, 200)
                .into(holder.imageHorizontal);
    }

    @Override
    public int getItemCount() {
        if (responseResult != null) {
            return responseResult.size();
        } else
            return 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageHorizontal;
        TextView imnageHorizontalName;

        MyViewHolder(View view) {
            super(view);
            imageHorizontal = view.findViewById(R.id.image_horizontal);
            imnageHorizontalName = view.findViewById(R.id.text_horizontal);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    categoryId.onSuccess(responseResult.get(pos).getStoreId());
                }
            });
        }
    }
}
