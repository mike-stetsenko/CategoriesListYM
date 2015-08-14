package com.mairos.categories.network;

import com.mairos.categories.network.models.CategoryNetwork;
import com.octo.android.robospice.request.SpiceRequest;

import java.util.List;

import retrofit.RestAdapter;

/**
 * Created by stetsenko on 18.02.2015.
 */
public class CategoriesRequest extends SpiceRequest<Void> {

    public CategoriesRequest() {
        super(Void.class);
    }

    @Override
    public Void loadDataFromNetwork() throws Exception {

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(MoneyYandexApi.API_URL)
                    .build();

            MoneyYandexApi retrofitService = restAdapter.create(MoneyYandexApi.class);

            List<CategoryNetwork> categories = retrofitService.getCategories();

            return null;
    }
}
