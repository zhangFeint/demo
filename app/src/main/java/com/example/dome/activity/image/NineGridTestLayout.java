package com.example.dome.activity.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;

import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.library.depending.utils.GlideApp;

import java.util.List;

/**
 * 描述：
 * 作者：HMY
 * 时间：2016/5/12
 */
public class NineGridTestLayout extends NineGridLayout {
    private Context context;
    protected static final int MAX_W_H_RATIO = 3;
private  boolean isFluidImage = false;
    public NineGridTestLayout(Context context) {
        super(context);
        this.context = context;
    }

    public NineGridTestLayout(Context context, AttributeSet attrs) {

        super(context, attrs);
        this.context = context;
    }

    @Override
    protected boolean displayOneImage(final RatioImageView imageView, final String url, final int parentWidth) {
        if (isFluidImage){
            GlideApp.with(context)
                    .asBitmap()//强制Glide返回一个Bitmap对象
                    .load(url)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            int w = resource.getWidth();
                            int h = resource.getHeight();
                            int newW;
                            int newH;
                            if (h > w * MAX_W_H_RATIO) {//h:w = 5:3
                                newW = parentWidth / 2;
                                newH = newW * 5 / 3;
                            } else if (h < w) {//h:w = 2:3
                                newW = parentWidth * 2 / 3;
                                newH = newW * 2 / 3;
                            } else {//newH:h = newW :w
                                newW = parentWidth / 2;
                                newH = h * newW / w;
                            }
                            Glide.with(context).asBitmap().load(url).into(imageView);
                            setOneImageLayoutParams(imageView, newW, newH);
                        }
                    });
        }else {
            displayImage( imageView,  url);
        }
        return false;
    }

    @Override
    protected void displayImage(RatioImageView imageView, String url) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(new ColorDrawable(Color.GRAY))
                .error(new ColorDrawable(Color.GRAY))
                .fallback(new ColorDrawable(Color.GRAY));
        Glide.with(context).load(url)
                .apply(requestOptions)
                .into(imageView);
    }

    @Override
    protected void onClickImage(int i, String url, List<String> urlList) {
        listener.onClickImage(i, url, urlList);

    }

    @Override
    protected void onLongClickImage(int position, String url, List<String> urlList) {
        longClickImageListener.onLongClickImage(position, url, urlList);
    }

    private OnLongClickImageListener longClickImageListener;

    public void setonLongClickImageListener(OnLongClickImageListener longClickImageListener) {
        this.longClickImageListener = longClickImageListener;
    }

    public interface OnLongClickImageListener {
        void onLongClickImage(int i, String url, List<String> urlList);
    }

    private OnClickImageListener listener;

    public void setonClickImageListener(OnClickImageListener listener) {
        this.listener = listener;
    }

    public interface OnClickImageListener {
        void onClickImage(int i, String url, List<String> urlList);
    }

}
