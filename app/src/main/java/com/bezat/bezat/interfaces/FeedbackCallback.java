package com.bezat.bezat.interfaces;

import com.bezat.bezat.api.feedbackResponse.FeedbackResponse;

public interface FeedbackCallback {

    void onSuccess(FeedbackResponse responseResult);

    void onFailure(Throwable e);

}
