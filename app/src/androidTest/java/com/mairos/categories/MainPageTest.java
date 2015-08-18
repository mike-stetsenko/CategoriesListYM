package com.mairos.categories;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import com.mairos.categories.network.RequestStatusObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.registerIdlingResources;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static com.android.support.test.deps.guava.base.Preconditions.checkNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainPageTest extends ActivityInstrumentationTestCase2<MainActivity_> {

    private MainActivity_ mActivity;
    private RequestStatusIdlingResource mRequestIR;

    public MainPageTest() {
        super(MainActivity_.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();

        // in order to synchronize
        RequestStatusObject rso = RequestStatusObject.getInstance();
        mRequestIR = new RequestStatusIdlingResource(rso);
        registerIdlingResources(mRequestIR);

        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();
    }

    @Test
    public void testActivityExists() {
        assertThat(mActivity, notNullValue());
    }

    @Test
    public void testListNavigation() {

        mRequestIR.lock();

        onView(ViewMatchers.withText("Телефон")).perform(click());

        onView(isRoot()).perform(pressBack());
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    private class RequestStatusIdlingResource implements IdlingResource,
            RequestStatusObject.RequestStatusChange {
        private RequestStatusObject mState;
        private ResourceCallback mCallback;

        public RequestStatusIdlingResource(RequestStatusObject status) {
            mState = checkNotNull(status);
        }

        @Override
        public String getName() {
            return "is request finished";
        }

        @Override
        public boolean isIdleNow() {
            return !mState.getState().equals(RequestStatusObject.IN_PROCESS);
        }

        @Override
        public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
            mCallback = resourceCallback;
            mState.setResourceCallback(this);
        }

        @Override
        public void onChange() {
            mCallback.onTransitionToIdle();
        }


        public void lock() {
            mState.setStarted();
        }
    }
}
