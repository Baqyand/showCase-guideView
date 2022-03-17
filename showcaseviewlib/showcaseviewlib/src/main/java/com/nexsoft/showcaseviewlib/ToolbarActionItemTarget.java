package com.nexsoft.showcaseviewlib;

import android.graphics.Point;
import androidx.appcompat.widget.Toolbar;


import androidx.annotation.IdRes;

public class ToolbarActionItemTarget implements Target{
    private final Toolbar toolbar;
    private final int menuItemId;

    public ToolbarActionItemTarget(Toolbar toolbar, @IdRes int itemId) {
        this.toolbar = toolbar;
        this.menuItemId = itemId;
    }

    @Override
    public Point getPoint() {
        return new ViewTargets(toolbar.findViewById(menuItemId)).getPoint();
    }

}
