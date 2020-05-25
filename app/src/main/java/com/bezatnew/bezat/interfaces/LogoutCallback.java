package com.bezatnew.bezat.interfaces;


import com.bezatnew.bezat.models.LogoutResponse;

public interface LogoutCallback {

    void onSuccess(LogoutResponse responseResult);

    void onFailure(Throwable e);

}
