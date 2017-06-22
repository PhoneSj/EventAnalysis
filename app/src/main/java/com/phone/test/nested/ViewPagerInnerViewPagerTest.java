package com.phone.test.nested;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.phone.test.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phone on 2017/5/18.
 */

public class ViewPagerInnerViewPagerTest extends Activity {

    private List<View> views=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager_inner_viewpager);
        ViewPager viewPager= (ViewPager) findViewById(R.id.viewPager);
        views.clear();
        views.add(generateItem1());
        views.add(generateItem2());
        views.add(generateItem1());
        views.add(generateItem1());
        viewPager.setAdapter(new OnePagerAdapter());
    }

    private View generateItem2() {
        View view=LayoutInflater.from(this).inflate(R.layout.item2,null);
        ViewPager itemViewPager= (ViewPager) view.findViewById(R.id.viewPager_item);
        itemViewPager.setAdapter(new MyPagerAdapter(this,R.layout.item,3));
        return view;
    }

    private View generateItem1() {
        View view= LayoutInflater.from(this).inflate(R.layout.item1,null);
        return  view;
    }

    class OnePagerAdapter extends PagerAdapter{

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
    }

    public void click(View view){
        Toast.makeText(this,"点击事件响应",Toast.LENGTH_SHORT).show();
    }
}
