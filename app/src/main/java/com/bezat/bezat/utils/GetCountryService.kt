package com.bezat.bezat.utils

import com.bezat.bezat.ServiceRetrofit
import com.bezat.bezat.models.CountryData
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class GetCountryService {

    private val compositeDisposable = CompositeDisposable()
    private val retrofit: Retrofit? = Retrofit.Builder()
        .baseUrl("http://Bezatapp.com/bezatapi/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
    private var serviceRetrofit: ServiceRetrofit? = retrofit?.create(ServiceRetrofit::class.java)

    fun getCountries(onSuccess: (countryData: CountryData) -> Unit, onError: () -> Unit) {
        serviceRetrofit?.countryResponse?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : Observer<CountryData> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onNext(t: CountryData) {
                    onSuccess.invoke(t)
                }

                override fun onError(e: Throwable) {
                    onError.invoke()
                }

                override fun onComplete() {
                    compositeDisposable.clear()
                }
            })
    }
}