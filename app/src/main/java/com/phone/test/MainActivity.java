package com.phone.test;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.phone.test.base.BaseProgressTest;

public class MainActivity extends ListActivity {

    private String titles[]={"基本传递流程","clock","bezier","ball","loading","float","test"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getListView().setAdapter(new ArrayAdapter<String>(this,android.R.layout
                .simple_list_item_1,titles));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent=null;
        switch (position){
            case 0:
                    intent=new Intent(this, BaseProgressTest.class);
                break;
//            case 1:
//                intent=new Intent(this, ClockTest.class);
//                break;
//            case 2:
//                intent=new Intent(this, BezierTest.class);
//                break;
//            case 3:
//                intent=new Intent(this, BallTest.class);
//                break;
//            case 4:
//                intent=new Intent(this, LoadingTest.class);
//                break;
//            case 5:
//                intent=new Intent(this, FloatText.class);
//                break;
//            case 6:
//                intent=new Intent(this, TestActivity.class);
//                break;
        }
        if(intent!=null){
            startActivity(intent);
        }
    }
}
