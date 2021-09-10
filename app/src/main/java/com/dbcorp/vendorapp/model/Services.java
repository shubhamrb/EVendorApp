package com.dbcorp.vendorapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Bhupesh Sen on 19-03-2021.
 */
public class Services implements Serializable  {

    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("master_category_id")
    @Expose
    private String masterCategoryId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("photo")
    @Expose
    private String photo;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getMasterCategoryId() {
        return masterCategoryId;
    }

    public void setMasterCategoryId(String masterCategoryId) {
        this.masterCategoryId = masterCategoryId;
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

}