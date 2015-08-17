package com.mairos.categories.network.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by stetsenko on 14.08.2015.
 */
public class CategoryNetwork {
    @SerializedName("title")
    private String title;

    @SerializedName("id")
    private String id;

    @SerializedName("subs")
    private List<CategoryNetwork> categories;

    public List<CategoryNetwork> getCategories() {
        return categories;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
