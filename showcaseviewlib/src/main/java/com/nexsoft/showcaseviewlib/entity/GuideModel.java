package com.nexsoft.showcaseviewlib.entity;

import android.view.View;

public class GuideModel {
    View view;
    String title;
    String message;


    public GuideModel() {
    }

    public GuideModel(View view, String title, String message) {
        this.view = view;
        this.title = title;
        this.message = message;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
