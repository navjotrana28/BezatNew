package com.bezat.bezat;


import com.bezat.bezat.api.contactusResponse.ContactUsRequest;
import com.bezat.bezat.api.contactusResponse.ContactUsResponse;
import com.bezat.bezat.api.feedbackResponse.FeedbackRequest;
import com.bezat.bezat.api.feedbackResponse.FeedbackResponse;
import com.bezat.bezat.interfaces.ContactUsSuccessResponse;
import com.bezat.bezat.interfaces.FeedbackCallback;
import com.bezat.bezat.interfaces.RegisterUserCallBack;
import com.bezat.bezat.interfaces.SearchRetaierInterface;
import com.bezat.bezat.models.RegisterRequestResponse;
import com.bezat.bezat.models.RegisterUserRequest;
import com.bezat.bezat.models.searchRetailerResponses.SearchResponseResult;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientRetrofit {


    private static Retrofit retrofit = null;
    private static ServiceRetrofit serviceRetrofit = null;

    public ClientRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://Bezatapp.com/bezatapi/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        serviceRetrofit = retrofit.create(ServiceRetrofit.class);
    }


    public void SendDataViaApi(ContactUsRequest request, final ContactUsSuccessResponse contactUsSuccessResponse) {
        serviceRetrofit.getContactSuccess(request.getName(), request.getEmail(), request.getComments())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ContactUsResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ContactUsResponse response) {
                        contactUsSuccessResponse.onSuccess(response);

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void registerUser(RegisterUserRequest request, RegisterUserCallBack callBack) {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        serviceRetrofit.registerUser(request.password, request.mobile_code, request.phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RegisterRequestResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(RegisterRequestResponse response) {
                        callBack.onResponse(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        compositeDisposable.clear();
                    }
                });
    }


    public void SearchRetailerResult(final SearchRetaierInterface retaierInterface) {
        serviceRetrofit.getSearchRetaierSuccess()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SearchResponseResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(SearchResponseResult responseResult) {
                        retaierInterface.onSuccess(responseResult);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void feedBackRequestApi(FeedbackRequest request, final FeedbackCallback callback) {
        serviceRetrofit.getFeedbackRequest(request.getUserId(), request.getFeedback(), request.getRetailerId(), request.getRatings())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FeedbackResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(FeedbackResponse responseResult) {
                        callback.onSuccess(responseResult);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void logOutAPi(String userID) {
        serviceRetrofit.getLogoutAPi(userID);
    }
}
