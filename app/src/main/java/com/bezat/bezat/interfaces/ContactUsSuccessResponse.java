package com.bezat.bezat.interfaces;


import com.bezat.bezat.api.contactusResponse.ContactUsResponse;

public interface ContactUsSuccessResponse {
    void onSuccess(ContactUsResponse response);

    void onFailure(Throwable e);
}