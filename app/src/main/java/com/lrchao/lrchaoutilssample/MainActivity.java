package com.lrchao.lrchaoutilssample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.lrchao.utils.AppUtils;
import com.lrchao.utils.LrchaoUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LrchaoUtils.getInstance().init(this);

        Log.e("a", AppUtils.getCurProcessName());
    }
}
