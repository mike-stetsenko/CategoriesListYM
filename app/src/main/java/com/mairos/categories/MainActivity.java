package com.mairos.categories;

import android.support.v7.app.AppCompatActivity;

import com.mairos.categories.data.Storage;
import com.mairos.categories.data.models.CategoryStorage;
import com.mairos.categories.events.CategoriesLoadedEvent;
import com.mairos.categories.network.CategoriesRequest;
import com.mairos.categories.network.CategoriesSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;

import de.greenrobot.event.EventBus;

@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.menu_main)
public class MainActivity extends AppCompatActivity {

    private SpiceManager mSpiceManager = new SpiceManager(CategoriesSpiceService.class);

    @Override
    protected void onStart() {
        mSpiceManager.start(this);
        EventBus.getDefault().registerSticky(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        mSpiceManager.shouldStop();
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void onEventMainThread(CategoriesLoadedEvent event) {
        Storage.get().getWithSelection(CategoryStorage.class, "parentId = ?", "0");
    }

    @OptionsItem(R.id.action_update)
    void actionUpdate() {
        CategoriesRequest request = new CategoriesRequest();

        mSpiceManager.execute(request, 0, DurationInMillis.ALWAYS_EXPIRED, null);
    }
}
