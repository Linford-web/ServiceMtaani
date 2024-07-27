package com.example.smtaani.models;

public class dashModel {

    private int imageId;
    private String title;
    private Class<?> activityClass;

    public dashModel(int imageId, String title, Class<?> activityClass) {
        this.imageId = imageId;
        this.title = title;
        this.activityClass = activityClass;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Class<?> getActivityClass() {
        return activityClass;
    }

    public void setActivityClass(Class<?> activityClass) {
        this.activityClass = activityClass;
    }
}

