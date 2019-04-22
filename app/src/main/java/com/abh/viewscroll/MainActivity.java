package com.abh.viewscroll;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private CircleView circleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        circleView = findViewById(R.id.circle_view);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                circleView.scrollByHandler();
            //    circleView.useAnimScrollTo(-500);
           //     circleView.smoothScrollTo(-200,0);
           //     circleView.scrollBy(100,100);
            }
        },1000);

    }
}
