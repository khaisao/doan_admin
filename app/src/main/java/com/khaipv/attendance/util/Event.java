package com.khaipv.attendance.util;

import android.widget.HorizontalScrollView;

public class Event {

    private final int x;
    private final int y;
    private final HorizontalScrollView view;

    public Event(HorizontalScrollView view, int x, int y) {
        this.x = x;
        this.y = y;
        this.view = view;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public HorizontalScrollView getView() {
        return view;
    }
}
