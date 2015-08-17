package com.mairos.categories.events;

/**
 * Created by Mike on 18.08.2015.
 */
public class CategoriesLoadedEvent {
    public boolean success = true;
    public String message = "";

    public CategoriesLoadedEvent(boolean success, String message) {
        this.message = message;
        this.success = success;
    }
}
