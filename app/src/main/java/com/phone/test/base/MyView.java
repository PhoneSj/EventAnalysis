package com.phone.test.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.phone.test.LogUtils;

/**
 * Created by Phone on 2017/5/17.
 */

public class MyView extends View {

    private static final String TAG="MyView";
    private static final boolean ENABLE=true;

    public MyView(Context context) {
        this(context,null);
    }

    public MyView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        LogUtils.showI(TAG,"dispatchTouchEvent",false);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                LogUtils.showI(TAG,"dispatchTouchEvent----ACTION_DOWN",ENABLE);
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtils.showI(TAG,"dispatchTouchEvent----ACTION_MOVE",ENABLE);
                break;
            case MotionEvent.ACTION_UP:
                LogUtils.showI(TAG,"dispatchTouchEvent----ACTION_UP",ENABLE);
                break;
            case MotionEvent.ACTION_CANCEL:
                LogUtils.showI(TAG,"dispatchTouchEvent----ACTION_CANCEL",ENABLE);
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogUtils.showI(TAG,"onTouchEvent",false);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                LogUtils.showI(TAG,"onTouchEvent----ACTION_DOWN",ENABLE);
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtils.showI(TAG,"onTouchEvent----ACTION_MOVE",ENABLE);
                break;
            case MotionEvent.ACTION_UP:
                LogUtils.showI(TAG,"onTouchEvent----ACTION_UP",ENABLE);
                break;
            case MotionEvent.ACTION_CANCEL:
                LogUtils.showI(TAG,"onTouchEvent----ACTION_CANCEL",ENABLE);
                break;
        }
        return true;
    }
}
