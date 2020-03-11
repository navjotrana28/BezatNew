package com.bezatnew.bezat.interfaces;


import com.bezatnew.bezat.api.contactusResponse.ContactUsResponse;

public interface ContactUsSuccessResponse {
    void onSuccess(ContactUsResponse response);

    void onFailure(Throwable e);
}