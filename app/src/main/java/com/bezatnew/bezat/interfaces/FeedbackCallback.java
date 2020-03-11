package com.bezatnew.bezat.interfaces;

import com.bezatnew.bezat.api.feedbackResponse.FeedbackResponse;

public interface FeedbackCallback {

    void onSuccess(FeedbackResponse responseResult);

    void onFailure(Throwable e);

}
