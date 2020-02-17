package com.bezat.bezat.interfaces;

import com.bezat.bezat.models.CountryData;

public interface CountryCallBack {

    void onSuccess(CountryData responseResult);

    void onFailure(Throwable e);
}
