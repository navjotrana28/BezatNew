package com.bezat.bezat.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.bezat.bezat.R;
import com.bezat.bezat.fragments.StoreOffer;
import com.bezat.bezat.interfaces.CategoryId;
import com.bezat.bezat.models.searchRetailerResponses.SearchResponseData;
import com.bezat.bezat.models.searchRetailerResponses.SearchRetailerStore;
import com.squareup.picasso.Picasso;
import org.json.JSONException;

import java.util.List;

public class SearchVerticalAdapter extends RecyclerView.Adapter<SearchVerticalAdapter.MyViewHolder> {

    private Context mcontext;
    CategoryId categoryId;
    private List<SearchRetailerStore> responseResult;

    public SearchVerticalAdapter(Context context, List<SearchRetailerStore> responseResult, CategoryId categoryId) {
        mcontext = context;
        this.categoryId = categoryId;
        this.responseResult = responseResult;
    }

    public void setDatumList(List<SearchRetailerStore> datumList) {
        this.responseResult = datumList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vertical_adapter_search_retailer, parent, false);
        MyViewHolder mHolder = new MyViewHolder(view);
        return mHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.imnageHorizontalName.setText(responseResult.get(position).getStoreName());
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
