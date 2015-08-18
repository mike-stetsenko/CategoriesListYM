package com.mairos.categories.data.models;

import nl.qbusict.cupboard.annotation.Index;

/**
 * Created by stetsenko on 14.08.2015.
 */
public class CategoryStorage {

    private Long _id;

    private long parentId;

    private String storeId;

    private String title;

    private boolean childExists;

    public boolean isChildExists() {
        return childExists;
    }

    public Long getId() {
        return _id;
    }

    public long getParentId() {
        return parentId;
    }

    public String getStoreId() {
        return storeId;
    }

    public String getTitle() {
        return title;
    }

    public CategoryStorage() {
    }

    public CategoryStorage(long parentId, String storeId, String title, boolean childExists) {
        this.parentId = parentId;
        this.storeId = storeId;
        this.title = title;
        this.childExists = childExists;
    }
}
