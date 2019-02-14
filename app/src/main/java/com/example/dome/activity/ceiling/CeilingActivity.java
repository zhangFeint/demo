package com.example.dome.activity.ceiling;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.dome.R;

public class CeilingActivity extends AppCompatActivity implements CeilingScrollView.OnScrollListener {
    private EditText search_edit;
    private CeilingScrollView myScrollView;
    private int searchLayoutTop;

    LinearLayout search01, search02;
    RelativeLayout rlayout;

    public static void show(Context context){
        context.startActivity(new Intent(context,CeilingActivity.class));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ceiling);
        init();
    }

    private void init() {
        search_edit = findViewById(R.id.search_edit);
        myScrollView = findViewById(R.id.myScrollView);
        search01 = findViewById(R.id.search01);
        search02 = findViewById(R.id.search02);
        rlayout = findViewById(R.id.rlayout);
        myScrollView.setOnScrollListener(this);

    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            searchLayoutTop = rlayout.getBottom();//获取searchLayout的顶部位置
        }
    }

    /**
     * 监听滚动Y值变化，通过addView和removeView来实现悬停效果
     * @param scrollY
     */
    @Override
    public void onScroll(int scrollY) {
        if (scrollY >= searchLayoutTop) {
            if (search_edit.getParent() != search01) {
                search02.removeView(search_edit);
                search01.addView(search_edit);
            }
        } else {
            if (search_edit.getParent() != search02) {
                search01.removeView(search_edit);
                search02.addView(search_edit);
            }
        }
    }
}
