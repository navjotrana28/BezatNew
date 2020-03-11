package com.bezatnew.bezat.interfaces;

import com.bezatnew.bezat.models.CouponResult;

public interface TotalCouponsCallback {
    void onSuccess(CouponResult responseResult);

    void onFailure(Throwable e);

}
