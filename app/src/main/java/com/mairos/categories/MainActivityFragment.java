package com.mairos.categories;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mairos.categories.data.Storage;
import com.mairos.categories.data.models.CategoryStorage;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EFragment(R.layout.fragment_main)
public class MainActivityFragment extends Fragment {

    public interface Callback {
        void onShowCategory(String categoryId);
        void onSelectCategory(long categoryId);
    }

    public static final String TAG = "MainActivityFragment";

    @ViewById(R.id.list_categories)
    protected ListView mCategoryListView;

    private CategoryArrayAdapter mAdapter;

    @InstanceState
    @FragmentArg
    protected String mCategoryIdArg;

    public static MainActivityFragment newInstance(String categoryId) {
        return MainActivityFragment_.builder()
                .mCategoryIdArg(categoryId)
                .build();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getActivity() instanceof Callback){
            ((Callback) getActivity()).onShowCategory(mCategoryIdArg);
        }
    }

    @AfterViews
    protected void init() {
        List<CategoryStorage> categories =
                Storage.get().getWithSelection(CategoryStorage.class, "parentId = ?", mCategoryIdArg);

        mAdapter = new CategoryArrayAdapter(categories.toArray(new CategoryStorage[categories.size()]));

        mCategoryListView.setAdapter(mAdapter);

        mCategoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mAdapter.getItem(position).isChildExists() && getActivity() instanceof Callback){
                    ((Callback) getActivity()).onSelectCategory(mAdapter.getItem(position).getId());
                }
            }
        });
    }

    private class CategoryArrayAdapter extends ArrayAdapter<CategoryStorage> {

        public CategoryArrayAdapter(CategoryStorage[] values) {
            super(getActivity(), R.layout.row_category, values);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View rowView = convertView;
            CategoryHolder categoryHolder;

            if (rowView == null) {
                categoryHolder = new CategoryHolder();
                rowView = LayoutInflater.from(getActivity()).inflate(R.layout.row_category, parent, false);
                categoryHolder.mId = (TextView) rowView.findViewById(R.id.category_name);
                categoryHolder.mName = (TextView) rowView.findViewById(R.id.category_id);
                categoryHolder.mMore = (ImageView) rowView.findViewById(R.id.more_icon);
                rowView.setTag(categoryHolder);
            } else {
                categoryHolder = (CategoryHolder) rowView.getTag();
            }

            CategoryStorage currentCategory =  getItem(position);

            categoryHolder.mName.setText(currentCategory.getTitle());
            categoryHolder.mId.setText(currentCategory.getStoreId());
            categoryHolder.mMore.setVisibility(currentCategory.isChildExists() ?
                                                        View.VISIBLE : View.INVISIBLE);

            return rowView;
        }
    }

    private class CategoryHolder {
        private ImageView mMore;
        private TextView mId;
        private TextView mName;
    }
}
