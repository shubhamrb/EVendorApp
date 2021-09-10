package com.dbcorp.vendorapp.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OTP {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}