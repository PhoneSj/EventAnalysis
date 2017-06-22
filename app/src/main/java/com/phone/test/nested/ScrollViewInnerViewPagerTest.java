package com.phone.test.nested;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.phone.test.R;

/**
 * Created by Phone on 2017/5/18.
 */

public class ScrollViewInnerViewPagerTest extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollview_inner_viewpager);
        ViewPager viewPager= (ViewPager) findViewById(R.id.viewPager_wrap);
        viewPager.setAdapter(new MyPagerAdapter(this,R.layout.item1,3));
    }

    public void click(View view){
        Toast.makeText(this,"点击事件响应",Toast.LENGTH_SHORT).show();
    }
}
