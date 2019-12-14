package com.bezat.bezat.api.feedbackResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeedbackRequest {

    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("feedback")
    @Expose
    private String feedback;
    @SerializedName("retailerId")
    @Expose
    private String retailerId;
    @SerializedName("ratings")
    @Expose
    private String ratings;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(String retailerId) {
        this.retailerId = retailerId;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

}
