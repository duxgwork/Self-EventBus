package com.github.kevin.eventbus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.kevin.eventbus.bean.EventBean;
import com.github.kevin.library.EventBus;
import com.github.kevin.eventbus.R;
import com.github.kevin.library.Subscibe;
import com.github.kevin.library.ThreadMode;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void enterNextPage(View view) {
        EventBus.getDefault().register(this);
        startActivity(new Intent(this, SecondActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Subscibe(threadMode = ThreadMode.BACKGROUND)
    public void getEvent(EventBean bean) {
        Log.e(TAG, "接收thread = : " + Thread.currentThread().getName());
        Log.e(TAG, "结果： " + bean.toString());
    }

}
