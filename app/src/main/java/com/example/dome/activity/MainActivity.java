package com.example.dome.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.dome.PermissionsUtils;
import com.example.dome.R;
import com.example.dome.activity.ceiling.CeilingActivity;
import com.example.dome.activity.image.ImageCollectionActivity;
import com.library.depending.baseview.BaseActivity;
import com.library.depending.customview.LoadingDialog;
import com.library.utils.httpservice.HttpRequestUtils;
import com.library.utils.httpservice.OkHttp3NetWork;
import com.library.utils.httpservice.OnLoadListener;
import com.library.utils.httpservice.OnNetWorkInterface;
import com.library.utils.httpservice.SubmitData;
import com.library.utils.httpservice.UploadDataAsyncTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/**
 * @author：zhangerpeng
 * 版本：
 * 日期：2019\2\12 0012
 * 描述：
 *
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {
    private Button button1, button2, button3, button4;
    private TimePickerView pvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    @Override
    public void initViews() {
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        PermissionsUtils.getInstance().setPermissionListener(new PermissionsUtils.OnPermissionListener() {
            @Override
            public void onAccredit() {
                Log.d("PermissionsUtils: ", "onAccredit: ");
            }

            @Override
            public void onNotAuthorized() {
                Log.d("PermissionsUtils: ", "onNotAuthorized: ");
            }
        });
        PermissionsUtils.getInstance().accreditPermissions(this, PermissionsUtils.CAMERA_PERMISSIONS, 111);

        initListener();
    }


    @Override
    public void initListener() {
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsUtils.getInstance().isPermissionsResult(grantResults);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                loadData();
                break;
            case R.id.button2:
                CeilingActivity.show(this);
                break;
            case R.id.button3:
                ImageCollectionActivity.show(this);
                break;
            case R.id.button4:
                showTimeSelector();
                break;
        }
    }

    /**
     * 时间选择器
     * 网址： https://blog.csdn.net/m0_37794706/article/details/78903576
     */
    private void showTimeSelector() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        //startDate.set(2013,1,1);
        Calendar endDate = Calendar.getInstance();
        //endDate.set(2020,1,1);

        //正确设置方式 原因：注意事项有说明
        startDate.set(2013, 0, 1);
        endDate.set(2025, 11, 31);

        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                Toast.makeText(MainActivity.this, getTime(date), Toast.LENGTH_SHORT).show();
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确认")//确认按钮文字
//                .setContentSize(18)//滚轮文字大小
                .setTitleSize(18)//标题文字大小
                .setTitleText("时间选择")//标题文字
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.BLUE)//确定按钮文字颜色
                .setCancelColor(Color.BLUE)//取消按钮文字颜色
                .setTitleBgColor(0xFFFFFFFF)//标题背景颜色 Night mode
                .setBgColor(0xFFFFFFFF)//滚轮背景颜色 Night mode
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(true)//是否显示为对话框样式
                .build();
        pvTime.setDate(Calendar.getInstance());

        Dialog mDialog = pvTime.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            pvTime.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                dialogWindow.setDimAmount(0.1f);
            }
        }
        pvTime.show();
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    @Override
    public void loadData() {
        final LoadingDialog dialog = new LoadingDialog(this,"加载中。。。");
        OnLoadListener loadListener = new OnLoadListener() {
            @Override
            public void onShow() {
                dialog.show();
            }

            @Override
            public void onConceal() {
                dialog.cancel();
            }
        };

        OkHttp3NetWork.submitDialog(this, HttpRequestUtils.REQUEST_GET, loadListener, new OnNetWorkInterface() {
            @Override
            public boolean validate() {

                return true;
            }

            @Override
            public SubmitData getSubmitData() {
                SubmitData submitData = new SubmitData("http://result.eolinker.com/ESjtgay076ea2b5dfec92a6c3755ad36cc0005df6f18991?uri=/app/schedule");
                return submitData;
            }

            @Override
            public void result(String result) {
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
