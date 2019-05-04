package com.wzy.floattext;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(MainActivity.this, MyService.class);
        intent.putExtra(MyService.ACTION, MyService.SHOW);
        startService(intent);

        finish();
    }

//    @Override
//    public void onAttachedToWindow() {
//        View view = getWindow().getDecorView();
//        WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view
//                .getLayoutParams();
//        lp.gravity = Gravity.LEFT | Gravity.TOP;
//        lp.x = 60;
//        lp.y = 100;
//        getWindowManager().updateViewLayout(view, lp);
//
//
//    }


}
