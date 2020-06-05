package com.bezatnew.bezat.interfaces;

import com.bezatnew.bezat.models.fav_offers_responses.FavOffersResponse;

public interface FavOffersListInterface {

    void onSuccess(FavOffersResponse responseResult);

    void onFailure(Throwable e);
}
