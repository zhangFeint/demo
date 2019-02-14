package com.example.dome.activity.image;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.donkingliang.imageselector.model.ImageModel;
import com.donkingliang.imageselector.view.SquareImageView;
import com.example.dome.R;

import java.util.ArrayList;
import java.util.List;

public class ImageCollectionActivity extends AppCompatActivity {
    private NineGridTestLayout layoutNineGrid;

    public static void show(Context context){
        context.startActivity(new Intent(context,ImageCollectionActivity.class));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_collection);
        layoutNineGrid =  findViewById(R.id.layout_nine_grid);
        final ArrayList list = new ArrayList();

        list.add("http://d.hiphotos.baidu.com/image/h%3D200/sign=201258cbcd80653864eaa313a7dca115/ca1349540923dd54e54f7aedd609b3de9c824873.jpg");
        list.add("http://d.hiphotos.baidu.com/image/h%3D200/sign=ea218b2c5566d01661199928a729d498/a08b87d6277f9e2fd4f215e91830e924b999f308.jpg");
        list.add("http://img.bimg.126.net/photo/ZZ5EGyuUCp9hBPk6_s4Ehg==/5727171351132208489.jpg");
        list.add("http://d.hiphotos.baidu.com/image/h%3D200/sign=ea218b2c5566d01661199928a729d498/a08b87d6277f9e2fd4f215e91830e924b999f308.jpg");
        list.add("http://img.bimg.126.net/photo/ZZ5EGyuUCp9hBPk6_s4Ehg==/5727171351132208489.jpg");
        layoutNineGrid.setIsShowAll(false);
        layoutNineGrid.setColumnColumns(4);
        layoutNineGrid.setUrlList(list);
        layoutNineGrid.setonClickImageListener(new NineGridTestLayout.OnClickImageListener() {
            @Override
            public void onClickImage(int i, String url, List<String> urlList) {
                Toast.makeText(ImageCollectionActivity.this, "点击了图片" + url, Toast.LENGTH_SHORT).show();
            }
        });
        layoutNineGrid.setonLongClickImageListener(new NineGridTestLayout.OnLongClickImageListener() {
            @Override
            public void onLongClickImage(int i, String url, List<String> urlList) {
                list.remove(i);
                layoutNineGrid.setUrlList(list);
            }
        });

    }



}
