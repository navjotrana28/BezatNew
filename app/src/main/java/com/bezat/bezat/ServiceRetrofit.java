package com.bezat.bezat;


import com.bezat.bezat.api.contactusResponse.ContactUsResponse;
import com.bezat.bezat.models.searchRetailerResponses.SearchResponseResult;
import io.reactivex.Observable;
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
    Observable<ContactUsResponse> getFeedbackRequest(@Field("userId") String userId,
                                                     @Field("feedback") String feedback,
                                                     @Field("retailerId") String retailerId,
                                                     @Field("ratings") String ratings);

}

