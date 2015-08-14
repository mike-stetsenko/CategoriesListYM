package com.mairos.categories.network;

import com.mairos.categories.network.models.CategoryNetwork;

import java.util.List;

import retrofit.http.GET;

/**
 * Created by Mike on 06.01.2015.
 */

public interface MoneyYandexApi {

    public static final String API_URL = "https://money.yandex.ru";

    @GET("/api/categories-list")
    List<CategoryNetwork> getCategories();
}
