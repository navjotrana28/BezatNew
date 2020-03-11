package com.bezatnew.bezat.utils

class URLS {
    companion object {
        //        const val BASE_PATH = "http://bindu.biz/bezatapi/"
        const val BASE_PATH = "http://bezatapp.com/bezatapi/"
        val OUTER_URL = BASE_PATH+"user/front_banner"
        val LOGIN_URL = BASE_PATH+"user/login"
        val REGISTER_URL = BASE_PATH+"user/register"
        val USER_BANNER = BASE_PATH+"user/banners"
        val LATEST_OFFER = BASE_PATH+"store/saved_offer_list?"
        val CATEGORY_LIST = BASE_PATH+"category/list"
        val VIP_OFFER = BASE_PATH+"user/vip_offer?"
        val GET_WINNER = BASE_PATH+"raffles/winners?year_month="
        val NOTIFICATION_LIST = BASE_PATH+"notification/notification_list?"
        val USER_PROFILE = BASE_PATH+"user/profile?"
        val PROFILE_Edit = BASE_PATH+"user/profile_edit"
        val PAGES_ABOUT = BASE_PATH+"pages/about"
        val PAGES_TERMS = BASE_PATH+"pages/terms_and_condition"
        val PAGES_PRIVACY = BASE_PATH+"pages/privacy_policy"
        val PAGES_CONTACTUS = BASE_PATH+"pages/contact"
        val PAGES_FAQ = BASE_PATH+"pages/faq"
        val PushNotification = BASE_PATH+"user/push_notification_status"
        val CHANGE_PASSWORD = BASE_PATH+"user/change_password"
        val SCAN_HISTORY = BASE_PATH+"raffles/user_monthly_raffles?"
        val PRIZES_LIST = BASE_PATH+"raffles/raffle_list?year_month="
        val PRIZES_DETAILS = BASE_PATH+"raffles/raffle_detail?raffle_id="
        val TOTAL_COUPON = BASE_PATH+"raffles/user_monthly_raffles?"
        val STORE_BY_LOCATION = BASE_PATH+"store/list_store_by_location?"
        val GET_COUNTRY = BASE_PATH+"user/get_country"
        val FORGOT_PASSWORD = BASE_PATH+"user/forgot_password"
        val GET_LOCATION = BASE_PATH+"user/location"
        val STORE_BY_OFFER = BASE_PATH+"store/offers?"
        val OFFER_DETAILS = BASE_PATH+"store/offer_details?"
        val OTP_VALIDATION = BASE_PATH+"user/otp_validation"
        val RESEND_OTP = BASE_PATH+"user/resend_otp";
        val SAVE_OFFER = BASE_PATH+"store/save_offer";
        val REMOVE_SAVE_OFFER = BASE_PATH+"store/remove_saved_offer";
        val FAV_STORE_LIST = BASE_PATH+"store/fav_store_list?userId=";

    }
}