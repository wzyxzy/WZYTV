package com.wzy.wzytv;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.TextRenderer;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.Formatter;
import java.util.List;
import java.util.Locale;

public class VideoViewExo extends AppCompatActivity {

    private static final String TAG = VideoViewExo.class.getSimpleName();
    private SimpleExoPlayerView mExoPlayerView;
    private SimpleExoPlayer mSimpleExoPlayer;
    private Context mContext;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_view_exo);
        mContext = this;
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        initPlayer();
        playVideo();

    }

    /**
     * 初始化player
     */
    private void initPlayer() {
        //1. 创建一个默认的 TrackSelector
//        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
//        TrackSelection.Factory videoTackSelectionFactory =
//                new AdaptiveTrackSelection.Factory(bandwidthMeter);
//        TrackSelector trackSelector =
//                new DefaultTrackSelector(videoTackSelectionFactory);
//        LoadControl loadControl = new DefaultLoadControl();
//        //2.创建ExoPlayer
//        mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this,trackSelector,loadControl);
//        //3.创建SimpleExoPlayerView
        // 1. Create a default TrackSelector
        Handler mainHandler = new Handler();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

// 2. Create the player
        mSimpleExoPlayer =
                ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        mExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.exoView);
        //4.为SimpleExoPlayer设置播放器
        mExoPlayerView.setPlayer(mSimpleExoPlayer);
    }

    private void playVideo() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String url = bundle.getString("url");
        Uri playerUri = Uri.parse(url);
        //测量播放过程中的带宽。 如果不需要，可以为null。
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        // 生成加载媒体数据的DataSource实例。
        DataSource.Factory dataSourceFactory
                = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "wzytv"), bandwidthMeter);
        // 生成用于解析媒体数据的Extractor实例。
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();


        // MediaSource代表要播放的媒体。
        MediaSource videoSource = new ExtractorMediaSource(playerUri, dataSourceFactory, extractorsFactory,
                null, null);
        //Prepare the player with the source.
        mSimpleExoPlayer.prepare(videoSource);
        //添加监听的listener
//        mSimpleExoPlayer.setVideoListener(mVideoListener);
        mSimpleExoPlayer.addListener(eventListener);
//        mSimpleExoPlayer.setTextOutput(mOutput);
        mSimpleExoPlayer.setPlayWhenReady(true);

    }


    TextRenderer.Output mOutput = new TextRenderer.Output() {
        @Override
        public void onCues(List<Cue> cues) {
            Log.i(TAG, "MainActivity.onCues.");
        }
    };

    private SimpleExoPlayer.VideoListener mVideoListener = new SimpleExoPlayer.VideoListener() {
        @Override
        public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
            Log.i(TAG, "MainActivity.onVideoSizeChanged.width:" + width + ", height:" + height);

        }

        @Override
        public void onRenderedFirstFrame() {
            Log.i(TAG, "MainActivity.onRenderedFirstFrame.");
        }
    };


    private ExoPlayer.EventListener eventListener = new ExoPlayer.EventListener() {
        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest) {
            Log.i(TAG, "onTimelineChanged");
        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            Log.i(TAG, "onTracksChanged");
        }

        @Override
        public void onLoadingChanged(boolean isLoading) {
            Log.i(TAG, "onLoadingChanged");
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            Log.i(TAG, "onPlayerStateChanged: playWhenReady = " + String.valueOf(playWhenReady)
                    + " playbackState = " + playbackState);
            switch (playbackState) {
                case ExoPlayer.STATE_ENDED:
                    Log.i(TAG, "Playback ended!");
                    //Stop playback and return to start position
                    setPlayPause(false);
                    mSimpleExoPlayer.seekTo(0);
                    break;
                case ExoPlayer.STATE_READY:
                    mProgressBar.setVisibility(View.GONE);
                    Log.i(TAG, "ExoPlayer ready! pos: " + mSimpleExoPlayer.getCurrentPosition()
                            + " max: " + stringForTime((int) mSimpleExoPlayer.getDuration()));
                    setProgress(0);
                    break;
                case ExoPlayer.STATE_BUFFERING:
                    Log.i(TAG, "Playback buffering!");
                    mProgressBar.setVisibility(View.VISIBLE);
                    break;
                case ExoPlayer.STATE_IDLE:
                    Log.i(TAG, "ExoPlayer idle!");
                    break;
            }
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            Log.i(TAG, "onPlaybackError: " + error.getMessage());
        }

        @Override
        public void onPositionDiscontinuity() {
            Log.i(TAG, "onPositionDiscontinuity");
        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            Log.i(TAG, "MainActivity.onPlaybackParametersChanged." + playbackParameters.toString());
        }
    };

    /**
     * Starts or stops playback. Also takes care of the Play/Pause button toggling
     *
     * @param play True if playback should be started
     */
    private void setPlayPause(boolean play) {
        mSimpleExoPlayer.setPlayWhenReady(play);
    }

    private String stringForTime(int timeMs) {
        StringBuilder mFormatBuilder;
        Formatter mFormatter;
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "MainActivity.onPause.");
        super.onPause();
        mSimpleExoPlayer.stop();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "MainActivity.onStop.");
        super.onStop();
        mSimpleExoPlayer.release();
    }
}
