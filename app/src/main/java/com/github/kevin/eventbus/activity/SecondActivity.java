package com.github.kevin.eventbus.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.kevin.eventbus.bean.EventBean;
import com.github.kevin.library.EventBus;
import com.github.kevin.eventbus.R;

public class SecondActivity extends BaseActivity {
    private static final String TAG = "SecondActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    public void backLastPage(View view) {
        EventBus.getDefault().post(new EventBean("zhangsan", "888888"));
        Log.e(TAG, "发送thread： " + Thread.currentThread().getName());
    }

}
