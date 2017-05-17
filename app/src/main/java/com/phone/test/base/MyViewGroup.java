package com.phone.test.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.phone.test.LogUtils;

/**
 * Created by Phone on 2017/5/17.
 */

public class MyViewGroup extends ViewGroup {

    private static final String TAG="MyViewGroup";
    private static final boolean ENABLE=true;

    public MyViewGroup(Context context) {
        this(context,null);
    }

    public MyViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean b, int left, int top, int right, int bottom) {
        int count=getChildCount();
        int up=0;
        for(int i=0;i<count;i++){
            final View child=getChildAt(i);
            child.layout(left,up,left+300,up+300);
            up+=300;
        }
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
    public boolean onInterceptTouchEvent(MotionEvent event) {
        LogUtils.showI(TAG,"onInterceptTouchEvent",false);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                LogUtils.showI(TAG,"onInterceptTouchEvent----ACTION_DOWN",ENABLE);
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtils.showI(TAG,"onInterceptTouchEvent----ACTION_MOVE",ENABLE);
//                break;
                return true;
            case MotionEvent.ACTION_UP:
                LogUtils.showI(TAG,"onInterceptTouchEvent----ACTION_UP",ENABLE);
                break;
            case MotionEvent.ACTION_CANCEL:
                LogUtils.showI(TAG,"onInterceptTouchEvent----ACTION_CANCEL",ENABLE);
                break;
        }
        return super.onInterceptTouchEvent(event);
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
        return super.onTouchEvent(event);
    }
}
