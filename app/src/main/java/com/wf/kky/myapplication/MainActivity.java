package com.wf.kky.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final LedView view = (LedView) findViewById(R.id.led);
        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                view.setLEDStr("ANDROID");
            }
        });
    }
}
