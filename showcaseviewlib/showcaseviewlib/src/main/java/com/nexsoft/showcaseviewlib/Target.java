package com.nexsoft.showcaseviewlib;

import android.graphics.Point;

public interface Target {
    Target NONE = () -> new Point(1000000, 1000000);

    Point getPoint();
}
