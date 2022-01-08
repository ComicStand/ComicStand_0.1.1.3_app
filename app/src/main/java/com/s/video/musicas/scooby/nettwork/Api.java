package com.s.video.musicas.scooby.nettwork;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import com.s.video.musicas.scooby.nettwork.model.AddFriendModel;
import com.s.video.musicas.scooby.nettwork.model.CheckVideoSubscriptionModel;
import com.s.video.musicas.scooby.nettwork.model.LiveUserModel;
import com.s.video.musicas.scooby.nettwork.model.LoginModel;
import com.s.video.musicas.scooby.nettwork.model.ModelClassData;
import com.s.video.musicas.scooby.nettwork.model.NewsFeedMdel;
import com.s.video.musicas.scooby.nettwork.model.OtpModel;
import com.s.video.musicas.scooby.nettwork.model.PaymentModel;
import com.s.video.musicas.scooby.nettwork.model.PremiumVideoModel;
import com.s.video.musicas.scooby.nettwork.model.SocialContactModel;
import com.s.video.musicas.scooby.nettwork.model.UpdateProfileModel;
import com.s.video.musicas.scooby.nettwork.model.UserDeatailsModel;
import com.s.video.musicas.scooby.nettwork.model.VideoLikeModel;
import com.s.video.musicas.scooby.nettwork.model.getLikeStatusModel;

public interface Api {
    @FormUrlEncoded
    @POST("user_signup")
    Single<OtpModel> register(@Field("name") String name, @Field("email") String email, @Field("mobile") String mobile, @Field("password") String password, @Field("photo") String photo);


    @FormUrlEncoded
    @POST("send_otp")
    Single<OtpModel> sendOtp(@Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("otp_verify")
    Single<OtpModel> otpVery(@Field("mobile") String mobile, @Field("otp") String otp);

    @FormUrlEncoded
    @POST("update_password")
    Single<OtpModel> updatePassword(@Field("mobile") String mobile, @Field("password") String password);


    @FormUrlEncoded
    @POST("user_login")
    Single<LoginModel> login(@Field("mobile") String mobile, @Field("password") String password);



    @FormUrlEncoded
    @POST("get_video")
    Single<PremiumVideoModel> getVideoList(@Field("user_id")String userID);


    @FormUrlEncoded
    @POST("check_video")
    Single<CheckVideoSubscriptionModel> chekVideoSuScription(@Field("user_id") String userID, @Field("video_id") String video_id);


    @FormUrlEncoded
    @POST("buy_video")
    Single<PaymentModel> sendPayment(@Field("user_id") String userID, @Field("video_id") String video_id, @Field("payment_status")String payment_status,@Field("key")String tID,@Field("amount")String amount);



    @FormUrlEncoded
    @POST("get_all_user")
    Single<SocialContactModel> getSocialContactList(@Field("user_id")String userID);



    @FormUrlEncoded
    @POST("upload_profile")
    Single<UpdateProfileModel> updateProfile(@Field("user_id")String userID,@Field("profile")String prfile);


    @FormUrlEncoded
    @POST("user_detail")
    Single<UserDeatailsModel> userProfiledatils(@Field("user_id")String signup_id);



    @POST("news")
    Single<NewsFeedMdel> getNewsFeed();


    @FormUrlEncoded
    @POST("add_frnds")
    Single<AddFriendModel> addFriend(@Field("frnd_id")String frnd_id,@Field("user_id")String user_id);


    @FormUrlEncoded
    @POST("dislike")
    Single<VideoLikeModel> sendLikeDislike(@Field("user_id")String user_id, @Field("video_id")String video_id,@Field("type")String type);



    @FormUrlEncoded
    @POST("like_dislike")
    Single<getLikeStatusModel> getLikeStatus(@Field("user_id")String user_id, @Field("video_id")String video_id);

    @FormUrlEncoded
    @POST("deviceToken")
    Single<ModelClassData> sendDeviceToken(@Field("user_id")String user_id, @Field("token")String token);



    @POST("user_group_list")
    Single<ModelClassData> getGroupLIst();


    @FormUrlEncoded
    @POST("vedio_by_user")
    Single<LiveUserModel> getUserList(@Field("vedio_id")String vedio_id);

    @FormUrlEncoded
    @POST("invitation")
    Single<ModelClassData> sendInvitation(@Field("user_id")String user_id, @Field("link") String link,@Field("video_id") String video_id, @Field("video_title") String video_title);


}

