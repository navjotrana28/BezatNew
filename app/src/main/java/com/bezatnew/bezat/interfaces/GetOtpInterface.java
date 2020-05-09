package com.bezatnew.bezat.interfaces;

public interface GetOtpInterface {
    void onOtpReceived(String otp);

    void onOtpTimeout();
}
