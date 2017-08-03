package com.wzy.wzytv;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.wzy.wzytv.adapters.TVAdapter;
import com.wzy.wzytv.controls.LightnessController;
import com.wzy.wzytv.controls.VolumeController;
import com.wzy.wzytv.model.TVModel;
import com.wzy.wzytv.tools.AESTools;

import java.util.ArrayList;
import java.util.List;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

import static io.vov.vitamio.MediaPlayer.VIDEOCHROMA_RGB565;

public class VideoTV extends AppCompatActivity implements AdapterView.OnItemClickListener, MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener, View.OnTouchListener {

    private VideoView vv;
    private String url;

    private LinearLayout drawer;

    private DrawerLayout dr_ly;
    private ListView lv2;

    private TVAdapter adapter;
    private List<TVModel.TvListEntity> tvListEntities;
    private ProgressBar probar;
    private TextView download_rate;
    private TextView load_rate;
    private String getUrl;
    private String tv_sources;
    private int posi;
    private static final String pass = "wzy2008";
    /**
     * 屏幕宽度
     */
    private int mScreenWidth;
    /**
     * 屏幕高度
     */
    private int mScreenHeight;
    /**
     * 每次滑动后的X
     */
    private float mLastMotionX;
    /**
     * 每次滑动后的Y
     */
    private float mLastMotionY;
    /**
     * 滑动有效的临界值
     */
    private int threshold = 30;
    private String userText;
    private String[] urls;
    private int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Vitamio.isInitialized(this);
//                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_tv);
        initView();
//        myOnclick();

    }


    private void initData() {
        RequestQueue mQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(getUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                initGson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        mQueue.add(stringRequest);

    }

    private void initGson(String response) {
        String s = AESTools.decrypt(response, pass);
        gotoGson(s);
    }

    private void gotoGson(String s) {
        Gson gson = new Gson();
        TVModel tvModel = gson.fromJson(s, TVModel.class);
        tvListEntities = tvModel.getTv_list();
        adapter.updateRes(tvListEntities);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            vv.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    private void initView() {
        vv = (VideoView) findViewById(R.id.vv);
        vv.setVideoChroma(VIDEOCHROMA_RGB565);
        vv.setHardwareDecoder(false);

        mScreenHeight = getResources().getDisplayMetrics().heightPixels;
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        MediaController mediaController = new MediaController(this);

        vv.setMediaController(mediaController);
//        vv.setVideoURI(Uri.parse("cache:/sdcard/download.mp4:" + uri));
        drawer = (LinearLayout) findViewById(R.id.drawer);

        dr_ly = (DrawerLayout) findViewById(R.id.dr_ly);

        lv2 = (ListView) findViewById(R.id.lv2);
        tvListEntities = new ArrayList<>();
        adapter = new TVAdapter(null, this, R.layout.item);
        lv2.setAdapter(adapter);
        lv2.setOnItemClickListener(this);


        probar = (ProgressBar) findViewById(R.id.probar);
        download_rate = (TextView) findViewById(R.id.download_rate);
        load_rate = (TextView) findViewById(R.id.load_rate);
        vv.setOnTouchListener(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        vv.setBufferSize(512);
//        vv.setHardwareDecoder(true);
        vv.setVideoChroma(VIDEOCHROMA_RGB565);
        url = bundle.getString("url");
        getUrl = bundle.getString("getUrl");
        tv_sources = bundle.getString("tv_sources");
        posi = bundle.getInt("position");
        if (getUrl.equalsIgnoreCase("http://cnbeijing.xyz/tv/tv9.m")) {
            userText = bundle.getString("userText");
            gotoGson(userText);
        } else {
            initData();
        }
        if (url.contains("#")) {
            analysis();
        } else {
            urls = new String[1];
            urls[0] = url;
            playnow();

        }
//        vv.setOnErrorListener(new MediaPlayer.OnErrorListener() {
//
//            @Override
//            public boolean onError(MediaPlayer mp, int what, int extra) {
//                if (what == 1 && urls.length > 0 && num < urls.length) {
////                    vv.stopPlayback();
//                    Toast.makeText(getApplicationContext(), "正在切换到源" + num, Toast.LENGTH_SHORT).show();
//
//                    num++;
//                    url = urls[num];
//                    vplay();
//                } else {
//                    Toast.makeText(getApplicationContext(), "没有可用来播放的视频", Toast.LENGTH_SHORT).show();
//                }
//                return true;
//            }
//
//        });

    }

    private void playnow() {
        if (getUrl.equalsIgnoreCase("http://cnbeijing.xyz/tv/tv4.m")) {
            pingyaoUpdate();
            url = url + tv_sources;
//            vplay();
        } else {
            vplay();
        }
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            vplay();
            handler.postDelayed(this, 358000);
        }
    };

    private void pingyaoUpdate() {
        handler.postDelayed(runnable, 10);
    }

    private void analysis() {
        urls = url.split("#");
        num = 0;
        url = urls[num];
    }

    private void vplay() {
//        Toast.makeText(this, url + tv_sources, Toast.LENGTH_SHORT).show();
        vv.setVideoPath(url);
//        vv.setVideoPath("cache:/sdcard/download.mp4:" + url + tv_sources);
        vv.setMediaController(new MediaController(this));
//        vv.setVideoURI(Uri.parse(url));
        vv.requestFocus();

        bufferVideo();
        prepare();

//        vv.start();
//        vv.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE,0);

    }

    private void bufferVideo() {
        vv.setOnInfoListener(this);
        vv.setOnBufferingUpdateListener(this);
    }

    private void prepare() {
        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setPlaybackSpeed(1.0f);
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        url = tvListEntities.get(position).getUrl();
        playnow();
        dr_ly.closeDrawer(drawer);
    }


    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                if (vv.isPlaying()) {
                    vv.pause();
                }
                probar.setVisibility(View.VISIBLE);
                download_rate.setText("");
                load_rate.setText("");
                download_rate.setVisibility(View.VISIBLE);
                load_rate.setVisibility(View.VISIBLE);

                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                vv.start();
                probar.setVisibility(View.GONE);
                download_rate.setVisibility(View.GONE);
                load_rate.setVisibility(View.GONE);
                break;
            case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                download_rate.setText("" + extra + "kb/s" + " ");
                break;
        }

        return true;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        load_rate.setText(percent + "%");
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        Log.d("screen", mScreenWidth + "");

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionX = x;
                mLastMotionY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaX = motionEvent.getX() - mLastMotionX;
                float deltaY = motionEvent.getY() - mLastMotionY;
                float absDeltaX = Math.abs(deltaX);
                float absDeltaY = Math.abs(deltaY);
                if (absDeltaX > threshold || absDeltaY > threshold) {
                    if (absDeltaX > absDeltaY) {
                        if (deltaX > 0) {
                            // TODO 增加音量
                            VolumeController.volumeUp(this, mScreenWidth, deltaX);
                        } else {
                            //TODO 降低音量
                            VolumeController.volumeDown(this, mScreenWidth, deltaX);
                        }
                    } else {
                        if (deltaY > 0) {
                            // TODO 降低屏幕亮度
                            LightnessController.lightnessDown(this, mScreenHeight, deltaY);

                        } else {
                            // TODO 提升屏幕亮度
                            LightnessController.lightnessUp(this, mScreenHeight, deltaY);
                        }
                    }
                } else {
                    return false;
                }
                mLastMotionX = x;
                mLastMotionY = y;
                break;

        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            posi++;
            url = tvListEntities.get(posi).getUrl();
            playnow();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            posi--;
            url = tvListEntities.get(posi).getUrl();
            playnow();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
//        return super.onKeyDown(keyCode, event);
    }
}
