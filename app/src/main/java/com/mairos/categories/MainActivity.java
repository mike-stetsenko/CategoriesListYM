package com.mairos.categories;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mairos.categories.data.Storage;
import com.mairos.categories.data.models.CategoryStorage;
import com.mairos.categories.events.CategoriesLoadedEvent;
import com.mairos.categories.network.CategoriesRequest;
import com.mairos.categories.network.CategoriesSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import de.greenrobot.event.EventBus;

@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.menu_main)
public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback {

    public static final String ROOT_CATEGORY_ID = "0";
    private SpiceManager mSpiceManager = new SpiceManager(CategoriesSpiceService.class);

    @ViewById(R.id.progressBarLoading)
    LinearLayout mProgressBarLoading;

    @InstanceState
    boolean mLoadingInProgress = false;

    @SystemService
    ConnectivityManager mConnectivityManager;

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

    private void startShowLoading(){
        mLoadingInProgress = true;
        mProgressBarLoading.setVisibility(View.VISIBLE);
    }
    private void stopShowLoading(){
        mLoadingInProgress = false;
        mProgressBarLoading.setVisibility(View.GONE);
    }

    public void onEventMainThread(CategoriesLoadedEvent event) {
        stopShowLoading();
        Toast.makeText(this, event.message, Toast.LENGTH_LONG).show();

        if (event.success) {
            clearFragments();
            showCategory(ROOT_CATEGORY_ID);
        }
    }

    @UiThread
    protected void clearFragments(){
        FragmentManager fm = getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    @OptionsItem(R.id.action_update)
    void actionUpdate() {

        NetworkInfo netInfo = mConnectivityManager.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()){
            CategoriesRequest request = new CategoriesRequest();
            request.setRetryPolicy(null);

            mSpiceManager.execute(request, 0, DurationInMillis.ALWAYS_EXPIRED, null);

            startShowLoading();
        } else {

            Toast.makeText(this, R.string.message_no_network_found, Toast.LENGTH_LONG).show();
        }
    }

    @UiThread
    protected void showCategory(String id){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.container, MainActivityFragment.newInstance(id), MainActivityFragment.TAG);
        if (!id.equals(ROOT_CATEGORY_ID)){
            ft.addToBackStack(id);
        }
        ft.commit();
    }

    @AfterViews
    protected void init(){
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                getSupportFragmentManager().findFragmentById(R.id.container).onResume();
            }
        });

        if(Storage.get().get(CategoryStorage.class).isEmpty()){
            actionUpdate();
        } else {
            if (getSupportFragmentManager().findFragmentById(R.id.container) == null) {
                showCategory(ROOT_CATEGORY_ID);
            }
        }

        if (mLoadingInProgress){
            startShowLoading();
        }
    }

    @OptionsItem(android.R.id.home)
    void onUpClick(){
        onBackPressed();
    }

    @Override
    public void onBackPressed(){
        if (getSupportFragmentManager().getBackStackEntryCount() == 0){
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onShowCategory(String categoryId) {
        ActionBar aBar = getSupportActionBar();
        if (aBar != null) {
            if (categoryId.equals(ROOT_CATEGORY_ID)) {
                aBar.setTitle(R.string.title_categories);
                aBar.setDisplayHomeAsUpEnabled(false);
            } else {
                List<CategoryStorage> category = Storage.get().
                        getWithSelection(CategoryStorage.class, "_id = ?", categoryId);
                if (!category.isEmpty()) {
                    aBar.setTitle(category.get(0).getTitle());
                }
                aBar.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    @Override
    public void onSelectCategory(long categoryId) {
        showCategory(Long.toString(categoryId));
    }
}