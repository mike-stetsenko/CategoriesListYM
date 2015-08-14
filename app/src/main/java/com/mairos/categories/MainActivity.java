package com.mairos.categories;

import android.support.v7.app.AppCompatActivity;

import com.mairos.categories.network.CategoriesRequest;
import com.mairos.categories.network.CategoriesSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;

@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.menu_main)
public class MainActivity extends AppCompatActivity {

    private SpiceManager mSpiceManager = new SpiceManager(CategoriesSpiceService.class);

    @Override
    protected void onStart() {
        mSpiceManager.start(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        mSpiceManager.shouldStop();
        super.onStop();
    }

    @OptionsItem(R.id.action_update)
    void actionUpdate() {
        CategoriesRequest request = new CategoriesRequest();

        mSpiceManager.execute(request, 0, DurationInMillis.ALWAYS_EXPIRED, null);
    }
}
