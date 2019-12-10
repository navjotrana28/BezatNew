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
import com.bezat.bezat.models.searchRetailerResponses.SearchResponseData;
import com.squareup.picasso.Picasso;

public class SearchVerticalAdapter extends RecyclerView.Adapter<SearchVerticalAdapter.MyViewHolder> {

    private Context mcontext;
    private SearchResponseData responseResult;

    public SearchVerticalAdapter(Context context, SearchResponseData responseResult) {
        mcontext = context;
        this.responseResult = responseResult;
    }

    public void setDatumList(SearchResponseData datumList) {
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
        holder.imnageHorizontalName.setText(responseResult.getStores().get(position).getStoreName());
        Picasso.get()
                .load(responseResult.getStores().get(position).getStoreImage())
                .resize(500, 200)
                .into(holder.imageHorizontal);
    }

    @Override
    public int getItemCount() {
        if (responseResult.getStores() != null) {
            return responseResult.getStores().size();
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
                }
            });
        }
    }
}
