package com.growfxtrade.utils;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface RequestInterface {

//    @GET("symbols?api_key=dSW5dB5k32vksvTu7AYnXzFMdmPL2pEh")
//    Call<ResponseBody> getCurrency();

    @GET("symbols?")
    Call<ResponseBody> getCurrency(@Query("api_key") String api_key
    );

//    @GET("trade/trade_api/withdraw.php")
    @GET("trade_api/withdraw.php")
    Call<ResponseBody> getWithdraw(
            @Query("id") String id,
            @Query("type") String type,
            @Query("amount") String amount
    );

    @GET("trade_api/change_password.php")
    Call<ResponseBody> getChangePassword(
            @Query("email") String email,
            @Query("oldpassword") String oldpassword,
            @Query("password") String passwordrd,
            @Query("confirm_password") String confirm_password
    );

    @GET("trade_api/get_profile.php")
    Call<ResponseBody> getPortfolio(
            @Query("id") String id
    );
    @POST("trade_api/get_pending_wallet.php")
    @FormUrlEncoded
    Call<ResponseBody> get_pending_wallet(
            @Field("id") String id
    );

    @POST("trade_api/get_pending_withdraw.php")
    @FormUrlEncoded
    Call<ResponseBody> get_pending_withdraw(
            @Field("id") String id
    );
    @POST("trade_api/get_traction.php")
    Call<ResponseBody> get_traction(
            @Query("id") String id
    );
    @GET("quotes?")
    Call<ResponseBody> getAddedCurrency(
            @Query("pairs") String pairs,
            @Query("api_key") String api_key
    );

    @POST("trade_api/forgot.php")
    @FormUrlEncoded
    Call<ResponseBody> getforgot(
            @Field("email") String email
    );

//    @POST("trade/trade_api/login.php")
    @POST("trade_api/login.php")
    @FormUrlEncoded
    Call<ResponseBody> getLoginDetails(
            @Field("email") String email,
            @Field("password") String password
    );

   /* @POST("trade_api/registers.php")
    @FormUrlEncoded
    Call<ResponseBody> getREgDetails(
            @Field("mono") String mono,
            @Field("email") String email,
            @Field("gender") String gender,
            @Field("password") String password,
            @Field("user_name") String user_name,
            @Field("city") String city,
            @Field("country") String country,
            @Field("bank_account") String accno,
            @Field("ifsc") String ifsccode,
            @Field("doc_id") String doc_id,
            @Field("doc1") String doc1,
            @Field("doc2") String doc2

    );*/

    @Multipart
    @POST("trade_api/registers.php")
    Call<ResponseBody> getREgDetails(
            @Part MultipartBody.Part image1,
            @Part MultipartBody.Part image2,
            @Query("mono") String mono,
            @Query("email") String email,
            @Query("gender") String gender,
            @Query("password") String password,
            @Query("user_name") String user_name,
            @Query("state") String state,
            @Query("city") String city,
            @Query("country") String country,
            @Query("document_type") String doc_type,
            @Query("bank_name") String bank_name,
            @Query("bank_account_no") String bank_account,
            @Query("ifsc") String ifsc,
            @Query("upi_id") String upi_id,
            @Query("doc_id") String doc_id,
            @Query("doc_no") String doc_no,
            @Query("pan_no") String pancard_no


    );
    @POST("trade_api/generate_otp.php")
    @FormUrlEncoded
    Call<ResponseBody> sendOtp(
            @Field("mobile") String mobile

    );

    @POST("trade_api/resend_otp.php")
    @FormUrlEncoded
    Call<ResponseBody> resendOtp(
            @Field("mobile") String mobile

    );


    @POST("trade_api/update_wallet.php")
    @Multipart
    @FormUrlEncoded
    Call<ResponseBody> addMoney(
            @Part MultipartBody.Part image1,
            @Part("id") RequestBody id,
            @Part("type") RequestBody  type,
            @Part("amount") RequestBody  amount

    );

    @POST("trade_api/contact.php")
    @FormUrlEncoded
    Call<ResponseBody> addContact(
            @Field("name") String name,
            @Field("email") String email,
           // @Field("phone") String phone,
           // @Field("reason") String reason,
            @Field("message") String message

    );

    @POST("trade_api/get_buy_sell.php")
    @FormUrlEncoded
    Call<ResponseBody> getOrderHistory(
            @Field("id") String id
    );
    @POST("trade_api/get_wallet.php")
    @FormUrlEncoded
    Call<ResponseBody> getPrfile(
            @Field("id") String id

    );

    @POST("trade_api/sell.php")
    @FormUrlEncoded
    Call<ResponseBody> getsell(
            @Field("id") String id,
            @Field("qty") String qty
    );
    @POST("trade_api/buy-sell.php")
    @FormUrlEncoded
    Call<ResponseBody> getBuySell(
            @Field("buy_sell") String type,
            @Field("currency_id") String currency_id,
            @Field("amount") String amount,
           /* @Field("qty") String qty,*/
            @Field("total") String total,
            @Field("id") String id,
            @Field("currency_name") String currency_name

    );
//    @POST("login/index.php")
//    @FormUrlEncoded
//    Call<Login> requestLogin(@Field("phone") String email, @Field("password") String password);


}
