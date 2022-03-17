package com.dbcorp.vendorapp.network;


import com.dbcorp.vendorapp.model.CategoryResponse;
import com.dbcorp.vendorapp.model.LoginDetails;
import com.dbcorp.vendorapp.model.OTP;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;


public interface ApiService {

//    String SHOP_IMG_URL = "http://top10india.in/upload/shop/";
//    String BASE_URL = "http://top10india.in/api/v1/vendor-api/";
//    String IMG_PRODUCT_URL = "http://top10india.in/upload/product/";

    String SHOP_IMG_URL = "https://visionotrans.com/backend/upload/shop/";
    String BASE_URL = "https://visionotrans.com/backend/api/v1/vendor-api/";
    String IMG_PRODUCT_URL = "https://visionotrans.com/backend/upload/product/";
    //https://visionotrans.com

    String APP_DEVICE_ID = "1234";

    String AppType = "( LIVE )";//LIVE

    //------------Login Api--------
    @FormUrlEncoded
    @POST(Constants.GET_OTP)
    Call<OTP> getOtp(@FieldMap Map<String, String> uData);


    //------------Register Api--------


    //------------Get Category Api--------
    @FormUrlEncoded
    @POST(Constants.GET_CATEGORY)
    Call<CategoryResponse> getCategory(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);


    @FormUrlEncoded
    @POST(Constants.GET_SUB_CAT)
    Call<String> getSubCategory(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);


    @FormUrlEncoded
    @POST(Constants.GET_CATEGORY)
    Call<CategoryResponse> getSubToSubCategory(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);


    @FormUrlEncoded
    @POST(Constants.GET_PRODUCT)
    Call<String> getProdct(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);


    // get vendor product
    @FormUrlEncoded
    @POST(Constants.GET_WARE_HOUSE_PRODUCT)
    Call<String> getWareHouseProduct(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);


    @FormUrlEncoded
    @POST(Constants.GET_HOME)
    Call<String> getHome(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);

    @FormUrlEncoded
    @POST(Constants.CHANGE_SHOP_STATUS)
    Call<String> changeShopStatus(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);


    @FormUrlEncoded
    @POST(Constants.GET_LOGIN)
    Call<LoginDetails> loginUser(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);


    @FormUrlEncoded
    @POST(Constants.ADD_OFFER)
    Call<String> addHome(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);


    //------------start offer------------
    @FormUrlEncoded
    @POST(Constants.GET_OFFER)
    Call<String> getOffer(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);

    @FormUrlEncoded
    @POST(Constants.DELETE_OFFER)
    Call<String> offerDelete(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);


    @FormUrlEncoded
    @POST(Constants.UPDATE_OFFER)
    Call<String> offerUpdate(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);


    //------------end offer------------

    //--------start Coupon-------------


    @FormUrlEncoded
    @POST(Constants.ADD_OCCUPON)
    Call<String> addCoupon(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);


    @FormUrlEncoded
    @POST(Constants.DELETE_OCCUPON)
    Call<String> deleteCoupon(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);


    @FormUrlEncoded
    @POST(Constants.GET_SUB_CATEGORY)
    Call<String> getSubtoSubCategory(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);


    @FormUrlEncoded
    @POST(Constants.Add_Message)
    Call<String> addMessage(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);


    //get order details
    @FormUrlEncoded
    @POST(Constants.GET_ORDER)
    Call<String> getOrder(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);


    // show order Details
    @FormUrlEncoded
    @POST(Constants.GET_ORDER_DETAILS)
    Call<String> getOrderDetails(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);


    @FormUrlEncoded
    @POST(Constants.GET_COUPON)
    Call<String> getCoupon(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);


    @FormUrlEncoded
    @POST(Constants.GET_CATEGORY)
    Call<String> getCategory(@Header("DID") String did, @FieldMap Map<String, String> uData);


    @FormUrlEncoded
    @POST(Constants.GET_CITY)
    Call<String> getCity(@Header("DID") String did, @FieldMap Map<String, String> uData);


    @FormUrlEncoded
    @POST(Constants.GET_SERVICE)
    Call<String> getServiceCat(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);

    @FormUrlEncoded
    @POST(Constants.GET_VENDOR_SERVICE)
    Call<String> getVendorService(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);


    // add service

    @FormUrlEncoded
    @POST(Constants.ADD_VENDOR_SERVICE)
    Call<String> addVendorService(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);

    @FormUrlEncoded
    @POST(Constants.CHANGE_SERVICE_STATUS)
    Call<String> changeStatus(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);


    @FormUrlEncoded
    @POST(Constants.GET_BOOKING_SERVICE)
    Call<String> getBookingService(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);


    @FormUrlEncoded
    @POST(Constants.ADD_PRODUCT)
    Call<String> addProduct(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);

    @FormUrlEncoded
    @POST(Constants.ADD_PRICE_CHANGE)
    Call<String> changePrice(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);

    @FormUrlEncoded
    @POST(Constants.PRODUCT_REQUEST)
    Call<String> productRequest(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);


    // get vendor product
    @FormUrlEncoded
    @POST(Constants.GET_VENDOR_PRODUCT)
    Call<String> getVendorProduct(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);

    //product on off
    @FormUrlEncoded
    @POST(Constants.PRODUCT_SWITCH)
    Call<String> productStatus(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);

    @FormUrlEncoded
    @POST(Constants.CHECK_BOX_PRODUCT)
    Call<String> checkBoxProduct(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);


    @FormUrlEncoded
    @POST(Constants.ORDER_ACCEPT)
    Call<String> orderAccept(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);


    @FormUrlEncoded
    @POST(Constants.GET_Booking)
    Call<String> getBooking(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);


    @FormUrlEncoded
    @POST(Constants.My_Chat)
    Call<String> getMyChat(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);


    @FormUrlEncoded
    @POST(Constants.My_LAST_Chat)
    Call<String> getLastMessage(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);


    @FormUrlEncoded
    @POST(Constants.ADD_REGISTER)
    Call<String> addRegister(@Header("DID") String did, @FieldMap Map<String, String> uData);


    //get order details
    @FormUrlEncoded
    @POST(Constants.GET_DRIVER)
    Call<String> getDriverList(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);


    @FormUrlEncoded
    @POST(Constants.ORDER_ASSIGN_TO_DRIVER)
    Call<String> orderAssign(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);

    @FormUrlEncoded
    @POST(Constants.PLAN_SERVICE)
    Call<String> getServicePlan(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);


    @FormUrlEncoded
    @POST(Constants.UPDATE_VENDOR_SETTING)
    Call<String> updateVendorSetting(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);

    @FormUrlEncoded
    @POST(Constants.VENDOR_SETTING)
    Call<String> getVendorSetting(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);


    @FormUrlEncoded
    @POST(Constants.SUBSCRIBE_PLAN)
    Call<String> subScribePlan(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);

    @FormUrlEncoded
    @POST(Constants.CHECK_PLAN_STATUS)
    Call<String> checkPlanStatus(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);


    @FormUrlEncoded
    @POST(Constants.UPDATE_FCM)
    Call<String> updateFCM(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);


    @FormUrlEncoded
    @POST(Constants.USER_LOGOUT)
    Call<String> logoutUser(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);


    @FormUrlEncoded
    @POST(Constants.UPDATE_NOTIFICATION)
    Call<String> updateNotification(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);


    @FormUrlEncoded
    @POST(Constants.GET_WALLET)
    Call<String> getWallet(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);


    @FormUrlEncoded
    @POST(Constants.Add_Payment_Request)
    Call<String> addPaymentRequest(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);


    @FormUrlEncoded
    @POST(Constants.Get_Payment_Request)
    Call<String> getPaymentRequest(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);

    @FormUrlEncoded
    @POST(Constants.Get_Product_Request)
    Call<String> getProductRequests(@Header("DID") String did, @Header("SK") String SK, @FieldMap Map<String, String> uData);


}
