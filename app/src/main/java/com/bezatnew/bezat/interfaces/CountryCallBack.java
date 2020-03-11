package com.bezatnew.bezat.interfaces;

import com.bezatnew.bezat.models.CountryData;

public interface CountryCallBack {

    void onSuccess(CountryData responseResult);

    void onFailure(Throwable e);
}
