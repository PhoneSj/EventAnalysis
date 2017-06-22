package com.phone.test.nested;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phone on 2017/5/18.
 */

public class MyPagerAdapter extends PagerAdapter {

    private List<View> views;

    public MyPagerAdapter(Context context,int layoutId,int count) {
        views=generateViews(context,layoutId,count);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view=views.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //                super.destroyItem(container, position, object);
        container.removeView(views.get(position));
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    private List<View> generateViews(Context context,int layoutId,int count) {
        List<View> views=new ArrayList<>();
        for(int i=0;i<count;i++){
            View view= LayoutInflater.from(context).inflate(layoutId,null);
            views.add(view);
        }
        return views;
    }
}
