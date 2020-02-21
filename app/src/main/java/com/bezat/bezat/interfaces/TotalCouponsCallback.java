package com.bezat.bezat.interfaces;

import com.bezat.bezat.models.CouponResult;
import com.bezat.bezat.models.searchRetailerResponses.SearchResponseResult;

public interface TotalCouponsCallback {
    void onSuccess(CouponResult responseResult);

    void onFailure(Throwable e);

}
