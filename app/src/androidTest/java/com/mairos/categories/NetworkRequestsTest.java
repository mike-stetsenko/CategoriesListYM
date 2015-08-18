package com.mairos.categories;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.MediumTest;

import com.mairos.categories.network.MoneyYandexApi;
import com.mairos.categories.network.models.CategoryNetwork;

import org.junit.After;
import org.junit.Before;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import retrofit.RestAdapter;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
@MediumTest
public class NetworkRequestsTest {

    private MoneyYandexApi mRetrofitService;
    private RestAdapter mRestAdapter;

    @Before
    public void setUp(){
        mRestAdapter = new RestAdapter.Builder()
                .setEndpoint(MoneyYandexApi.API_URL)
                .build();

        mRetrofitService = mRestAdapter.create(MoneyYandexApi.class);
    }

    @Test
    public void CheckCategoriesRequest() {

        List<CategoryNetwork> categories = mRetrofitService.getCategories();

        assertThat("no categories - parsing is OK, but no data ... strange", categories.size(), not(0));
    }

    @After
    public void tearDown() {
        // Make sure that we call the tearDown() method of ActivityInstrumentationTestCase2
        // to clean up and not leak any objects.
    }
}