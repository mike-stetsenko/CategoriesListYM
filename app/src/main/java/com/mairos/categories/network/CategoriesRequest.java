package com.mairos.categories.network;

import com.mairos.categories.CategoriesApplication;
import com.mairos.categories.R;
import com.mairos.categories.data.Storage;
import com.mairos.categories.data.models.CategoryStorage;
import com.mairos.categories.events.CategoriesLoadedEvent;
import com.mairos.categories.network.models.CategoryNetwork;
import com.octo.android.robospice.request.SpiceRequest;

import java.util.List;

import de.greenrobot.event.EventBus;
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
            CategoriesApplication app = CategoriesApplication.getInstance();

            try {
                List<CategoryNetwork> categories = retrofitService.getCategories();
                Storage.get().clearTable(CategoryStorage.class);

                addCategoryToDB(0, categories);

                EventBus.getDefault().postSticky(new CategoriesLoadedEvent(true,
                        app.getString(R.string.message_success_loaded)));
            } catch (Exception e){

                EventBus.getDefault().postSticky(new CategoriesLoadedEvent(false,
                        app.getString(R.string.message_not_loaded) + ": " + e.getMessage()));
                throw e;
            }

            return null;
    }

    private void addCategoryToDB(long parentId, List<CategoryNetwork> nodes){
        CategoryStorage nodeToDB;
        long newParentId;
        for(CategoryNetwork node : nodes) {
            nodeToDB = new CategoryStorage(parentId, node.getId(), node.getTitle(), node.getCategories() != null);
            newParentId = Storage.get().put(nodeToDB);
            if (node.getCategories() != null){
                addCategoryToDB(newParentId, node.getCategories());
            }
        }
    }
}
