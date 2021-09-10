package com.dbcorp.vendorapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Bhupesh Sen on 08-02-2021.
 */

public class SubToSubCategory {

    @SerializedName("sub_to_sub_category_id")
    @Expose
    private String subToSubCategoryId;
    @SerializedName("sub_cat_id")
    @Expose
    private String subCatId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("active")
    @Expose
    private String active;

    public String getSubToSubCategoryId() {
        return subToSubCategoryId;
    }

    public void setSubToSubCategoryId(String subToSubCategoryId) {
        this.subToSubCategoryId = subToSubCategoryId;
    }

    public String getSubCatId() {
        return subCatId;
    }

    public void setSubCatId(String subCatId) {
        this.subCatId = subCatId;
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

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

}