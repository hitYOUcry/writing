package com.tencent.strategy.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.tencent.strategy.R;

/**
 * @author nemoqjzhang
 * @date 2018/9/13 14:50.
 */
public class StubActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView txv = findViewById(R.id.textView);
        txv.setText("我是StubActivity");
    }
}
