package com.bezatnew.bezat.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import com.bezatnew.bezat.R;

public class TotalCoupons extends AppCompatActivity {
    
    RecyclerView recycleTotalCoupons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_coupons);

        recycleTotalCoupons=findViewById(R.id.recycleTotalCoupons);
        getTotalCoupons();
    }

    private void getTotalCoupons() {
    }
}
