package com.phone.test.nested;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.phone.test.R;

/**
 * Created by Phone on 2017/5/18.
 */

public class ViewPagerInnerScrollViewTest extends Activity {

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager_inner_scrollview);
        viewPager= (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new MyPagerAdapter(this,R.layout.item,3));
    }

}
