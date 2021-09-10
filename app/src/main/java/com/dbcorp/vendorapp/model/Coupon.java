package com.dbcorp.vendorapp.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Bhupesh Sen on 09-02-2021.
 */
public class Coupon {
    @SerializedName("coupons_id")
    @Expose
    private String couponsId;
    @SerializedName("vendor_id")
    @Expose
    private String vendorId;
    @SerializedName("coupon_code")
    @Expose
    private String couponCode;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("startdate")
    @Expose
    private String startdate;
    @SerializedName("enddate")
    @Expose
    private String enddate;
    @SerializedName("per_user_limit")
    @Expose
    private String perUserLimit;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("add_date")
    @Expose
    private Object addDate;
    @SerializedName("add_by")
    @Expose
    private Object addBy;
    @SerializedName("active")
    @Expose
    private String active;
    @SerializedName("is_approve")
    @Expose
    private String isApprove;

    public String getCouponsId() {
        return couponsId;
    }

    public void setCouponsId(String couponsId) {
        this.couponsId = couponsId;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getPerUserLimit() {
        return perUserLimit;
    }

    public void setPerUserLimit(String perUserLimit) {
        this.perUserLimit = perUserLimit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getAddDate() {
        return addDate;
    }

    public void setAddDate(Object addDate) {
        this.addDate = addDate;
    }

    public Object getAddBy() {
        return addBy;
    }

    public void setAddBy(Object addBy) {
        this.addBy = addBy;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getIsApprove() {
        return isApprove;
    }

    public void setIsApprove(String isApprove) {
        this.isApprove = isApprove;
    }
}

