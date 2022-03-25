package com.nexsoft.showcaseviewlib.entity;

import android.view.View;

public class GuideModel {
    View view;
    String title;
    String message;
    String linkText;
    Class<?> linkClass;
    boolean gotoNewClassWithButton;


    public GuideModel() {
    }

    public GuideModel(View view, String title, String message) {
        this.view = view;
        this.title = title;
        this.message = message;
    }

    public GuideModel(View view, String title, String message, String linkText) {
        this.view = view;
        this.title = title;
        this.message = message;
        this.linkText = linkText;
    }

    public GuideModel(View view, String title, String message, Class<?> linkClass) {
        this.view = view;
        this.title = title;
        this.message = message;
        this.linkClass = linkClass;
    }

    public GuideModel(View view, String title, String message, Class<?> linkClass, boolean gotoNewClassWithButton) {
        this.view = view;
        this.title = title;
        this.message = message;
        this.gotoNewClassWithButton = true;
        this.linkClass = linkClass;
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

    public Class<?> getLinkClass() {
        return linkClass;
    }

    public void setLinkClass(Class<?> linkClass) {
        this.linkClass = linkClass;
    }

    public String getLinkText() {
        return linkText;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    public boolean isGotoNewClassWithButton() {
        return gotoNewClassWithButton;
    }

    public void setGotoNewClassWithButton(boolean gotoNewClassWithButton) {
        this.gotoNewClassWithButton = gotoNewClassWithButton;
    }
}
