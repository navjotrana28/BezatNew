package com.bezatnew.bezat;


import com.bezatnew.bezat.api.contactusResponse.ContactUsResponse;
import com.bezatnew.bezat.api.feedbackResponse.FeedbackResponse;
import com.bezatnew.bezat.models.CountryData;
import com.bezatnew.bezat.models.CouponResult;
import com.bezatnew.bezat.models.LogoutResponse;
import com.bezatnew.bezat.models.RegisterRequestResponse;
import com.bezatnew.bezat.models.searchRetailerResponses.SearchResponseResult;
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
    @POST("raffles/user_monthly_raffles?")
    Observable<CouponResult> getCouponResult(@Field("userId") String userId,
                                             @Field("&year_month=") String currentDate);

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
                                                     @Field("phone") String phone,
                                                     @Field("smsHashCode") String smsHashCode);

    @FormUrlEncoded
    @POST("staff/logout")
    Observable<LogoutResponse> getLogoutAPi(@Field("userID") String userId);

    @GET("user/get_country")
    Observable<CountryData> getCountryResponse();
}

