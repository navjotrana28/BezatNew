package com.bezat.bezat.interfaces;

import com.bezat.bezat.models.RegisterRequestResponse;

public interface RegisterUserCallBack {
    void onResponse(RegisterRequestResponse response);

    void onError(Throwable throwable);
}
