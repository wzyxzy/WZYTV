package com.wzy.floattext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 悬浮窗view
 */
public class FloatingView extends FrameLayout implements RecognitionListener {
    private Context mContext;
    private View mView;
    private TextView mImageView;
    private int mTouchStartX, mTouchStartY;//手指按下时坐标
    private WindowManager.LayoutParams mParams;
    private FloatingManager mWindowManager;


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    recognize();
                    break;
            }
        }
    };

    public FloatingView(Context context) {
        super(context);
        mContext = context.getApplicationContext();
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        mView = mLayoutInflater.inflate(R.layout.floating_view, null);
        mImageView = (TextView) mView.findViewById(R.id.imageview);
        mWindowManager = FloatingManager.getInstance(mContext);
//        recognize();
    }

    public void show() {
        recognize();
        mParams = new WindowManager.LayoutParams();
        mParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
//        mParams.x = 0;
//        mParams.y = 100;
        //总是出现在应用程序窗口之上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }

//        mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        //设置图片格式，效果为背景透明
        mParams.format = PixelFormat.RGBA_8888;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR |
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        mParams.width = LayoutParams.WRAP_CONTENT;
        mParams.height = LayoutParams.WRAP_CONTENT;
        mWindowManager.addView(mView, mParams);

//        makeResultListener();

    }


    public void recognize() {
        SpeechRecognizer speech = SpeechRecognizer.createSpeechRecognizer(mContext);
        speech.setRecognitionListener(this);
        Intent recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, mContext.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
        speech.startListening(recognizerIntent);
    }


    public void hide() {
        mWindowManager.removeView(mView);
    }

    private OnTouchListener mOnTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mTouchStartX = (int) event.getRawX();
                    mTouchStartY = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    mParams.x += (int) event.getRawX() - mTouchStartX;
                    mParams.y += (int) event.getRawY() - mTouchStartY;//相对于屏幕左上角的位置
                    mWindowManager.updateView(mView, mParams);
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            return true;
        }
    };

    @Override
    public void onReadyForSpeech(Bundle bundle) {
        Log.d("wsss", "onReadyForSpeech");
    }

    @Override
    public void onBeginningOfSpeech() {
        mImageView.setText("字幕测试：");
        Log.d("wsss", "onBeginningOfSpeech");
    }

    @Override
    public void onRmsChanged(float v) {
        Log.d("wsss", "onRmsChanged");

    }

    @Override
    public void onBufferReceived(byte[] bytes) {
        Log.d("wsss", "onBufferReceived");

    }

    @Override
    public void onEndOfSpeech() {
        Log.d("wsss", "onEndOfSpeech");
//        SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
        handler.sendEmptyMessageDelayed(0, 1000);

    }

    @Override
    public void onError(int i) {
        Log.d("wsss", "errorCode=" + i + "");
        switch (i) {
            case 6:
                handler.sendEmptyMessageDelayed(0, 1000);

                break;
        }

    }

    @Override
    public void onResults(Bundle bundle) {
        Log.d("wsss", "onResults");

        ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        for (String match : matches) {
            mImageView.append(match);
        }


    }

    @Override
    public void onPartialResults(Bundle bundle) {
        Log.d("wsss", "onPartialResults");

    }

    @Override
    public void onEvent(int i, Bundle bundle) {
        Log.d("wsss", "onEvent");

    }
}