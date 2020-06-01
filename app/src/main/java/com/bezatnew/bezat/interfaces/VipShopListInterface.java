package com.bezatnew.bezat.interfaces;

import com.bezatnew.bezat.models.vip_list_responses.VipShopListResponse;

public interface VipShopListInterface {

    void onSuccess(VipShopListResponse responseResult);

    void onFailure(Throwable e);
}
