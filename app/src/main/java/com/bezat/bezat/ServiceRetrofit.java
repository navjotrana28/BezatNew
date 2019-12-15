package com.bezat.bezat;


import com.bezat.bezat.api.contactusResponse.ContactUsResponse;
import com.bezat.bezat.api.feedbackResponse.FeedbackResponse;
import com.bezat.bezat.models.RegisterRequestResponse;
import com.bezat.bezat.models.searchRetailerResponses.SearchResponseResult;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ServiceRetrofit {

    @FormUrlEncoded
    @POST("user/sendEnquiry")
    Observable<ContactUsResponse> getContactSuccess(@Field("name") String name,
                                                    @Field("email") String email,
                                                    @Field("comments") String comments);


    @GET("category/list")
    Observable<SearchResponseResult> getSearchRetaierSuccess();

    @FormUrlEncoded
    @POST("user/feedback")
    Observable<FeedbackResponse> getFeedbackRequest(@Field("userId") String userId,
                                                    @Field("feedback") String feedback,
                                                    @Field("retailerId") String retailerId,
                                                    @Field("ratings") String ratings);

    @FormUrlEncoded
    @POST("user/register")
    Observable<RegisterRequestResponse> registerUser(@Field("password") String password,
                                                     @Field("mobile_code") String mobile_code,
                                                     @Field("phone") String phone);

    @FormUrlEncoded
    @POST("staff/logout")
    Call<Void> getLogoutAPi(@Field("userId") String userId);
}

