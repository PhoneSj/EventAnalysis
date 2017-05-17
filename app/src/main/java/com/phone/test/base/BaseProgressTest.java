package com.phone.test.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

import com.phone.test.LogUtils;
import com.phone.test.R;

/**
 * Created by Phone on 2017/5/17.
 */

public class BaseProgressTest extends Activity {

    private static final String TAG="Activity";
    private static final boolean ENABLE=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baseprogress);
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
        return super.onTouchEvent(event);
    }
}
