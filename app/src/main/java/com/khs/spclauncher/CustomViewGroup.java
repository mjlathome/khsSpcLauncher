package com.khs.spclauncher;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;

/**
 * Created by Mark on 3/31/2016.
 * see:
 * https://gist.github.com/sarme/7e4dc90e2478ade310e6
 */
public class CustomViewGroup extends ViewGroup {

    public CustomViewGroup(Context context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i("customViewGroup", "**********Intercepted");
        return true;
    }
}
