package com.dbcorp.vendorapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Bhupesh Sen on 25-04-2021.
 */
public class ServicePackagePoint implements Serializable {

    @SerializedName("package_detail_id")
    @Expose
    private String packageDetailId;
    @SerializedName("detail")
    @Expose
    private String detail;
    @SerializedName("active")
    @Expose
    private String active;
    @SerializedName("package_id")
    @Expose
    private String packageId;

    public String getPackageDetailId() {
        return packageDetailId;
    }

    public void setPackageDetailId(String packageDetailId) {
        this.packageDetailId = packageDetailId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

}