package com.dbcorp.vendorapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by Bhupesh Sen on 25-03-2021.
 */
public class BookingServiceChat implements Serializable {

    @SerializedName("user_id")
    @Expose
    private String user_id;
    @SerializedName("service_chat_booking_id")
    @Expose
    private String service_chat_booking_id;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("vendor_id")
    @Expose
    private String vendor_id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("photo")
    @Expose
    private String photo;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getService_chat_booking_id() {
        return service_chat_booking_id;
    }

    public void setService_chat_booking_id(String service_chat_booking_id) {
        this.service_chat_booking_id = service_chat_booking_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(String vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
