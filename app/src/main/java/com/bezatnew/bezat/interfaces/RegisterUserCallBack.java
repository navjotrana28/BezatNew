package com.bezatnew.bezat.interfaces;

import com.bezatnew.bezat.models.RegisterRequestResponse;

public interface RegisterUserCallBack {
    void onResponse(RegisterRequestResponse response);

    void onError(Throwable throwable);
}
