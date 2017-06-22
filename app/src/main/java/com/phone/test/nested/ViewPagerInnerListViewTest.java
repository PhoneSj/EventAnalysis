package com.phone.test.nested;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.phone.test.R;

/**
 * Created by Phone on 2017/5/18.
 */

public class ViewPagerInnerListViewTest extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager_inner_listview);
        ListView listView= (ListView) findViewById(R.id.listView);
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 10;
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                if(view==null){
                    view= LayoutInflater.from(ViewPagerInnerListViewTest.this).inflate(R.layout
                            .item2,null);
                    ViewPager viewPager= (ViewPager) view.findViewById(R.id.viewPager_item);
                    viewPager.setAdapter(new MyPagerAdapter(ViewPagerInnerListViewTest.this,R
                            .layout.item1,3));
                }
                return view;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(ViewPagerInnerListViewTest.this,"点击了第"+i+"项",Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    public void click(View view){
        Toast.makeText(this,"点击时间响应",Toast.LENGTH_SHORT).show();
    }
}
