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

    public CategoryStorage() {
    }

    public CategoryStorage(long parentId, String storeId, String title) {
        this.parentId = parentId;
        this.storeId = storeId;
        this.title = title;
    }
}
