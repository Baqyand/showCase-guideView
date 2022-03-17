package com.nexsoft.showcaseviewlib;

import android.app.Activity;
import android.graphics.Point;
import android.view.View;

public class ViewTargets implements Target{
    private final View mView;

    public ViewTargets(View view) {
        mView = view;
    }

    public ViewTargets(int viewId, Activity activity) {
        mView = activity.findViewById(viewId);
    }

    @Override
    public Point getPoint() {
        int[] location = new int[2];
        mView.getLocationInWindow(location);
        int x = location[0] + mView.getWidth() / 2;
        int y = location[1] + mView.getHeight() / 2;
        return new Point(x, y);
    }
}
